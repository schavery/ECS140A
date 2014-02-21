/* *** This file is given as part of the programming assignment. *** */

public class Parser {

	// tok is global to all these parsing methods;
	// scan just calls the scanner's scan method and saves the result in tok.
	private Token tok; // the current token
	private void scan() {
		tok = scanner.scan();
	}

	private SymbolTable st;
	private CodeGen cg;
	private Scan scanner;

	Parser(Scan scanner) {
		st = new SymbolTable();
		cg = new CodeGen();
		this.scanner = scanner;

		scan();
		program();

		if( tok.kind != TK.EOF ) {
			parse_error("junk after logical end of program");
		} else {
			st.printAll();
		}
	}

	private void program() {
		cg.program();
		block();
	}

	private void block() {
		cg.blockbegin();
		st.begin();
		if(is(TK.VAR)) {
			declarations();
		}
		statement_list();
		st.end();
		cg.blockend();
	}

	private void statement_list() {
		while(true) {
			if(is(TK.FA) || is(TK.IF) || is(TK.DO) || is(TK.PRINT) || is(TK.ID)) {
				statement();
			} else {
				break;
			}
		}
	}

	private void statement() {
		if(is(TK.FA)) {
			fa();
		} else if(is(TK.IF)) {
			ifx();
		} else if(is(TK.DO)) { 
			dox();
		} else if(is(TK.PRINT)) {
			print();
		} else if(is(TK.ID)) {
			assignment();
		}
	}

	private void assignment() {
		if(is(TK.ID)) {
			// we need to scan past this id token, to make sure we're assigning
			// so we copy tok here, to do st.assign with it
			Token tt = new Token(tok.kind, tok.string, tok.lineNumber);

			if(!st.exists(tok.string)) {
				System.err.println("undeclared variable " + tok.string + " on line " + tok.lineNumber);
				System.exit(1);
			} else {
				scan();
				if(is(TK.ASSIGN)) {
					cg.assignbegin(tt.string);
					st.assign(tt);
					scan();
				} else {
					// we didn't get the assignment operator as expected
					System.err.println( "mustbe: want ASSIGN, got " + tok);
					parse_error( "missing token (mustbe)" );
				}
			}
		} else {
			// we expected an ID for assignment but got something else
			// duplicated mustbe to keep the diffs right
			System.err.println( "mustbe: want ID, got " + tok);
			parse_error( "missing token (mustbe)" );
		}
		expression();
		cg.assignend();
	}

	private void ifx() {
		mustbe(TK.IF);
		cg.beginIF();
		guarded_commands();
		mustbe(TK.FI);
		cg.keywordEnd(CodeGen.IS_ANYTHING);
	}

	private void dox() {
		mustbe(TK.DO);
		cg.beginDO();
		guarded_commands();
		mustbe(TK.OD);
		cg.keywordEnd(CodeGen.IS_ANYTHING);
	}

	private void fa() {
		mustbe(TK.FA);
		if(is(TK.ID)) {
			if(!st.exists(tok.string)) {
				System.err.println("undeclared variable " + tok.string + " on line " + tok.lineNumber);
				System.exit(1);
			} else {
				Token tt = new Token(tok.kind, tok.string, tok.lineNumber);
				scan();
				if(is(TK.ASSIGN)) {
					st.assign(tt);
					scan();

					cg.beginFA();
					cg.assignbegin(tt.string);
					cg.faLoopVar(tt.string);

				} else {
					// we didn't get the assignment operator as expected
					System.err.println( "mustbe: want ASSIGN, got " + tok);
					parse_error( "missing token (mustbe)" );
				}
			}
		}

		expression();
		mustbe(TK.TO);

		cg.beginTO();
		expression();
		cg.endTO();
		
		if(is(TK.ST)) {
			scan();

			cg.beginST();
			expression();
			cg.keywordEnd(CodeGen.IS_ANYTHING);
		}

		commands();
		mustbe(TK.AF);
		cg.keywordEnd(CodeGen.IS_ANYTHING);
	}


	// don't want to use elsex when we are in a do, just if
	private void guarded_commands() {
		guarded_command();
		while(true) {
			if(is(TK.BOX)) {
				cg.keywordEnd(CodeGen.IS_TK_BOX);
				scan();
				guarded_command();
			} else {
				if(is(TK.ELSE)) {
					cg.diELSE();
					scan();
					commands();
					break;
				} else {
					break;
				}
			}
		}
    }

	private void guarded_command() {
		expression();
		cg.closeParen();
		commands();
	}

	private void commands() {
		mustbe(TK.ARROW);
		block();
	}

	private void declarations() {
		mustbe(TK.VAR);
		while( is(TK.ID) ) {
			// need info from symbol table here.
			if(st.addSymbol(tok)) {
				cg.declare(tok.string);
			}
			scan();
		}
		mustbe(TK.RAV);
	}

	private void print() {
		mustbe(TK.PRINT);
		cg.printbegin();
		expression();
		cg.printend();
	}

	private void expression() {
		cg.expressionBegin();
		simple();
		if(containsRelop()) {
			relop();
			simple();
		}
		cg.expressionEnd();
	}

	private void simple() {
		term();
		while(true) {
			if(containsAddop()) {
				addop();
				term();
			} else {
				break;
			}
		}
	}

	private void term() {
		factor();
		while(true) {
			if(containsMultop()) {
				multop();
				factor();
			} else {
				break;
			}
		}
	}

	private void factor() {
        if(is(TK.LPAREN)) {
            cg.literal("(");
            scan();
            expression();
            mustbe(TK.RPAREN);
            cg.literal(")");
        } else if(is(TK.ID)){
            if(!st.exists(tok.string)) {
                System.err.println("undeclared variable " + tok.string + " on line " + tok.lineNumber);
                System.exit(1);
            } else {
                st.use(tok);
                cg.identifier(tok.string);
                scan();
            }
        } else if(is(TK.NUM)) {
            cg.literal(tok.string);
            scan();
        } else if(is(TK.SQR)) {
            scan();
            cg.literal("^");
            expression();
            cg.literal(")");
         } else if (is(TK.SQRT)) {
            scan();
            cg.literal("@");
            expression();
            cg.literal(")");
        } else {
            parse_error("factor");
        }
	}
    
	private void relop() {
		cg.literal(" ");
		cg.literal(tok.string);
		cg.literal(" ");
		scan();
	}

	private void multop(){
		cg.literal(" ");
		cg.literal(tok.string);
		cg.literal(" ");
		scan();
	}

	private void addop() {
		cg.literal(" ");
		cg.literal(tok.string);
		cg.literal(" ");
		scan();
	}

	private enum relop {
		EQ,       // =
		NE,       // /=
		LT,       // <
		GT,       // >
		LE,       // <=
		GE        // >=
	}

	private boolean containsRelop() {
		for(relop r : relop.values()) {
			if(r.name().equals(tok.kind.name())) {
				return true;
			}
		}
		return false;
	}

	private boolean containsAddop() {
		if(tok.kind == TK.MINUS || tok.kind == TK.PLUS)
			return true;
		return false;
	}

	private boolean containsMultop() {
		if(tok.kind == TK.DIVIDE || tok.kind == TK.TIMES)
			return true;
		return false;
	}

	// is current token what we want?
	private boolean is(TK tk) {
		return tk == tok.kind;
	}

	// ensure current token is tk and skip over it.
	private void mustbe(TK tk) {
		if( ! is(tk) ) {
			System.err.println( "mustbe: want " + tk + ", got " +
									tok);
			parse_error( "missing token (mustbe)" );
		}
		scan();
	}

	private void parse_error(String msg) {
		System.err.println( "can't parse: line "
							+ tok.lineNumber + " " + msg );
		System.exit(1);
	}
}
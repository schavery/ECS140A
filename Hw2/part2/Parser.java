/* *** This file is given as part of the programming assignment. *** */

public class Parser {


    // tok is global to all these parsing methods;
    // scan just calls the scanner's scan method and saves the result in tok.
    private Token tok; // the current token
    private void scan() {
        tok = scanner.scan();
    }

    private Scan scanner;
    Parser(Scan scanner) {
        this.scanner = scanner;
        scan();
        program();
        if( tok.kind != TK.EOF )
            parse_error("junk after logical end of program");
    }

    private void program() {
        block();
    }

    private void block() {
        // set up new symbol table stack entry
        if(is(TK.VAR)) {
            declarations();
        }
        statement_list();
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
        // else {
        //     // should never enter here, since statement list protects
        //      parse_error("Improper statement : Expected fa, if, do, print or assignment");
        // }
    }

    private void assignment() {
        mustbe(TK.ID);
        mustbe(TK.ASSIGN);
        expression();
    }

    private void ifx() {
        mustbe(TK.IF);
        guarded_commands();
        mustbe(TK.FI);
    }

    private void dox() {
        mustbe(TK.DO);
        guarded_commands();
        mustbe(TK.OD);
    }

    private void fa() {
        mustbe(TK.FA);
        mustbe(TK.ID);
        mustbe(TK.ASSIGN);
        expression();
        mustbe(TK.TO);
        expression();
        if(is(TK.ST)) {
            scan();
            expression();
        }
        commands();
        mustbe(TK.AF);
    }

    private void guarded_commands() {
        guarded_command();
        while(true) {
            if(is(TK.BOX)) {
                scan();
                guarded_command();
            } else {
                // scan();
                if(is(TK.ELSE)) {
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
        commands();
    }

    private void commands() {
        mustbe(TK.ARROW);
        block();
    }

    private void declarations() {
        mustbe(TK.VAR);
        while( is(TK.ID) ) {
            scan();
        }
        mustbe(TK.RAV);
    }

    // you'll need to add a bunch of methods here
    private void print() {
        mustbe(TK.PRINT);
        expression();
    }

    private void expression() {
        simple();
        if(containsRelop()) {
            relop();
            simple();
        }
    }

    private void simple() {
        term();
        while(true) {
            if(containsAddop()) {
                addop();
                term();
                // scan();
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
                // scan();
            } else {
                break;
            }
        }
    }

    private void factor() {
        if(is(TK.LPAREN)) {
            scan();
            expression();
            mustbe(TK.RPAREN);
        } else if(is(TK.ID)) {
            scan();
        } else if(is(TK.NUM)) {
            scan();
        } else {
            parse_error("factor");
        }
    }

    private void relop() {
        scan();
    }

    private void multop(){
        scan();
    }

    private void addop() {
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
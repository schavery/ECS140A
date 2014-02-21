import java.util.ArrayDeque;

public class CodeGen {

	// we will use this enum in a stack to keep track
	// of what kind of state we are in
	// we will need to do different things based on each one
		// on do and if:
		// we want to print and store
		// on fa, we want to print and store
		// on st: we want to store only, for later printing
	private enum keyword {
		DO,
		IF,
		FA,
		ST,
		PRINT,
		TO
	}
	private ArrayDeque<keyword> state = new ArrayDeque<keyword>();

	// a constant to pass in when we are wrapping up a control block
	// and we need to know whether we are going to keep going or not
	public static final int IS_TK_BOX = 1;
	public static final int IS_ANYTHING = 0;

	// depending on the state, we will either want
	// to print and store an expression
	// just print an expression
	// or just store an expression for later.
	private ArrayDeque<ArrayDeque<String>> expressions 
		= new ArrayDeque<ArrayDeque<String>>();

	// we use this to build an individual expression string,
	// since we wont know how many tokens to expect until
	// we get an ending token.
	private String currExp = "";

	// some private methods for actually outputting text.
	// looks at the state to see what to do, this way we can 
	// centralize the logic for whether we print and store or not
	// printline.
	// simpler because when we want to print a whole line
	// we wont be in the middle of an expression.
	private void pl(String s) {
		System.out.println(s);
	}

	// print.
	// we expect to be getting expression strings here
	// so we need to be dealing with them.
	private void p(String s) {
		boolean doPrint = false;
		boolean doStore = false;
		
		keyword k = state.peek();
		// in java, switch can't handle null
		if(k == null) {
			doPrint = true;
		} else {
			switch(k) {
				case DO:
				case IF:
				case FA: {
					doPrint = true;
					doStore = true;
					break;
				}
				case TO:
				case ST: {
					doStore = true;
					break;
				}
				case PRINT: {
					doPrint = true;
				}
			}
		}

		if(doStore) {
			currExp += s;
		}

		if(doPrint) {
			System.out.print(s);
		}
	}

	// we should call this after getting the ending
	// tokens: AF, FI, OD
	// also when we get a TK.BOX
	// in here is where we will change state, and manipulate the
	// expression list
	public void keywordEnd(int boxReceived) {
		if(boxReceived == IS_TK_BOX) {
			myKeywordEnd(true);
		} else {
			myKeywordEnd(false);
		}
	}

	private void myKeywordEnd(boolean box) {
		// if box tells us whether to pop or not.
		switch(state.peek()) {
			case DO:
			case IF: {
				if(!box) {
					expressions.pop();
					state.pop();
				} else {
					// repeat the control statement for
					// whatever state we are in
					handleBox();
				}
				break;
			}
			case FA: {
				expressions.pop();
				state.pop();
				break;
			}
			case ST: {
				// we do not want to pop expressions here!
				// leave state too.
			}
		}
	}

	private void handleBox() {
		if(state.peek() == keyword.DO) {
			p("while(");
		} else {
			p("if(");
		}
	}

	// here we can set up the string for holding the incoming expression
	public void expressionBegin() {
		currExp = "";
	}

	public void expressionEnd() {
		if(!currExp.equals("") && state.peek() != null) {
			expressions.peek().push(currExp);
		}
	}

	public void program() {
		pl("#include <stdio.h>");
		pl("int main()");
	}

	public void blockbegin() {
		pl("{");
		if(state.peek() == keyword.ST) {
			pl("if(" + expressions.peek().pop() + ") {");
		}
		expressions.push(new ArrayDeque<String>());
	}

	public void blockend() {
		if(state.peek() == keyword.ST) {
			pl("}");
			state.pop();
		}
		pl("}");
		expressions.pop();
	}

	public void declare(String s) {
		pl("int x_" + s + " = -12345;");
	}

	public void assignbegin(String s) {
		p("x_" + s + " = ");
	}

	// we will get an expression between these two,
	// but we don't need to store it, unless we're in FA

	public void assignend() {
		pl(";");
	}

	// same with these two
	public void printbegin() {
		state.push(keyword.PRINT);
		p("printf(\"%d\\n\", ");
	}

	public void printend() {
		pl(");");
		state.pop();
	}

	// in an expression, we will call literal or identifier
	// nothing else should be called.
	// the literal method needs to translate some strings to their
	// c equivalents
	public void literal(String s) {
		String op;
        switch(s) {
            case "/=": op = "!="; break;
            case "=": op = "=="; break;
            default: op = s;
        }
        p(op);
	}

	public void identifier(String s) {
        p("x_" + s);
	}
	// end expression system.

	// IF FI
	public void beginIF() {
		state.push(keyword.IF);
		expressions.push(new ArrayDeque<String>());
		p("if(");
	}

	// DO OD
	public void beginDO() {
		state.push(keyword.DO);
		expressions.push(new ArrayDeque<String>());
		p("while(");
	}
	
	public void diELSE() {
		// here is where we can print the expressions needed
		// to set up the proper else logic for our do or if block
		String test = "if(!(";
		while(!expressions.peek().isEmpty()) {
			test += expressions.peek().poll();
			if(!expressions.peek().isEmpty()) {
				test += ") && !(";
			} else {
				test += "))";
			}
		}
		pl(test);
	}

	// FA AF
	public void beginFA() {
		state.push(keyword.FA);
		expressions.push(new ArrayDeque<String>());
		p("for(");
	}
	
	public void faLoopVar(String s) {
		expressions.peek().push("x_" + s);
	}

	// ST and TO
	public void beginTO() {
		p("; ");
		state.push(keyword.TO);
	}

	public void endTO() {
		state.pop();
		// this would be the second expression, or the most
		// recently received one.
		String e2 = expressions.peek().pop();
		String e1 = expressions.peek().pop();

		// our identifier.
		String id = expressions.peek().pop();

		pl(id + " <= (" + e2 + "); " + id + "++)");

	}
	
	public void beginST() {
		state.push(keyword.ST);
	}

	public void closeParen() {
		p(") ");
	}
}

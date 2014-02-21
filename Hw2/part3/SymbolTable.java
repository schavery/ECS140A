/*
// part 3
begin a block
end block
search - goes in this block first, then above

// part 4
second vector of objects containing this
declare
assign
use
to string
*/

import java.util.Vector;
// import java.util.ArrayList;

public class SymbolTable {
	private Vector<Vector<Symbol>> symtab = new Vector<Vector<Symbol>>();
	private Vector<Symbol> symlist = new Vector<Symbol>();

	public void begin() {
		Vector<Symbol> block = new Vector<Symbol>();
		symtab.add(block);
	}

	public void end() {
		symtab.remove(symtab.lastElement());
	}

	// analagous to declaration
	public void addSymbol(Token t) {
		if(!declared(t.string)) {
			Symbol s = new Symbol();
			s.id = t.string;
			s.declaredOn = t.lineNumber;
			s.nestingDepth = symtab.size();

			symtab.lastElement().add(s);

			symlist.add(s);
		} else {
			System.err.println("variable " + t.string + " is redeclared on line " + t.lineNumber);
		}

	}

	// to search for redeclarations
	public boolean exists(String id) {
		if(symtab.isEmpty()) {
			return false;
		} else {
			for(int ii = symtab.size() - 1; ii >= 0; --ii) {
				for(int iii = 0; iii < symtab.get(ii).size(); ++iii) {
					if(symtab.get(ii).get(iii).id.equals(id)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean declared(String id) {
		if(symtab.isEmpty()) {
			return false;
		} else {
			if(symtab.lastElement().isEmpty()) {
				return false;
			} else {
				for(int ii = 0; ii < symtab.lastElement().size(); ++ii) {
					if(symtab.lastElement().get(ii).id.equals(id)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// for assignations

	// for uses

} // class
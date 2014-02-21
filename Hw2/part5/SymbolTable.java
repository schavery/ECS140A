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

public class SymbolTable {
	private Vector<Vector<Symbol>> symtab = new Vector<Vector<Symbol>>();
	private Vector<Vector<Symbol>> ss2 = new Vector<Vector<Symbol>>();

	public void begin() {
		Vector<Symbol> block = new Vector<Symbol>();
		symtab.add(block);
	}

	public void end() {

		int nDepth = 0;

		if(!symtab.lastElement().isEmpty()) {
			// nesting depth is the same across the vector
			nDepth = symtab.lastElement().get(0).nestingDepth;

			if(!ss2.isEmpty()) {
				boolean added = false;
				for(int ii = 0; ii < ss2.size(); ++ii) {
					// compare by line number of the first element of each vector
					// when we find one that is greater than nDepth,
					// we insert before that one
					// anything on the stack SHOULD have at least one element
					int dd = ss2.get(ii).get(0).nestingDepth;
					if(dd <= nDepth) {
						continue;
					} else {
						ss2.add(ii, symtab.lastElement());
						added = true;
						break;
					}
				}
				if(!added) {
					// add at the last index
					ss2.add(ss2.size(), symtab.lastElement());
				}
			} else {
				// put the vector where ever, since theres nothing that can go wrong
				ss2.add(0, symtab.lastElement());

			} // are there any frames at all?
		} // is this stack empty?

		// pop
		symtab.remove(symtab.lastElement());

	} // end()

	// analagous to declaration
	public boolean addSymbol(Token t) {
		if(!declared(t.string)) {
			Symbol s = new Symbol();
			s.id = t.string;
			s.declaredOn = t.lineNumber;
			s.nestingDepth = symtab.size() - 1;

			symtab.lastElement().add(s);
			return true;
		} else {
			System.err.println("variable " + t.string + " is redeclared on line " + t.lineNumber);
			return false;
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
	public void assign(Token t) {
		// find the variable as close to the top of the stack as possible
		for(int ii = symtab.size() - 1; ii >= 0; --ii) {

			Vector<Symbol> block = symtab.get(ii);

			for(int iii = 0; iii < block.size(); ++iii) {

				Symbol s = block.get(iii);

				if(s.id.equals(t.string)) {

					s.assignedList.add(s.assignedList.size(), (Integer) t.lineNumber);
					return;
				}
			}
		}
	}

	// for uses
	public void use(Token t) {
		for(int ii = symtab.size() - 1; ii >= 0; --ii) {

			Vector<Symbol> block = symtab.get(ii);

			for(int iii = 0; iii < block.size(); ++iii) {

				Symbol s = block.get(iii);

				if(s.id.equals(t.string)) {

					s.accessedList.add(s.accessedList.size(), (Integer) t.lineNumber);
					return;
				}
			}
		}
	}

	// call the print function on our list
	public void printAll() {
		while(!ss2.isEmpty()) {
			// iterate over the elements, and find the one with the lowest line number
			// depends on the vectors already being in depth based ordering
			// pop it, print it, and repeat
			int lowestEncounteredLine = Integer.MAX_VALUE;
			int indexOfLowest = 0;

			for(int ii = 0; ii < ss2.size(); ++ii) {
				// lowest line should be first element
				int dOn = ss2.get(ii).get(0).declaredOn;
				if(dOn < lowestEncounteredLine) {
					indexOfLowest = ii;
					lowestEncounteredLine = dOn;
				}
			}

			// now we have the element to be printed, at indexOfLowest;
			Vector<Symbol> p = ss2.get(indexOfLowest);
			for(Symbol s : p) {
				System.err.println(s);			}
			ss2.remove(indexOfLowest);
		}
	}

} // class
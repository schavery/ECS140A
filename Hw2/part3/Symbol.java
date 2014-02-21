import java.util.ArrayList;
import java.lang.StringBuilder;

public class Symbol {

	// symbol name
	public String id;

	// where is declared
	public int declaredOn;
	public int nestingDepth;

	// list of lines where is used
	public ArrayList<Integer> assignedList = new ArrayList<Integer>();
	public ArrayList<Integer> accessedList = new ArrayList<Integer>();

	public String toString() {
		StringBuilder sb = new StringBuilder(128);
		sb.append(id);
		sb.append("\n");
		sb.append("declared on line ");
		sb.append(declaredOn);
		sb.append(" at nesting depth ");
		sb.append(nestingDepth);
		sb.append("\n");
		sb.append("assigned to on: ");
		sb.append(getAssignments());
		sb.append("\n");
		sb.append("used on: ");
		sb.append(getUses());
		sb.append("\n");
		return sb.toString();
	}

	private String getAssignments() {
		StringBuilder sb = new StringBuilder(64);

		for(int ii = 0; ii < assignedList.size(); ++ii) {
			int assignment = assignedList.get(ii);
			int counter = 1;
			while(assignedList.get(++ii) == assignment) {
				++counter;
			}

			// decrement to not miss the next element
			--ii;

			if(counter > 1) {
				sb.append(assignment + "(" + counter + ") ");
			} else {
				sb.append(assignment + " ");
			}
		}

		return sb.toString();
	}

	private String getUses() {
		StringBuilder sb = new StringBuilder(64);

		for(int ii = 0; ii < accessedList.size(); ++ii) {
			int use = accessedList.get(ii);
			int counter = 1;
			while(accessedList.get(++ii) == use) {
				++counter;
			}

			// decrement to not miss the next element
			--ii;

			if(counter > 1) {
				sb.append(use + "(" + counter + ") ");
			} else {
				sb.append(use + " ");
			}
		}

		return sb.toString();
	}
};
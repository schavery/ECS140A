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
		sb.append("  declared on line ");
		sb.append(declaredOn);
		sb.append(" at nesting depth ");
		sb.append(nestingDepth);
		sb.append("\n");
		String assignments = getAssignments();
		if(assignments.length() == 0) {
			sb.append("  never assigned");
		} else {
			sb.append("  assigned to on:");
			sb.append(assignments);
		}
		sb.append("\n");
		String uses = getUses();
		if(uses.length() == 0) {
			sb.append("  never used");
		} else {
			sb.append("  used on:");
			sb.append(uses);
		}
		
		return sb.toString();
	}

	private String getAssignments() {
		StringBuilder sb = new StringBuilder(64);

		for(int ii = 0; ii < assignedList.size(); ++ii) {
			int assignment = assignedList.get(ii);
			assignedList.remove(ii);
			--ii;
			int counter = 1;
			while(assignedList.contains(assignment)) {
				++counter;
				assignedList.remove((Integer) assignment);
			}
			if(counter > 1) {
				sb.append(" " + assignment + "(" + counter + ")");
			} else {
				sb.append(" " + assignment);
			}
		}
		return sb.toString();
	}

	private String getUses() {
		StringBuilder sb = new StringBuilder(64);

		for(int ii = 0; ii < accessedList.size(); ++ii) {
			int used = accessedList.get(ii);
			accessedList.remove(ii);
			--ii;
			int counter = 1;
			while(accessedList.contains(used)) {
				++counter;
				accessedList.remove((Integer) used);
			}
			if(counter > 1) {
				sb.append(" " + used + "(" + counter + ")");
			} else {
				sb.append(" " + used);
			}
		}
		return sb.toString();
	}
};
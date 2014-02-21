public class JumbleIt implements SeqIt {
	private Jumble jumble;
	private int cursor;

	public JumbleIt(Jumble s) {
		jumble = s;
		cursor = 0;
	}

	public boolean hasNext() {
		if(cursor == jumble.num) {
			return false;
		} else {
			return true;
		}
	}

	public int next() {
		if(!hasNext()) {
			System.err.println("JumbleIt called past end");
			System.exit(1);
		}
		return jumble.values[cursor++];
		
	}
	
}
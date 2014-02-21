public class DeltaIt implements SeqIt {
	private Delta delta;
	private int cursor;

	public DeltaIt(Delta s) {
		delta = s;
		cursor = 0;
	}

	public boolean hasNext() {
		if(cursor == delta.num) {
			return false;
		} else {
			return true;
		}
	}

	public int next() {
		if(!hasNext()) {
			System.err.println("DeltaIt called past end");
			System.exit(1);
		}

		return delta.initial + cursor++ * delta.delta;
	}
	
}
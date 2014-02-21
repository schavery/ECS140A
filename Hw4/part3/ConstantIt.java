public class ConstantIt implements SeqIt {
	private Constant constant;
	private int cursor;

	public ConstantIt(Constant s) {
		constant = s;
		cursor = 0;
	}

	public boolean hasNext() {
		if(cursor == constant.num) {
			return false;
		} else {
			return true;
		}
	}

	public int next() {
		if(!hasNext()) {
			System.err.println("ConstantIt called past end");
			System.exit(1);
		}

		++cursor;
		return constant.value;
	}

}
public class Plus {

	public static Seq plus(Constant c1, Constant c2) {
		int num = Math.min(c1.num, c2.num);
		return new Constant(num, c1.value + c2.value);
	}

	public static Seq plus(Delta d1, Delta d2) {
		int num = Math.min(d1.num, d2.num);
		return new Delta(num, d1.initial + d2.initial, d1.delta + d2.delta);
	}

	public static Seq plus(Jumble j1, Jumble j2) {
		int num = Math.min(j1.num, j2.num);
		int [] jumblaya = new int[num];
		for(int ii = 0; ii < num; ++ii) {
			jumblaya[ii] = j1.values[ii] + j2.values[ii];
		}
		return new Jumble(jumblaya);
	}

	public static Seq plus(Constant c1, Delta d1) {
		int num = Math.min(c1.num, d1.num);
		return new Delta(num, d1.initial + c1.value, d1.delta);
	}

	public static Seq plus(Delta d1, Constant c1) {
		int num = Math.min(c1.num, d1.num);
		return new Delta(num, d1.initial + c1.value, d1.delta);
	}

	public static Seq plus(Constant c1, Jumble j1) {
		int num = Math.min(c1.num, j1.num);
		int [] narr = new int[num];
		for(int ii = 0; ii < narr.length; ++ii) {
			narr[ii] = c1.value + j1.values[ii];
		}
		return new Jumble(narr);
	}

	public static Seq plus(Jumble j1, Constant c1) {
		int num = Math.min(c1.num, j1.num);
		int [] narr = new int[num];
		for(int ii = 0; ii < narr.length; ++ii) {
			narr[ii] = c1.value + j1.values[ii];
		}
		return new Jumble(narr);
	}

	public static Seq plus(Delta d1, Jumble j1) {
		int num = Math.min(d1.num, j1.num);
		int [] narr = new int[num];

		for(int ii = 0; ii < narr.length; ++ii) {
			narr[ii] = (d1.initial + ii * d1.delta) + j1.values[ii];
		}

		return new Jumble(narr);
	}

	public static Seq plus(Jumble j1, Delta d1) {
		int num = Math.min(d1.num, j1.num);
		int [] narr = new int[num];

		for(int ii = 0; ii < narr.length; ++ii) {
			narr[ii] = (d1.initial + ii * d1.delta) + j1.values[ii];
		}

		return new Jumble(narr);
	}

}
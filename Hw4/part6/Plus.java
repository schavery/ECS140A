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
		int[] jumblaya = new int[num];
		for(int i = 0; i < num; i++) {
			jumblaya[i] = j1.values[i] + j2.values[i];
		}
		return new Jumble(jumblaya);
	}

}
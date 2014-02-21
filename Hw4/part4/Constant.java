public class Constant extends Seq {
	protected int num;
	protected int value;

	public Constant(int num, int value){
		if(num <= 0) {
			this.num = 0;
			this.value = 0;
		} else {
			this.num = num;
			this.value = value;
		}
	}

	public String toString(){
		return "[ " + num + " : " + value + " ]";
	}

	public int min() {
		if(num == 0) {
			return 0;
		} else {
			return value;
		}
	}

}
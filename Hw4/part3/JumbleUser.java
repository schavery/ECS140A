public class JumbleUser {
	public static int lengthLongestNDCSS1(Jumble j) {
		JumbleIt ji = new JumbleIt(j);

		int longest = 1;
		int curLongest = 1;
		int cur = Integer.MIN_VALUE;
		int curNext = 0;

		if(ji.hasNext()){
			cur = ji.next();
		} else {
			return 0;
		}

		while(ji.hasNext()) {
			curNext = ji.next();
			if(cur <= curNext) {
				curLongest++;
				cur = curNext;
			} else {
				if(longest < curLongest) {
					longest = curLongest;
				}

				curLongest = 1;
				cur = curNext;
			}
		}

		// takes care the case if the last sequnce turned out to be longest
		return Math.max(curLongest,longest);
	}
}
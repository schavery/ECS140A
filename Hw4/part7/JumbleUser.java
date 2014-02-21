public class JumbleUser {
	public static int lengthLongestNDCSS1(Jumble j) {
		JumbleIt ji = new JumbleIt(j);

		int longest = 1;
		int curLongest = 1;
		int cur = Integer.MIN_VALUE;
		int curNext = 0;

		if(ji.hasNext()){
			try {
				cur = ji.next();
			} catch(UsingIteratorPastEndException ignore) {}
		} else {
			return 0;
		}

		while(ji.hasNext()) {
			try {
				curNext = ji.next();
			} catch(UsingIteratorPastEndException ignore) {}
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

	public static int lengthLongestNDCSS2(Jumble j) {
		JumbleIt ji = new JumbleIt(j);

		int longest = 1;
		int curLongest = 1;
		int cur = Integer.MIN_VALUE;
		int curNext = 0;

		try {
			cur = ji.next();
		} catch(UsingIteratorPastEndException u) {
			// this means that there are no elements
			return 0;
		}

		try {
			while(true) {
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
		} catch(UsingIteratorPastEndException u) {
			return Math.max(curLongest,longest);
		}
	}
}
public class Plus {

	public static Seq plus(Seq s1, Seq s2) {
		SeqIt si1 = s1.createSeqIt();
		SeqIt si2 = s2.createSeqIt();

		// int num = Math.min(s1.num, s2.num);
		// if(num == 0) {
		// 	return new Constant(0,0);
		// }

		int count = 0;
		int lastSum = 0;
		int currSum = 0;
		int lastDiff = 0;
		int currDiff = 0;
		int initial = 0;
		boolean isJumble = false;

		while(si1.hasNext() && si2.hasNext()) {
			if(count == 0) {
				// first time round
				try {
					lastSum	= si1.next() + si2.next();
				} catch(UsingIteratorPastEndException ignore) {}
				initial = lastSum;
			} else if(count == 1) {
				try {
					currSum = si1.next() + si2.next();
				} catch(UsingIteratorPastEndException ignore) {}
				currDiff = currSum - lastSum;
				lastDiff = currDiff;
				lastSum = currSum;
			} else {
				try {
					currSum = si1.next() + si2.next();
				} catch(UsingIteratorPastEndException ignore) {}
				currDiff = currSum - lastSum;

				if(currDiff != lastDiff) {
					isJumble = true;
				} else {
					lastDiff = currDiff;
					lastSum = currSum;
				}
			}
			++count;
		}

		if(isJumble) {
			// refresh the iterators.
			si1 = s1.createSeqIt();
			si2 = s2.createSeqIt();
			int [] narr = new int[count];
			for(int ii = 0; ii < count; ++ii) {
				try {
					narr[ii] = si1.next() + si2.next();
				} catch(UsingIteratorPastEndException ignore) {}
			}
			return new Jumble(narr);

		} else {
			if(count == 0) {
				return new Constant(0, 0);
			} else if(count == 1) {
				return new Constant(1, initial);
			} else if(currDiff == 0) {
				return new Constant(count, lastSum);
			} else {
				return new Delta(count, initial, lastDiff);
			}
		}
	}
}

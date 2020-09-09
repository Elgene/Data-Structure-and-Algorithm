/**
 * A new KMP instance is created for every substring search performed. Both the
 * pattern and the text are passed to the constructor and the search method. You
 * could, for example, use the constructor to create the match table and the
 * search method to perform the search itself.
 */
public class KMP {
	int mTable[];
	public KMP(String pattern, String text) {
		// TODO maybe fill this in.

		int m = pattern.length();
		char S[] = pattern.toCharArray();
		mTable = new int[m];
		mTable[0] = -1;
		mTable[1] = 0;
		int j = 0;  //position in prefix
		int pos = 2;//  position in table
		while (pos < m) {
			if (S[pos - 1] == S[j]) { //substring pos-1 and 0..j match
				mTable[pos] = j + 1;
				pos++;
				j++;
			} else if (j > 0) { //mismatch so restart the prefix
				j = mTable[j];
			} else {
				j = 0;    //run out of candidate prefixes
				mTable[pos] = 0;
				pos++;
			}
		}
	}

	/**
	 * Perform KMP substring search on the given text with the given pattern.
	 * 
	 * This should return the starting index of the first substring match if it
	 * exists, or -1 if it doesn't.
	 */
	public int search(String pattern, String text) {
		// TODO fill this in.
		char S[] = pattern.toCharArray();
		char T[] = text.toCharArray();
		int kIndex = 0; //start of current match in T
		int iIndex = 0; //position of current character in S
		int m = pattern.length();
		int n = text.length();
		while (kIndex + iIndex < n) {
			if (S[iIndex] == T[kIndex + iIndex]) {
				iIndex = iIndex+1; //matches
				if (iIndex == m) {
					return kIndex; // found S
				}
			} else if (mTable[iIndex] == -1) { //mismatch,no self overlap here
				iIndex = 0;
				kIndex = kIndex + iIndex + 1;
			} else { // mismatch with self overlap
				kIndex = kIndex + iIndex - mTable[iIndex]; //match position jumps forward
				iIndex = mTable[iIndex];
			}
		}
		return -1; //failed to find S
	}
}

public class BoyerMoore {
    private static final int aplhabetSize = Character.MAX_VALUE + 1;

    /**
     * Do jump table for the mismatched info
     */
    public static int[] boyerCharacterTable(String pattern) {
        final int patternLength = pattern.length();
        int[] boyerTable = new int[aplhabetSize];

        for (int i = 0; i < boyerTable.length; i++)
            boyerTable[i] = patternLength;

        for (int i = 0; i < patternLength - 1; i++)
            boyerTable[pattern.charAt(i)] = patternLength - 1 - i;

        return boyerTable;
    }

    /**
     * Get the prefix
     */
    private static boolean isPrefix(String pattern, int positionPrefix) {
        for (int i = positionPrefix, j = 0; i < pattern.length(); i++, j++)
            if (pattern.charAt(i) != pattern.charAt(j)){ return false;}

        return true;
    }

    /**
     * Get match and in reverse, between pattern[0, pos]
     * and pattern[0, len)
     */
    private static int suffixLen(String pattern, int pos) {
        int sufLength = 0;

        for (int i = pos, j = pattern.length() - 1; i >= 0; i--, j--) {
            if (pattern.charAt(i) != pattern.charAt(j)) {break;}
            sufLength++;
        }

        return sufLength;
    }

    /**
     * Do jump table for the position a mismatch happens
     */
    public static int[] BoyerJumpTable(String pattern) {
        final int patternLen = pattern.length();
        int[] table = new int[patternLen];

        int lastPosition = patternLen;

        //doing the prefix backwards
        for (int i = patternLen; i > 0; i--) {
            if (isPrefix(pattern, i))
                lastPosition = i;

            table[patternLen - i] = lastPosition - i + patternLen;
        }

        for (int i = 0; i < patternLen - 1; i++) {
            int finalSuffixLen = suffixLen(pattern, i);
            table[finalSuffixLen] = patternLen - 1 - i + finalSuffixLen;
        }

        return table;
    }

    public static int boyerSearch(String pattern, String text, int[] characterTable, int[] boyerJumpTable) {
        final int patternLen = pattern.length();
        final int textLen = text.length();

        //if pattern is empty
        if (patternLen == 0)
            return 0;

        int textPosition = patternLen - 1;
        int patternPosition;

        while (textPosition < textLen) {
            for (patternPosition = patternLen - 1; pattern.charAt(patternPosition) == text.charAt(textPosition); patternPosition--, textPosition--)
                if (patternPosition == 0) //if they matches
                    return textPosition;

            //Jump to the largest with proper amount  in forward
            textPosition += Math.max(boyerJumpTable[patternLen - 1 - patternPosition], characterTable[text.charAt(textPosition)]);
        }

        return -1;
    }

}

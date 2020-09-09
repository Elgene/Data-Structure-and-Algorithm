public class BruteForce {

    public int search(String pattern, String text){
        char[] wordArray = pattern.toCharArray();
        char[] textArray = text.toCharArray();

        int wordLength = wordArray.length; //m
        int textLength = textArray.length; //n

        for (int k = 0; k <= textLength - wordLength; k++) {
            boolean found = true;
            for (int i = 0; i < wordLength; i++) {
                if (wordArray[i] != textArray[k + i]) {
                    found=false;
                    break;
                }
            }
            if (found) {
                return k;
            }
        }

        return -1;
    }

}

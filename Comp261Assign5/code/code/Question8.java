public class Question8 {

        public static void main(String[] args) {
            String x = "1101";
            String y = "110";
            System.out.println("X=" + x);
            System.out.println("Y=" + y);

            String z = binaryEncrypt("1101", "110");

            System.out.println("Encrypted Z is=" + z);
            binaryDecrypt(z);
        }

        public static String binaryEncrypt(String x, String y) { // binary value: 101, 1101
            String xBinaryLength = Integer.toBinaryString(x.length()); //returns the binary value

            if (xBinaryLength.length() == 1) {
                xBinaryLength = "00" + xBinaryLength;
            }
            if (xBinaryLength.length() == 2) {
                xBinaryLength = "0" + xBinaryLength;
            }
            String z = x + "" + y + xBinaryLength;
            return z;
        }

        public static void binaryDecrypt(String z) {
            String x = "";
            String y = "";
            String binaryTemp = z.charAt(z.length() - 3) + "" + z.charAt(z.length() - 2) + "" + z.charAt(z.length() - 1);
            z = z.substring(0, z.length() - 3);
            int binaryDecimal = Integer.parseInt(binaryTemp, 2);

            for (int i = 0; i < binaryDecimal; i++) {
                x += z.charAt(i);
            }

            for (int i = binaryDecimal; i < z.length(); i++) {
                y += z.charAt(i);
            }
            System.out.println("X is: " + x + "\n Y is: " + y);
        }
    }


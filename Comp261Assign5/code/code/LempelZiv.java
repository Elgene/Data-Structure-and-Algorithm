import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * A new instance of LempelZiv is created for every run.
 */
public class LempelZiv {
	private static final int windowSize = 50000;
	private static final Pattern tuplePat = Pattern.compile("(?=\\[\\d+\\|\\d+\\|[\\S\\s]])");

	/**
	 * Take uncompressed input as a text string, compress it, and return it as a
	 * text string.
	 */
	public String compress(String input) {
		// TODO fill this in.
		int cursor = 0;
		final int inputLength = input.length();
		StringBuilder output = new StringBuilder();

        long time = System.nanoTime();

		while (cursor < inputLength) {
			int currentPosition;
			int pos = 0;
			int lempleLength = 0;
			int start = Math.max(cursor - windowSize, 0);
			String window = input.substring(start, cursor);

			while (cursor + lempleLength < inputLength - 1 && (currentPosition = window.indexOf(input.substring(cursor, cursor + lempleLength + 1))) >= 0) {
				pos = currentPosition;
				lempleLength++;
			}

			if (lempleLength > 0) { // is match found
				output.append('[');
				output.append(window.length() - pos);
				output.append('|');
				output.append(lempleLength);
				output.append('|');
				output.append(input.charAt(cursor + lempleLength));
				output.append(']');
			}
			else {
				output.append("[0|0|");
				output.append(input.charAt(cursor));
				output.append(']');
			}

			cursor += lempleLength + 1;
		}

        System.out.printf("Took %dns\n", System.nanoTime() - time);
		return output.toString();
	}

	/**
	 * Take compressed input as a text string, decompress it, and return it as a
	 * text string.
	 */
	public String decompress(String compressed) {
		// TODO fill this in.
		StringBuilder output = new StringBuilder();

		Scanner scanner = new Scanner(compressed);
		scanner.useDelimiter(tuplePat);

		while (scanner.hasNext()) {
			//Split the tuple up into its own  tokens
			String tupleRead = scanner.next();
			tupleRead = tupleRead.substring(1, tupleRead.length() - 1); //Remove all the brackets

			char tupleChar = tupleRead.charAt(tupleRead.length() - 1);
			tupleRead = tupleRead.substring(0, tupleRead.length() - 2);

			int sepOfPosition = tupleRead.indexOf('|');

			int offset = Integer.parseInt(tupleRead.substring(0, sepOfPosition));
			int length = Integer.parseInt(tupleRead.substring(sepOfPosition + 1));

			for (int i = 0; i < length; i++) {
				output.append(output.charAt(output.length() - offset));
			}
			output.append(tupleChar);
		}

		return output.toString();
	}

	/**
	 * The getInformation method is here for your convenience, you don't need to
	 * fill it in if you don't want to. It is called on every run and its return
	 * value is displayed on-screen. You can use this to print out any relevant
	 * information from your compression.
	 */
	public String getInformation() {
		return "";
	}
}

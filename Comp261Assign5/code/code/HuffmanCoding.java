import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * A new instance of HuffmanCoding is created for every run. The constructor is
 * passed the full text to be encoded or decoded, so this is a good place to
 * construct the tree. You should store this tree in a field and then use it in
 * the encode and decode methods.
 */
public class HuffmanCoding {

	private class huffmanNode implements Comparable<huffmanNode> {
		public huffmanNode leftNode = null, rightNode = null;
		public ArrayList<Character> codeRecord = new ArrayList<>();
		public char huffmanValue;
		public int huffmanFrequency;

		public huffmanNode(char huffmanValue, int huffmanFrequency) {
			this.huffmanValue = huffmanValue;
			this.huffmanFrequency = huffmanFrequency;
		}

		public int compareTo(huffmanNode o) {
			return huffmanFrequency - o.huffmanFrequency;
		}
	}

	private HashMap<Character, String> encodingTable = new HashMap<>();
	private HashMap<String, Character> decodingTable = new HashMap<>();



	/**
	 * This would be a good place to compute and store the tree.
	 */
	public HuffmanCoding(String text) {
		// TODO fill this in.
		if (text.length() <= 1)
			return;

		//Create a the frequency huffman table
		HashMap<Character, huffmanNode> countsMap = new HashMap<>();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (countsMap.containsKey(c))
				countsMap.get(c).huffmanFrequency++;
			else
				countsMap.put(c, new huffmanNode(c, 1));
		}

		//Build the frequency tree
		PriorityQueue<huffmanNode> countQueue = new PriorityQueue<>(countsMap.values());
		while (countQueue.size() > 1) {
			huffmanNode left = countQueue.poll();
			huffmanNode right = countQueue.poll();
			huffmanNode parent = new huffmanNode('\0', left.huffmanFrequency + right.huffmanFrequency);
			parent.leftNode = left;
			parent.rightNode = right;

			countQueue.offer(parent);
		}

		huffmanNode rootNode = countQueue.poll();

		//Assign the codes to each node in the tree
		Stack<huffmanNode> huffmanNodeStack = new Stack<>();
		huffmanNodeStack.add(rootNode);
		while (!huffmanNodeStack.empty()) {
			huffmanNode huffmanNode = huffmanNodeStack.pop();

			if (huffmanNode.huffmanValue != '\0') {
				StringBuilder sb = new StringBuilder();
				huffmanNode.codeRecord.forEach(sb::append);
				String codeSb = sb.toString();

				encodingTable.put(huffmanNode.huffmanValue, codeSb);
				decodingTable.put(codeSb, huffmanNode.huffmanValue);
			}
			else {
				huffmanNode.leftNode.codeRecord.addAll(huffmanNode.codeRecord);
				huffmanNode.leftNode.codeRecord.add('0');
				huffmanNode.rightNode.codeRecord.addAll(huffmanNode.codeRecord);
				huffmanNode.rightNode.codeRecord.add('1');
			}

			if (huffmanNode.leftNode != null)
				huffmanNodeStack.add(huffmanNode.leftNode);

			if (huffmanNode.rightNode != null)
				huffmanNodeStack.add(huffmanNode.rightNode);
		}
	}


	/**
	 * Take an input string, text, and encode it with the stored tree. Should
	 * return the encoded text as a binary string, that is, a string containing
	 * only 1 and 0.
	 */
	public String encode(String text) {
		// TODO fill this in.
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < text.length(); i++) {
			sb.append(encodingTable.get(text.charAt(i)));
		}
		return sb.toString();
	}

	/**
	 * Take encoded input as a binary string, decode it using the stored tree,
	 * and return the decoded text as a text string.
	 */
	public String decode(String encoded) {
		// TODO fill this in.
		StringBuilder output = new StringBuilder();

		int i = 0;
		while (i < encoded.length()) {
			StringBuilder characterBuilder = new StringBuilder();

			while (!decodingTable.containsKey(characterBuilder.toString()))
				characterBuilder.append(encoded.charAt(i++));

			output.append(decodingTable.get(characterBuilder.toString()));
		}

		return output.toString();
	}

	/**
	 * The getInformation method is here for your convenience, you don't need to
	 * fill it in if you don't wan to. It is called on every run and its return
	 * value is displayed on-screen. You could use this, for example, to print
	 * out the encoding tree.
	 */
	public String getInformation() {
		StringBuilder sb = new StringBuilder();

		encodingTable.forEach((c, s) -> {
			sb.append(c);
			sb.append(" -> ");
			sb.append(s);
			sb.append('\n');
		});

		return sb.toString();
	}
	}


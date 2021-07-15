import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class ScrabbleCheater {
	// source:https://github.com/powerlanguage/word-lists/blob/master/common-7-letter-words.txt
	static String FILE_LOCATION = "/Users/Anni/Downloads/common-7-letter-words.txt";
	ArrayList<String> fileList;

	final int hashTableSize = 1000;
	LinkedList<String>[] hashTable;
	static int maxCollisionCounter = 0;
	static int maxCollisionPosition = 0;

	public ScrabbleCheater() {
		hashTable = new LinkedList[hashTableSize];
		for (int i = 0; i < hashTable.length; i++) {
			hashTable[i] = new LinkedList<String>();
		}
	}

	public static void main(String[] args) throws IOException {
		ScrabbleCheater main = new ScrabbleCheater();

		// reads file in general
		main.readFile();
		System.out.println("Maximum collision: " + maxCollisionCounter);
		System.out.println("Maximum collision position is " + maxCollisionPosition + ".");

		// check input in file
		HashTable hashT = new HashTable();
		main.readFileWords(FILE_LOCATION);

		for (String word : main.fileList) {
			hashT.add(main.generateHashCode(word), word);
		}

		// display input of "" and use method lookUp to check input
		System.out.println(main.lookUp(hashT.hashTable, "airflow"));

	}

	public void readFile() throws FileNotFoundException {
		File file = new File(FILE_LOCATION);
		String currentInput = "";
		BufferedReader bf = null;
		int count = 0;
		bf = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

		try {
			currentInput = bf.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		while (currentInput != null) {
			try {
				currentInput = bf.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (currentInput != null) {
				String n = normalize(currentInput);
				int hash = generateHashCode(n);
				hashTable(currentInput, hash);
			}
			count++;
		}

		try {
			bf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("The file consists of " + count + " words.");

	}

	private String normalize(String word) {
		char[] originalArray = word.toCharArray();
		Arrays.sort(originalArray);
		String sort = new String(originalArray);
		return sort;
	}

	private void hashTable(String word, int hash) {
		if (hash > 0 && hash < hashTable.length) {
			hashTable[hash].add(word);
			int collisionC = hashTable[hash].size();
			if (collisionC > maxCollisionCounter) {
				maxCollisionCounter = collisionC;
				maxCollisionPosition = hash;
			}
		}
	}

	// read words in file
	private void readFileWords(String file) throws IOException {
		FileReader fr = new FileReader(FILE_LOCATION);
		BufferedReader br = new BufferedReader(fr);
		fileList = new ArrayList<String>();

		String words;
		while ((words = br.readLine()) != null) {
			fileList.add(words);
		}

		br.close();

	}

	// Source: https://www.baeldung.com/java-hashcode
	public int generateHashCode(String word) {
		int hash = 7;
		// makes sure that it can also read words written in lower case
		char[] letters = word.toLowerCase().toCharArray();
		Arrays.sort(letters);
		for (int i = 0; i < word.length(); i++) {
			hash = hash * 31 + letters[i];
		}
		hash = (hash < 0) ? hash * -1 : hash;
		return hash % 123 + 1;

	}

	// lookup method that takes a word as a string, returns an array of Strings
	// corresponding to all the words at the hash location
	private String lookUp(Dictionary[] hashTable, String word) {
		int hash = generateHashCode(word);

		char[] letters = word.toLowerCase().toCharArray();
		// sort and normalize letters
		Arrays.sort(letters);
		String input = null;
		Dictionary valueInput = hashTable[hash];
		System.out.println("Input: " + word);
		if (hashTable[hash] == null) {
			input = "Sorry, no match found. Please try again.";
			return input;
		} else {
			while (valueInput.getValue() != null) {
				char[] findWord = valueInput.getValue().toLowerCase().toCharArray();
				Arrays.sort(findWord);
				if (Arrays.equals(letters, findWord)) {
					input = valueInput.getValue();
					input = "... how about " + valueInput.getValue() + " ?";
					return input;
				} else {
					if (valueInput.getNext() == null)
						break;
					valueInput = valueInput.getNext();
					findWord = valueInput.getValue().toLowerCase().toCharArray();
					Arrays.sort(findWord);
				}
			}
		}
		input = "Sorry, no match found. Please try again.";
		return input;
	}

}
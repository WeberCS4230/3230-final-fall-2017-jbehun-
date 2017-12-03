package boggle;

import java.io.*;
import java.util.*;

public class Dictionary {

	ArrayList<String> dictionary;

	public Dictionary() {

		Scanner input;
		dictionary = new ArrayList<String>();
		try {
			input = new Scanner(new FileReader("dictionary.txt"));
			while (input.hasNext()) {
				dictionary.add(input.next());
			}
			input.close();
		} catch (FileNotFoundException e) {
			System.out.println("Failed to read in dictionary");
			e.printStackTrace();
		}
	}

	public boolean isValidWord(String word) {
		word = word.toUpperCase();
		return dictionary.contains(word);
	}

	public static void main(String[] args) {
		Dictionary dictionary = new Dictionary();
		System.out.println(dictionary.isValidWord("moon"));

	}

}

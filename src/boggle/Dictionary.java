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
	
	public boolean isAdjancentWord(int[] positions) {
		
		int[] mid = {0,1,2,4,6,8,9,10};
		int[] rightSide = {0,1,4,8,9};
		int[] leftSide = {1,2,6,9,10};
		boolean adjacent = true;
		
		for(int i =0; i < positions.length - 1; i++) {
			int diff = positions[i + 1] - positions[i] + 5;
			
			switch (positions[i] % 4) {
			
			case 0: adjacent = isPositionsAdjecent(diff, leftSide);
				break;
				
			case 3: adjacent = isPositionsAdjecent(diff, rightSide);	
				break;
			default: adjacent = isPositionsAdjecent(diff, mid);
			}
			
			if (adjacent == false) {
				return false;
			}
		}
		
		return true;	
	}
	
	private boolean isPositionsAdjecent(int diff, int[] validValues) {
		boolean inArray = false;
		
		for(int i = 0; i < validValues.length; i++) {
			if(diff == validValues[i]) {
				inArray = true;
			}
		}
		
		return inArray;
	}

	public static void main(String[] args) {
		Dictionary dictionary = new Dictionary();
		System.out.println(dictionary.isValidWord("moon"));
		
		int[] array = {13,14,15};
		System.out.println(dictionary.isAdjancentWord(array));

	}

}

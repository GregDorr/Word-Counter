package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * The purpose of this class is to read the actual file and count the words.
 * As the file is being read, add each word to a HashMap.
 * The Scanner will ignore punctuation.
 * 
 * @version 1.0
 * 
 * @author Greg Dorr
 * 
 */
public class ReadCount {

	//Variables
	private Scanner read;					//Scanner to read the file
	private int totalNumberWords;			//Total number of words in the file
	private HashMap<String,Integer> map;	//Stores the words
	
	
	/**
	 * Constructor that takes in a File type
	 * The constructor calls all the other code.
	 * @param file
	 */
	public ReadCount(File file){
		 
		read = null;
		totalNumberWords = 0;
		map = new HashMap<String,Integer>();
		 
		
		try {
			read = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		wordSearch();
	}
	
	/**
	 * This is where the file is read.
	 * The Scanner will loop through the document picking up one string at a time.
	 * After the string is captured it'll pass through a regular expression statement. 
	 * The regular expression only accepts a-z, A-Z and apostrophes. 
	 * It'll remove any other character. 
	 */
	private void wordSearch(){
		while (read.hasNext()){
			
			String temp = null;
			temp = read.next();
			temp = temp.toLowerCase();
			
			String lettersOnly = temp.replaceAll("[^a-zA-Z ']", "");
		
			if(lettersOnly.equals("")){
				continue;
			}
				
			totalNumberWords++;
			addHash(lettersOnly);
		}
	}
	
	/**
	 * This is a function where it adds to the hashmap.
	 * It looks to see if a key is already in the map.
	 * If a key is already in the map, it'll increase the key's value by one.
	 * @param input
	 */
	private void addHash(String input){
		if(map.get(input) == null){
			map.put(input,1);
		}
		
		else{
			int value = map.get(input) + 1;
			map.put(input, value);
		}
	}
	
	/**
	 * This will return the string version of the map, Total number of words, and the number of Unique Words.
	 * 
	 * @return String
	 */
	public String toString(){
		
		return "Total = " + totalNumberWords + '\n' + "Unique Words = " + map.size() + "\n-------------\n" +  map.toString();
	}
	
}

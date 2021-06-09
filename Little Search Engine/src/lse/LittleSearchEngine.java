package lse;

import java.io.*;
import java.util.*;

import javax.swing.text.TabExpander;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/

		HashMap<String, Occurrence> table = new HashMap<String, Occurrence>();

		Scanner docSC = new Scanner(new File(docFile)); 

		while(docSC.hasNext()){

			String word = docSC.next();
			word = getKeyword(word);

			if(word != null){
				
				if(table.containsKey(word) == true) {
					table.get(word).frequency +=1;

				} else {
					Occurrence occ = new Occurrence(docFile, 1);
					table.put(word, occ);

				}

			}

		}
		

		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return table;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/

		for (String k : kws.keySet()){

			if(keywordsIndex.containsKey(k) == true){ //add to arraylist occurences 

				String d = kws.get(k).document;
				int f = kws.get(k).frequency;

				Occurrence occ = new Occurrence(d, f);

				keywordsIndex.get(k).add(occ);
				insertLastOccurrence(keywordsIndex.get(k));

			} else { //add to hashmap 

				String d = kws.get(k).document;
				int f = kws.get(k).frequency;

				Occurrence occ = new Occurrence(d, f);
				ArrayList<Occurrence> temp = new ArrayList<Occurrence>();
				temp.add(occ);

				keywordsIndex.put(k, temp);

			}

		}

	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/

		String keyword = word;
		int charIndex;

		for(charIndex = keyword.length() - 1; 
            charIndex >= 0 && checkChar(keyword.charAt(charIndex)) != false;
            	charIndex--);

		keyword = keyword.substring(0, charIndex + 1).toLowerCase();

		if (keyword.matches("^[a-zA-Z]*$") == false || keyword.equals("")) return null;
			else if (noiseWords.contains(keyword)) 	 					   return null;
			else return keyword;
		
	}

	private static boolean checkChar (char c){

        boolean stop = false;

        switch (c) {
            case '.': case ',': case '?': case ':': case ';': case '!':
                stop = true;
                break;
            default:
                stop = false;
                break;
        }

        return stop;
    }

	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		
		ArrayList<Integer> searchIndexes = new ArrayList<Integer>();

		Occurrence occTA = occs.get(occs.size() - 1);
		
		int freq = occTA.frequency;

		int left = 0;
		int right = occs.size() - 2;

		boolean found = false;

		while (left <= right) {
	
			int mid = left + (right - left) / 2;
			searchIndexes.add(mid);
  
			// Check if x is present at mid
			if (occs.get(mid).frequency == freq) {
				found = true;
				occs.add(mid, occTA);
				break;
			}
  
			// If x greater, ignore left half
			if (occs.get(mid).frequency > freq) left = mid + 1;
  				else right = mid - 1;
		
		}

		if (found == false) occs.add(left, occTA);
		occs.remove(occs.size() - 1);

		if(searchIndexes.size() == 1) return null;
			else return searchIndexes;

	}
	
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/

		
		if(keywordsIndex.containsKey(kw1) == false && keywordsIndex.containsKey(kw2) == false){
			return null;
		
		} else if (keywordsIndex.containsKey(kw1) == false && keywordsIndex.containsKey(kw2) == true){
			return trimIntoDocArray(keywordsIndex.get(kw2));

		} else if (keywordsIndex.containsKey(kw1) == true && keywordsIndex.containsKey(kw2) == false){
			return trimIntoDocArray(keywordsIndex.get(kw1));

		} else {

			ArrayList<Occurrence> list1 = keywordsIndex.get(kw1);
			ArrayList<Occurrence> list2 = keywordsIndex.get(kw2);
			ArrayList<Occurrence> list3 = new ArrayList<Occurrence>();

			//System.out.println("kw1: " + list1);
			//System.out.println("kw2: " + list2);
			
			list3.addAll(list2);

			for (Occurrence occ1 : list1){

				boolean add = true;

				for(Occurrence occ2 : list2){

					if(occ1.document.equals(occ2.document)){
						if(occ1.frequency >= occ2.frequency){
							list3.remove(occ2);
							addOccList(occ1, list3);
							add = false;
							break;
						} else {
							add = false;

						}

					}

				}

				if (add == true) addOccList(occ1, list3);

			}


			//System.out.println("list3: " + list3);

			return trimIntoDocArray(list3);
		
		}

	}

	private static void addOccList (Occurrence occ, ArrayList<Occurrence> arr){

		for (int i = 0; i < arr.size(); i++) {
           
            if (arr.get(i).frequency > occ.frequency) continue;
            
            if (arr.get(i).frequency == occ.frequency) {
                arr.add(i, occ);
                return;
            }
            
            arr.add(i, occ);
            return;
        }
       
		
        arr.add(occ);
		
	}

	private static ArrayList<String> trimIntoDocArray (ArrayList<Occurrence> arr){

		ArrayList<String> returnArray = new ArrayList<String>();

		while(arr.size() > 5){
			arr.remove(arr.size() - 1);
		}

		for(Occurrence occ : arr) {
			returnArray.add(occ.document);
		}

		return returnArray;
	}

}

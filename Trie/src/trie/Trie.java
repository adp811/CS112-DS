package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		
		TrieNode root = new TrieNode(null, null, null);

        Indexes firstWord = new Indexes(0, (short) 0, (short) (allWords[0].length() - 1));
        root.firstChild = new TrieNode(firstWord, null, null);

        for(int wordIndex = 1; wordIndex < allWords.length; wordIndex++){

            TrieNode curr = root.firstChild;

            int startIndexPref = curr.substr.startIndex; 
            int endIndexPref = curr.substr.endIndex;

            int startIndexWord = 0;
            int endIndexWord = allWords[wordIndex].length() - 1;

            boolean isAdded = false;

            while(isAdded != true){

                boolean addSibling = false;
                boolean addChild = false;
                boolean addBoth = false;

                char a = allWords[curr.substr.wordIndex].charAt(startIndexPref);
                char b = allWords[wordIndex].charAt(startIndexWord);

                if(a == b){

                    if(startIndexPref == endIndexPref) {
                        //full match with prefix (move to firstchild)
                        if(curr.firstChild != null){
                            curr = curr.firstChild;

                            startIndexPref = curr.substr.startIndex;
                            endIndexPref = curr.substr.endIndex;

                            startIndexWord += 1;

                        } else {
                            addChild = true;

                        }

                    } else {
                        //keep checking next character
                        startIndexPref +=1;
                        startIndexWord +=1;

                    }


                } else {

                    if(startIndexPref == curr.substr.startIndex) {
                        //first char not in common, move to next sibling
                        if(curr.sibling != null){
                            curr = curr.sibling;

                            startIndexPref = curr.substr.startIndex; 
                            endIndexPref = curr.substr.endIndex;

                        } else {
                            addSibling  = true;

                        }

                    } else {
                        //partial match found, create new child node
                        addBoth = true;


                    }

                }


                if(addChild){

                    //add remaining string of the indexWord as a child 

                    Indexes tempIndexes = new Indexes(
                        wordIndex,
                        (short) (startIndexWord + 1), //+1 because current char matches already 
                        (short) endIndexWord);
                    
                    TrieNode tempNode = new TrieNode(
                        tempIndexes,
                        null,
                        null);

                    curr.firstChild = tempNode;
                    isAdded = true;
                
                } else if(addSibling){

                    //add remaining string of the indexWord as a sibling

                    Indexes tempIndexes = new Indexes(
                        wordIndex,
                        (short) startIndexWord,
                        (short) endIndexWord);

                    TrieNode tempNode = new TrieNode(
                        tempIndexes,
                        null,
                        null);

                    curr.sibling = tempNode;
                    isAdded = true;
                
                } else if (addBoth) {

                    //add unmatched char from prefix and indexWord as child and sibling
                    //of the current node

                    Indexes tempIndexesC = new Indexes(
                        curr.substr.wordIndex,
                        (short) startIndexPref,
                        (short) endIndexPref);

                    TrieNode tempNodeC = new TrieNode(
                        tempIndexesC,
                        null,
                        null);

                    Indexes tempIndexesS = new Indexes(
                        wordIndex,
                        (short) startIndexWord,
                        (short) endIndexWord);
    
                    TrieNode tempNodeS = new TrieNode(
                        tempIndexesS,
                        null,
                        null);

                    TrieNode temp = curr.firstChild;

                    curr.substr.endIndex = (short) (startIndexPref - 1);

                    curr.firstChild = tempNodeC;
                    curr.firstChild.sibling = tempNodeS;
                    curr.firstChild.firstChild = temp;

                    isAdded = true;
                
                }
                
            }
            
        }
        
        return root;

	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/
		
		boolean searchPrefixMatched = false;

		TrieNode curr = root.firstChild;

        ArrayList<TrieNode> matches = new ArrayList<TrieNode>();

		int startIndexSearch = 0;
		int endIndexSearch = prefix.length() - 1;

		int startIndexCurr = curr.substr.startIndex;
		int endIndexCurr = curr.substr.endIndex;


		while(searchPrefixMatched != true) {

            char a = allWords[curr.substr.wordIndex].charAt(startIndexCurr);
            char b = prefix.charAt(startIndexSearch);

            if (a == b) {

                if(startIndexSearch == endIndexSearch && 
                    startIndexCurr != endIndexCurr){
                    searchPrefixMatched = true;

                } else if (startIndexSearch != endIndexSearch && 
                    startIndexCurr == endIndexCurr) {

                    if (curr.firstChild != null) {
                        curr = curr.firstChild;
                    
                        startIndexCurr = curr.substr.startIndex;
                        endIndexCurr = curr.substr.endIndex;
                    
                        startIndexSearch +=1;
                    
                    } else {
                        break;

                    }
                        
                } else if (startIndexSearch == endIndexSearch && 
                    startIndexCurr == endIndexCurr){
                    searchPrefixMatched = true;

                } else { 
                    startIndexCurr +=1;
                    startIndexSearch +=1;

                }

            } else {

                if(curr.sibling != null) {
                    curr = curr.sibling;

                    startIndexCurr = curr.substr.startIndex;
                    endIndexCurr = curr.substr.endIndex;

                } else {
                    break;

                }

            }	
           
		} 

        if(searchPrefixMatched) {
            if(curr.firstChild == null) matches.add(curr);
            else matches = find(curr.firstChild);

            return matches;

        } else {
            return null;

        }

	}

	private static ArrayList<TrieNode> find(TrieNode node){

		ArrayList<TrieNode> wordMatches = new ArrayList<TrieNode>();

		if (node.sibling != null) {
			wordMatches.addAll(find(node.sibling));
		}

		if(node.firstChild != null) {
			wordMatches.addAll(find(node.firstChild));
		}

		if(node.firstChild == null) {
			wordMatches.add(node);
		}

		return wordMatches;

	}

	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }

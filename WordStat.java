import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;

/**
 * A class to compute various statistics on a collection of words.
 * @author Josh Hager
 */
public class WordStat {

    /* stores words in text as keys and their counts as values */
    private HashTable wordTable;

    /* stores word pairs ("word1 word2") as keys and their counts as values */
    private HashTable wordPairTable;

    /* a list of the HashEntries from wordTable sorted in order of their value (count) */
    private ArrayList<HashEntry> sortedWords;

    /* a list of the HashEntries from wordPairTable sorted in order of their value (count) */
    private ArrayList<HashEntry> sortedPairs;

    /* stores words as keys and their rank as values */
    private HashTable wordRankTable;

    /* stores word pairs as keys and their rank as values */
    private HashTable pairRankTable;

    /* stores the current path of this .java file */
    private String path = this.getClass().getClassLoader().getResource("").getPath();
    
    /**
     * Creates a new WordStat that computes word statistics from a file.
     * @param fileName the name of the file
     * @throws FileNotFoundException if the file cannot be found
     * @throws IOException if there is an I/O error
     * @throws NoSuchMethodException if the getTable method used to gain access to the HashEntries cannot be found
     * @throws SecurityException if there is a security exception in reflection of getTable method
     * @throws IllegalAccessException when the getTable method used to gain access to the HashEntries cannot be accessed
     * @throws IllegalArgumentException when there is an illegal argument in the reflection of the getTable method
     * @throws InvocationTargetException when there is an exception thrown by the reflected getTable method
     */
    public WordStat(String fileName) throws FileNotFoundException, IOException, NoSuchMethodException, 
                                            SecurityException, IllegalAccessException, IllegalArgumentException, 
                                            InvocationTargetException{
        Tokenizer t = new Tokenizer(fileName);

        wordTable = hashWords(t);

        wordPairTable = hashWordPairs(t);

        sortedWords = sortEntries(wordTable);

        sortedPairs = sortEntries(wordPairTable);

        wordRankTable = hashSortedEntries(sortedWords);

        pairRankTable = hashSortedEntries(sortedPairs);
    }

    /**
     * Creates a new WordStat that computes word statistics from a String array.
     * @param inputWords the String array containing the input words
     * @throws NoSuchMethodException if the getTable method used to gain access to the HashEntries cannot be found
     * @throws SecurityException if there is a security exception in reflection of getTable method
     * @throws IllegalAccessException when the getTable method used to gain access to the HashEntries cannot be accessed
     * @throws IllegalArgumentException when there is an illegal argument in the reflection of the getTable method
     * @throws InvocationTargetException when there is an exception thrown by the reflected getTable method
     */
    public WordStat(String[] inputWords) throws NoSuchMethodException, SecurityException, IllegalAccessException, 
                                                IllegalArgumentException, InvocationTargetException{
        Tokenizer t = new Tokenizer(inputWords);

        wordTable = hashWords(t);

        wordPairTable = hashWordPairs(t);

        sortedWords = sortEntries(wordTable);

        sortedPairs = sortEntries(wordPairTable);

        wordRankTable = hashSortedEntries(sortedWords);

        pairRankTable = hashSortedEntries(sortedPairs);
    }

    /**
     * Gets the word count of a specified word.
     * @param word the word to get the count of
     * @return the count of that word
     */
    public int wordCount(String word){
        int wordCount = getWordTable().get(word);
        
        if(wordCount == -1){
            return 0;
        }

        return wordCount;
    }

    /**
     * Gets the count of a specified word pair.
     * @param w1 the first word in the pair
     * @param w2 the second word in the pair
     * @return the count of the word pair
     */
    public int wordPairCount(String w1, String w2){
        int wordPairCount = getWordPairTable().get(w1 + " " + w2);
        
        if(wordPairCount == -1){
            return 0;
        }

        return wordPairCount;
    }

    /**
     * Gets the rank of the specified word, where 1 is the rank of the most common word.
     * @param word the word to get the rank of
     * @return the rank of the word
     * @throws NoSuchElementException if the word is not in the text
     */
    public int wordRank(String word) throws NoSuchElementException{
        int wordRank = getWordRankTable().get(word);

        if(wordRank == -1){
            throw new NoSuchElementException();
        }

        return wordRank;
    }

    /**
     * Gets the rank of the specified word pair, where 1 is the rank of the most common word pair.
     * @param w1 the first word in the word pair
     * @param w2 the second word in the word pair
     * @return the rank of the word pair
     * @throws NoSuchElementException if the word pair is not in the text
     */
    public int wordPairRank(String w1, String w2) throws NoSuchElementException{
        int wordPairRank = getPairRankTable().get(w1 + " " + w2);

        if(wordPairRank == -1){
            throw new NoSuchElementException();
        }

        return wordPairRank;
    }

    /**
     * Returns a String[] containing the k most common words in the text, in decreasing order of their count.
     * @param k the number of most common words to retrieve
     * @return a String[] of the k most common words
     * @throws kIsTooLargeException if there are less than k words in the text, excluding duplicates
     */
    public String[] mostCommonWords(int k) throws kIsTooLargeException{

        /* if there are <k words in sortedWords, k is too large */
        if(k > getSortedWords().size()){
            throw new kIsTooLargeException();
        }

        String[] mostCommonWords = new String[k];

        /* the last k words in the sorted list will be the k most common, so add them to array from back to front */
        for(int i = 0; i < mostCommonWords.length; i++){
            mostCommonWords[i] = getSortedWords().get(getSortedWords().size() - i - 1).getKey();
        }

        return mostCommonWords;
    }

    /**
     * Returns a String[] containing the k least common words in the text, in increasing order of their count.
     * @param k the number of least common words to retrieve
     * @return a String[] of the k least common words
     * @throws kIsTooLargeException if there are less than k words in the text, excluding duplicates
     */
    public String[] leastCommonWords(int k) throws kIsTooLargeException{
        
        /* if there are <k words in sortedWords, k is too large */
        if(k > getSortedWords().size()){
            throw new kIsTooLargeException();
        }

        String[] leastCommonWords = new String[k];

        /* the first k words in the sorted list will be the k least common, so add them to array from front to back */
        for(int i = 0; i < leastCommonWords.length; i++){
            leastCommonWords[i] = getSortedWords().get(i).getKey();
        }

        return leastCommonWords;
    }

    /**
     * Returns a String[] containing the k most common word pairs in the text, in decreasing order of their count.
     * @param k the number of most common word pairs to retrieve
     * @return a String[] of the k most common word pairs, with each element in the form "word1 word2"
     * @throws kIsTooLargeException if there are less than k word pairs in the text, including duplicates
     */
    public String[] mostCommonWordPairs(int k) throws kIsTooLargeException{
        
        /* if there are <k word pairs in sortedPairs, k is too large */
        if(k > getSortedPairs().size()){
            throw new kIsTooLargeException();
        }

        String[] mostCommonWordPairs = new String[k];

        /* the last k words in the sorted word pairs list will be the k most common, so add them to array from back to front */
        for(int i = 0; i < mostCommonWordPairs.length; i++){
            mostCommonWordPairs[i] = getSortedPairs().get(getSortedPairs().size() - i - 1).getKey();
        }

        return mostCommonWordPairs;
    }

    /**
     * Returns the k most common collocations of a base word, within one word.
     * @param k the number of collocations to retrieve
     * @param baseWord the word to obtain the collocations from
     * @param i the relative position to the base word; only i = 1 (directly following base word) or i = -1 (directly preceding base word) are supported
     * @return a String[] containing the k most common collocations of the base word
     * @throws kIsTooLargeException if k exceeds the number of word pairs in the text, or k exceeds the number of different collocations a word has
     * @throws UnsupportedOperationException if i is a number other than 1 or -1
     */
    public String[] mostCommonCollocs(int k, String baseWord, int i) throws kIsTooLargeException{

        if(!(i == 1 || i == -1)){
            throw new UnsupportedOperationException();
        }

        /* if there are <k word pairs in sortedPairs, k is too large */
        if(k > getSortedPairs().size()){
            throw new kIsTooLargeException();
        }

        String[] mostCommonCollocs = new String[k];

        if(i == 1){

            // start at the back of the list of pairs (most common)
            int pairsIndex = getSortedPairs().size() - 1;

            // and add to the front of the collocs list
            int collocsIndex = 0;

            while(collocsIndex < mostCommonCollocs.length){

                // if we've checked all the word pairs and there aren't enough to fill the array, then k is too large
                if(pairsIndex < 0){
                    throw new kIsTooLargeException();
                }

                String pair = getSortedPairs().get(pairsIndex).getKey();

                // add the other word in the pair to the list of collocs if the pair begins with base word
                if(pair.contains(baseWord + " ")){
                    mostCommonCollocs[collocsIndex] = pair.substring(baseWord.length() + 1);
                    collocsIndex++;
                }

                pairsIndex--;
            }

        }
        else if(i == -1){

            // start at the back of the list of pairs (most common)
            int pairsIndex = getSortedPairs().size() - 1;

            // and add to the front of the collocs list
            int collocsIndex = 0;

            while(collocsIndex < mostCommonCollocs.length){

                // if we've checked all the word pairs and there aren't enough to fill the array, then k is too large
                if(pairsIndex < 0){
                    throw new kIsTooLargeException();
                }

                String pair = getSortedPairs().get(pairsIndex).getKey();

                // add the other word in the pair to the list of collocs if the pair ends with base word
                if(pair.contains(" " + baseWord)){
                    mostCommonCollocs[collocsIndex] = pair.substring(0, pair.length() - baseWord.length() - 1);
                    collocsIndex++;
                }

                pairsIndex--;
            }
        }
        
        return mostCommonCollocs;
    }

    /**
     * A helper method to hash words from a Tokenizer's wordList into a HashTable.
     * @param t the Tokenizer to obtain the wordList from
     * @return the HashTable containing the words
     */
    private HashTable hashWords(Tokenizer t){
        HashTable wordTable = new HashTable();

        for(String word : t.wordList()){
            wordTable.put(word, 1);
        }

        return wordTable;
    }

    /**
     * A helper method to hash word pairs from a Tokenizer's wordList into a HashTable. 
     * @param t the Tokenizer to obtain the wordList from
     * @return the HashTable containing the words
     */
    private HashTable hashWordPairs(Tokenizer t){
        HashTable wordPairTable = new HashTable();

        for(int i = 0; i < t.wordList().size() - 1; i++){

            String wordPair = t.wordList().get(i) + " " + t.wordList().get(i + 1);
            wordPairTable.put(wordPair, 1);
    
        }
        
        return wordPairTable;
    }

    /**
     * A helper method to sort the entries in a HashTable.  For the purpose of word statistics, the 
     * position of each entry is it's word's rank, in reverse order.  For example, the word with
     * rank 1 (most frequently occurring) is in the last index of the list, and word with the lowest rank
     * (least frequently occuring) is at the beginning of the list.
     * @param hashTable the table to obtain the entries to sort from
     * @return the list of sorted entries
     * @throws NoSuchMethodException if the getTable method used to gain access to the HashEntries cannot be found
     * @throws SecurityException if there is a security exception in reflection of getTable method
     * @throws IllegalAccessException when the getTable method used to gain access to the HashEntries cannot be accessed
     * @throws IllegalArgumentException when there is an illegal argument in the reflection of the getTable method
     * @throws InvocationTargetException when there is an exception thrown by the reflected getTable method
     */
    private ArrayList<HashEntry> sortEntries(HashTable hashTable) throws NoSuchMethodException, SecurityException, 
                                                                    IllegalAccessException, IllegalArgumentException, 
                                                                    InvocationTargetException{
        HashEntry[] table = WordStat.reflectTable(hashTable);
        ArrayList<HashEntry> sortedEntries = new ArrayList<HashEntry>();

        for(HashEntry entry : table){
            if(entry != null){
                sortedEntries.add(entry);

                if(entry.hasChain()){
                    sortedEntries.addAll(entry.getChain());
                }
            }
        }

        Collections.sort(sortedEntries);

        return sortedEntries;
    }

    /**
     * A helper method to be used in conjunction with sortEntries. In order to support random access to the rank
     * of each word, this method is to be used to hash each word with its rank stored as its HashEntry's value.
     * @param sortedEntries the list of sorted entries
     * @return a HashTable where each HashEntry's value is its rank
     */
    private HashTable hashSortedEntries(ArrayList<HashEntry> sortedEntries){
        HashTable rankTable = new HashTable();

        for(int i = 0; i < sortedEntries.size(); i++){
            rankTable.put(sortedEntries.get(i).getKey(), sortedEntries.size() - i);
        }

        return rankTable;
    }

    /**
     * A helper method to access the private table field in HashTable using Java reflection.
     * @param test the HashTable to retrieve the table field from
     * @return the table field of the specified HashTable
     * @throws NoSuchMethodException when the method cannot be found
     * @throws SecurityException when there is a security violation
     * @throws IllegalAccessException when the specified class, method, constructor, or field cannot be accessed
     * @throws IllegalArgumentException when a Method is passed an illegal argument
     * @throws InvocationTargetException when there is an exception thrown by the invoked method
     */
    private static HashEntry[] reflectTable(HashTable hashTable) throws NoSuchMethodException, SecurityException, IllegalAccessException, 
                                                            IllegalArgumentException, InvocationTargetException{
        Method getTable = hashTable.getClass().getDeclaredMethod("getTable");
        getTable.setAccessible(true);
        return (HashEntry[])(getTable.invoke(hashTable));
    }


    private HashTable getWordTable() {
        return wordTable;
    }

    private HashTable getWordPairTable() {
        return wordPairTable;
    }

    private ArrayList<HashEntry> getSortedWords(){
        return sortedWords;
    }

    private ArrayList<HashEntry> getSortedPairs(){
        return sortedPairs;
    }

    private HashTable getWordRankTable(){
        return wordRankTable;
    }

    private HashTable getPairRankTable(){
        return this.pairRankTable;
    }

    public String getPath(){
        return path;
    }

    public static void main(String[] args) throws NoSuchMethodException, SecurityException, 
                                            IllegalAccessException, IllegalArgumentException, InvocationTargetException, 
                                            FileNotFoundException, IOException, NoSuchElementException, kIsTooLargeException{
        String[] inputWords = new String[]{"The", "Fitnessgram", "Pacer", "Test"};
        WordStat demo = new WordStat(inputWords);
        System.out.print("\nA demonstration of the WordStat class.  WordStat takes a text, normalizes its words, and computes various statistics on those words.\n\n" +
                ">String[] inputWords = new String[]{\"The\", \"Fitnessgram\", \"Pacer\", \"Test\"}\n" +
                ">WordStat demo = new WordStat(inputWords)\n" +
                ">demo.wordCount(fitnessgram)\n" +
                ">" + demo.wordCount("fitnessgram") + "\n" +
                "A WordStat instance can be created using a String[] as input.\n\n" + 

                ">demo.getPath()\n" +
                ">" + demo.getPath() + "\n" +
                "getPath() allows the user to get the path of WordStat's .java file, to allow for easy file input as long as .txt files are in\n" +
                "a directory specified in the CLASSPATH environmental variable.\n\n"
        );
        demo = new WordStat(demo.getPath() + "\\HolyGrail.txt");
        System.out.print(
                ">demo = new WordStat(demo.getPath() + \"\\HolyGrail.txt\")\n" +
                "A WordStat instance can also be created using a .txt file as input.  Here, we'll use the script from Monty Python and the Holy Grail.\n\n" +

                ">demo.wordCount(\"ni\")\n" +
                ">" + demo.wordCount("ni") + "\n" +
                "wordCount(String word) can be used to obtain the count of any word, and zero if the word does not appear in the text.\n" +
                "We can check to see how many times the Knights say \"ni\" this way.\n\n" +

                ">demo.wordPairCount(\"unladen\", \"swallow\")\n" +
                ">" + demo.wordPairCount("unladen", "swallow") + "\n" +
                "wordPairCount(String w1, String w2) can be used to obtain the count of any word pair, and zero if the word does not appear in the text.\n\n" +

                ">demo.wordRank(\"shrubbery\")\n" +
                ">" + demo.wordRank("shrubbery") + "\n" +
                "wordRank(String word) can be used to obtain the rank of a word, where 1 is the rank of the most common word.\n\n" +

                ">demo.wordPairRank(\"a\", \"witch\")\n" +
                ">" + demo.wordPairRank("a", "witch") + "\n" +
                "wordPairRank(String w1, String w2) can be used to obtain the rank of a word pair, where 1 is the rank of the most common word pair.\n\n" +

                ">import java.util.Arrays\n" +
                ">Arrays.toString(demo.mostCommonWords(5))\n" +
                ">" + Arrays.toString(demo.mostCommonWords(5)) + "\n" +
                "mostCommonWords(int k) returns the k most common words in the text in decreasing order of their count.\n\n" +

                ">Arrays.toString(demo.leastCommonWords(5))\n" +
                ">" + Arrays.toString(demo.leastCommonWords(5)) + "\n" +
                "leastCommonWords(int k) returns the k least common words in the text in increasing order of their count.\n\n" + 

                ">Arrays.toString(demo.mostCommonWordPairs(5))\n" +
                ">" + Arrays.toString(demo.mostCommonWordPairs(5)) + "\n" +
                "mostCommonWordPairs(int k) returns the k most common word pairs in the text in decreasing order of their count.\n\n" +

                ">Arrays.toString(demo.mostCommonCollocs(3, \"sir\", 1))\n" + 
                ">" + Arrays.toString(demo.mostCommonCollocs(3, "sir", 1)) + "\n" +
                ">Arrays.toString(demo.mostCommonCollocs(2, \"grail\", -1))\n" +
                ">" + Arrays.toString(demo.mostCommonCollocs(2, "grail", -1)) + "\n" +
                "mostCommonCollocs(int k, String baseWord, int i) returns the k most common following (i = 1) or preceding (i = -1) collocations of a base word."
        );
    }
}
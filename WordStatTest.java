import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * A class to test WordStat.  Conditions for each test are found on the right.
 * @author Josh Hager
 */
public class WordStatTest {
    
    // get the current path of this .java file, **MAKE SURE .TXT FILES ARE IN SAME FOLDER**
    public String path = this.getClass().getClassLoader().getResource("").getPath();
                                                                                        //CONDITION:
    @Test
    public void testWordCount() throws FileNotFoundException, IOException, NoSuchMethodException, 
                                    SecurityException, IllegalAccessException, IllegalArgumentException, 
                                    InvocationTargetException{
        String[] inputWords = new String[0];
        WordStat test = new WordStat(inputWords);

        assertEquals(0, test.wordCount("bulbasaur"));                                   //zero words

        inputWords = new String[]{"bulbasaur"};
        test = new WordStat(inputWords);
        assertEquals(1, test.wordCount("bulbasaur"));                                   //1 word

        assertEquals(0, test.wordCount("charizard"));                                   //1 word, word is not in table

        inputWords = new String[]{"bulbasaur", "squirtle"};
        test = new WordStat(inputWords);
        assertEquals(1, test.wordCount("bulbasaur"));
        assertEquals(1, test.wordCount("squirtle"));                                    //>1 word

        assertEquals(0, test.wordCount("charizard"));                                   //>1 word, word is not in table

        inputWords = new String[]{"bulbasaur", "squirtle", "charmander"};
        test = new WordStat(inputWords);
        assertEquals(1, test.wordCount("bulbasaur"));                                   //>1 word, word is first in wordList

        inputWords = new String[]{"squirtle", "bulbasaur", "charmander"};
        test = new WordStat(inputWords);
        assertEquals(1, test.wordCount("bulbasaur"));                                   //>1 word, word is in middle of wordList

        inputWords = new String[]{"charmander", "squirtle", "bulbasaur"};
        test = new WordStat(inputWords);
        assertEquals(1, test.wordCount("bulbasaur"));                                   //>1 word, word is last in wordList

        inputWords = new String[]{"bulbasaur", "charmander", "squirtle", 
                                    "bulbasaur"};
        test = new WordStat(inputWords);
        assertEquals(2, test.wordCount("bulbasaur"));                                   //>1 word, 1 word has >1 occurrences

        inputWords = new String[]{"bulbasaur", "charmander", "charmander", 
                                    "squirtle", "bulbasaur"};
        test = new WordStat(inputWords);
        assertEquals(2, test.wordCount("bulbasaur")); 
        assertEquals(2, test.wordCount("charmander"));                                  //>1 word, >1 word has >1 occurrences

        test = new WordStat(path + "\\MultipleLines.txt");
        assertEquals(3, test.wordCount("the"));                                         //make sure files can be read
    }

    @Test
    public void testWordPairCount() throws FileNotFoundException, IOException, NoSuchMethodException, 
                                        SecurityException, IllegalAccessException, IllegalArgumentException, 
                                        InvocationTargetException{
        String[] inputWords = new String[0];
        WordStat test = new WordStat(inputWords);

        assertEquals(0, test.wordPairCount("hello", "world"));                          //zero words

        inputWords = new String[]{"hello"};
        test = new WordStat(inputWords);
        assertEquals(0, test.wordPairCount("hello", "world"));                          //one word

        inputWords = new String[]{"hello", "world"};
        test = new WordStat(inputWords);
        assertEquals(1, test.wordPairCount("hello", "world"));                          //one word pair

        inputWords = new String[]{"hello", "world", "my",
                                    "name", "is", "quandale"};
        test = new WordStat(inputWords);
        assertEquals(1, test.wordPairCount("hello", "world"));                          //>1 word pair, pair is first

        assertEquals(1, test.wordPairCount("my", "name"));                              //>1 word pair, pair is in middle

        assertEquals(1, test.wordPairCount("is", "quandale"));                          //>1 word pair, pair is last

        inputWords = new String[]{"quandale", "dingle", "hello", "world", "my",
                                    "name", "is", "quandale", "dingle"};                
        test = new WordStat(inputWords);
        assertEquals(2, test.wordPairCount("quandale", "dingle"));                      //>1 word pair, 1 pair has multiple occurrences

        inputWords = new String[]{"quandale", "dingle", "hello", "world", "my",
                                    "name", "hello", "world", "is", "quandale", "dingle"};                
        test = new WordStat(inputWords);
        assertEquals(2, test.wordPairCount("quandale", "dingle"));
        assertEquals(2, test.wordPairCount("hello", "world"));                          //>1 word pair, >1 pairs have multiple occurrences

        assertEquals(0, test.wordPairCount("case", "western"));                         //word pair count does not exist

        test = new WordStat(path + "\\MultipleLines.txt");
        assertEquals(1, test.wordPairCount("the", "fitnessgram"));                      //make sure files can be read
    }

    @Test
    public void testWordRank() throws NoSuchMethodException, SecurityException, IllegalAccessException, 
                                IllegalArgumentException, InvocationTargetException, FileNotFoundException, IOException{
        String[] inputWords = new String[0];
        WordStat test = new WordStat(inputWords);

        try{                                                          
            test.wordRank("hello");
            fail("No exception thrown when zero words");
        }
        catch(NoSuchElementException e){
            /* correct exception was thrown */
        }
        catch(Exception e){
            fail("Wrong type of exception thrown when zero words");                     //zero words
        }

        inputWords = new String[]{"hello"};
        test = new WordStat(inputWords);
        assertEquals(1, test.wordRank("hello"));                                        //one word

        inputWords = new String[]{"hello", "hello", "world"};
        test = new WordStat(inputWords);
        assertEquals(1, test.wordRank("hello"));
        assertEquals(2, test.wordRank("world"));                                        //>1 word

        try{                                                          
            test.wordRank("quandale");
            fail("No exception thrown when word does not exist");
        }
        catch(NoSuchElementException e){
            /* correct exception was thrown */
        }
        catch(Exception e){
            fail("Wrong type of exception thrown when word does not exist");            //word does not exist
        }

        test = new WordStat(path + "\\UniqueWordCounts.txt");
        assertEquals(1, test.wordRank("hello"));
        assertEquals(2, test.wordRank("my"));
        assertEquals(3, test.wordRank("name"));
        assertEquals(4, test.wordRank("is"));
        assertEquals(5, test.wordRank("quandale"));                                     //read input file
    }

    @Test
    public void testWordPairRank() throws NoSuchMethodException, SecurityException, IllegalAccessException, 
                                        IllegalArgumentException, InvocationTargetException, FileNotFoundException, IOException{
        String[] inputWords = new String[0];
        WordStat test = new WordStat(inputWords);

        try{                                                          
            test.wordPairRank("hello", "world");
            fail("No exception thrown when zero words, zero word pairs");
        }
        catch(NoSuchElementException e){
            /* correct exception was thrown */
        }
        catch(Exception e){
            fail("Wrong type of exception thrown when zero words, zero word pairs");     //zero words, zero word pairs
        }

        inputWords = new String[]{"hello"};
        test = new WordStat(inputWords);
        try{                                                          
            test.wordPairRank("hello", "world");
            fail("No exception thrown when one word, zero word pairs");
        }
        catch(NoSuchElementException e){
            /* correct exception was thrown */
        }
        catch(Exception e){
            fail("Wrong type of exception thrown when one word, zero word pairs");       //one word, zero word pairs
        }

        inputWords = new String[]{"hello", "world"};
        test = new WordStat(inputWords);
        assertEquals(1, test.wordPairRank("hello", "world"));                           //one word pair

        inputWords = new String[]{"hello", "world", "hello", "world"};
        test = new WordStat(inputWords);
        assertEquals(1, test.wordPairRank("hello", "world"));                           
        assertEquals(2, test.wordPairRank("world", "hello"));                           //>1 word pair

        try{                                                          
            test.wordPairRank("quandale", "dingle");
            fail("No exception thrown when word pair does not exist");
        }
        catch(NoSuchElementException e){
            /* correct exception was thrown */
        }
        catch(Exception e){
            fail("Wrong type of exception thrown when word pair does not exist");       //word pair does not exist
        }

        test = new WordStat(path + "\\UniqueWordPairCounts.txt");
        assertEquals(1, test.wordPairRank("hello", "world"));                           
        assertEquals(2, test.wordPairRank("world", "hello"));
        assertEquals(3, test.wordPairRank("world", "my"));                              //read input from file       
    }

    @Test
    public void testMostCommonWords() throws NoSuchMethodException, SecurityException, IllegalAccessException, 
                                        IllegalArgumentException, InvocationTargetException, kIsTooLargeException, FileNotFoundException, IOException{
        String[] inputWords = new String[0];
        WordStat test = new WordStat(inputWords);

        try{                                                          
            test.mostCommonWords(3);
            fail("No exception thrown when zero words");
        }
        catch(kIsTooLargeException e){
            /* correct exception was thrown */
        }
        catch(Exception e){
            fail("Wrong type of exception thrown when zero words");                      //zero words
        }

        inputWords = new String[]{"hello"};
        test = new WordStat(inputWords);
        assertEquals("[hello]", Arrays.toString(test.mostCommonWords(1)));              //one word

        inputWords = new String[]{"hello", "hello", "world"};
        test = new WordStat(inputWords);
        assertEquals("[hello]", Arrays.toString(test.mostCommonWords(1)));              
        assertEquals("[hello, world]", Arrays.toString(test.mostCommonWords(2)));       //>1 word

        try{                                                          
            test.mostCommonWords(3);
            fail("No exception thrown when not enough words");
        }
        catch(kIsTooLargeException e){
            /* correct exception was thrown */
        }
        catch(Exception e){
            fail("Wrong type of exception thrown when not enough words");               //not enough words
        }

        test = new WordStat(path + "\\UniqueWordCounts.txt");
        assertEquals("[hello]", Arrays.toString(test.mostCommonWords(1)));              
        assertEquals("[hello, my]", Arrays.toString(test.mostCommonWords(2)));
        assertEquals("[hello, my, name]", Arrays.toString(test.mostCommonWords(3)));
        assertEquals("[hello, my, name, is]", Arrays.toString(test.mostCommonWords(4)));
        assertEquals("[hello, my, name, is, quandale]", Arrays.toString(test.mostCommonWords(5)));      //read input from file
    }

    @Test
    public void testLeastCommonWords() throws NoSuchMethodException, SecurityException, IllegalAccessException, 
                                        IllegalArgumentException, InvocationTargetException, kIsTooLargeException, 
                                        FileNotFoundException, IOException{
        String[] inputWords = new String[0];
        WordStat test = new WordStat(inputWords);

        try{                                                          
            test.leastCommonWords(3);
            fail("No exception thrown when zero words");
        }
        catch(kIsTooLargeException e){
            /* correct exception was thrown */
        }
        catch(Exception e){
            fail("Wrong type of exception thrown when zero words");                      //zero words
        }

        inputWords = new String[]{"hello"};
        test = new WordStat(inputWords);
        assertEquals("[hello]", Arrays.toString(test.leastCommonWords(1)));              //one word

        inputWords = new String[]{"hello", "hello", "world"};
        test = new WordStat(inputWords);
        assertEquals("[world]", Arrays.toString(test.leastCommonWords(1)));              
        assertEquals("[world, hello]", Arrays.toString(test.leastCommonWords(2)));        //>1 word
        
        try{                                                          
            test.leastCommonWords(3);
            fail("No exception thrown when not enough words");
        }
        catch(kIsTooLargeException e){
            /* correct exception was thrown */
        }
        catch(Exception e){
            fail("Wrong type of exception thrown when not enough words");               //not enough words
        }

        test = new WordStat(path + "\\UniqueWordCounts.txt");
        assertEquals("[quandale]", Arrays.toString(test.leastCommonWords(1)));              
        assertEquals("[quandale, is]", Arrays.toString(test.leastCommonWords(2)));
        assertEquals("[quandale, is, name]", Arrays.toString(test.leastCommonWords(3)));
        assertEquals("[quandale, is, name, my]", Arrays.toString(test.leastCommonWords(4)));
        assertEquals("[quandale, is, name, my, hello]", Arrays.toString(test.leastCommonWords(5)));      //read input from file
    }

    @Test
    public void testMostCommonWordPairs() throws NoSuchMethodException, SecurityException, IllegalAccessException, 
                                            IllegalArgumentException, InvocationTargetException, kIsTooLargeException, FileNotFoundException, IOException{
        String[] inputWords = new String[0];
        WordStat test = new WordStat(inputWords);

        try{                                                          
            test.mostCommonWordPairs(3);
            fail("No exception thrown when zero words");
        }
        catch(kIsTooLargeException e){
            /* correct exception was thrown */
        }
        catch(Exception e){
            fail("Wrong type of exception thrown when zero words");                      //zero words
        }

        inputWords = new String[]{"hello"};
        test = new WordStat(inputWords);
        try{                                                          
            test.mostCommonWordPairs(1);
            fail("No exception thrown when one word, zero word pairs");
        }
        catch(kIsTooLargeException e){
            /* correct exception was thrown */
        }
        catch(Exception e){
            fail("Wrong type of exception thrown when one word, zero word pairs");       //one word, zero word pairs
        }

        inputWords = new String[]{"hello", "world"};
        test = new WordStat(inputWords);
        assertEquals("[hello world]", Arrays.toString(test.mostCommonWordPairs(1)));     //one word pair

        inputWords = new String[]{"hello", "world", "hello", "world"};
        test = new WordStat(inputWords);
        assertEquals("[hello world]", Arrays.toString(test.mostCommonWordPairs(1)));     
        assertEquals("[hello world, world hello]", Arrays.toString(test.mostCommonWordPairs(2)));   //>1 word pair

        try{                                                          
            test.mostCommonWordPairs(3);
            fail("No exception thrown when not enough word pairs");
        }
        catch(kIsTooLargeException e){
            /* correct exception was thrown */
        }
        catch(Exception e){
            fail("Wrong type of exception thrown when not enough word pairs");           //not enough word pairs
        }

        test = new WordStat(path + "\\UniqueWordPairCounts.txt");
        assertEquals("[hello world]", Arrays.toString(test.mostCommonWordPairs(1)));     
        assertEquals("[hello world, world hello]", Arrays.toString(test.mostCommonWordPairs(2)));     
        assertEquals("[hello world, world hello, world my]", Arrays.toString(test.mostCommonWordPairs(3)));   //read input from file
    }

    @Test
    public void testMostCommonCollocs() throws NoSuchMethodException, SecurityException, IllegalAccessException, 
                                            IllegalArgumentException, InvocationTargetException, kIsTooLargeException, FileNotFoundException, IOException{
        String[] inputWords = new String[0];
        WordStat test = new WordStat(inputWords);

        try{                                                          
            test.mostCommonCollocs(1, "hello", 1);
            fail("No exception thrown when zero words");
        }
        catch(kIsTooLargeException e){
            /* correct exception was thrown */
        }
        catch(Exception e){
            fail("Wrong type of exception thrown when zero words");                             //i = 1, zero words
        }

        inputWords = new String[]{"hello"};
        test = new WordStat(inputWords);
        try{                                                          
            test.mostCommonCollocs(1, "hello", 1);
            fail("No exception thrown when one word");
        }
        catch(kIsTooLargeException e){
            /* correct exception was thrown */
        }
        catch(Exception e){
            fail("Wrong type of exception thrown when one word");                               //i = 1, one word
        }

        inputWords = new String[]{"hello", "my"};
        test = new WordStat(inputWords);
        assertEquals("[my]", Arrays.toString(test.mostCommonCollocs(1, "hello", 1)));           //one colloc, i = 1

        inputWords = new String[]{"hello", "my", "name", "hello", "is", "hello", "my", "reginald"};
        test = new WordStat(inputWords);
        assertEquals("[my, is]", Arrays.toString(test.mostCommonCollocs(2, "hello", 1)));       //>1 colloc, i = 1

        try{                                                          
            test.mostCommonCollocs(1, "reginald", 1);
            fail("No exception thrown when word has no collocs");
        }
        catch(kIsTooLargeException e){
            /* correct exception was thrown */
        }
        catch(Exception e){
            fail("Wrong type of exception thrown when word has no collocs");                    //i = 1, word has no collocs
        }

        try{                                                          
            test.mostCommonCollocs(3, "hello", 1);
            fail("No exception thrown when not enough collocs");
        }
        catch(kIsTooLargeException e){
            /* correct exception was thrown */
        }
        catch(Exception e){
            fail("Wrong type of exception thrown when not enough collocs");                     //i = 1, not enough collocs
        }

        inputWords = new String[0];
        test = new WordStat(inputWords);
        try{                                                          
            test.mostCommonCollocs(1, "hello", -1);
            fail("No exception thrown when zero words");
        }
        catch(kIsTooLargeException e){
            /* correct exception was thrown */
        }
        catch(Exception e){
            fail("Wrong type of exception thrown when zero words");                             //i = -1, zero words
        }

        inputWords = new String[]{"hello"};
        test = new WordStat(inputWords);
        try{                                                          
            test.mostCommonCollocs(1, "hello", -1);
            fail("No exception thrown when one word");
        }
        catch(kIsTooLargeException e){
            /* correct exception was thrown */
        }
        catch(Exception e){
            fail("Wrong type of exception thrown when one word");                               //i = -1, one word
        }

        inputWords = new String[]{"hello", "my"};
        test = new WordStat(inputWords);
        assertEquals("[hello]", Arrays.toString(test.mostCommonCollocs(1, "my", -1)));          //1 colloc, i = -1

        inputWords = new String[]{"quandale", "hello", "my", "name", "walter", "my", "hello", "my"};
        test = new WordStat(inputWords);
        assertEquals("[hello]", Arrays.toString(test.mostCommonCollocs(1, "my", -1)));
        assertEquals("[hello, walter]", Arrays.toString(test.mostCommonCollocs(2, "my", -1)));  //>1 colloc, i = -1

        try{                                                          
            test.mostCommonCollocs(1, "quandale", -1);
            fail("No exception thrown when word has no collocs");
        }
        catch(kIsTooLargeException e){
            /* correct exception was thrown */
        }
        catch(Exception e){
            fail("Wrong type of exception thrown when word has no collocs");                    //i = -1, word has no collocs
        }

        try{                                                          
            test.mostCommonCollocs(3, "my", -1);
            fail("No exception thrown when not enough collocs");
        }
        catch(kIsTooLargeException e){
            /* correct exception was thrown */
        }
        catch(Exception e){
            fail("Wrong type of exception thrown when not enough collocs");                     //i = -1, not enough collocs
        }

        try{                                                          
            test.mostCommonCollocs(3, "hello", 12);
            fail("No exception thrown when i != 1 or -1");
        }
        catch(UnsupportedOperationException e){
            /* correct exception was thrown */
        }
        catch(Exception e){
            fail("Wrong type of exception thrown when i != 1 or -1");                           //i != -1 or 1
        }

        test = new WordStat(path + "\\Collocs.txt");
        assertEquals("[my, name, is]", Arrays.toString(test.mostCommonCollocs(3, "hello", 1)));   //read from a file
    }
}

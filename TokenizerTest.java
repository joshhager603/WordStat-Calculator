import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A class to test Tokenizer. Conditions for each test are found on the right.
 * Ensure that the proper .txt files are in the same folder as this .java file.
 * @author Josh Hager
 */
public class TokenizerTest {

    // get the current path of this .java file, **MAKE SURE .TXT FILES ARE IN SAME FOLDER**
    public String path = this.getClass().getClassLoader().getResource("").getPath();
                                                                                                //CONDITION:
    @Test
    public void testFirstConstructor() throws FileNotFoundException, IOException{
        Tokenizer test = new Tokenizer(path + "\\Empty.txt");

        assertEquals("[]", test.wordList().toString());                                         //empty file

        test = new Tokenizer(path + "\\OneCharacter.txt");
        assertEquals("[a]", test.wordList().toString());                                        //file with one character

        test = new Tokenizer(path + "\\OneLine.txt");
        assertEquals("[the, fitnessgram, pacer, test, is, a, multistage, aerobic, " +
                        "capacity, test, that, progressively, gets, more, difficult, " +
                        "as, it, continues]", test.wordList().toString());                      //file with >1 character, 1 line

        test = new Tokenizer(path + "\\MultipleLines.txt");
        assertEquals("[the, fitnessgram, pacer, test, is, a, multistage, aerobic, " +
                        "capacity, test, that, progressively, gets, more, difficult, " +
                        "as, it, continues, the, meter, pacer, test, will, begin, in, " + 
                        "seconds, line, up, at, the, start]", test.wordList().toString());      //file with >1 lines
    }

    @Test
    public void testSecondConstructor(){
        String[] inputWords = new String[]{""};
        Tokenizer test = new Tokenizer(inputWords);

        assertEquals("[]", test.wordList().toString());                                         //empty array

        inputWords = new String[]{"And"};
        test = new Tokenizer(inputWords);
        assertEquals("[and]", test.wordList().toString());                                      //array length 1

        inputWords = new String[]{"The", "FitnessGram", "Pacer", "Test"};
        test = new Tokenizer(inputWords);
        assertEquals("[the, fitnessgram, pacer, test]", test.wordList().toString());            //array length >1 
    }

    @Test
    public void testWordList() throws FileNotFoundException, IOException{
        Tokenizer test = new Tokenizer(path + "\\OneLine.txt");

        assertEquals("[the, fitnessgram, pacer, test, is, a, multistage, aerobic, " +
                        "capacity, test, that, progressively, gets, more, difficult, " +
                        "as, it, continues]", test.wordList().toString());                      //should return test's wordList
    }


    @Test
    public void testNormalize(){
        String test = "";

        assertEquals("", Tokenizer.normalize(test));                                            //empty string

        test = "a";
        assertEquals("a", Tokenizer.normalize(test));                                           //string length 1

        test = "and";
        assertEquals("and", Tokenizer.normalize(test));                                         //string length >1

        test = "!";
        assertEquals("", Tokenizer.normalize(test));                                            //1 illegal character

        test = "!!";
        assertEquals("", Tokenizer.normalize(test));                                            //>1 illegal character

        test = "!its";
        assertEquals("its", Tokenizer.normalize(test));                                        //1 illegal character as first character

        test = "i!ts";
        assertEquals("its", Tokenizer.normalize(test));                                        //1 illegal character as middle character
        
        test = "its!";
        assertEquals("its", Tokenizer.normalize(test));                                        //1 illegal character as last character

        test = "!i!ts!";
        assertEquals("its", Tokenizer.normalize(test));                                        //>1 illegal character mixed with legal characters

        test = "A";
        assertEquals("a", Tokenizer.normalize(test));                                           //1 capital letter

        test = "And";
        assertEquals("and", Tokenizer.normalize(test));                                         //1 capital letter as first character

        test = "aNd";
        assertEquals("and", Tokenizer.normalize(test));                                         //1 capital letter as middle character

        test = "anD";
        assertEquals("and", Tokenizer.normalize(test));                                         //1 capital letter as last character

        test = "AnD";
        assertEquals("and", Tokenizer.normalize(test));                                         //>1 capital letter
    }
}

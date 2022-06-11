import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Reads text from a file, normalizes the words, and inserts them into an ArrayList.
 * @author Josh Hager
 */
public class Tokenizer {

    private ArrayList<String> wordList = new ArrayList<String>();

    /**
     * Creates a new Tokenizer, obtains and normalizes words from the specified file, and enters them into the Tokenizer's wordList.
     * @param fileName the name of the file to read the words from
     * @throws FileNotFoundException if the file cannot be found
     * @throws IOException if an I/O error occurs
     */
    public Tokenizer(String fileName) throws FileNotFoundException, IOException{
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = reader.readLine();

        while(line != null){ 
            String[] lineArray = line.split("\s");

            for(String word : lineArray){
                word = Tokenizer.normalize(word);

                if(word != ""){
                    wordList.add(word);
                }
            }

            line = reader.readLine();
        }

        reader.close();
    }

    /**
     * Creates a new Tokenizer, normalizes the words in the inputted String array, and enters them into the Tokenizer's wordList.
     * @param inputWords the array of input words
     */
    public Tokenizer(String[] inputWords){
        for(String word : inputWords){
            word = Tokenizer.normalize(word);

            if(word != ""){
                wordList.add(word);
            }
        }
    }

    /**
     * Returns this Tokenizer's wordList.
     * @return this Tokenizer's wordList
     */
    public ArrayList<String> wordList(){
        return wordList;
    }

    /**
     * Normalizes a given String.
     * @param word the String/word to be normalized
     * @return the normalized String/word
     */
    public static String normalize(String word){
        word = word.toLowerCase();

        StringBuilder b = new StringBuilder();
         for(int i = 0; i < word.length(); i++){
            if(Character.isLetter(word.charAt(i))){
                b.append(word.charAt(i));
            }
        }

        return b.toString();
    }
}

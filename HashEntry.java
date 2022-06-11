import java.util.LinkedList;

/**
 * An entry for the HashTable class.
 * @author Josh Hager
 */
public class HashEntry implements Comparable<HashEntry>{

    private String key;

    private int value;

    /* used for closed addressing in HashTable */
    private LinkedList<HashEntry> chain;

    public HashEntry(String key, int value){
        this.key = key;
        this.value = value;
    }

    @Override
    public int compareTo(HashEntry entry) {
        return this.getValue() - entry.getValue();
    }

    public String getKey(){
        return key;
    }

    public int getValue(){
        return value;
    }

    public void setValue(int value){
        this.value = value;
    }

    public LinkedList<HashEntry> getChain(){
        return chain;
    }

    public void setChain(LinkedList<HashEntry> chain){
        this.chain = chain;
    }

    public boolean hasChain(){
        return chain != null;
    }
}

import java.util.LinkedList;

/**
 * A hash table containing String keys and int values.  Closed addressing with chaining is used to handle collisions.
 * On a collision where keys match, the value of the current entry with that key is increased by one rather than adding a new entry.
 * @author Josh Hager 
 */
public class HashTable {

    private HashEntry[] table;

    private double loadFactor = .75;

    /* the number of entries in the table, not counting entries in chains */
    private int slotsFilled = 0;

    /**
     * Creates a new HashTable with default size 100.
     */
    public HashTable() {
        table = new HashEntry[100];
    }

    /**
     * Creates a new HashTable with a specified size.
     * @param size the specified size
     */
    public HashTable(int size) {
        table = new HashEntry[size];
    }

    /**
     * Puts a key-value pair into the hash table using Java's hashCode() function.  In the event of a collision where keys match,
     * the value of that key's HashEntry is increased by 1.
     * @param key the key to be entered into the hash table
     * @param value the value associated with the key
     */
    public void put(String key, int value) {
        put(key, value, key.hashCode());
    }

    /**
     * Puts a key-value pair into the hash table using a specified hash code.  In the event of a collision where keys match,
     * the value of that key's HashEntry is increased by 1.
     * @param key the key to be entered into the hash table
     * @param value the value associated with the key
     * @param hashCode the hash code to be used to place the key
     */
    public void put(String key, int value, int hashCode) {

        if(table.length == 0){
            rehash();
        }

        if (((double)slotsFilled / (double)table.length) > loadFactor) {
            rehash();
        }

        int i = Math.abs(hashCode) % table.length;

        if (table[i] == null) {
            table[i] = new HashEntry(key, value);
            slotsFilled++;
        } 
        else if (table[i].getKey().equals(key)) {
            table[i].setValue(table[i].getValue() + 1);
        } 
        else if (!table[i].hasChain()) {
            table[i].setChain(new LinkedList<HashEntry>());
            table[i].getChain().add(new HashEntry(key, value));
        } 
        else {
            boolean keyFound = false;

            for (HashEntry entry : table[i].getChain()) {
                if (entry.getKey().equals(key)) {
                    entry.setValue(entry.getValue() + 1);
                    keyFound = true;
                }
            }

            if (!keyFound) {
                table[i].getChain().add(new HashEntry(key, value));
            }
        }
    }

    /**
     * Updates the value associated with a given key.  If the key is not in the hash table, it is added.
     * @param key the key
     * @param value the new value associated with the key
     */
    public void update(String key, int value) {
        HashEntry targetEntry = getHashEntry(key, key.hashCode());

        if (targetEntry != null) {
            targetEntry.setValue(value);
        } 
        else {
            put(key, value);
        }
    }

    /**
     * Returns the value of a given key, searching using Java's hashCode() function.
     * @param key the key to look for
     * @return that key's value, -1 if key is not in table
     */
    public int get(String key) {
        return get(key, key.hashCode());
    }

    /**
     * Returns the value of a given key, searching using a specified hash code.
     * @param key the key to look for
     * @param hashCode the hash code to search for the key with
     * @return that key's value, -1 if key is not in table
     */
    public int get(String key, int hashCode) {
        HashEntry targetEntry = getHashEntry(key, hashCode);

        if (targetEntry == null) {
            return -1;
        }

        return targetEntry.getValue();
    }

    /**
     * Returns the HashEntry in the table with the specified key.
     * @param key the key to search for
     * @param hashCode the hash code used to find the key
     * @return the HashEntry of the specified key, null if that key is not in table
     */
    private HashEntry getHashEntry(String key, int hashCode){

        if(table.length == 0){
            return null;
        }

        int i = Math.abs(hashCode) % table.length;

        if(table[i] == null){
            return null;
        } 
        else if(table[i].getKey().equals(key)){
            return table[i];
        } 
        else if(table[i].hasChain()){

            for (HashEntry entry : table[i].getChain()){
                if (entry.getKey().equals(key)){
                    return entry;
                }
            }

        }
        return null;
    }

    /**
     * Rehashes the hash table.
     */
    public void rehash(){
        if (table.length != 0){
            HashTable newTable = new HashTable(table.length * 2);

            for (HashEntry entry : table){
                if(entry != null){

                    newTable.put(entry.getKey(), entry.getValue());

                    if (entry.hasChain()){
                        for (HashEntry chainEntry : entry.getChain()) {
                            newTable.put(chainEntry.getKey(), chainEntry.getValue());
                        }
                    }

                }
            }

            setTable(newTable.getTable());
            setSlotsFilled(newTable.getSlotsFilled());
        }
        else{
            setTable(new HashEntry[100]);
        }
    }

    /**
     * Gets the load factor of this hash table.
     * @return the load factor of this hash table
     */
    public double getLoadFactor() {
        return loadFactor;
    }

    /**
     * Sets the load factor of this hash table.
     * @param loadFactor the new load factor
     */
    public void setLoadFactor(double loadFactor) {
        this.loadFactor = loadFactor;
    }

    /**
     * Gets the HashEntry[] representing this hash table.
     * @return the HashEntry[] representing this hash table.
     */
    private HashEntry[] getTable() {
        return table;
    }

    /**
     * Sets the HashEntry[] representing this hash table.
     * @param table the new HashEntry[]
     */
    private void setTable(HashEntry[] table) {
        this.table = table;
    }

    /**
     * Gets the number of slots that are filled in this hash table's table.
     * @return the number of slots that are filled in this hash table's table
     */
    private int getSlotsFilled() {
        return slotsFilled;
    }

    /**
     * Sets the number of slots that are filled in this hash table's table.
     * @param slotsFilled the new number of slots filled
     */
    private void setSlotsFilled(int slotsFilled) {
        this.slotsFilled = slotsFilled;
    }
}

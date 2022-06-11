import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A class to test HashTable. Conditions for each test are found on the right.
 * @author Josh Hager
 */
public class HashTableTest {
    
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
    public static HashEntry[] reflectTable(HashTable test) throws NoSuchMethodException, SecurityException, IllegalAccessException, 
                                                            IllegalArgumentException, InvocationTargetException{
        Method getTable = test.getClass().getDeclaredMethod("getTable");
        getTable.setAccessible(true);
        return (HashEntry[])(getTable.invoke(test));
    }

    /**
     * A helper method to access the private slotsFilled field in HashTable using Java reflection.
     * @param test the HashTable to retrieve the slotsFilled field from
     * @return the slotsFilled field of the specified HashTable
     * @throws NoSuchMethodException when the method cannot be found
     * @throws SecurityException when there is a security violation
     * @throws IllegalAccessException when the specified class, method, constructor, or field cannot be accessed
     * @throws IllegalArgumentException when a Method is passed an illegal argument
     * @throws InvocationTargetException when there is an exception thrown by the invoked method
     */
    public static int reflectSlotsFilled(HashTable test) throws NoSuchMethodException, SecurityException, IllegalAccessException, 
                                                            IllegalArgumentException, InvocationTargetException{
        Method getSlotsFilled = test.getClass().getDeclaredMethod("getSlotsFilled");
        getSlotsFilled.setAccessible(true);
        return (int)(getSlotsFilled.invoke(test));
    }

    /* also tests second constructor */                                                          //CONDITION:
    @Test
    public void testPut1() throws NoSuchMethodException, SecurityException, IllegalAccessException, 
                            IllegalArgumentException, InvocationTargetException{
        HashTable test = new HashTable(20);
        HashEntry[] testTable = HashTableTest.reflectTable(test);

        int helloCode = Math.abs("hello".hashCode()) % 20;
        test.put("hello", 1);
        assertEquals("hello", testTable[helloCode].getKey());                                   //check that key.hashCode() was used to place key
    }

    /* also tests first constructor */
    @Test
    public void testPut2() throws NoSuchMethodException, SecurityException, IllegalAccessException, 
                            IllegalArgumentException, InvocationTargetException{
        HashTable test = new HashTable();
        HashEntry[] testTable = HashTableTest.reflectTable(test);

        int helloCode = 50 % testTable.length;
        test.put("hello", 1, 50);
        assertEquals("hello", testTable[helloCode].getKey());                                   //slot is empty

        test.put("hello", 1, 50);
        assertEquals(2, testTable[helloCode].getValue());                                       //slot's entry contains key

        test.put("hotdog", 1, 50);
        assertEquals("hotdog", testTable[helloCode].getChain().get(0).getKey());                //collision, no chain

        test.put("helper", 1, 50);
        assertEquals("helper", testTable[helloCode].getChain().get(1).getKey());                //collision, chain length 1, key is not in chain

        test.put("handle", 1, 50);
        assertEquals("handle", testTable[helloCode].getChain().get(2).getKey());                //collision, chain length >1, key is not in chain

        test.put("hotdog", 1, 50);
        assertEquals(2, testTable[helloCode].getChain().get(0).getValue());                     //first entry in chain contains key

        test.put("helper", 1, 50);
        assertEquals(2, testTable[helloCode].getChain().get(1).getValue());                     //middle entry in chain contains key

        test.put("handle", 1, 50);
        assertEquals(2, testTable[helloCode].getChain().get(2).getValue());                     //last entry in chain contains key

        test = new HashTable(10);
        test.put("bulbasaur", 1, 1);
        test.put("charmander", 1, 2);
        test.put("squirtle", 1, 3);
        test.put("pikachu", 1, 4);
        test.put("ivysaur", 1, 5);
        test.put("charmeleon", 1, 6);
        test.put("wartortle", 1, 7);
        test.put("raichu", 1, 8);
        test.put("meowth", 1, 9);
        testTable = HashTableTest.reflectTable(test);
        assertEquals(20, testTable.length);                                                     //check that rehash occurred when load factor exceeded

        test = new HashTable(0);
        test.put("bulbasaur", 1);
        testTable = HashTableTest.reflectTable(test);
        assertEquals(100, testTable.length);                                                    //check that rehash occurred when table size zero

    }

    @Test
    public void testUpdate(){
        HashTable test = new HashTable();

        test.update("hello", 2);
        assertEquals(2, test.get("hello"));                                                     //key was not in list, use get to make sure it was added

        test.update("hello", 4);
        assertEquals(4, test.get("hello"));                                                     //key was in list, use get to make sure value was updated
    }

    @Test
    public void testGet1(){
        HashTable test = new HashTable();

        test.put("hello", 1);                                                                   //use put(key, value) to place key with key.hashCode()
        assertEquals(1, test.get("hello"));                                                     //check that key.hashCode() was used to find key

        assertEquals(-1, test.get("handle"));                                                   //key not in table
    }

    @Test
    public void testGet2(){
        HashTable test = new HashTable();

        assertEquals(-1, test.get("hello", 50));                                                //slot is empty

        test.put("hello", 1, 50);
        assertEquals(1, test.get("hello", 50));                                                 //key is in slot

        test.put("hotdog", 1, 50);
        assertEquals(1, test.get("hotdog", 50));                                                //key is in chain, chain length 1
        
        test.put("helper", 1, 50);
        test.put("handle", 1, 50);

        assertEquals(1, test.get("hotdog", 50));                                                //first entry in chain contains key, chain length >1

        assertEquals(1, test.get("helper", 50));                                                //middle entry in chain contains key, chain length >1

        assertEquals(1, test.get("handle", 50));                                                //last entry in chain contains key, chain length >1

        assertEquals(-1, test.get("human", 50));                                                //chain does not contain key

        test = new HashTable(0);
        assertEquals(-1, test.get("hello"));                                                    //table size zero

        test = new HashTable(1);
        test.setLoadFactor(1);
        test.put("hello", 1);
        assertEquals(1, test.get("hello"));                                                     //table size one
    }

    @Test
    public void testRehash() throws NoSuchMethodException, SecurityException, IllegalAccessException, 
                                IllegalArgumentException, InvocationTargetException{
        HashTable test = new HashTable(0);

        test.rehash();
        HashEntry[] testTable = HashTableTest.reflectTable(test);
        assertEquals(0, HashTableTest.reflectSlotsFilled(test));
        assertEquals(100, testTable.length);                                                    //table size zero

        test = new HashTable(1);
        test.rehash();
        testTable = HashTableTest.reflectTable(test);
        assertEquals(0, HashTableTest.reflectSlotsFilled(test));
        assertEquals(2, testTable.length);                                                      //table size one

        test = new HashTable();
        test.rehash();
        testTable = HashTableTest.reflectTable(test);
        assertEquals(0, HashTableTest.reflectSlotsFilled(test));
        assertEquals(200, testTable.length);                                                    //table size >1, empty table

        test = new HashTable();
        test.put("hello", 1);
        test.rehash();
        testTable = HashTableTest.reflectTable(test);
        assertEquals(1, HashTableTest.reflectSlotsFilled(test));
        assertEquals(200, testTable.length);
        assertEquals(1, test.get("hello"));                                                     //table size >1, 1 entry in table

        test.put("catamaran", 1);
        test.rehash();
        testTable = HashTableTest.reflectTable(test);
        assertEquals(2, HashTableTest.reflectSlotsFilled(test));
        assertEquals(400, testTable.length);
        assertEquals(1, test.get("hello"));
        assertEquals(1, test.get("catamaran"));                                                 //table size >1, >1 entry in table

        test = new HashTable();
        test.put("hello", 1);
        test.put("catamaran", 1);
        test.put("helper", 1, "hello".hashCode());                                              
        test.rehash();
        testTable = HashTableTest.reflectTable(test);
        assertEquals(3, HashTableTest.reflectSlotsFilled(test));
        assertEquals(200, testTable.length);
        assertEquals(1, test.get("hello"));
        assertEquals(1, test.get("catamaran"));
        assertEquals(1, test.get("helper"));                                                    //table size >1, 1 entry has chain

        test = new HashTable();
        test.put("hello", 1);
        test.put("catamaran", 1);
        test.put("helper", 1, "hello".hashCode());       
        test.put("candor", 1, "catamaran".hashCode());                                       
        test.rehash();
        testTable = HashTableTest.reflectTable(test);
        assertEquals(4, HashTableTest.reflectSlotsFilled(test));
        assertEquals(200, testTable.length);
        assertEquals(1, test.get("hello"));
        assertEquals(1, test.get("catamaran"));
        assertEquals(1, test.get("helper"));                                                    
        assertEquals(1, test.get("candor"));                                                    //table size >1, >1 entry has chain
    }

    @Test
    public void testGetLoadFactor(){
        HashTable test = new HashTable();

        assertEquals(.75, test.getLoadFactor(), .001);                                          //check method returns loadFactor
    }

    @Test
    public void testSetLoadFactor(){
        HashTable test = new HashTable();

        test.setLoadFactor(.81);
        assertEquals(.81, test.getLoadFactor(), .001);                                          //check method changes loadFactor
    }
}

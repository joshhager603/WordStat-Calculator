import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

/**
 * A class to test HashEntry.  Conditions for each test are found on the right.
 * @author Josh Hager
 */
public class HashEntryTest {
                                                            //CONDITION:
    /* also tests constructor */
    @Test
    public void testGetKey(){
        HashEntry test = new HashEntry("a", 1);

        assertEquals("a", test.getKey());                   //should return intialized key
    }

    @Test
    public void testGetValue(){
        HashEntry test = new HashEntry("a", 1);

        assertEquals(1, test.getValue());                   //should return initialized value
    }

    @Test
    public void testSetValue(){
        HashEntry test = new HashEntry("a", 1);

        test.setValue(5);
        assertEquals(5, test.getValue());                   //should return set value
    }

    @Test
    public void testGetChain(){
        HashEntry test = new HashEntry("a", 1);

        assertEquals(null, test.getChain());                        //no chain

        test.setChain(new LinkedList<HashEntry>());
        test.getChain().add(new HashEntry("b", 1));
        
        assertEquals("b", test.getChain().get(0).getKey());
        assertEquals(1, test.getChain().get(0).getValue());         //check that chain is retrieved and can be added to
    }

    @Test
    public void testSetChain(){
        HashEntry test = new HashEntry("a", 1);

        test.setChain(new LinkedList<HashEntry>());
        assertEquals(0, test.getChain().size());                    //check that chain was changed from null to a LL of size 0
    }

    @Test
    public void testHasChain(){
        HashEntry test = new HashEntry("a", 1);

        assertEquals(false, test.hasChain());                       //no chain

        test.setChain(new LinkedList<HashEntry>());
        assertEquals(true, test.hasChain());                        //has a chain
    }
}

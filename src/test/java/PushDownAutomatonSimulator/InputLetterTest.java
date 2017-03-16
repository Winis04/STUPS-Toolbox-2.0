package PushDownAutomatonSimulator;

import org.junit.Test;

import static org.junit.Assert.*;


public class InputLetterTest {

    @Test
    public void equal() {
        InputLetter a = new InputLetter("a");
        InputLetter b = new InputLetter("b");
        InputLetter c = new InputLetter("a");
        assertTrue(a.equals(c));
        assertFalse(b.equals(a));
    }



    @Test
    public void hashCodeTest() {
        InputLetter a = new InputLetter("isabel");
        InputLetter b = new InputLetter("isabel");
        InputLetter c = new InputLetter("wingen");
        assertEquals(a.hashCode(),b.hashCode());
        assertNotEquals(b.hashCode(),c.hashCode());
    }
}
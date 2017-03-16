package PushDownAutomatonSimulator;

import org.junit.Test;

import static org.junit.Assert.*;


public class StackLetterTest {
    @Test
    public void equal() {
        StackLetter a = new StackLetter("a");
        StackLetter b = new StackLetter("b");
        StackLetter c = new StackLetter("a");
        assertTrue(a.equals(c));
        assertFalse(b.equals(a));
    }



    @Test
    public void hashCodeTest() {
        StackLetter a = new StackLetter("isabel");
        StackLetter b = new StackLetter("isabel");
        StackLetter c = new StackLetter("wingen");
        assertEquals(a.hashCode(),b.hashCode());
        assertNotEquals(b.hashCode(),c.hashCode());
    }
}
package GrammarSimulator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Isabel
 * @since 14.01.2017
 */
public class TerminalTest {

    @Test
    public void equal() {
        Terminal a = new Terminal("a");
        Terminal b = new Terminal("b");
        Terminal c = new Terminal("a");
        assertTrue(a.equals(c));
        assertFalse(b.equals(a));
    }

    @Test
    public void hashCodeTest() {
        Terminal a = new Terminal("isabel");
        Terminal b = new Terminal("isabel");
        Terminal c = new Terminal("wingen");
        assertEquals(a.hashCode(),b.hashCode());
        assertNotEquals(b.hashCode(),c.hashCode());
    }
}
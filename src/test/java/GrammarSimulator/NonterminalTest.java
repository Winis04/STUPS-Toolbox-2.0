package GrammarSimulator;

import org.junit.Test;

import static org.junit.Assert.*;


public class NonterminalTest {
    @Test
    public void equal() {
        Nonterminal a = new Nonterminal("a");
        Nonterminal b = new Nonterminal("b");
        Nonterminal c = new Nonterminal("a");
        assertFalse(b.equals(c));
        assertTrue(a.equals(c));
    }


    @Test
    public void hashCodeTest() {
        Nonterminal a = new Nonterminal("a");
        Nonterminal a2 = new Nonterminal("a");
        Nonterminal b = new Nonterminal("bla");
        assertEquals(a.hashCode(),a2.hashCode());
        assertNotEquals(b.hashCode(),a.hashCode());
    }
}
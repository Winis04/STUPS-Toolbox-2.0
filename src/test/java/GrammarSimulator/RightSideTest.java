package GrammarSimulator;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by Isabel on 27.12.2016.
 */
public class RightSideTest {
    @Test
    public void equals() throws Exception {

        ComparableList<Symbol> a = new ComparableList<>(Arrays.asList(new Symbol[]{new Nonterminal("A"), new Nonterminal("B")}));
        ComparableList<Symbol> b = new ComparableList<>(Arrays.asList(new Symbol[]{new Nonterminal("A"), new Nonterminal("B")}));
        ComparableList<Symbol> c = new ComparableList<>(Arrays.asList(new Symbol[]{new Nonterminal("C"), new Nonterminal("D")}));
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
        assertTrue(a.equals(a));
        assertFalse(a.equals(c));
        assertFalse(c.equals(a));
        assertFalse(c.equals(b));
        assertFalse(b.equals(c));
    }

    @Test
    public void contains() throws Exception {
        ComparableList<Symbol> a = new ComparableList<>(Arrays.asList(new Symbol[]{new Nonterminal("A"), new Nonterminal("B")}));
        assertTrue(a.contains(new Nonterminal("A")));
    }

}
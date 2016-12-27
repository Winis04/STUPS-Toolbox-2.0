package GrammarSimulator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Isabel on 27.12.2016.
 */
public class RightSideTest {
    @Test
    public void equals() throws Exception {
        RightSide<Symbol> a = new RightSide<>();
        a.add(new Nonterminal("A"));
        a.add(new Nonterminal("B"));
        RightSide<Symbol> b = new RightSide<>();
        b.add(new Nonterminal("A"));
        b.add(new Nonterminal("B"));
        RightSide<Symbol> c = new RightSide<>();
        c.add(new Nonterminal("C"));
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
        RightSide<Symbol> a = new RightSide<>();
        a.add(new Nonterminal("A"));
        a.add(new Nonterminal("B"));
        assertTrue(a.contains(new Nonterminal("A")));
    }

}
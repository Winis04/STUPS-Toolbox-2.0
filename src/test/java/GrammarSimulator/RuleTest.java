package GrammarSimulator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Isabel on 27.12.2016.
 */
public class RuleTest {
    @Test
    public void copy() throws Exception {
        Rule rule = new Rule(new Nonterminal("A"));
        RightSide<Symbol> right = new RightSide<>();
        right.add(new Nonterminal("A"));
        right.add(new Terminal("b"));
        rule.setRightSide(right);
        Rule copy = rule.copy();
        assertTrue(copy.equals(rule));
    }

    @Test
    public void equals() throws Exception {
        Rule rule = new Rule(new Nonterminal("A"));
        RightSide<Symbol> right = new RightSide<>();
        right.add(new Nonterminal("A"));
        right.add(new Terminal("b"));
        rule.setRightSide(right);

        Rule rule2 = new Rule(new Nonterminal("A"));
        RightSide<Symbol> right2 = new RightSide<>();
        right2.add(new Nonterminal("A"));
        right2.add(new Terminal("b"));
        rule2.setRightSide(right2);

        assertTrue(rule.equals(rule2));
    }

}
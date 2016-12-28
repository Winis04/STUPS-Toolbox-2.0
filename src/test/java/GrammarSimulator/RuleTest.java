package GrammarSimulator;

import org.junit.Test;

import java.util.HashSet;

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
        HashSet<Rule> test = new HashSet<>();
        test.add(rule);
        test.add(rule2);
        assertTrue(test.size()==1);
        Rule rule3  = new Rule(new Nonterminal("A"));
        RightSide<Symbol> right3 = new RightSide<>();
        right3.add(new Nonterminal("A"));
        right3.add(new Terminal("C"));
        right3.add(new Nonterminal("dfklsj"));
        rule3.setRightSide(right3);
        test.add(rule3);
        assertTrue(test.size()==2);
    }

}
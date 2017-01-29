package GrammarSimulator;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

/**
 * @author Isabel
 * @since 27.12.2016
 */
public class RuleTest {
    @Test
    public void copy() throws Exception {

        ArrayList<Symbol> right = new ArrayList<>();
        right.add(new Nonterminal("A"));
        right.add(new Terminal("b"));
        Rule rule = new Rule(new Nonterminal("A"),right);
        Rule copy = rule.copy();
        assertTrue(copy.equals(rule));
    }

    @Test
    public void equals() throws Exception {

        ArrayList<Symbol> right = new ArrayList<>();
        right.add(new Nonterminal("A"));
        right.add(new Terminal("b"));
        Rule rule = new Rule(new Nonterminal("A"),right);


        ArrayList<Symbol> right2 = new ArrayList<>();
        right2.add(new Nonterminal("A"));
        right2.add(new Terminal("b"));
        Rule rule2 = new Rule(new Nonterminal("A"),right2);

        assertTrue(rule.equals(rule2));
        HashSet<Rule> test = new HashSet<>();
        test.add(rule);
        test.add(rule2);
        assertTrue(test.size()==1);


        ArrayList<Symbol> right3 = new ArrayList<>();
        right3.add(new Nonterminal("A"));
        right3.add(new Terminal("C"));
        right3.add(new Nonterminal("a"));
        Rule rule3  = new Rule(new Nonterminal("A"),right3);
        test.add(rule3);
        assertTrue(test.size()==2);
    }

    @Test
    public void hashsetTest() {
        HashSet<Integer> res = IntStream.range(0,10).mapToObj(i -> {
            HashSet<Integer> set = new HashSet<>();
            set.add(i);
            return set;
        }).reduce(new HashSet<>(), (x, y) -> {
            x.addAll(y);
            return x;
        });
    }

}
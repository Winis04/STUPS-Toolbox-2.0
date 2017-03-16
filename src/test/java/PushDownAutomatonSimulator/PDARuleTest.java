package PushDownAutomatonSimulator;


import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Collections;

public class PDARuleTest {
    @Test
    public void testEquals() {
        PDARule rule1 = new PDARule(new State("z0"),new State("z1"),new InputLetter("a"),new StackLetter("A"), Collections.singletonList(new StackLetter("B")));
        PDARule rule2 = new PDARule(new State("z0"),new State("z1"),new InputLetter("a"),new StackLetter("A"), Collections.singletonList(new StackLetter("B")));
        PDARule rule3 = new PDARule(new State("x"),new State("z1"),new InputLetter("a"),new StackLetter("A"), Collections.singletonList(new StackLetter("B")));
        PDARule rule4 = new PDARule(new State("z0"),new State("x"),new InputLetter("a"),new StackLetter("A"), Collections.singletonList(new StackLetter("B")));
        PDARule rule5 = new PDARule(new State("z0"),new State("z1"),new InputLetter("x"),new StackLetter("A"), Collections.singletonList(new StackLetter("B")));
        PDARule rule6 = new PDARule(new State("z0"),new State("z1"),new InputLetter("a"),new StackLetter("x"), Collections.singletonList(new StackLetter("B")));
        PDARule rule7 = new PDARule(new State("z0"),new State("z1"),new InputLetter("a"),new StackLetter("A"), Collections.singletonList(new StackLetter("x")));
        assertEquals(rule1,rule2);
        assertNotEquals(rule1,rule3);
        assertNotEquals(rule1,rule4);
        assertNotEquals(rule1,rule5);
        assertNotEquals(rule1,rule6);
        assertNotEquals(rule1,rule7);

    }

    @Test
    public void testhashCode() {
        PDARule rule1 = new PDARule(new State("z0"),new State("z1"),new InputLetter("a"),new StackLetter("A"), Collections.singletonList(new StackLetter("B")));
        PDARule rule2 = new PDARule(new State("z0"),new State("z1"),new InputLetter("a"),new StackLetter("A"), Collections.singletonList(new StackLetter("B")));
        PDARule rule3 = new PDARule(new State("x"),new State("z1"),new InputLetter("a"),new StackLetter("A"), Collections.singletonList(new StackLetter("B")));
        PDARule rule4 = new PDARule(new State("z0"),new State("x"),new InputLetter("a"),new StackLetter("A"), Collections.singletonList(new StackLetter("B")));
        PDARule rule5 = new PDARule(new State("z0"),new State("z1"),new InputLetter("x"),new StackLetter("A"), Collections.singletonList(new StackLetter("B")));
        PDARule rule6 = new PDARule(new State("z0"),new State("z1"),new InputLetter("a"),new StackLetter("x"), Collections.singletonList(new StackLetter("B")));
        PDARule rule7 = new PDARule(new State("z0"),new State("z1"),new InputLetter("a"),new StackLetter("A"), Collections.singletonList(new StackLetter("x")));
        assertEquals(rule1.hashCode(),rule2.hashCode());
        assertNotEquals(rule1.hashCode(),rule3.hashCode());
        assertNotEquals(rule1.hashCode(),rule4.hashCode());
        assertNotEquals(rule1.hashCode(),rule5.hashCode());
        assertNotEquals(rule1.hashCode(),rule6.hashCode());
        assertNotEquals(rule1.hashCode(),rule7.hashCode());

    }

}
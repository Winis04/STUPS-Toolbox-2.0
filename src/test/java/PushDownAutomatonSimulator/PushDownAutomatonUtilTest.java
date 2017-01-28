package PushDownAutomatonSimulator;

import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.*;

/**
 * @author Isabel
 * @since 07.01.2017
 */
public class PushDownAutomatonUtilTest {
    @Test
    public void stack() {
        Stack<String> stack = new Stack();
        stack.add("1");
        stack.add("2");
        stack.add("3");
    }

}
package PushDownAutomatonSimulator;

import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

import static org.junit.Assert.assertEquals;


public class RunThroughInfoTest {

    @Test
    public void testToStack() {
        Stack<String> test = new Stack<>();
        test.push("A");
        test.push("B");
        test.push("C");
        List<String> list = Collections.unmodifiableList(test);
        Stack<String> test2 = new Stack<>();
        list.forEach(test2::push);
        assertEquals(test.pop(),test2.pop());
    }

}
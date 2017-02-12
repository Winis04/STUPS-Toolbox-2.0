package GrammarSimulator;


import org.junit.Test;


import java.util.ArrayList;
import java.util.List;


import static org.junit.Assert.*;

/**
 * @author isabel
 * @since 19.01.17
 */

public class ConfigurationTest {

    /**
     * checks equal and hashCode
     */
    @Test
    public void hashAndEqual() {
        List<Symbol> list1 = new ArrayList<>();
        list1.add(new Nonterminal("A"));
        list1.add(new Terminal("b"));
        List<Symbol> list2 = new ArrayList<>();
        list2.add(new Nonterminal("x"));
        Configuration conf1 = new Configuration(list1,null,null);
        Configuration conf2 = new Configuration(list1,null,null);
        Configuration conf3 = new Configuration(list2,null,null);
        assertTrue(conf1.equals(conf2));
        assertEquals(conf1,conf2);
        assertEquals(conf1.hashCode(),conf2.hashCode());
        assertNotEquals(conf3,conf1);

        ArrayList<String> test = new ArrayList<>();
        ArrayList<String> test2 = new ArrayList<>();
        test.add("Hallo");
        test.add("Isabel");

        test2.add("JO");
        test2.add("Ja");

        test.addAll(1,test2);
        System.out.println(test.toString());
    }





}
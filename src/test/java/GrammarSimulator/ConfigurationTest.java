package GrammarSimulator;


import org.junit.Ignore;
import org.junit.Test;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import static org.junit.Assert.*;

/**
 * Created by isabel on 19.01.17.
 */
public class ConfigurationTest {
    @Test
    public void getChildren() throws Exception {
        Grammar grammar = GrammarUtil.parse(getResourceAsFile("/Grammar/test11.gr"));




    }

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

    @Test
    public void listTest() {
        List<Symbol> list1 = new ArrayList<>();
        List<Symbol> list2 = new ArrayList<>();
        list1.add(new Nonterminal("a"));
        list1.add(new Nonterminal("b"));
        list2.add(new Nonterminal("a"));
        list2.add(new Nonterminal("b"));
        assertTrue(GrammarUtil.listEqual(list1,list2));
    }

    @Ignore
    public File getResourceAsFile(String path) {

        try {
            InputStream input = getClass().getResourceAsStream(path);
            if (input == null) {
                return null;
            }

            File res = File.createTempFile(String.valueOf(input.hashCode()), ".tmp");
            res.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(res)) {
                //copy stream
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            return res;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
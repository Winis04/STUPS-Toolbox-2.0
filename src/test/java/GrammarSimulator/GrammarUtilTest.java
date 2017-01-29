package GrammarSimulator;

import GrammarParser.lexer.LexerException;
import GrammarParser.parser.ParserException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * @author Isabel
 * @since 14.01.2017
 */
public class GrammarUtilTest {

    List<File> files;
    @Before
    public void initialize() {

        files = IntStream.rangeClosed(1,13).mapToObj(i -> getResourceAsFile("/Grammar/test"+i+".gr")).collect(Collectors.toList());

    }
    @Test
    public void parse() throws Exception {
        files.forEach(file -> {
            try {
                assertTrue(GrammarUtil.parse(file) != null);
            } catch (IOException | LexerException | ParserException e) {
                e.printStackTrace();
            }
        });

    }

    @Test
    public void calculateNullable() {
        Grammar tmp;
        try {
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test1.gr"));
            assertEquals(GrammarUtil.calculateNullable(tmp),new HashSet<Nonterminal>());

            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test2.gr"));
            assertEquals(GrammarUtil.calculateNullable(tmp),new HashSet<Nonterminal>());

            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test3.gr"));
            HashSet<Nonterminal> set = new HashSet<>();
            set.add(new Nonterminal("A"));
            set.add(new Nonterminal("B"));
            set.add(new Nonterminal("C"));
            assertEquals(GrammarUtil.calculateNullable(tmp),set);

            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test4.gr"));
            set = new HashSet<>();
            set.add(new Nonterminal("S"));
            assertEquals(GrammarUtil.calculateNullable(tmp),set);

            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test5.gr"));
            set.add(new Nonterminal("B"));
            assertEquals(GrammarUtil.calculateNullable(tmp),set);

            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test6.gr"));
          set = new HashSet<>();
            set.add(new Nonterminal("A"));
            set.add(new Nonterminal("B"));
            set.add(new Nonterminal("C"));
            assertEquals(GrammarUtil.calculateNullable(tmp),set);

            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test7.gr"));
            set = new HashSet<>();
            set.add(new Nonterminal("S"));
            set.add(new Nonterminal("A"));
            assertEquals(GrammarUtil.calculateNullable(tmp),set);

            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test8.gr"));
            assertEquals(GrammarUtil.calculateNullable(tmp),new HashSet<Nonterminal>());

            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test9.gr"));
            assertEquals(GrammarUtil.calculateNullable(tmp),new HashSet<Nonterminal>());

            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test10.gr"));
            assertEquals(GrammarUtil.calculateNullable(tmp),new HashSet<Nonterminal>());

            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test11.gr"));
            assertEquals(GrammarUtil.calculateNullable(tmp),new HashSet<Nonterminal>());

            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test12.gr"));
            assertEquals(GrammarUtil.calculateNullable(tmp),new HashSet<Nonterminal>());

            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test13.gr"));
            assertEquals(GrammarUtil.calculateNullable(tmp),new HashSet<Nonterminal>());


        } catch (IOException e) {
            e.printStackTrace();
        } catch (LexerException e) {
            e.printStackTrace();
        } catch (ParserException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void removeLambdaRules() throws Exception {
        files.forEach(file -> {
            try {
                Grammar grammar = GrammarUtil.parse(file);
                assertTrue(GrammarUtil.isLambdaFree(GrammarUtil.removeLambdaRules(grammar)));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (LexerException e) {
                e.printStackTrace();
            } catch (ParserException e) {
                e.printStackTrace();
            }
        });

    }

    @Test
    public void eliminateUnitRules() throws Exception {
        files.forEach(file -> {
            try {
                Grammar grammar = GrammarUtil.parse(file);
                assertFalse(GrammarUtil.hasUnitRules(GrammarUtil.eliminateUnitRules(grammar)));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (LexerException e) {
                e.printStackTrace();
            } catch (ParserException e) {
                e.printStackTrace();
            }
        });
    }

    @Ignore
    public void chomskyNormalForm() throws Exception {
        files.forEach(file -> {
            try {
                Grammar grammar = GrammarUtil.parse(file);
                Grammar grammar1 = GrammarUtil.removeLambdaRules(grammar);
                Grammar grammar2 = GrammarUtil.eliminateUnitRules(grammar1);
                Grammar grammar3 = GrammarUtil.chomskyNormalForm(grammar2);
                assertFalse(GrammarUtil.hasUnitRules(grammar2));
                assertTrue(GrammarUtil.isInChomskyNormalForm(grammar3));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (LexerException e) {
                e.printStackTrace();
            } catch (ParserException e) {
                e.printStackTrace();
            }
        });


    }

    @Test
    public void isChomskyNormalForm() {
        Grammar tmp = null;
        try {
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test1.gr"));
            assertFalse(GrammarUtil.isInChomskyNormalForm(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test2.gr"));
            assertFalse(GrammarUtil.isInChomskyNormalForm(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test3.gr"));
            assertFalse(GrammarUtil.isInChomskyNormalForm(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test4.gr"));
            assertFalse(GrammarUtil.isInChomskyNormalForm(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test5.gr"));
            assertFalse(GrammarUtil.isInChomskyNormalForm(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test6.gr"));
            assertFalse(GrammarUtil.isInChomskyNormalForm(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test7.gr"));
            assertFalse(GrammarUtil.isInChomskyNormalForm(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test8.gr"));
            assertTrue(GrammarUtil.isInChomskyNormalForm(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test9.gr"));
            assertTrue(GrammarUtil.isInChomskyNormalForm(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test10.gr"));
            assertFalse(GrammarUtil.isInChomskyNormalForm(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test11.gr"));
            assertFalse(GrammarUtil.isInChomskyNormalForm(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test12.gr"));
            assertFalse(GrammarUtil.isInChomskyNormalForm(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test13.gr"));
            assertFalse(GrammarUtil.isInChomskyNormalForm(tmp));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LexerException e) {
            e.printStackTrace();
        } catch (ParserException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void isLambdaFree() {
        Grammar tmp = null;
        try {
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test1.gr"));
            assertTrue(GrammarUtil.isLambdaFree(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test2.gr"));
            assertTrue(GrammarUtil.isLambdaFree(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test3.gr"));
            assertFalse(GrammarUtil.isLambdaFree(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test4.gr"));
            assertFalse(GrammarUtil.isLambdaFree(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test5.gr"));
            assertFalse(
                    GrammarUtil.isLambdaFree(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test6.gr"));
            assertFalse(GrammarUtil.isLambdaFree(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test7.gr"));
            assertFalse(GrammarUtil.isLambdaFree(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test8.gr"));
            assertTrue(GrammarUtil.isLambdaFree(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test9.gr"));
            assertTrue(GrammarUtil.isLambdaFree(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test10.gr"));
            assertTrue(GrammarUtil.isLambdaFree(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test11.gr"));
            assertTrue(GrammarUtil.isLambdaFree(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test12.gr"));
            assertTrue(GrammarUtil.isLambdaFree(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test13.gr"));
            assertTrue(GrammarUtil.isLambdaFree(tmp));
        } catch (IOException | ParserException | LexerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void hasUnitRules() {
        Grammar tmp = null;
        try {
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test1.gr"));
            assertFalse(GrammarUtil.hasUnitRules(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test2.gr"));
            assertTrue(GrammarUtil.hasUnitRules(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test3.gr"));
            assertFalse(GrammarUtil.hasUnitRules(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test4.gr"));
            assertFalse(GrammarUtil.hasUnitRules(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test5.gr"));
            assertTrue(GrammarUtil.hasUnitRules(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test6.gr"));
            assertFalse(GrammarUtil.hasUnitRules(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test7.gr"));
            assertFalse(GrammarUtil.hasUnitRules(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test8.gr"));
            assertFalse(GrammarUtil.hasUnitRules(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test9.gr"));
            assertFalse(GrammarUtil.hasUnitRules(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test10.gr"));
            assertFalse(GrammarUtil.hasUnitRules(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test11.gr"));
            assertFalse(GrammarUtil.hasUnitRules(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test12.gr"));
            assertFalse(GrammarUtil.hasUnitRules(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test7.gr"));
            assertFalse(GrammarUtil.hasUnitRules(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test13.gr"));
            assertTrue(GrammarUtil.hasUnitRules(tmp));
        } catch (IOException | LexerException | ParserException e) {
            e.printStackTrace();
        }
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
package GrammarSimulator;

import GrammarParser.lexer.LexerException;
import GrammarParser.parser.ParserException;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Created by Isabel on 14.01.2017.
 */
public class GrammarUtilTest {
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
            assertTrue(GrammarUtil.isLambdaFree(tmp));
            tmp = GrammarUtil.parse(getResourceAsFile("/Grammar/test5.gr"));
            assertTrue(GrammarUtil.isLambdaFree(tmp));
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
    private File getResourceAsFile(String path) {

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
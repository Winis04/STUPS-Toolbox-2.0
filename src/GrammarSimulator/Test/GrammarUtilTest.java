package GrammarSimulator.Test;

import CLIPlugins.CLIPlugin;
import CLIPlugins.GrammarLoadPlugin;
import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import org.junit.Before;
import  org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Isabel on 24.10.2016.
 */
public class GrammarUtilTest {
    @Test
    public void calculateNullable1() throws Exception {

    }

    @Test
    public void hashSetEqual() throws Exception {
        HashSet<String> a=new HashSet<>();
        a.add("Hallo");
        a.add("Du");
        HashSet<String> b=new HashSet<>();
        b.add("Du");
        b.add("Hallo");
        HashSet<String> c=new HashSet<>();
        c.add("Hallo");
        assertFalse("these hashsets should not be equal",GrammarUtil.hashSetEqual(a,c));
        assertFalse("these hashsets should not be equal",GrammarUtil.hashSetEqual(b,c));
        assertFalse("these hashsets should not be equal",GrammarUtil.hashSetEqual(c,a));
        assertFalse("these hashsets should not be equal",GrammarUtil.hashSetEqual(c,b));
        assertTrue("these hashsets should be equal",GrammarUtil.hashSetEqual(a,b));
        assertTrue("these hashsets should be equal",GrammarUtil.hashSetEqual(b,a));
    }


    GrammarLoadPlugin lg;
    String[] paths;
    @Before
    public void method() {
        paths=new String[]{"test/test1.gr"};
        lg=new GrammarLoadPlugin();
    }
    public Grammar loadNewGrammar(String path) {
        Grammar g=(Grammar)lg.execute(null,new String[]{path});
        return g;
    }
    @Test
    public void isLambdaFree() {
        boolean[] trueOrFalse={true};
        Grammar g;
        for(int i=0;i<trueOrFalse.length;i++) {
            g=loadNewGrammar(paths[i]);
            if(trueOrFalse[i]) {
                assertTrue(i+": this grammar should be lambda-free", GrammarUtil.isLambdaFree(g));
            } else {
                assertFalse(i+": this grammar should be lambda-free", GrammarUtil.isLambdaFree(g));
            }
        }
    }
    @Test
    public void hasUnitRules() {
        boolean[] trueOrFalse={false};
        Grammar g;
        for(int i=0;i<trueOrFalse.length;i++) {
            g=loadNewGrammar(paths[i]);
            if(trueOrFalse[i]) {
                assertTrue(i+": this grammar should be unit-rule-free", GrammarUtil.hasUnitRules(g));
            } else {
                assertFalse(i+": this grammar should not be unit-rule-free", GrammarUtil.hasUnitRules(g));
            }
        }
    }
    @Test
    public void startSymbolOnRightSide() throws Exception {
        boolean[] trueOrFalse={true};
        Grammar g;
        for(int i=0;i<trueOrFalse.length;i++) {
            g=loadNewGrammar(paths[i]);
            if(trueOrFalse[i]) {
                assertTrue(i+": this grammar should have the startsymbol on the right side", GrammarUtil.startSymbolOnRightSide(g));
            } else {
                assertFalse(i+": this grammar should not have the startsymbol on the right side", GrammarUtil.startSymbolOnRightSide(g));
            }
        }
    }

    @Test
    public void languageContainsLambda() throws Exception {
        boolean[] trueOrFalse={false};
        Grammar g;
        for(int i=0;i<trueOrFalse.length;i++) {
            g=loadNewGrammar(paths[i]);
            if(trueOrFalse[i]) {
                assertTrue(i+": this grammar should have the empty word in its language", GrammarUtil.languageContainsLambda(g));
            } else {
                assertFalse(i+": this grammar should not have the empty word in its language", GrammarUtil.languageContainsLambda(g));
            }
        }
    }

    @Test
    public void startSymbolPointsOnLambda() throws Exception {
        boolean[] trueOrFalse={false};
        Grammar g;
        for(int i=0;i<trueOrFalse.length;i++) {
            g=loadNewGrammar(paths[i]);
            if(trueOrFalse[i]) {
                assertTrue(i+": this grammar's startsymbol should point on lambda", GrammarUtil.startSymbolPointsOnLambda(g));
            } else {
                assertFalse(i+": this grammar's startsymbol should not point on lambda", GrammarUtil.startSymbolPointsOnLambda(g));
            }
        }
    }
    @Test
    public void isCircleFree() {
        boolean[] trueOrFalse={true};
        Grammar g;
        for(int i=0;i<trueOrFalse.length;i++) {
            g=loadNewGrammar(paths[i]);
            if(trueOrFalse[i]) {
                assertTrue(i+": this grammar's startsymbol should be circle free", GrammarUtil.isCircleFree(g));
            } else {
                assertFalse(i+": this grammar's startsymbol should not be circle free", GrammarUtil.isCircleFree(g));
            }
        }
    }
    @Test
    public void calculateNullable() throws Exception {

    }

    @Test
    public void removeUnneccesaryEpsilons() throws Exception {

    }

    @Test
    public void replaceLambda() throws Exception {

    }

    @Test
    public void removeLambdaRules_StepThree() throws Exception {

    }

    @Test
    public void removeLambdaRules_StepTwo() throws Exception {

    }

    @Test
    public void getSymbolListsWithoutEmptyRules() throws Exception {

    }

    @Test
    public void specialRuleForEmptyWord() throws Exception {

    }


    @Test
    public void removeCircleRules() throws Exception {
        Grammar g;
        for(int i=0;i<paths.length;i++) {
            g=loadNewGrammar(paths[i]);
            GrammarUtil.removeCircleRules(g);
            assertTrue("this grammar should now be circle free",GrammarUtil.isCircleFree(g));
        }
    }

    @Test
    public void findUnitRules() throws Exception {

    }

    @Test
    public void dfs() throws Exception {

    }

    @Test
    public void removeUnitRules() throws Exception {
        Grammar g;
        for(int i=0;i<paths.length;i++) {
            g=loadNewGrammar(paths[i]);
            GrammarUtil.removeCircleRules(g);
            assertFalse("this grammar should now be without unit rules",GrammarUtil.hasUnitRules(g));
        }
    }

    @Test
    public void replaceNonterminal() throws Exception {

    }

}

package GrammarSimulator.Test;

import CLIPlugins.CLIPlugin;
import CLIPlugins.GrammarLoadPlugin;
import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import org.junit.Before;
import  org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Isabel on 24.10.2016.
 */
public class GrammarUtilTest {


    GrammarLoadPlugin lg;
    String[] paths;
    @Before
    public void method() {
        paths=new String[]{"test/test.gr","test/test2.gr","test/test3.gr","test/test4.gr"};
        lg=new GrammarLoadPlugin();
    }
    public Grammar loadNewGrammar(String path) {
        Grammar g=(Grammar)lg.execute(null,new String[]{path});
        return g;
    }
    @Test
    public void lambdaFreeGrammarsShouldBeDetectedAsLambdaFree() {
        boolean[] trueOrFalse={false,true,true,false};
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
    public void grammarsWithUnitRulesShouldHaveUnitRules() {
        boolean[] trueOrFalse={false,true,true,false};
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
        boolean[] trueOrFalse={false,false,false,true};
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
        boolean[] trueOrFalse={false,false,false,true};
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
        boolean[] trueOrFalse={false,false,false,true};
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

}

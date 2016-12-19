package CLIPlugins;

import GrammarSimulator.Grammar;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import static org.junit.Assert.fail;

/**
 * Created by Isabel on 19.12.2016.
 */
public class GrammarLoadPluginTest {
    GrammarLoadPlugin loadPlugin;
    @Before
    public void before() {
        this.loadPlugin = new GrammarLoadPlugin();
    }
    @Test
    public void loadGrammar() {
        Grammar grammar = (Grammar) loadPlugin.execute(null,new String[]{"src/test/resources/Grammar/test3.gr"});
        grammar.printConsole(new BufferedWriter(new OutputStreamWriter(System.out)));
    }
}
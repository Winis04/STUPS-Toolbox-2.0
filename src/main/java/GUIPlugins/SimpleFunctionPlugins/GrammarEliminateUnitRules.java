package GUIPlugins.SimpleFunctionPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Main.Storable;

/**
 * Created by Isabel on 03.12.2016.
 */
public class GrammarEliminateUnitRules extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Object object) {
        if(object == null) {
            gui.errorDialog("Please load a grammar before using this command!");
            return null;
        }
        Grammar grammar = (Grammar) object;
        if(!GrammarUtil.hasUnitRules(grammar)) {
            gui.infoDialog("this grammar has no unit rules!");
        } else {
            CLIPlugins.GrammarEliminateUnitRules cliPlugin = new CLIPlugins.GrammarEliminateUnitRules();
            cliPlugin.execute(object,new String[]{});
        }
        return grammar;
    }

    @Override
    String getName() {
        return "eliminate Unit Rules";
    }

    @Override
    public Class inputType() {
        return Grammar.class;
    }

    @Override
    Class outputType() {
        return Grammar.class;
    }

    @Override
    public boolean createsOutput() {
        return true;
    }
}

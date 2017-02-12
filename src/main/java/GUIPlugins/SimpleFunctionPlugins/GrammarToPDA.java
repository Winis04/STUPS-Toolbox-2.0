package GUIPlugins.SimpleFunctionPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Main.Storable;
import PushDownAutomatonSimulator.PushDownAutomaton;



public class GrammarToPDA extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Object object) {
        if(object == null) {
            gui.errorDialog("Please load a grammar before using this command!");
            return null;
        }
        Grammar grammar = (Grammar) object;
        return GrammarUtil.toPDA(grammar);
    }

    @Override
    public String getName() {
        return "To PDA";
    }

    @Override
    public Class inputType() {
        return Grammar.class;
    }

    @Override
    Class outputType() {
        return PushDownAutomaton.class;
    }

    @Override
    public boolean createsOutput() {
        return false;
    }
}

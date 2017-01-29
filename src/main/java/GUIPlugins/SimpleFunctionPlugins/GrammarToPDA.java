package GUIPlugins.SimpleFunctionPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Main.Storable;
import PushDownAutomatonSimulator.PushDownAutomaton;

/**
 * @author Isabel
 * @since 03.12.2016
 */
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
    String getName() {
        return "to PDA";
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

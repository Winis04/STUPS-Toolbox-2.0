package GUIPlugins.SimpleFunctionPlugins;

import GrammarSimulator.Grammar;
import Main.Storable;
import PushDownAutomatonSimulator.PushDownAutomaton;
import PushDownAutomatonSimulator.PushDownAutomatonUtil;

public class PDAToGrammar extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Object object) {
        if(object != null) {
            PushDownAutomaton pda = (PushDownAutomaton) object;
            return PushDownAutomatonUtil.toGrammar(pda);
        }
        return null;
    }

    @Override
   public String getName() {
        return "PDA to Grammar";
    }

    @Override
    public Class inputType() {
        return PushDownAutomaton.class;
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

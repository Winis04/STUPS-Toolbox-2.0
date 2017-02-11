package GUIPlugins.SimpleFunctionPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Main.Storable;
import Print.Printer;
import PushDownAutomatonSimulator.PushDownAutomaton;
import PushDownAutomatonSimulator.PushDownAutomatonUtil;

public class Dummy extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Object object) {
        if(object != null) {
            PushDownAutomaton pda = (PushDownAutomaton) object;
            return PushDownAutomatonUtil.renameStates(PushDownAutomatonUtil.splitRules(PushDownAutomatonUtil.renameStates(pda)));
        }
        return null;
    }

    @Override
   public String getName() {
        return "dummy";
    }

    @Override
    public Class inputType() {
        return PushDownAutomaton.class;
    }

    @Override
    Class outputType() {
        return PushDownAutomaton.class;
    }

    @Override
    public boolean createsOutput() {
        return true;
    }
}

package GUIPlugins.SimpleFunctionPlugins;

import Main.Storable;
import PushDownAutomatonSimulator.PushDownAutomaton;
import PushDownAutomatonSimulator.PushDownAutomatonUtil;



public class Dummy extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Object object) {
        if(object != null) {
            PushDownAutomaton pda = (PushDownAutomaton) object;
            return PushDownAutomatonUtil.splitRules(pda);
        }
        return null;
    }

    @Override
   public String getName() {
        return "Dummy";
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

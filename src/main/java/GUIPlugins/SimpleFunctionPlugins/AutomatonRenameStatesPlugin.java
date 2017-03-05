package GUIPlugins.SimpleFunctionPlugins;

import AutomatonSimulator.Automaton;
import AutomatonSimulator.AutomatonUtil;
import Main.Storable;

/**
 * @author fabian
 * @since 26.07.16
 */


public class AutomatonRenameStatesPlugin extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Object object) {
        Automaton automaton = (Automaton) object;
        Automaton deepCopy = (Automaton) automaton.deep_copy();
        Automaton algo = AutomatonUtil.renameStates(automaton);
        algo.setPreviousAutomaton(deepCopy);
        return algo;
    }

    @Override
    public String getName() {
        return "Rename States";
    }

    @Override
    public Class inputType() {
        return Automaton.class;
    }

    @Override
    public Class outputType() {
        return Automaton.class;
    }


}

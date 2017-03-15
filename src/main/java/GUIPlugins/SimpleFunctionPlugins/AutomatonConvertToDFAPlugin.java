package GUIPlugins.SimpleFunctionPlugins;

import AutomatonSimulator.Automaton;
import AutomatonSimulator.AutomatonUtil;
import Main.Storable;

/**
 * @author fabian
 * @since 24.07.16
 */


public class AutomatonConvertToDFAPlugin extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Storable object) {
        Automaton automaton = (Automaton) object;
        Automaton deepCopy = (Automaton) automaton.deep_copy();
        Automaton algo = AutomatonUtil.convertToDFA(automaton);
        algo.setPreviousAutomaton(deepCopy);
        return algo;
    }

    @Override
    public String getName() {
        return "Convert to DFA";
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

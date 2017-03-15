package GUIPlugins.SimpleFunctionPlugins;

import AutomatonSimulator.Automaton;
import AutomatonSimulator.AutomatonUtil;
import Main.Storable;

/**
 * @author fabian
 * @since 18.06.16
 */


public class AutomatonRemoveEpsilonTransitionsPlugin extends SimpleFunctionPlugin {

    @Override
    public Storable execute(Storable object) {
        Automaton automaton = (Automaton) object;
        Automaton deepCopy = (Automaton) automaton.deep_copy();
        Automaton algo = AutomatonUtil.removeEpsilonTransitions(automaton);
        algo.setPreviousAutomaton(deepCopy);
        return algo;
    }

    @Override
    public String getName() {
        return "Remove Epsilon Transitions";
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

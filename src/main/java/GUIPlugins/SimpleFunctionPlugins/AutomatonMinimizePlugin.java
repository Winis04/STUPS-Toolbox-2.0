package GUIPlugins.SimpleFunctionPlugins;

import AutomatonSimulator.Automaton;
import AutomatonSimulator.AutomatonUtil;
import Main.Storable;




public class AutomatonMinimizePlugin extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Object object) {
        Automaton automaton = (Automaton) object;
        Automaton deepCopy = (Automaton) automaton.deep_copy();
        Automaton algo = AutomatonUtil.minimizeDFA(automaton);
        algo.setPreviousAutomaton(deepCopy);
        return algo;
    }

    @Override
    public String getName() {
        return "Minimize";
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

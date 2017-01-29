package GUIPlugins.SimpleFunctionPlugins;

import AutomatonSimulator.Automaton;
import AutomatonSimulator.AutomatonUtil;
import Main.Storable;

/**
 * @author fabian
 * @since 28.07.16
 */
@SuppressWarnings("ALL")
public class AutomatonCompletePlugin extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Object object) {
        Automaton automaton = (Automaton) object;
        return AutomatonUtil.completeDFA(automaton);
    }

    @Override
    public String getName() {
        return "Complete";
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

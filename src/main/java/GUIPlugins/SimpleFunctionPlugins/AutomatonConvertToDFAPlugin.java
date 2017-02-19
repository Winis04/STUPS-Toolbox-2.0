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
    public Storable execute(Object object) {
        Automaton automaton = (Automaton) object;
        return AutomatonUtil.convertToDFA(automaton);
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

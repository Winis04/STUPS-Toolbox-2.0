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
        return AutomatonUtil.renameStates(automaton);
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

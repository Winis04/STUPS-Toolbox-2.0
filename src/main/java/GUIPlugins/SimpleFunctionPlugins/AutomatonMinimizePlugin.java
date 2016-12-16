package GUIPlugins.SimpleFunctionPlugins;

import AutomatonSimulator.Automaton;
import AutomatonSimulator.AutomatonUtil;
import Main.GUI;
import javafx.scene.control.MenuItem;

/**
 * Created by Isabel on 01.12.2016.
 */
public class AutomatonMinimizePlugin extends SimpleFunctionPlugin {
    @Override
    public Object execute(Object object) {
        Automaton automaton = (Automaton) object;
        return AutomatonUtil.minimizeDFA(automaton);
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

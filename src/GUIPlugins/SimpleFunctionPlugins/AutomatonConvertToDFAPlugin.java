package GUIPlugins.SimpleFunctionPlugins;

import AutomatonSimulator.Automaton;
import AutomatonSimulator.AutomatonUtil;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 * Created by fabian on 24.07.16.
 */
public class AutomatonConvertToDFAPlugin implements SimpleFunctionPlugin{
    @Override
    public Object execute(Object object) {
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

    @Override
    public MenuItem getMenuItem() {
        return new MenuItem("Convert to DFA");
    }
}

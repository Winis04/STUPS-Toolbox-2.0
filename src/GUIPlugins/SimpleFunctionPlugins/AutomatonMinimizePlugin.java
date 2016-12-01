package GUIPlugins.SimpleFunctionPlugins;

import AutomatonSimulator.Automaton;
import AutomatonSimulator.AutomatonUtil;
import Main.GUI;
import javafx.scene.control.MenuItem;

/**
 * Created by Isabel on 01.12.2016.
 */
public class AutomatonMinimizePlugin implements SimpleFunctionPlugin {
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

    @Override
    public MenuItem getMenuItem(GUI gui) {
        AutomatonMinimizePlugin plugin= this;
        MenuItem item = new MenuItem(this.getName());
        item.setOnAction(t -> {
            Object ret= plugin.execute(gui.getCli().objects.get(plugin.inputType()));
            if(ret != null) {
                gui.getCurrentDisplayPlugin().refresh(ret);
            }
        });
        return item;
    }
}

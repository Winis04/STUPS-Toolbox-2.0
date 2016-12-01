package GUIPlugins.SimpleFunctionPlugins;

import AutomatonSimulator.Automaton;
import AutomatonSimulator.AutomatonUtil;
import Console.Storable;
import Main.GUI;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 * Created by fabian on 18.06.16.
 */
public class AutomatonRemoveEpsilonTransitionsPlugin implements SimpleFunctionPlugin {

    @Override
    public Object execute(Object object) {
        Automaton automaton = (Automaton) object;
        return AutomatonUtil.removeEpsilonTransitions(automaton);
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

    @Override
    public MenuItem getMenuItem(GUI gui) {
        AutomatonRemoveEpsilonTransitionsPlugin plugin= this;
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

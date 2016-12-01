package GUIPlugins.SimpleFunctionPlugins;

import AutomatonSimulator.Automaton;
import AutomatonSimulator.AutomatonUtil;
import Console.Storable;
import Main.GUI;
import javafx.scene.control.MenuItem;

/**
 * Created by fabian on 26.07.16.
 */
public class AutomatonRenameStatesPlugin implements SimpleFunctionPlugin {
    @Override
    public Object execute(Object object) {
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

    @Override
    public MenuItem getMenuItem(GUI gui) {
        AutomatonRenameStatesPlugin plugin= this;
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

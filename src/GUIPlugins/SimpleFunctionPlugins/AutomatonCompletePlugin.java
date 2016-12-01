package GUIPlugins.SimpleFunctionPlugins;

import AutomatonSimulator.Automaton;
import AutomatonSimulator.AutomatonUtil;
import Main.GUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 * Created by fabian on 28.07.16.
 */
public class AutomatonCompletePlugin implements SimpleFunctionPlugin {
    @Override
    public Object execute(Object object) {
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

    @Override
    /**
     * return a MenuItem for the SimpleFunctionPlugin.
     * @return
     */
    public MenuItem getMenuItem(GUI gui) {
        AutomatonCompletePlugin plugin= this;
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

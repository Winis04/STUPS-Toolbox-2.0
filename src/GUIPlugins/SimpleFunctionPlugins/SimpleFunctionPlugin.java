package GUIPlugins.SimpleFunctionPlugins;


import Main.GUI;
import javafx.scene.control.MenuItem;
import GrammarSimulator.Grammar;

/**
 * Created by fabian on 18.06.16.
 *
 * SimplePlugins are used on Storables and pop up as context menus of these
 */

public abstract class SimpleFunctionPlugin {


    boolean operatesOnAllStorables = false;
    GUI gui = null;

    /**
     * Takes an object, does something with it, and returns the changed object.
     * For example: If a plugin executes an algorithm on an automaton, the object needs to be casted to {@link AutomatonSimulator.Automaton}.
     *              Then, the algorithm can be executed on it, and then this method returns the changed automaton.
     *              It is also possible, to return an object, that is of a completely different type, than the input object.
     *
     * @param object The object.
     * @return The changed object.
     */
    public abstract Object execute(Object object);

    /**
     * Returns the plugin's name.
     *
     * @return The plugin's name.
     */
    abstract String getName();

    /**
     * Returns the desired object-type, needed by {@link #execute(Object)}.
     * For example: If {@link #execute(Object)} needs an automaton, this method returns {@link AutomatonSimulator.Automaton}.class.
     *
     * @return The object-type.
     */
    public abstract Class inputType();

    /**
     * Returns the type of the object that {@link #execute(Object)} returns.
     * For example: If {@link #execute(Object)} returns an automaton, this method returns {@link AutomatonSimulator.Automaton}.class.
     *
     * @return The object-type.
     */
    abstract Class outputType();


    public void setGUI(GUI gui) {
        this.gui=gui;
    }
    /**
     * return a MenuItem for the SimpleFunctionPlugin.
     * @return
     */
    public MenuItem getMenuItem(GUI gui) {
        SimpleFunctionPlugin plugin= this;
        MenuItem item = new MenuItem(this.getName());
        item.setOnAction(t -> {
            Object ret= plugin.execute(gui.getCli().objects.get(plugin.inputType()));

            if(ret != null) {
                gui.refresh(ret);

            }

        });
        return item;

    }

    public boolean operatesOnAllStorables() {
        return false;
    }
    public boolean operatesOnSuperClass() {
        return false;
    }

    public boolean createsOutput() {
        return false;
    }
}

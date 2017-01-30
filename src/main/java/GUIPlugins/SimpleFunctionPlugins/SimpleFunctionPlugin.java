package GUIPlugins.SimpleFunctionPlugins;


import Main.GUI;
import Main.Storable;
import javafx.scene.control.MenuItem;

/**
 * SimplePlugins are used on Storables and pop up as context menus of these
 * @author fabian
 * @since 18.06.16
 */

@SuppressWarnings("ALL")
public abstract class SimpleFunctionPlugin implements Comparable{

    /**
     * reference to the gui
     */
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
    protected abstract Storable execute(Object object);

    /**
     * Returns the plugin's name.
     *
     * @return The plugin's name.
     */
    public abstract String getName();

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


    /**
     * must be called, if the plugin should have access to the gui. this is necessary, if the plugin does more than manipulation of one object
     * @param gui the GUI
     */
    public void setGUI(GUI gui) {
        this.gui=gui;
    }
    /**
     * return a MenuItem for the SimpleFunctionPlugin, that is shown in the context-menu of the treeView
     * @param gui the GUI
     * @return the MenuItem
     */
    public MenuItem getMenuItem(GUI gui) {
        SimpleFunctionPlugin plugin= this;
        MenuItem item = new MenuItem(this.getName());
        if(plugin.shouldBeDisabled()) {
            item.setDisable(true);
        }
        item.setOnAction(t -> {
            Object ret= plugin. execute(gui.getContentController().getObjects().get(plugin.inputType()));
            Storable storable = (Storable) ret;
            if(ret != null) {
                Class clazz = ret.getClass();

                gui.getContentController().getObjects().put(clazz,storable); //add new object as the current object
                gui.getContentController().getStore().get(clazz).put(storable.getName(),storable); //add object to the store
                gui.refresh(ret); //switch to new object
                gui.refresh(); //refresh the treeView

            }

        });
        return item;

    }

    boolean shouldBeDisabled() {
        return false;
    }
    /**
     * should be overwritten, if the plugin should be able to operate on more than one type of storables, e.g. {@link Copy}
     * @return default: false
     */
    public boolean operatesOnAllStorables() {
        return false;
    }

    /**
     * states, if the plugin creates output. plugins with output are blue framed when in latex mode
     * @return default: false
     */
    public boolean createsOutput() {
        return false;
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof SimpleFunctionPlugin) {
            SimpleFunctionPlugin other = (SimpleFunctionPlugin) o;
            if(this.operatesOnAllStorables() && other.operatesOnAllStorables()) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }
}

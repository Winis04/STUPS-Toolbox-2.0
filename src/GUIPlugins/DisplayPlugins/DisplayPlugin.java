package GUIPlugins.DisplayPlugins;

import Main.GUI;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;


/**
 * Created by fabian on 18.06.16.
 */
public interface DisplayPlugin {

    /**
     * Return a JavaFX-Node that visualizes the given object.
     * This could be really anything, deriving from Node.
     * For example a Pane containing more Nodes, or even a SwingNode,
     * so Swing can also be used to display the object.
     *
     * @param object The object, that should be displayed.
     * @return The JavaFX-{@link Node}.
     */
    Node display(Object object);

    /**
     * This method is called by the main program, when the object has changed,
     * so the plugin gets the opportunity to update the view.
     *
     * @param object The object, that is being displayed.
     */
    Node refresh(Object object);

    /**
     * This method is called by the main program, when the user desires to create a new object from scratch.
     * What it should do, is, create the new object and update the view.
     *
     * @return The newly created object.
     */

    Object newObject();

    /**
     * Returns this plugin's name.
     *
     * @return This plugin's name.
     */
    String getName();

    /**
     * Returns the object-type, that this plugin can display.
     * For example: If this plugin displays an automaton, this method returns {@link AutomatonSimulator.Automaton}.class
     * @return The object-type.
     */
    Class displayType();

    void setGUI(GUI gui);
}

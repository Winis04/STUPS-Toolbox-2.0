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
     * This method is called by the main program, when the user desires to load an object from a file.
     * What it should do, is show some sort of window that lets the user choose a file (e.g. with {@link javafx.stage.FileChooser}),
     * parse the file into an object and update the view.
     *
     * @return The opened object.
     */
    Object openFile();

    /**
     * This method is called by the main program, when the user desires to save the given object to a file.
     * What it should do, is show some sort of window that lets the user choose a file (e.g. with {@link javafx.stage.FileChooser}),
     * and save the object to file, so that it can be loaded by {@link #openFile()}.
     *
     * @param object The object, that should be saved.
     */
    void saveFile(Object object);

    /**
     * Returns a set of optional Menus, that will be displayed besides the Filemenu in the menubar.
     * May also return null, if there are no additional menus for this plugin.
     *
     * @param object The object, that is being displayed.
     * @param node The JavaFX-{@link Node}, that displays the object.
     * @return The set of Menus.
     */
    HashSet<Menu> menus(Object object, Node node);

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

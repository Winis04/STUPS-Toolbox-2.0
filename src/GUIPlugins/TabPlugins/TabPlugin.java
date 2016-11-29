package GUIPlugins.TabPlugins;

import GUIPlugins.DisplayPlugins.DisplayPlugin;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;

/**
 * Created by isabel on 29.11.16.
 */
public abstract class TabPlugin {
    /** the tab belonging to this plugin **/
    private Tab tab;
    /** the big pane showing content **/
    BorderPane contentPane;
    /** **/
    BorderPane pane;


    /**
     * Returns a JavaFX-{@link Node}, e.g. a {@link javafx.scene.layout.Pane},
     * that contains all the necessary control elements, for this plugin.
     *
     * @param object The object that is currently being displayed by the active {@link GUIPlugins.DisplayPlugins.DisplayPlugin}.
     * @param GUI The currently loaded display-plugin. This can be used, to interact with the plugin.
     * @return The JavaFX-{@link Node}.
     */
    public abstract Node getFxNode(Object object, DisplayPlugin GUI);

    /**
     * Returns this plugin's name.
     *
     * @return This plugin's name.
     */
    public abstract String getName();

    /**
     * Returns the object-type, that this plugin works with.
     * For example: If {@link #getFxNode(Object, DisplayPlugin)} needs an automaton, this method returns {@link AutomatonSimulator.Automaton}.class.
     *
     * @return The object-type.
     */
    public abstract Class displayPluginType();

    public void setTab(Tab tab) {
        this.tab=tab;
    }
    public void setContentPane(BorderPane contentPanePane) {
        this.contentPane=contentPanePane;
    }

    public void setPane(BorderPane pane) {
        this.pane = pane;
    }

    public Tab getTab() {
        return tab;
    }


}

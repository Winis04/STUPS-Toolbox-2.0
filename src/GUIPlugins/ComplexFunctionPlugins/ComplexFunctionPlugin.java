package GUIPlugins.ComplexFunctionPlugins;

import GUIPlugins.DisplayPlugins.DisplayPlugin;
import javafx.scene.Node;

/**
 * Created by fabian on 19.06.16.
 */
public interface ComplexFunctionPlugin {

    /**
     * Returns a JavaFX-{@link Node}, e.g. a {@link javafx.scene.layout.Pane},
     * that contains all the necessary control elements, for this plugin.
     *
     * @param object The object that is currently being displayed by the active {@link GUIPlugins.DisplayPlugins.DisplayPlugin}.
     * @param GUI The currently loaded display-plugin. This can be used, to interact with the plugin.
     * @return The JavaFX-{@link Node}.
     */
    Node getFxNode(Object object, DisplayPlugin GUI);

    /**
     * Returns this plugin's name.
     *
     * @return This plugin's name.
     */
    String getName();

    /**
     * Returns the object-type, that this plugin works with.
     * For example: If {@link #getFxNode(Object, DisplayPlugin)} needs an automaton, this method returns {@link AutomatonSimulator.Automaton}.class.
     *
     * @return The object-type.
     */
    Class displayPluginType();
}

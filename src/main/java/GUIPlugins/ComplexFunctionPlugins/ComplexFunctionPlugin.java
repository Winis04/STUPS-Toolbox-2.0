package GUIPlugins.ComplexFunctionPlugins;

import GUIPlugins.DisplayPlugins.DisplayPlugin;
import GrammarSimulator.Symbol;
import Main.GUI;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * A plugin for the GUI, which executes more complicate commands than
 * {@link GUIPlugins.SimpleFunctionPlugins.SimpleFunctionPlugin}.
 * @author fabian
 * @since 19.06.16
 */


public abstract class ComplexFunctionPlugin {

    protected GUI gui;

    /**
     * Every  ComplexFunctionPlugin can process one type of objects.
     * @return the class of the object this ComplexFunctionPlugin processes
     */
    public abstract Class getInputType();

    /**
     * Returns a JavaFX-{@link Node}, e.g. a {@link javafx.scene.layout.Pane},
     * that contains all the necessary control elements, for this plugin.
     *
     * @param object The object that is currently being displayed by the active {@link GUIPlugins.DisplayPlugins.DisplayPlugin}.
     * @param GUI The currently loaded display-plugin. This can be used, to interact with the plugin.
     * @return The JavaFX-{@link Node}.
     */
    abstract Node getFxNode(Object object, DisplayPlugin GUI);

    /**
     * Returns this plugin's name.
     *
     * @return This plugin's name.
     */
    protected abstract String getName();

    /**
     *
     * @param object the object this ComplexFunctionPlugin handles
     * @param GUI the {@link DisplayPlugin} which displays the class belonging to object
     * @return a {@link Tab} which contains the content of {@link #getFxNode}
     */
    public Tab getAsTab(Object object, DisplayPlugin GUI) {
        Tab tab=new Tab(this.getName());
        AnchorPane anchorPane = new AnchorPane();
        Node node = getFxNode(object,GUI);
        anchorPane.getChildren().add(node);
        double height = GUI.getGUI().getOverviewController().heightTabPane();
        AnchorPane.setTopAnchor(node,0.0);
        AnchorPane.setBottomAnchor(node,0.0);
        AnchorPane.setRightAnchor(node,0.0);
        AnchorPane.setLeftAnchor(node,0.0);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(anchorPane);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        tab.setContent(scrollPane);
        if(GUI.getGUI().getStateController().isTooltips()) {
            tab.setTooltip(new Tooltip(tooltip()));
        } else {
            tab.setTooltip(null);
        }
        return tab;
    }

    /**
     * Returns the object-type, that this plugin works with.
     * For example: If {@link #getFxNode(Object, DisplayPlugin)} needs an automaton, this method returns {@link AutomatonSimulator.Automaton}.class.
     *
     * @return The object-type.
     */
    public abstract Class displayPluginType();

    /**
     * states, if the plugin creates output. plugins with output are blue framed when in latex mode
     * @return default: false. if true, this methods creates output
     */

    public boolean createsOutput() {
        return false;
    }

    public abstract String tooltip();

    public void setGUI(GUI gui) {
        this.gui=gui;
    }
}

package GUIPlugins.DisplayPlugins.GrammarTabs;

import GrammarSimulator.Grammar;
import javafx.scene.Node;

/**
 * @author fabian
 * @since 15.08.16
 */
public interface GrammarTab {

    /**
     * Return a JavaFX-Node that visualizes the function of this tab.
     * This could be really anything, deriving from Node.
     * For example a Pane containing more Nodes, or even a SwingNode,
     * so Swing can also be used.
     *
     * @param grammar The grammar, that should be displayed.
     * @return The JavaFX-Node.
     */
    Node getFxNode(Grammar grammar);

    /**
     * Returns this tab's name.
     *
     * @return This tab's name.
     */
    String getName();
}

package GUIPlugins.DisplayPlugins.GrammarTabs;

import GrammarSimulator.Grammar;
import javafx.scene.Node;
//TODO better java doc
/**
 * Visualizes things that have to do with {@link Grammar}s
 * @author fabian
 * @since 15.08.16
 */

@SuppressWarnings("unused")
interface GrammarTab {

    /**
     * Return a JavaFX-Node that visualizes the function of this tab.
     * This could be really anything, deriving from Node.
     * For example a Pane containing more Nodes, or even a SwingNode,
     * so Swing can also be used.
     *
     * @param grammar The grammar, that should be displayed.
     * @return The JavaFX-Node.
     */
    @SuppressWarnings("unused")
    Node getFxNode(Grammar grammar);

    /**
     * Returns this tab's name.
     *
     * @return This tab's name.
     */
    @SuppressWarnings("unused")
    String getName();
}

package GUIPlugins.DisplayPlugins;

import GUIPlugins.DisplayPlugins.GrammarTabs.EditTab;
import GUIPlugins.DisplayPlugins.GrammarTabs.FirstFollowTab;
import GUIPlugins.DisplayPlugins.GrammarTabs.LLParsingTableTab;
import GrammarSimulator.Grammar;
import Main.GUI;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

/**
 * Displays {@link Grammar}.
 * @author fabian
 * @since 13.08.16
 */

public class GrammarGUI implements DisplayPlugin {

    /**
     * This GUI's root pane.
     */
    private TabPane rootPane;

    private GUI gui;

    @Override
    public Node display(Object object) {
        /*
      The grammar, that is being displayed.
     */
        Grammar grammar = (Grammar) object;

        rootPane = new TabPane();

        /*
      Contains all {@link GrammarTab}s.
     */

        rootPane.getTabs().clear();
        Tab edit =new Tab("edit");
        edit.setContent(new EditTab(gui).getFxNode(grammar));
        edit.setClosable(false);
        rootPane.getTabs().add(edit);
        return rootPane;
    }

     @Override
     public Node clear() {
         return new AnchorPane();
     }

    /**
     * creates a {@link Tab} in which the first and follow sets are printed
     * @param grammar the {@link Grammar} it handles
     */
    public void firstFollow(Grammar grammar) {
        String name="First Follow";
        if(rootPane.getTabs().stream().anyMatch(tab -> tab.getText().equals(name))) {
            rootPane.getTabs().forEach(tab -> {
                if (tab.getText().equals(name)) {
                    rootPane.getSelectionModel().select(tab);
                }
            });
        } else {
            Tab ff = new Tab(name);
            ff.setContent(new FirstFollowTab().getFxNode(grammar));
            ff.setClosable(true);
            rootPane.getTabs().add(ff);
        }
    }

    /**
     * creates a {@link Tab} in which the LL-Parsing-Table is printed
     * @param grammar the {@link Grammar} it handles
     */
    public void llParsingTableTab(Grammar grammar) {
        String name="LL-parsing Table";
        if(rootPane.getTabs().stream().anyMatch(tab -> tab.getText().equals(name))) {
            rootPane.getTabs().forEach(tab -> {
                if (tab.getText().equals(name)) {
                    rootPane.getSelectionModel().select(tab);
                }
            });
        } else {
            Tab ll = new Tab(name);
            ll.setContent(new LLParsingTableTab().getFxNode(grammar));
            ll.setClosable(true);
            rootPane.getTabs().add(ll);
        }
    }

    @Override
    public Node refresh(Object object) {
        return this.display(object);
    }

    @Override
    public Object newObject() {
        return new Grammar();
    }

    @Override
    public String getName() {
        return "Grammar";
    }

    @Override
    public Class displayType() {
        return Grammar.class;
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui=gui;
    }

    @Override
    public GUI getGUI() {
        return gui;
    }

    /**
     * Getter-Method for {@link #rootPane}
     * @return the {@link #rootPane}
     */
    public TabPane getRootPane() {
        return rootPane;
    }
}

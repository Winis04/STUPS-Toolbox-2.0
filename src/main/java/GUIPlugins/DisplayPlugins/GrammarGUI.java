package GUIPlugins.DisplayPlugins;

import GUIPlugins.DisplayPlugins.GrammarTabs.EditTab;
import GUIPlugins.DisplayPlugins.GrammarTabs.FirstFollowTab;
import GUIPlugins.DisplayPlugins.GrammarTabs.GrammarTab;
import GUIPlugins.DisplayPlugins.GrammarTabs.LLParsingTableTab;
import GrammarSimulator.Grammar;
import Main.GUI;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.util.*;

/**
 * Created by fabian on 13.08.16.
 */
public class GrammarGUI implements DisplayPlugin {

    /**
     * The grammar, that is being displayed.
     */
    private Grammar grammar;

    /**
     * This GUI's root pane.
     */
    private TabPane rootPane;

    /**
     * Contains all {@link GrammarTab}s.
     */
    private HashSet<GrammarTab> tabs;

    /**
     * Maps each GUI-Tab to its {@link GrammarTab}.
     */
    private HashMap<Tab, GrammarTab> tabMap = new HashMap<>();

    private GUI gui;

    @Override
    public Node display(Object object) {
        grammar = (Grammar) object;

        rootPane = new TabPane();

        tabs = new HashSet<>();

        rootPane.getTabs().clear();
        Tab edit =new Tab("edit");
        edit.setContent(new EditTab(gui).getFxNode(grammar));
        edit.setClosable(false);
        rootPane.getTabs().add(edit);
        return rootPane;
    }


    public void firstfollow(Grammar grammar) {
        String name="First Follow";
        if(rootPane.getTabs().stream().anyMatch(tab -> tab.getText().equals(name))) {
            rootPane.getTabs().stream().forEach(tab -> {
                if(tab.getText().equals(name)) {
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
    public void llParsingTableTab(Grammar grammar) {
        String name="LL-parsing Table";
        if(rootPane.getTabs().stream().anyMatch(tab -> tab.getText().equals(name))) {
            rootPane.getTabs().stream().forEach(tab -> {
                if(tab.getText().equals(name)) {
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

    public TabPane getRootPane() {
        return rootPane;
    }
}

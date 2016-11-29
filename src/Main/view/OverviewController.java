package Main.view;

import AutomatonSimulator.Automaton;
import Console.CLI;
import GUIPlugins.ComplexFunctionPlugins.ComplexFunctionPlugin;
import GUIPlugins.TabPlugins.TabPlugin;
import Main.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import sun.reflect.generics.tree.Tree;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Isabel on 28.11.2016.
 */
public class OverviewController {
    @FXML
    TreeView<String> treeView;
    @FXML
    TabPane tabPane;
    @FXML
    BorderPane contentPane;

    HashSet<TabPlugin> tabPlugins=null;
    HashMap<Class, Tab> tabs=new HashMap<>();

    private GUI gui;
    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public OverviewController() {
        makeTree();
    }
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        makeTree();

    }
    public void makeTree() {
        TreeItem<String> root=new TreeItem<>("Storables");
        if(!CLI.store.keySet().isEmpty()) {
            /** top items **/
            CLI.store.keySet().stream()
                    .forEach(clazz -> {
                        List<TreeItem<String>> list= CLI.store.get(clazz).keySet().stream()
                                .map(key -> new TreeItem<String>(key+""))
                                .collect(Collectors.toList());
                        TreeItem<String> top=new TreeItem<String>(clazz.getSimpleName());
                        top.getChildren().addAll(list);
                        top.setExpanded(true);
                        root.getChildren().add(top);
                    });

            root.setExpanded(true);
            treeView.setRoot(root);
        }
    }
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public void addTabs(HashSet<TabPlugin> tabPlugins) {
        tabPlugins.stream().forEach(tabPlugin -> {
            tabPlugin.setContentPane(contentPane);
            BorderPane pane=new BorderPane();
            tabPlugin.setPane(pane);
            Tab tab=new Tab(tabPlugin.getClass().getSimpleName(),pane);
            tabPlugin.setTab(tab);

            tabPlugin.initialize(new Automaton());
        });
        this.tabPlugins=tabPlugins;
        tabPlugins.stream().forEach(tabPlugin -> {
            // creates a tab for every class in tabPlugins
            tabs.put(tabPlugin.getClass(),tabPlugin.getTab());
        });
        tabPane.getTabs().addAll(tabs.values());
    }
}

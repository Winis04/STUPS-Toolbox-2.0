package Main.view;

import AutomatonSimulator.Automaton;
import Console.CLI;
import GUIPlugins.TabPlugins.TabPlugin;

import Main.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.*;

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
    @FXML
    FlowPane simpleFunctionPane;


    private GUI gui;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public OverviewController() {
//
    }
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

    }
    public void makeTree() {
        TreeItem<String> root=new TreeItem<>("Storables");
        if(!gui.getCli().store.keySet().isEmpty()) {
            /** top items **/

            gui.getCli().store.keySet().stream()
                    .forEach(clazz -> {
                        List<TreeItem<String>> list= gui.getCli().store.get(clazz).keySet().stream()
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



    public TabPane getTabPane() {
        return tabPane;
    }

    public BorderPane getContentPane() {
        return contentPane;
    }

    public FlowPane getSimpleFunctionPane() {
        return simpleFunctionPane;
    }
}

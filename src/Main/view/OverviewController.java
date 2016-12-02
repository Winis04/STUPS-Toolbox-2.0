package Main.view;



import Console.Storable;
import GUIPlugins.DisplayPlugins.DisplayPlugin;
import Main.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static javax.swing.tree.TreeSelectionModel.SINGLE_TREE_SELECTION;

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

    ContextMenu dynamicContextMenu=new ContextMenu();

    private HashMap<Class, List<MenuItem>> map = new HashMap<>();


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

    public void makeTree(ArrayList<MenuItem> dynamicMenu) {

        TreeItem<String> root=new TreeItem<>("Storables");

        if(!gui.getCli().store.keySet().isEmpty()) {
            /** top items **/

            gui.getCli().store.keySet().stream()
                    .forEach(clazz -> {
                        // the name of the storable objects
                        TreeItem<String> top=new TreeItem<String>(clazz.getSimpleName());

                        // every object of the top-type
                        List<TreeItem<String>> list= gui.getCli().store.get(clazz).keySet().stream()
                                .map(key -> new TreeItem<>(key))
                                .collect(Collectors.toList());

                        top.getChildren().addAll(list);
                        top.setExpanded(true);
                        root.getChildren().add(top);
                    });

            treeView.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
                if (e.getButton()==MouseButton.SECONDARY) {
                    TreeItem selected = treeView.getSelectionModel().getSelectedItem();

                    //item is selected - this prevents fail when clicking on empty space
                    if (selected!=null) {
                        //open context menu on current screen position

                       if(selected.getParent().getValue().equals("Grammar")) {
                           this.openContextMenu(dynamicMenu,e.getScreenX(),e.getScreenY());
                       } else {
                           this.openContextMenu(dynamicMenu,e.getScreenX(),e.getScreenY());
                       }
                    }
                } else {
                    //any other click cause hiding menu
                    dynamicContextMenu.hide();
                }
            });


            root.setExpanded(true);
            treeView.setRoot(root);
            treeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                // the selected Item
                TreeItem<String> selectedItem = newValue;
                if(!newValue.getParent().equals(root) && !newValue.equals(root)) {
                    // the String that belongs to the parent treeItem
                    String parent = selectedItem.getParent().getValue().toLowerCase();
                    // we get the parents (and the childs class) by looking in the lookup table
                    Class parentClass = gui.getCli().lookUpTable.get(parent);


                    // now we can get the matching storable object
                    Storable selectedStorable = gui.getCli().store.get(parentClass).get(selectedItem.getValue());
                    // put it as the current grammar/automaton/..

                    gui.getCli().objects.put(parentClass, selectedStorable);
                    gui.switchDisplayGui(parentClass);
                    gui.refresh(selectedStorable);

                } else {

                }
            });


        }
    }

    private void openContextMenu(List<MenuItem> list,double x, double y) {
        dynamicContextMenu.getItems().clear();
        dynamicContextMenu.getItems().addAll(list);

        //show menu
        dynamicContextMenu.show(treeView, x, y);
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

    public TreeView<String> getTreeView() {
        return treeView;
    }


}

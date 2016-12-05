package Main.view;



import Console.Storable;
import GUIPlugins.DisplayPlugins.GrammarGUI;
import GUIPlugins.SimpleFunctionPlugins.SimpleFunctionPlugin;
import GrammarSimulator.Grammar;
import Main.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.util.*;
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

    public void makeTree(Collection<SimpleFunctionPlugin> dynamicMenu) {

        if(treeView.getRoot()!=null) {
            treeView.getRoot().getChildren().clear();
        }
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
                    if (selected!=null && !selected.equals(root)) {

                        //open context menu on current screen position
                        if(selected.getParent().equals(root)) {
                            String clazz = selected.getValue().toString().toLowerCase();
                            this.openContextMenuOnSuperClass(gui.getCli().lookUpTable.get(clazz),dynamicMenu,e.getScreenX(),e.getScreenY());
                        } else {
                            this.openContextMenu(getSuperTypeOfSelectedItem(selected),dynamicMenu, e.getScreenX(), e.getScreenY());
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

                if(newValue != null && !newValue.equals(root) && newValue.getParent()!= null &&!newValue.getParent().equals(root) && newValue.getParent().getParent()!=null) {
                    // the String that belongs to the parent treeItem
                   gui.switchStorable(selectedItem);

                }
            });
            treeView.setEditable(true);
           // treeView.setEditable(true);
         //   ContextMenu contextMenu = new ContextMenu();
         //   contextMenu.getItems().addAll(gui.getSimpleFunctionPlugins().values().stream().map(x -> x.getMenuItem(gui)).collect(Collectors.toList()));
         //   treeView.setContextMenu(contextMenu);
        }
    }

    public Class getSuperTypeOfSelectedItem(TreeItem<String> selectedItem) {
        // the String that belongs to the parent treeItem
        String parent = selectedItem.getParent().getValue().toLowerCase();
        // we get the parents (and the childs class) by looking in the lookup table
        Class parentClass = gui.getCli().lookUpTable.get(parent);
        return parentClass;

    }


    private void openContextMenu(Class parent, Collection<SimpleFunctionPlugin> list, double x, double y) {
        dynamicContextMenu.getItems().clear();
        dynamicContextMenu.getItems().addAll(list.stream()
                .filter(sfp -> sfp.operatesOnAllStorables()==false && sfp.operatesOnSuperClass()==false && sfp.inputType().equals(parent) )
                .map(sfp -> sfp.getMenuItem(gui)).collect(Collectors.toList()));
        dynamicContextMenu.getItems().addAll(list.stream()
                .filter(sfp -> sfp.operatesOnAllStorables()==true && sfp.operatesOnSuperClass()==false)
                .map(sfp -> sfp.getMenuItem(gui)).collect(Collectors.toList()));

        //show menu
        dynamicContextMenu.show(treeView, x, y);
    }
    private void openContextMenuOnSuperClass(Class clazz, Collection<SimpleFunctionPlugin> list, double x, double y) {
        dynamicContextMenu.getItems().clear();
        dynamicContextMenu.getItems().addAll(list.stream()
                .filter(sfp -> sfp.operatesOnSuperClass()==true)
                .map(sfp -> sfp.getMenuItem(gui)).collect(Collectors.toList()));

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


    public TreeView<String> getTreeView() {
        return treeView;
    }


}

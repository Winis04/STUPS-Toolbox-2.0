package Main.view;



import GUIPlugins.SimpleFunctionPlugins.SimpleFunctionPlugin;
import Main.GUI;
import Print.PrintMode;
import Print.Printer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Isabel
 * @since 28.11.2016
 */
@SuppressWarnings("ALL")
public class OverviewController {
    @FXML
    TreeView<String> treeView;
    @FXML
    TabPane tabPane;
    @FXML
    BorderPane contentPane;
    @FXML
    AnchorPane rightSide;
    @FXML
    AnchorPane overview;


    private final ContextMenu dynamicContextMenu=new ContextMenu();

    public static Comparator<Class> classComparator = (c1,c2) -> c1.getSimpleName().compareTo(c2.getName());

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
            /* top items **/

            gui.getCli().store.keySet().stream().sorted(classComparator).forEach(clazz -> {
                // the name of the storable objects
                TreeItem<String> top = new TreeItem<>(clazz.getSimpleName());

                // every object of the top-type
                List<TreeItem<String>> list = gui.getCli().store.get(clazz).keySet().stream().sorted()
                        .map(TreeItem::new)
                        .collect(Collectors.toList());

                top.getChildren().addAll(list);
                top.setExpanded(true);
                root.getChildren().add(top);
            });

            treeView.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
                if (e.getButton()==MouseButton.SECONDARY) {
                    TreeItem<String> selected = treeView.getSelectionModel().getSelectedItem();
                    //item is selected - this prevents fail when clicking on empty space
                    if (selected!=null && !selected.equals(root)) {

                        //open context menu on current screen position
                        if(!selected.getParent().equals(root)) {
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

                if(newValue != null && !newValue.equals(root) && newValue.getParent()!= null &&!newValue.getParent().equals(root) && newValue.getParent().getParent()!=null) {
                    // the String that belongs to the parent treeItem
                   gui.switchStorable(newValue);

                }
            });
            treeView.setEditable(true);
           // treeView.setEditable(true);
         //   ContextMenu contextMenu = new ContextMenu();
         //   contextMenu.getItems().addAll(gui.getSimpleFunctionPlugins().values().stream().map(x -> x.getMenuItem(gui)).collect(Collectors.toList()));
         //   treeView.setContextMenu(contextMenu);
        } //TODO need else here? Store is empty, do what?
    }

    public Class getSuperTypeOfSelectedItem(TreeItem<String> selectedItem) {
        // the String that belongs to the parent treeItem
        String parent = selectedItem.getParent().getValue().toLowerCase();
        // we get the parents (and the child's class) by looking in the lookup table
        return gui.getCli().lookUpTable.get(parent);

    }


    private void openContextMenu(Class parent, Collection<SimpleFunctionPlugin> list, double x, double y) {
        dynamicContextMenu.getItems().clear();
        dynamicContextMenu.getItems().addAll(list.stream()
                .filter(sfp -> !sfp.operatesOnAllStorables()  && sfp.inputType().equals(parent) )
                .map(sfp -> {
                    MenuItem item = sfp.getMenuItem(gui);
                    if(sfp.createsOutput() && Printer.printmode!= PrintMode.NO) {
                        item.setStyle("-fx-border-color: blue");
                    }
                    return item;
                }).collect(Collectors.toList()));
        dynamicContextMenu.getItems().addAll(list.stream()
                .filter(sfp -> sfp.operatesOnAllStorables() )
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

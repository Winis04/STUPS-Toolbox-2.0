package Main.view;



import GUIPlugins.SimpleFunctionPlugins.SimpleFunctionPlugin;
import Main.GUI;
import Main.Storable;
import Print.PrintMode;
import Print.Printer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.text.Collator;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.stream.Collectors;




@SuppressWarnings("unused")
public class OverviewController {
    @SuppressWarnings("unused")
    @FXML
    TreeView<String> treeView;
    @SuppressWarnings("unused")
    @FXML
    TabPane tabPane;
    @SuppressWarnings("unused")
    @FXML
    BorderPane contentPane;
    @SuppressWarnings("unused")
    @FXML
    AnchorPane rightSide;
    @SuppressWarnings("unused")
    @FXML
    AnchorPane overview;


    @SuppressWarnings("unused")
    private final ContextMenu dynamicContextMenu=new ContextMenu();

    @SuppressWarnings("unused")
    private Collection<SimpleFunctionPlugin> dynamicMenuContent;

    @SuppressWarnings("unused")
    private final Collator collator = Collator.getInstance(Locale.ENGLISH);

    @SuppressWarnings("unused")
    private final Comparator<SimpleFunctionPlugin> sfpComparator = (x, y) -> collator.compare(x.getName().replaceAll(" ",""),y.getName().replaceAll(" ",""));
    @SuppressWarnings("unused")
    private GUI gui;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    @SuppressWarnings("unused")
    public OverviewController() {
//
    }

    @SuppressWarnings("unused")
    public void initialize(GUI gui, Collection<SimpleFunctionPlugin> dynamicMenuContent) {
        this.gui=gui;
        this.dynamicMenuContent=dynamicMenuContent.stream().sorted(sfpComparator).collect(Collectors.toSet());
       // this.dynamicMenuContent=dynamicMenuContent;
        makeTree();
    }
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @SuppressWarnings("unused")
    @FXML
    private void initialize() {

    }

    @SuppressWarnings("unused")
    public void updateTree() {

       //TODO check if the store is empty?
        treeView.getRoot().getChildren().forEach(top -> top.getChildren().clear());
        treeView.getRoot().getChildren()
                .forEach(top -> {
                    Class clazz = gui.getContent().getLookUpTable().get(top.getValue().toLowerCase());
                    HashMap<String, Storable> map = gui.getContent().getStore().get(clazz);
                    if(map != null && !map.isEmpty()) {
                        map.keySet().stream()
                                .sorted(String::compareTo)
                                .forEach(key -> {
                                    TreeItem<String> child = new TreeItem<>(key);
                                    top.getChildren().add(child);
                                });
                    }
                });

    }
    @SuppressWarnings("unused")
    private void makeTree() {
        TreeItem<String> root=new TreeItem<>("Storables");

        gui.getContent().getLookUpTable().keySet().stream().filter(s -> !s.equals("pda"))
                .sorted()
                .forEach(type -> {
                    Class clazz = gui.getContent().getLookUpTable().get(type);
                    TreeItem<String> top = new TreeItem<>(clazz.getSimpleName());
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
                        this.openContextMenu(getSuperTypeOfSelectedItem(selected),dynamicMenuContent, e.getScreenX(), e.getScreenY());
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

    }

    @SuppressWarnings("unused")
    public Class getSuperTypeOfSelectedItem(TreeItem<String> selectedItem) {
        // the String that belongs to the parent treeItem
        String parent = selectedItem.getParent().getValue().toLowerCase();
        // we get the parents (and the child's class) by looking in the lookup table
        return gui.getContent().getLookUpTable().get(parent);

    }


    @SuppressWarnings("unused")
    private void openContextMenu(Class parent, Collection<SimpleFunctionPlugin> list, double x, double y) {
        dynamicContextMenu.getItems().clear();
        dynamicContextMenu.getItems().addAll(list.stream()
                .sorted((s,t) -> s.getName().compareTo(t.getName()))
                .filter(sfp -> !sfp.operatesOnAllStorables() && sfp.inputType().equals(parent)  )
                .map(sfp -> {
                    MenuItem item = sfp.getMenuItem(gui);
                    if(sfp.createsOutput() && Printer.printmode!= PrintMode.NO) {
                        item.setStyle("-fx-border-color: blue");
                    }
                    return item;
                }).collect(Collectors.toList()));
        dynamicContextMenu.getItems().add(new SeparatorMenuItem());


        dynamicContextMenu.getItems().addAll(list.stream()
                .sorted((s,t) -> s.getName().compareTo(t.getName()))
                .filter(SimpleFunctionPlugin::operatesOnAllStorables)
                .map(sfp -> {
                    MenuItem item = sfp.getMenuItem(gui);
                    if(sfp.createsOutput() && Printer.printmode!= PrintMode.NO) {
                        item.setStyle("-fx-border-color: blue");
                    }
                    return item;
                }).collect(Collectors.toList()));
        //show menu

        dynamicContextMenu.show(treeView, x, y);
    }


    @SuppressWarnings("unused")
    public void setGui(GUI gui) {
        this.gui = gui;
    }



    @SuppressWarnings("unused")
    public TabPane getTabPane() {
        return tabPane;
    }

    @SuppressWarnings("unused")
    public BorderPane getContentPane() {
        return contentPane;
    }


    @SuppressWarnings("unused")
    public TreeView<String> getTreeView() {
        return treeView;
    }



}

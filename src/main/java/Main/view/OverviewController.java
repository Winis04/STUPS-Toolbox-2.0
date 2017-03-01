package Main.view;



import GUIPlugins.SimpleFunctionPlugins.SimpleFunctionPlugin;
import Main.GUI;
import Main.Storable;
import Print.PrintMode;
import Print.Printer;
import Print.StringLiterals;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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


    private Collection<SimpleFunctionPlugin> dynamicMenuContent;


    private final Collator collator = Collator.getInstance(Locale.ENGLISH);


    private final Comparator<SimpleFunctionPlugin> sfpComparator = (x, y) -> collator.compare(x.getName().replaceAll(" ",""),y.getName().replaceAll(" ",""));

    private GUI gui;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */

    public OverviewController() {
//
    }


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

    @FXML
    private void initialize() {

    }


    public void updateTree() {

       //TODO check if the store is empty?
        if(gui.getStateController().isTooltips()) {
            treeView.setTooltip(new Tooltip(StringLiterals.TOOLTIP_TREEVIEW));
        } else {
            treeView.setTooltip(null);
        }
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

    private void makeTree() {
        if(gui.getStateController().isTooltips()) {
            treeView.setTooltip(new Tooltip(StringLiterals.TOOLTIP_TREEVIEW));
        } else {
            treeView.setTooltip(null);
        }
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


    public Class getSuperTypeOfSelectedItem(TreeItem<String> selectedItem) {
        // the String that belongs to the parent treeItem
        String parent = selectedItem.getParent().getValue().toLowerCase();
        // we get the parents (and the child's class) by looking in the lookup table
        return gui.getContent().getLookUpTable().get(parent);

    }



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

    public double heightTabPane() {
        return tabPane.getHeight();
    }



}

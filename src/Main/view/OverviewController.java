package Main.view;



import AutomatonSimulator.Automaton;
import GrammarSimulator.Grammar;
import Main.GUI;
import com.sun.webkit.ContextMenuItem;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.HashMap;
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

    public void makeTree() {
        dynamicContextMenu=new ContextMenu();

        TreeItem<String> root=new TreeItem<>("Storables");

        if(!gui.getCli().store.keySet().isEmpty()) {
            /** top items **/

            gui.getCli().store.keySet().stream()
                    .forEach(clazz -> {
                        // the name of the storable objects
                        TreeItem<String> top=new TreeItem<String>(clazz.getSimpleName());
                        // every object of the top-type
                        List<TreeItem<String>> list= gui.getCli().store.get(clazz).keySet().stream()
                                .map(key -> new TreeItem<>(key+""))
                                .collect(Collectors.toList());

                        top.getChildren().addAll(list);
                        top.setExpanded(true);
                        root.getChildren().add(top);
                    });


            /** context menus **/

            /**
             * grammar
             */
            MenuItem editGrammar=new MenuItem("edit");
            ArrayList<MenuItem> grammarList=new ArrayList<>();
            grammarList.add(editGrammar);
            /**
             * minimize
             */
            MenuItem minimize=new Menu("minimize");
            ArrayList<MenuItem> autoList=new ArrayList<>();
            autoList.add(minimize);
            map.put(Grammar.class,grammarList);
            map.put(Automaton.class,autoList);



            treeView.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
                if (e.getButton()==MouseButton.SECONDARY) {
                    TreeItem selected = treeView.getSelectionModel().getSelectedItem();

                    //item is selected - this prevents fail when clicking on empty space
                    if (selected!=null) {
                        //open context menu on current screen position
                       if(selected.getParent().getValue().equals("Grammar")) {
                           this.openContextMenu(map,Grammar.class,e.getScreenX(),e.getScreenY());
                       } else {
                           this.openContextMenu(map,Automaton.class,e.getScreenX(),e.getScreenY());
                       }
                    }
                } else {
                    //any other click cause hiding menu
                    dynamicContextMenu.hide();
                }
            });


            root.setExpanded(true);
            treeView.setRoot(root);


        }
    }

    private void openContextMenu(HashMap<Class,List<MenuItem>> map, Class clazz,double x, double y) {
        dynamicContextMenu.getItems().clear();
        dynamicContextMenu.getItems().addAll(map.get(clazz));

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
}

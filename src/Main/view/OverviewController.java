package Main.view;

import Main.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;

/**
 * Created by Isabel on 28.11.2016.
 */
public class OverviewController {
    @FXML
    AnchorPane left;
    @FXML
    AnchorPane right;
    @FXML
    TreeView<String> treeView;

    private GUI gui;
    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public OverviewController() {
    }
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {



    }
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}

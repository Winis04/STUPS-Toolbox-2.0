package Main.view;

import Main.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

/**
 * Created by Isabel on 28.11.2016.
 */
public class RootController {
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu choosePlugin;
    @FXML
    private Menu file;
    @FXML
    private Menu export;
    @FXML
    private Menu options;

    // Reference to the main application.
    private GUI gui;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public RootController() {
    }
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

    }

    /**
     * is called by the GUI to give a reference back to itself
     * @param gui
     */
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}

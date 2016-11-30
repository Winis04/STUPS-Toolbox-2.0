package Main.view;

import Main.GUI;
import Main.GUI_Copy;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

/**
 * Created by Isabel on 28.11.2016.
 */
public class RootController {


    private GUI_Copy gui;

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

    public void setGui(GUI_Copy gui) {
        this.gui = gui;
    }
}

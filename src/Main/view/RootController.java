package Main.view;


import Main.GUI;
import javafx.fxml.FXML;

/**
 * Created by Isabel on 28.11.2016.
 */
public class RootController {


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

    public void setGui(GUI gui) {
        this.gui = gui;
    }
}

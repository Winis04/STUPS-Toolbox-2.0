package Main.view;


import Console.Storable;
import Main.GUI;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

/**
 * Created by Isabel on 28.11.2016.
 */
public class RootController {


    private GUI gui;
    @FXML
    BorderPane borderPane;

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

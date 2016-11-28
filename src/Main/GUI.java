package Main;

import GUIPlugins.ComplexFunctionPlugins.ComplexFunctionPlugin;
import GUIPlugins.DisplayPlugins.DisplayPlugin;
import GUIPlugins.SimpleFunctionPlugins.SimpleFunctionPlugin;
import Console.CLI;
import Main.view.OverviewController;
import Main.view.RootController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sun.plugin.javascript.navig.Anchor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by fabian on 17.06.16.
 */
public class GUI extends Application{

    private CLI cli;

    private OverviewController overviewController;
    private RootController rootController;

    public static boolean IS_VISIBLE=false;
    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) {
        Platform.setImplicitExit(false);
        this.cli=new CLI(this);
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");

        initRootLayout();

        showPersonOverview();
        primaryStage.setOnCloseRequest(event -> {
            IS_VISIBLE = false;
            primaryStage.close();
        });
        new Thread(() -> cli.start()).start();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUI.class.getResource("view/Root.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Give the controller access to the main app.
            rootController = loader.getController();
            rootController.setGui(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showPersonOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUI.class.getResource("view/overview.fxml"));
            AnchorPane overview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
           rootLayout.setCenter(overview);
            // Give the controller access to the main app.
            overviewController = loader.getController();
            overviewController.setGui(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    /**
     * This method is called when 'gui' is entered into the Console.CLI and shows the Main.GUI.
     */
    public void show() {
        //Set IS_VISIBLE and refresh the currently loaded display-plugin,
        //as the displayed object may have changed since the Main.GUI was last opened.
        IS_VISIBLE = true;
        overviewController.makeTree();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

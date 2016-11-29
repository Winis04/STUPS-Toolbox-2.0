package Main;

import GUIPlugins.DisplayPlugins.DisplayPlugin;
import GUIPlugins.TabPlugins.TabPlugin;
import Console.CLI;
import Main.view.OverviewController;
import Main.view.RootController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;

/**
 * Created by fabian on 17.06.16.
 */
public class GUI extends Application{
    /** the cli instance that is called by the gui **/
    private CLI cli;
    /** controller for the right part of the scree**/
    private OverviewController overviewController;
    /** controller for the main menu and stuff **/
    private RootController rootController;
    /** true, when the gui is visible. Otherwise, false **/
    public boolean IS_VISIBLE=false;

    private Stage primaryStage;
    private BorderPane rootLayout;

    HashSet<TabPlugin> tabPlugins=new HashSet<>();

    @Override
    public void start(Stage primaryStage) {
        Platform.setImplicitExit(false);
        this.cli=new CLI(this);
        this.primaryStage = primaryStage;



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
            loader.setLocation(GUI.class.getResource("view/Overview.fxml"));
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
        loadtabPlugIns();
        primaryStage.show();
    }
    public void loadtabPlugIns() {
        String packagePath = null;
        try {
            packagePath = Thread.currentThread().getContextClassLoader().getResources("GUIPlugins/TabPlugins").nextElement().getFile().replace("%20", " ");
            File[] classes = new File(packagePath).listFiles();
            URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[]{new URL("file://" + packagePath)});
            for(File file : classes) {
                if(file.getName().endsWith(".class") && !file.getName().equals("TabPlugin.class") && !file.getName().contains("$")) {
                    TabPlugin instance = (TabPlugin) urlClassLoader.loadClass("GUIPlugins.TabPlugins." + file.getName().substring(0, file.getName().length() - 6)).newInstance();
                    tabPlugins.add(instance);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        overviewController.addTabs(tabPlugins);

    }

    public static void main(String[] args) {
        launch(args);
    }

}

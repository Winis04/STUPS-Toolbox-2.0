package Main;

import AutomatonSimulator.Automaton;
import Console.CLI;
import GUIPlugins.ComplexFunctionPlugins.ComplexFunctionPlugin;
import GUIPlugins.DisplayPlugins.AutomatonGUI;
import GUIPlugins.DisplayPlugins.DisplayPlugin;
import GUIPlugins.DisplayPlugins.GrammarGUI;
import GUIPlugins.SimpleFunctionPlugins.SimpleFunctionPlugin;
import GrammarSimulator.Grammar;
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
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by fabian on 17.06.16.
 */
public class GUI extends Application{
    /**
     * true, if the Main.GUI is visible. The Console.CLI will deactivate itself, if this is true.
     */
    public boolean IS_VISIBLE = false;

    /**
     * The main stage for JavaFX.
     */
    private Stage primaryStage;

    /**
     * The currenty displayed display-plugin.
     */
    private DisplayPlugin currentDisplayPlugin;

    /**
     * Contains all complex plugins. The class-type of every plugin is mapped to a instance of the plugin.
     */
    private HashSet<ComplexFunctionPlugin> complexFunctionPlugins = new HashSet<>();


    /**
     * Displays all complex plugins that match the display-type of the currently loaded display-plugin.
     */
    private TabPane complexFunctionsPane;

    /**
     *
     */
    private RootController rootController;
    private OverviewController overviewController;

    private BorderPane root;
    private BorderPane functionsPane;
    private FlowPane simpleFunctionsPane;

    private HashMap<Class, SimpleFunctionPlugin> simpleFunctionPlugins;

    private  HashMap<Class, DisplayPlugin> displayPlugins = new HashMap<>();

   // ArrayList<MenuItem> dynamicMenu = new ArrayList<>();
    /**
     * The main method. It just launches the JavaFX-Application Thread.
     *
     * @param args This program doesn't take any arguments, so this array will just be ignored
     */
    public static void main(String[] args) {
        launch();
    }

    private CLI cli;

    /**
     * This method is called when 'gui' is entered into the Console.CLI and shows the Main.GUI.
     */
    public void show() {
        //Set IS_VISIBLE and refresh the currently loaded display-plugin,
        //as the displayed object may have changed since the Main.GUI was last opened.
        IS_VISIBLE = true;
        overviewController.makeTree(simpleFunctionPlugins.values());
        if(currentDisplayPlugin != null) {
            currentDisplayPlugin.refresh(cli.objects.get(currentDisplayPlugin.displayType()));
            refreshComplexPlugins();
        }

        primaryStage.show();
    }


    private void refreshComplexPlugins() {
        complexFunctionsPane.getTabs().clear();
        for(ComplexFunctionPlugin plugin : complexFunctionPlugins) {
            if(plugin.getInputType().equals(currentDisplayPlugin.displayType())) {
                complexFunctionsPane.getTabs().add(plugin.getAsTab(cli.objects.get(currentDisplayPlugin.displayType()), currentDisplayPlugin));
            }
        }
    }

    /**
     * This method is called by the launch()-call in the main-method. It gets executed when the program is started.
     *
     * @param stage The Main.GUI's main stage.
     */
    @Override
    public void start(Stage stage) {
        this.cli=new CLI(this);
        //Prevent the JavaFX-Application Thread from exiting, when the window is closed.
        Platform.setImplicitExit(false);

        primaryStage = stage;

        //Initialize HashMaps for all display, simple, and complex plugins.
        //Each HashMap maps the class-type of each plugin to an instance of it.

        simpleFunctionPlugins = new HashMap<>();
        complexFunctionPlugins = new HashSet<>();

        //Maps the name-string of each simple plugin to an instance of it.
        //This is needed to execute a simple plugin, when the execute-button is pressed.
        HashMap<String, SimpleFunctionPlugin> executeMap = new HashMap<>();

        //Load one instance for each plugin in the packages "DisplayPlugin, SimpleFunctionPlugin and ComplexFunctionPlugin.
        try {
            String packagePath = Thread.currentThread().getContextClassLoader().getResources("GUIPlugins/DisplayPlugins").nextElement().getFile().replace("%20", " ");
            File[] classes = new File(packagePath).listFiles();
            URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[]{new URL("file://" + packagePath)});
            for(File file : classes) {
                if(file.getName().endsWith(".class") && !file.getName().equals("DisplayPlugin.class") && !file.getName().equals("ComplexDisplayPlugin.class") && !file.getName().contains("$")) {
                    DisplayPlugin instance = (DisplayPlugin) urlClassLoader.loadClass("GUIPlugins.DisplayPlugins." + file.getName().substring(0, file.getName().length() - 6)).newInstance();
                    displayPlugins.put(instance.displayType(), instance);

                }
            }

            packagePath = Thread.currentThread().getContextClassLoader().getResources("GUIPlugins/SimpleFunctionPlugins").nextElement().getFile().replace("%20", " ");
            classes = new File(packagePath).listFiles();
            urlClassLoader = URLClassLoader.newInstance(new URL[]{new URL("file://" + packagePath)});
            for(File file : classes) {
                if(file.getName().endsWith(".class") && !file.getName().equals("SimpleFunctionPlugin.class") && !file.getName().contains("$")) {
                    SimpleFunctionPlugin instance = (SimpleFunctionPlugin) urlClassLoader.loadClass("GUIPlugins.SimpleFunctionPlugins." + file.getName().substring(0, file.getName().length() - 6)).newInstance();
                    simpleFunctionPlugins.put(instance.getClass(), instance);
                }
            }

            packagePath = Thread.currentThread().getContextClassLoader().getResources("GUIPlugins/ComplexFunctionPlugins").nextElement().getFile().replace("%20", " ");
            classes = new File(packagePath).listFiles();
            urlClassLoader = URLClassLoader.newInstance(new URL[]{new URL("file://" + packagePath)});
            for(File file : classes) {
                if(file.getName().endsWith(".class") && !file.getName().equals("ComplexFunctionPlugin.class") && !file.getName().contains("$")) {
                    ComplexFunctionPlugin instance = (ComplexFunctionPlugin) urlClassLoader.loadClass("GUIPlugins.ComplexFunctionPlugins." + file.getName().substring(0, file.getName().length() - 6)).newInstance();
                    complexFunctionPlugins.add(instance);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        primaryStage.setTitle("STUPS-Toolbox");


        //Initialize panes for the currently loaded display and function plugins.
      //  BorderPane root = new BorderPane();
        initRootLayout();
        showOverview();
            overviewController.getTabPane().getTabs().add(new Tab("Hallo"));
     //   BorderPane functionsPane = new BorderPane();
       // FlowPane simpleFunctionsPane = new FlowPane();

        //Create a ComboBox to display all the simple plugins and a button to execute the selected plugin.
        ComboBox<String> functionsBox = new ComboBox<>();
        Button functionsButton = new Button("Execute");

        //When the functionsButton is pressed, the input-type for the selected simple plugin is loaded into "input",
        //so that we can get the corresponding object from the HashMap "Console.CLI.objects".
        //Then we can call the plugin's execute-method with the object as a parameter.
        functionsButton.setOnAction(event -> {
            Class input = executeMap.get(functionsBox.getSelectionModel().getSelectedItem()).inputType();
            Object ret = executeMap.get(functionsBox.getSelectionModel().getSelectedItem()).execute(cli.objects.get(input));
            if(ret != null) {
                cli.objects.put(input, ret);
                currentDisplayPlugin.refresh(ret);
                refreshComplexPlugins();
            }
        });

        //Initialize the Menubar, the plugin menu, and the file menu.


        //Setup the setOnAction-method for the menu item of every display plugin.

        simpleFunctionsPane.setHgap(10);
        simpleFunctionsPane.setPadding(new Insets(2, 0, 2, 5));
        simpleFunctionsPane.getChildren().add(functionsBox);
        simpleFunctionsPane.getChildren().add(functionsButton);



     //   root.setCenter(new Label("STUPS-Toolbox"));
      //  root.setBottom(functionsPane);

    //    primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setOnCloseRequest(event -> {
            IS_VISIBLE = false;
            primaryStage.close();
        });

        //Now, that everything is loaded, we can start the Console.CLI in a different Thread.
        //The JavaFX-Application Thread will continue running in the background,
        //and the GUI will be made visible, when the user gives the appropriate command.
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
            root = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(root);
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
    public void showOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUI.class.getResource("view/Overview.fxml"));
            AnchorPane overview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            root.setCenter(overview);
            // Give the controller access to the main app.
            overviewController = loader.getController();
            overviewController.setGui(this);
            if(overviewController.getTreeView().getRoot() == null) {
                overviewController.makeTree(simpleFunctionPlugins.values());
            }
            this.functionsPane=overviewController.getContentPane();
            this.simpleFunctionsPane=overviewController.getSimpleFunctionPane();
            this.complexFunctionsPane=overviewController.getTabPane();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refresh(Object object) {
        functionsPane.setCenter(currentDisplayPlugin.refresh(object));
    }
    public CLI getCli() {
        return cli;
    }



    public DisplayPlugin getCurrentDisplayPlugin() {
        return currentDisplayPlugin;
    }


    public void switchDisplayGui(Class clazz) {
            if(clazz.equals(Grammar.class)) {
                currentDisplayPlugin = displayPlugins.get(Grammar.class);
                if(currentDisplayPlugin==null) {
                    currentDisplayPlugin = new GrammarGUI();
                }
            } else {
                currentDisplayPlugin = displayPlugins.get(Automaton.class);
                if(currentDisplayPlugin==null) {
                    currentDisplayPlugin = new AutomatonGUI();
                }
            }
            refreshComplexPlugins();
    }

    public HashMap<Class, SimpleFunctionPlugin> getSimpleFunctionPlugins() {
        return simpleFunctionPlugins;
    }
}

package Main;

import Console.CLI;
import GUIPlugins.ComplexFunctionPlugins.ComplexFunctionPlugin;
import GUIPlugins.DisplayPlugins.DisplayPlugin;
import GUIPlugins.SimpleFunctionPlugins.SimpleFunctionPlugin;
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by fabian on 17.06.16.
 */
public class GUI_Copy extends Application{
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
     * Contains all complex plugins. The class-type of every plugin is mapped to a instane of the plugin.
     */
    private HashMap<Class, ComplexFunctionPlugin> complexFunctionPlugins = new HashMap<>();

    /**
     * Displays all complex plugins that match the display-type of the currently loaded display-plugin.
     */
    private GridPane complexFunctionsPane = new GridPane();

    /**
     *
     */
    private RootController rootController;
    private OverviewController overviewController;

    private BorderPane root;
    private BorderPane functionsPane;
    private FlowPane simpleFunctionsPane;
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
        if(currentDisplayPlugin != null) {
            currentDisplayPlugin.refresh(cli.objects.get(currentDisplayPlugin.displayType()));
            refreshComplexPlugins();
        }

        primaryStage.show();
    }

    private void refreshComplexPlugins() {
        complexFunctionsPane.getChildren().clear();
        int i = 0;
        for(Class functionPlugin : complexFunctionPlugins.keySet()) {
            if(complexFunctionPlugins.get(functionPlugin).displayPluginType().equals(currentDisplayPlugin.getClass())) {
                ComplexFunctionPlugin plugin = complexFunctionPlugins.get(functionPlugin);
                FlowPane pane = new FlowPane();
                Label label = new Label(plugin.getName() + ":");

                pane.setHgap(10);
                pane.getChildren().add(label);
                pane.getChildren().add(plugin.getFxNode(cli.objects.get(currentDisplayPlugin.displayType()), currentDisplayPlugin));

                complexFunctionsPane.add(pane, 0, i);
                i++;
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
        HashMap<Class, DisplayPlugin> displayPlugins = new HashMap<>();
        HashMap<Class, SimpleFunctionPlugin> simpleFunctionPlugins = new HashMap<>();
        complexFunctionPlugins = new HashMap<>();

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
                    displayPlugins.put(instance.getClass(), instance);
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
                    complexFunctionPlugins.put(instance.getClass(), instance);
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
        MenuBar menuBar = new MenuBar();

        Menu pluginMenu = new Menu("Choose Plugin");
        Menu fileMenu = new Menu("File");

        MenuItem newMenu = new MenuItem("New");
        MenuItem openMenu = new MenuItem("Open");
        MenuItem saveMenu = new MenuItem("Save");
        fileMenu.getItems().addAll(newMenu, openMenu, saveMenu);

        HashSet<Menu> menus = new HashSet<>();

        //Setup the setOnAction-method for the menu item of every display plugin.
        for(Class displayPlugin : displayPlugins.keySet()) {
            MenuItem menuItem = new MenuItem(displayPlugins.get(displayPlugin).getName());

            menuItem.setOnAction(event -> {
                //get the display plugin and its corresponding object.
                currentDisplayPlugin = displayPlugins.get(displayPlugin);
                Object object = cli.objects.get(displayPlugins.get(displayPlugin).displayType());

                //if the object doesn't exist, create a new one.
                if(object == null) {
                    try {
                        object = displayPlugins.get(displayPlugin).displayType().newInstance();
                        cli.objects.put(displayPlugins.get(displayPlugin).displayType(), object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //Now, that the display plugin and object are loaded, we can display the plugin.
                root.setCenter(currentDisplayPlugin.display(object));

                //Setup the menubar.
                menuBar.getMenus().clear();
                menus.clear();
                menuBar.getMenus().add(pluginMenu);
                menus.addAll(currentDisplayPlugin.menus(cli.objects.get(currentDisplayPlugin.displayType()), root.getCenter()));
                menuBar.getMenus().add(fileMenu);
                menuBar.getMenus().addAll(menus);

                //Setup the new, save and open menu-items.
                if(currentDisplayPlugin != null) {
                    newMenu.setOnAction(event1 -> {
                        Object ret = currentDisplayPlugin.newObject();
                        if(ret != null) {
                            cli.objects.put(currentDisplayPlugin.displayType(), ret);
                            currentDisplayPlugin.refresh(ret);
                            refreshComplexPlugins();
                        }
                    });

                    openMenu.setOnAction(event1 -> {
                        Object ret = currentDisplayPlugin.openFile();
                        if(ret != null) {
                            cli.objects.put(currentDisplayPlugin.displayType(), ret);
                            currentDisplayPlugin.refresh(ret);
                            refreshComplexPlugins();
                        }
                    });

                    saveMenu.setOnAction(event1 -> currentDisplayPlugin.saveFile(cli.objects.get(currentDisplayPlugin.displayType())));
                } else {
                    //if no plugin is loaded, the menu-items don't have any function.
                    newMenu.setOnAction(event1 -> {});
                    openMenu.setOnAction(event1 -> {});
                    saveMenu.setOnAction(event1 -> {});
                }

                //Display all simple plugins which input type matches the display type of the display plugin.
                functionsBox.getItems().clear();
                executeMap.clear();
                for(Class functionPlugin : simpleFunctionPlugins.keySet()) {
                    if(simpleFunctionPlugins.get(functionPlugin).inputType().equals(object.getClass())) {
                        functionsBox.getItems().add(simpleFunctionPlugins.get(functionPlugin).getName());
                        executeMap.put(simpleFunctionPlugins.get(functionPlugin).getName(), simpleFunctionPlugins.get(functionPlugin));
                    }
                }
                functionsBox.getSelectionModel().selectFirst();

                //Display all complex plugins which input type matches the display type of the display plugin.
                refreshComplexPlugins();

                functionsPane.setVisible(true);
                complexFunctionsPane.setVisible(true);
                simpleFunctionsPane.setVisible(true);

                //if there are no function plugin, make the functionsPane invisible.
                if(complexFunctionsPane.getChildren().isEmpty()) {
                    complexFunctionsPane.setVisible(false);
                }

                if(functionsBox.getItems().isEmpty()) {
                    simpleFunctionsPane.setVisible(false);
                }

                if(complexFunctionsPane.getChildren().isEmpty() && functionsBox.getItems().isEmpty()) {
                    functionsPane.setVisible(false);
                }
            });

            pluginMenu.getItems().add(menuItem);
        }

        //construct the rest of the Main.GUI. This is pretty straight forward.
        menuBar.getMenus().add(pluginMenu);
        complexFunctionsPane.setVgap(5);
        complexFunctionsPane.setPadding(new Insets(2, 0, 2, 5));

        simpleFunctionsPane.setHgap(10);
        simpleFunctionsPane.setPadding(new Insets(2, 0, 2, 5));
        simpleFunctionsPane.getChildren().add(functionsBox);
        simpleFunctionsPane.getChildren().add(functionsButton);
        functionsPane.setCenter(complexFunctionsPane);
        functionsPane.setRight(simpleFunctionsPane);
        functionsPane.setStyle("-fx-border-color: black");
        functionsPane.setVisible(false);

        root.setTop(menuBar);
     //   root.setCenter(new Label("STUPS-Toolbox"));
      //  root.setBottom(functionsPane);

    //    primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setOnCloseRequest(event -> {
            IS_VISIBLE = false;
            primaryStage.close();
        });

        //Now, that everything is loaded, we can start the Console.CLI in a different Thread.
        //The JavaFX-Application Thread will continue running in the background,
        //and the Main.GUI will be made visible, when the user gives the appropriate command.
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
            this.functionsPane=overviewController.getContentPane();
            this.simpleFunctionsPane=overviewController.getSimpleFunctionPane();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CLI getCli() {
        return cli;
    }
}

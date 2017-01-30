package Main;

import AutomatonSimulator.Automaton;
import GUIPlugins.ComplexFunctionPlugins.ComplexFunctionPlugin;
import GUIPlugins.DisplayPlugins.AutomatonGUI;
import GUIPlugins.DisplayPlugins.DisplayPlugin;
import GUIPlugins.DisplayPlugins.GrammarGUI;
import GUIPlugins.DisplayPlugins.PushDownAutomatonGUI;
import GUIPlugins.SimpleFunctionPlugins.SimpleFunctionPlugin;
import GrammarSimulator.Grammar;
import Main.view.OverviewController;
import Main.view.RootController;
import Print.PrintMode;
import Print.Printer;
import PushDownAutomatonSimulator.PushDownAutomaton;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * One of the two main classes of the application. Allows the user to interact with the application
 * via a graphical user interface.
 * Created by fabian on 17.06.16.
 * @author fabian
 * @since 17.06.16
 */
@SuppressWarnings("ALL")
public class GUI extends Application{
    /**
     * true, if the Main.GUI is visible. The Main.CLI will deactivate itself, if this is true.
     */
    public boolean IS_VISIBLE = false;

    /**
     * The main stage for JavaFX.
     */
    private Stage primaryStage;


    /**
     * The currently displayed display-plugin.
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
     * the Controller for the root gui (the menu bar)
     */
    private RootController rootController;
    /**
     * the controller for the content
     */
    private OverviewController overviewController;

    private BorderPane root;
    private BorderPane functionsPane;

    private HashMap<Class, SimpleFunctionPlugin> simpleFunctionPlugins;

    private final HashMap<Class, DisplayPlugin> displayPlugins = new HashMap<>();

    private int textSize = 12;

    /**
     * name of the null symbol
     */
    public static String nameOfNullSymbol = "\u03B5";

    private String defaultStyle = "/blue.css";
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
     * This method is called when 'gui' is entered into the Main.CLI and shows the Main.GUI.
     */
    void show() {
        Printer.printmode = PrintMode.NO;
        //Set IS_VISIBLE and refresh the currently loaded display-plugin,
        //as the displayed object may have changed since the Main.GUI was last opened.
        IS_VISIBLE = true;
        //TODO something like update Tree
        overviewController.updateTree();
        if(currentDisplayPlugin != null) {
            currentDisplayPlugin.refresh(cli.objects.get(currentDisplayPlugin.displayType()));
            refreshComplexPlugins();
        }

        primaryStage.show();
    }


    /**
     * refreshes the view. Depending on the current display plugin type, the right current object is chosen,
     * selected in the treeView and displayed in the middle.
     * Is called after a plugin's execution
     */
    public void refresh() {
        Collection<SimpleFunctionPlugin> list=simpleFunctionPlugins.values().stream()
                .sorted((x,y) -> x.compareTo(y))
                .collect(Collectors.toList());
        overviewController.updateTree(); //makes the tree //todo!
        if(currentDisplayPlugin != null) {
            currentDisplayPlugin.refresh(cli.objects.get(currentDisplayPlugin.displayType())); //shows current object
            refreshComplexPlugins(); //refreshes the complex plugins
        }
        /* selected the right treeViewObject **/
        if(overviewController.getTreeView().getRoot() != null && !overviewController.getTreeView().getRoot().getChildren().isEmpty()) {
            Optional<TreeItem<String>> s = overviewController.getTreeView().getRoot().getChildren()
                    .stream()
                    .reduce((x, y) -> {
                        if (x != null && x.getValue().equals(currentDisplayPlugin.displayType().getSimpleName())) {
                            return x;
                        } else if (y != null && y.getValue().equals(currentDisplayPlugin.displayType().getSimpleName())) {
                            return y;
                        } else {
                            return null;
                        }
                    });

            if (s.isPresent()) {
                TreeItem<String> root = s.get();
                Optional<TreeItem<String>> selected = root.getChildren().stream()
                        .reduce((x, y) -> {
                            if (x != null && x.getValue().equals(((Storable) cli.objects.get(currentDisplayPlugin.displayType())).getName())) {
                                return x;
                            } else if (y != null && y.getValue().equals(((Storable) cli.objects.get(currentDisplayPlugin.displayType())).getName())) {
                                return y;
                            } else {
                                return null;
                            }
                        });
                if (selected.isPresent()) {
                    overviewController.getTreeView().getSelectionModel().select(selected.get());
                }
            }
        }


    }

    /**
     * saves a object of type {@link Storable} in a hashmap, so the user can switch between objects
     * @param storable the object that should be stored
     * @param clazz the {@link Class}
     * @param name name of the storable object. Functions as the key.
     */
    public void addToStore(Storable storable, Class clazz, String name) {
        Storable storable1 = storable.otherName(name);
        cli.store.putIfAbsent(clazz, new HashMap<>());
        cli.store.get(clazz).put(name, storable1);
        refresh();
    }

    /**
     * changes the object currently displayed depending on the selection of the user
     * @param selectedItem the currently selected TreeView cell
     */
    public void switchStorable(TreeItem<String> selectedItem) {
        String parent = selectedItem.getParent().getValue().toLowerCase();
        // we get the parents (and the child's class) by looking in the lookup table
        Class parentClass = cli.lookUpTable.get(parent);


        // now we can get the matching storable object
        Storable selectedStorable = cli.store.get(parentClass).get(selectedItem.getValue());
        // put it as the current grammar/automaton/..

        cli.objects.put(parentClass, selectedStorable);
        switchDisplayGui(parentClass);
        refresh(selectedStorable);
    }

    /**
     * refreshes the complex plugins in the bottom. Is necessary when the type of the object changes.
     * There are different complex plugins for different types
     */
    public void refreshPlugins() {
        refreshComplexPlugins();
    }

    private void refreshComplexPlugins() {
        complexFunctionsPane.getTabs().clear();
        complexFunctionPlugins.stream().filter(plugin -> plugin.getInputType().equals(currentDisplayPlugin.displayType())).forEachOrdered(plugin -> {
            Tab current = plugin.getAsTab(cli.objects.get(currentDisplayPlugin.displayType()), currentDisplayPlugin);
            if (Printer.printmode == PrintMode.LATEX && plugin.createsOutput()) {
                current.setStyle("-fx-background-color: aqua;");
            } else {
                current.setStyle("");
            }
            complexFunctionsPane.getTabs().add(current);
        });
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


        // load every plugin
        Reflections reflections = new Reflections("GUIPlugins/DisplayPlugins");
        Set<Class<? extends DisplayPlugin>> s = reflections.getSubTypesOf(DisplayPlugin.class);
        s.forEach(r -> {
            try {
                DisplayPlugin displayPlugin = r.newInstance();

                displayPlugins.put(displayPlugin.displayType(), displayPlugin);
                displayPlugin.setGUI(this);

            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        reflections = new Reflections("GUIPlugins/ComplexFunctionPlugins");
        Set<Class<? extends ComplexFunctionPlugin>> s2 = reflections.getSubTypesOf(ComplexFunctionPlugin.class);
        s2.forEach(cfp -> {
            ComplexFunctionPlugin plugin = null;
            try {
                plugin = cfp.newInstance();
                complexFunctionPlugins.add(plugin);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        reflections = new Reflections("GUIPlugins/SimpleFunctionPlugins");
        Set<Class<? extends SimpleFunctionPlugin>> s3 = reflections.getSubTypesOf(SimpleFunctionPlugin.class);
        s3.forEach(sfp -> {
            try {
                SimpleFunctionPlugin plugin = sfp.newInstance();
                simpleFunctionPlugins.put(plugin.getClass(), plugin);
                plugin.setGUI(this);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });


        primaryStage.setTitle("STUPS-Toolbox");


        //Initialize panes for the currently loaded display and function plugins.
      //  BorderPane root = new BorderPane();
        initRootLayout();
        showOverview();

     //   BorderPane functionsPane = new BorderPane();
       // FlowPane simpleFunctionsPane = new FlowPane();



        primaryStage.setOnCloseRequest(event -> {
            IS_VISIBLE = false;
            Printer.printmode=PrintMode.CONSOLE;
            primaryStage.close();
        });

        setStyleSheet(defaultStyle);

        //Now, that everything is loaded, we can start the Main.CLI in a different Thread.
        //The JavaFX-Application Thread will continue running in the background,
        //and the GUI will be made visible, when the user gives the appropriate command.
       new Thread(() -> cli.start()).start();
    }
    /**
     * Initializes the root layout.
     */
    private void initRootLayout() {

        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Root.fxml"));
            root = loader.load();

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
     * opens a {@link FileChooser} with title "load "+string, that lets the user open a file
     * @param string the name of the dialog
     * @return a {@link File}
     */
    public File loadFile(String string) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("load "+string);
        return fileChooser.showOpenDialog(primaryStage);
    }

    /**
     * returns the primary {@link Stage} of the GUI
     * @return the primary {@link Stage}
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * returns a {@link File} in that the user has chosen for saving in a {@link FileChooser}
     * @param string the name of the dialog
     * @return a {@link File}
     */
    public File openFileToSave(String string) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("save "+string);
        return fileChooser.showSaveDialog(primaryStage);
    }
    /**
     * Shows the person overview inside the root layout.
     */
    private void showOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Overview.fxml"));
            AnchorPane overview = loader.load();

            // Set person overview into the center of root layout.
            root.setCenter(overview);
            // Give the controller access to the main app.
            overviewController = loader.getController();
            overviewController.initialize(this,simpleFunctionPlugins.values());


            overview.setOnKeyTyped(event -> {
                if(event.getCharacter().equals("u")) {
                    textSize +=1;
                    overview.setStyle("-fx-font-size: "+(textSize));

                }
                if(event.getCharacter().equals("d")) {
                    textSize -=1;
                    overview.setStyle("-fx-font-size: "+(textSize));

                }
            });
            this.functionsPane=overviewController.getContentPane();

            this.complexFunctionsPane=overviewController.getTabPane();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * refreshes the GUI so that it shows the passed object and the matching {@link ComplexFunctionPlugin}s
     * @param object the object that is displayed
     */
    public void refresh(Object object) {
        switchDisplayGui(object.getClass());
        functionsPane.setCenter(currentDisplayPlugin.refresh(object));
        refreshComplexPlugins();
    }

    /**
     * getter for {@link CLI}
     * @return the {@link CLI}
     */
    public CLI getCli() {
        return cli;
    }


    /**
     * Getter-Method for the current {@link DisplayPlugin}
     * @return the current {@link DisplayPlugin}
     */
    public DisplayPlugin getCurrentDisplayPlugin() {
        return currentDisplayPlugin;
    }


    /**
     * if the type of the current selected storable changes, the {@link DisplayPlugin} has to be changed
     * @param clazz the {@link Class} of the current selected object
     */
    public void switchDisplayGui(Class clazz) {
            if(clazz.equals(Grammar.class)) {
                currentDisplayPlugin = displayPlugins.get(Grammar.class);
                if(currentDisplayPlugin==null) {
                    currentDisplayPlugin = new GrammarGUI();
                }
            } else if(clazz.equals(Automaton.class)){
                currentDisplayPlugin = displayPlugins.get(Automaton.class);
                if(currentDisplayPlugin==null) {
                    currentDisplayPlugin = new AutomatonGUI();
                }
            } else if(clazz.equals(PushDownAutomaton.class)) {
                currentDisplayPlugin = displayPlugins.get(PushDownAutomaton.class);
                if(currentDisplayPlugin==null) {
                    currentDisplayPlugin = new PushDownAutomatonGUI();
                }
            }
                //TODO: if you create a new storable you must add the matching gui here
            refreshComplexPlugins();
    }

    /**
     * opens a {@link Dialog} with the forwarded strings
     * @param type the {@link javafx.scene.control.Alert.AlertType} of the dialog
     * @param title the title of the dialog
     * @param header the header text
     * @param content the content text
     */
    public void dialog(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    /**
     * opens a error {@link Dialog}  with the forwarded string
     * @param string the title of the dialog
     */
    public void errorDialog(String string) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(string);
        alert.showAndWait();
    }
    /**
     * opens a info {@link Dialog}  with the forwarded string
     * @param string the title of the dialog
     */
    public void infoDialog(String string) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(string);

        alert.showAndWait();

    }


    /**
     * the controller for the content panel
     * @return an {@link OverviewController}
     */
    public OverviewController getOverviewController() {
        return overviewController;
    }

    /**
     * the controller for the root elements (menus, etc)
     * @return a {@link RootController}
     */
    public RootController getRootController() {
        return rootController;
    }

    /**
     * sets the style sheet.
     * @param path Path to to the stylesheet
     */
    public void setStyleSheet(String path) {
        primaryStage.getScene().getStylesheets().clear();
        String css = this.getClass().getResource(path).toExternalForm();
        defaultStyle = css;
        primaryStage.getScene().getStylesheets().add(css);
    }

    /**
     * set the style sheet to a external file
     * @param file a File not located in the resources
     */
    public void setStyleSheetExternal(File file) {
        primaryStage.getScene().getStylesheets().clear();
        String css =  "file:///" + file.getAbsolutePath().replace("\\", "/");
        defaultStyle = css;
        primaryStage.getScene().getStylesheets().add(css);
    }


}

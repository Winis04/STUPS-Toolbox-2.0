package Main.view;


import AutomatonSimulator.Automaton;
import AutomatonSimulator.AutomatonUtil;
import CLIPlugins.PrintModePlugin;
import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Main.GUI;
import Main.Storable;
import PushDownAutomatonSimulator.PushDownAutomaton;
import PushDownAutomatonSimulator.PushDownAutomatonUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;




@SuppressWarnings("unused")
public class RootController {


    @SuppressWarnings("unused")
    private GUI gui;
    @SuppressWarnings("unused")
    @FXML
    BorderPane borderPane;
    @SuppressWarnings("unused")
    @FXML
    MenuBar menuBar;
    @SuppressWarnings("unused")
    @FXML
    Menu Help;
    @SuppressWarnings("unused")
    @FXML
    Menu Export;
    @SuppressWarnings("unused")
    @FXML
    MenuItem latexModeOn;
    @SuppressWarnings("unused")
    @FXML
    MenuItem latexModeOff;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    @SuppressWarnings("unused")
    public RootController() {
    }
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @SuppressWarnings({"EmptyMethod", "unused"})
    @FXML
    private void initialize() {
    }
    @SuppressWarnings("unused")
    @FXML
    private void latexModeOn() {


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("choose latex file");
        ExtensionFilter extFilter = new ExtensionFilter("latex","*.tex");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(gui.getPrimaryStage());

        if(file!=null) {
            latexModeOn.setDisable(true);
            latexModeOff.setDisable(false);
            borderPane.setStyle("-fx-border-color: blue; -fx-border-width: 4;");


            String path = file.getAbsolutePath();



            PrintModePlugin printModePlugin = new PrintModePlugin();
            printModePlugin.execute(null, new String[]{"latex", path, "--force"});
            if(gui.getCurrentDisplayPlugin() != null) {
                gui.refreshPlugins();
            }
        }
    }
    @SuppressWarnings("unused")
    @FXML
    private void latexModeOff() {
        latexModeOn.setDisable(false);
        latexModeOff.setDisable(true);
        borderPane.setStyle("");

        PrintModePlugin printModePlugin = new PrintModePlugin();
        printModePlugin.execute(null,new String[]{"no"});
        if(gui.getCurrentDisplayPlugin() != null) {
            gui.refreshPlugins();
        }
    }


    @SuppressWarnings("unused")
    @FXML
    public void about() {

    }

    @SuppressWarnings("unused")
    @FXML
    public void save() {

    }

    @SuppressWarnings("unused")
    @FXML
    public void changeNullSymbol() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("choose Nullsymbol");
        alert.setHeaderText(null);
        alert.setContentText("choose, which presentation of the nullsymbol you want");

        ButtonType epsilon = new ButtonType("\u03B5");
        ButtonType lambda = new ButtonType("\u03BB");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(epsilon, lambda, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent()) {
            if (result.get() == epsilon) {
                GUI.nameOfNullSymbol = "\u03B5";


            } else if (result.get() == lambda) {
                GUI.nameOfNullSymbol = "\u03BB";


            }
        } //TODO else-case

        TreeItem<String> selectedItem = gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem();

        String parent = selectedItem.getParent().getValue().toLowerCase();
        // we get the parents (and the child's class) by looking in the lookup table
        Class parentClass = gui.getContent().getLookUpTable().get(parent);


        // now we can get the matching storable object
        Storable selectedStorable =  gui.getContent().getStore().get(parentClass).get(selectedItem.getValue());
        // put it as the current grammar/automaton/..

        gui.getContent().getObjects().put(parentClass, selectedStorable);
        gui.switchDisplayGui(parentClass);
        gui.refresh(selectedStorable);

    }

    @SuppressWarnings("unused")
    @FXML
    private void changeStyle() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("change css-style");
        alert.setHeaderText(null);
        alert.setContentText("choose a premade style or your own .css");

        ButtonType royal = new ButtonType("royal");
        ButtonType autumn = new ButtonType("autumn");
        ButtonType ocean = new ButtonType("ocean");
        ButtonType own = new ButtonType("own...");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(royal, autumn, ocean, own, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent()) {
            if (result.get() == royal) {
                gui.setStyleSheet("/royal.css");
            } else if (result.get() == autumn) {
                gui.setStyleSheet("/mild.css");
            } else if (result.get() == ocean) {
                gui.setStyleSheet("/blue.css");
            } else if (result.get() == own) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("open .css");
                fileChooser.getExtensionFilters().addAll(
                        new ExtensionFilter("style-sheets", "*.css"));

                File selectedFile = fileChooser.showOpenDialog(gui.getPrimaryStage());
                if (selectedFile != null) {
                    gui.setStyleSheet(selectedFile.getAbsolutePath());
                } //TODO else case
            }
        } //TODO else case
    }

    @SuppressWarnings("unused")
    private void loadStorable(Class clazz) {
        File file = gui.loadFile(clazz.getSimpleName());
        if(file != null) {
            BufferedReader reader;
            TextInputDialog dialog = new TextInputDialog(file.getName());
            dialog.setTitle("Choose a name");
            dialog.setContentText("Please enter Name:");
            Optional<String> result = dialog.showAndWait();
            if(result.isPresent() && !result.get().isEmpty()) {
                String name = result.get();
                try {
                    reader = new BufferedReader(new FileReader(file));
                    String string = "";
                    String line;
                    while ((line = reader.readLine()) != null) {
                        string = string + line + "\n";
                    }
                    Storable storable;
                    if (clazz.equals(Grammar.class)) {
                        storable = GrammarUtil.parse(string, name);
                    } else if (clazz.equals(Automaton.class)) {
                        storable = AutomatonUtil.parse(string, name);
                    } else if (clazz.equals(PushDownAutomaton.class)) {
                        storable = PushDownAutomatonUtil.parse(string, name);
                    } else {
                        storable = null;
                        //TODO: add other types here
                    }
                    if (storable != null) {
                        gui.getContent().getObjects().put(clazz, storable);
                        gui.getContent().getStore().get(clazz).put(name,storable);
                        gui.refresh(storable);
                        gui.refresh();
                    }

                } catch (IOException e) {
                    gui.errorDialog("I/O Error");

                } catch (Exception e) {
                    gui.errorDialog("this is not a valid file");
                }
            }

        }
    }

    @SuppressWarnings("unused")
    @FXML
    public void loadGrammar() {
       loadStorable(Grammar.class);

    }
    @SuppressWarnings("unused")
    @FXML
    public void loadAutomaton() {
       loadStorable(Automaton.class);

    }
    @SuppressWarnings("unused")
    @FXML
    public void loadPushDownAutomaton() {
        loadStorable(PushDownAutomaton.class);
    }

    @SuppressWarnings("unused")
    @FXML
    public void saveCurrentGrammar() {
        Object object = gui.getContent().getObjects().get(Grammar.class);
        if(object != null) {
            Grammar grammar = (Grammar) object;
            File file = gui.openFileToSave("Grammar");
            if(file!=null) {
                GrammarUtil.save(grammar, file);
            }
        }
    }

    @SuppressWarnings("unused")
    @FXML
    public void saveCurrentAutomaton() {
        Object object = gui.getContent().getObjects().get(Automaton.class);
        if(object != null) {
            Automaton automaton = (Automaton) object;
            File file = gui.openFileToSave("Automaton");
            if(file!=null) {
                AutomatonUtil.save(automaton, file);
            }
        }
    }

    @SuppressWarnings("unused")
    @FXML
    public void saveCurrentPushDownAutomaton() {
        Object object = gui.getContent().getObjects().get(PushDownAutomaton.class);
        if(object != null) {
            PushDownAutomaton pda= (PushDownAutomaton) object;
            File file = gui.openFileToSave("PDA");
            if(file!=null) {
                try {
                    PushDownAutomatonUtil.save(pda, file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("unused")
    @FXML
    public void toGraphViz() {
        Object object = gui.getContent().getObjects().get(Automaton.class);
        if(object != null) {
            Automaton automaton = (Automaton) object;
            File file = gui.openFileToSave("Automaton");
            if(file!=null) {
                AutomatonUtil.toGraphViz(automaton, file);
            }
        }
    }

    @SuppressWarnings("unused")
    @FXML
    public void switchWorkspace() {
        DirectoryChooser chooser = new DirectoryChooser();
        if(new File(gui.getStateController().getPath_to_workspace()).exists()) {
            chooser.setInitialDirectory(new File(gui.getStateController().getPath_to_workspace()));
        }
        chooser.setTitle("Choose new workspace");
        File dir = chooser.showDialog(gui.getPrimaryStage());
       if(!gui.getStateController().switchWorkspace(dir)) {
           gui.errorDialog("not a valid workspace!");
       }


    }

    @SuppressWarnings("unused")
    @FXML
    public void newGrammar() {
        Grammar grammar = new Grammar();
        Storable storable = new Grammar(grammar.getStartSymbol(),grammar.getRules(),"newGrammar", (Grammar) grammar.getPreviousVersion());
        Class clazz = Grammar.class;
        String name = storable.getName();
        gui.getContent().getObjects().put(clazz, storable);
        gui.getContent().getStore().get(clazz).put(name,storable);
        gui.refresh(storable);
        gui.refresh();
    }

    @SuppressWarnings("unused")
    @FXML
    public void newPDA() {
        PushDownAutomaton pda = new PushDownAutomaton();
        Storable storable = new PushDownAutomaton(pda.getStartState(),pda.getInitialStackLetter(),pda.getRules(),"newPDA", (PushDownAutomaton) pda.getPreviousVersion());
        Class clazz = PushDownAutomaton.class;
        String name = storable.getName();
        gui.getContent().getObjects().put(clazz, storable);
        gui.getContent().getStore().get(clazz).put(name,storable);
        gui.refresh(storable);
        gui.refresh();
    }

    @SuppressWarnings("unused")
    @FXML
    public void newAutomaton() {
        Storable storable = new Automaton();
        Class clazz = Automaton.class;
        String name = storable.getName();
        gui.getContent().getObjects().put(clazz, storable);
        gui.getContent().getStore().get(clazz).put(name,storable);
        gui.refresh(storable);
        gui.refresh();
    }




    @SuppressWarnings("unused")
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}

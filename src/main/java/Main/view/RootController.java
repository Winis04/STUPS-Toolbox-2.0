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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;


/**
 * Created by Isabel on 28.11.2016.
 */
public class RootController {

    boolean showLatexDialog=true;

    boolean inLatexMode=false;


    private GUI gui;
    @FXML
    BorderPane borderPane;
    @FXML
    MenuBar menuBar;
    @FXML
    Menu Help;
    @FXML
    Menu Export;
    @FXML
    MenuItem latexModeOn;
    @FXML
    MenuItem latexModeOff;

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
    @FXML
    private void latexModeOn() {
        String[] arr = new String[]{"*.tex"};
        ArrayList<String> list = new ArrayList<>(Arrays.asList(arr));

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("choose latex file");
        ExtensionFilter extFilter = new ExtensionFilter("latex","*.tex");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(gui.getPrimaryStage());

        if(file!=null) {
            inLatexMode = true;
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
    @FXML
    private void latexModeOff() {
        inLatexMode=false;
        latexModeOn.setDisable(false);
        latexModeOff.setDisable(true);
        borderPane.setStyle("");

        PrintModePlugin printModePlugin = new PrintModePlugin();
        printModePlugin.execute(null,new String[]{"no"});
        if(gui.getCurrentDisplayPlugin() != null) {
            gui.refreshPlugins();
        }
    }


    @FXML
    public void about() {

    }

    @FXML
    public void save() {

    }

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
        if (result.get() == epsilon){
            GUI.nameOfNullSymbol = "\u03B5";
        } else if (result.get() == lambda) {
            GUI.nameOfNullSymbol = "\u03BB";
        } else {
            // ... user chose CANCEL or closed the dialog
        }

        TreeItem<String> selectedItem = gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem();

        String parent = selectedItem.getParent().getValue().toLowerCase();
        // we get the parents (and the childs class) by looking in the lookup table
        Class parentClass = gui.getCli().lookUpTable.get(parent);


        // now we can get the matching storable object
        Storable selectedStorable = gui.getCli().store.get(parentClass).get(selectedItem.getValue());
        // put it as the current grammar/automaton/..

        gui.getCli().objects.put(parentClass, selectedStorable);
        gui.switchDisplayGui(parentClass);
        gui.refresh(selectedStorable);

    }

    @FXML
    private void changeStyle() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("change css-style");
        alert.setHeaderText(null);
        alert.setContentText("choose a premade style or your own .css");

        ButtonType royal = new ButtonType("royal");
        ButtonType autumn = new ButtonType("autumn");
        ButtonType own = new ButtonType("own...");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(royal, autumn, own, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == royal){
            gui.setStyleSheet("/royal.css");
        } else if (result.get() == autumn) {
            gui.setStyleSheet("/mild.css");
        } else if (result.get() == own){
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("open .css");
            fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("style-sheets", "*.css"));

            File selectedFile = fileChooser.showOpenDialog(gui.getPrimaryStage());
            if (selectedFile != null) {
                gui.setStyleSheetExterne(selectedFile);
            }
        } else {

        }
    }

    private void loadStorable(Class clazz) {
        File file = gui.loadFile(clazz.getSimpleName());
        if(file != null) {
            BufferedReader reader = null;
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
                        gui.getCli().objects.put(clazz, storable);
                        gui.getCli().store.get(clazz).put(name,storable);
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

    @FXML
    public void loadGrammar() {
       loadStorable(Grammar.class);

    }
    @FXML
    public void loadAutomaton() {
       loadStorable(Automaton.class);

    }
    @FXML
    public void loadPushDownAutomaton() {
        loadStorable(PushDownAutomaton.class);
    }

    @FXML
    public void saveCurrentGrammar() {
        Object object = gui.getCli().objects.get(Grammar.class);
        if(object != null) {
            Grammar grammar = (Grammar) object;
            File file = gui.openFileToSave("Grammar");
            if(file!=null) {
                GrammarUtil.save(grammar, file);
            }
        }
    }

    @FXML
    public void saveCurrentAutomaton() {
        Object object = gui.getCli().objects.get(Automaton.class);
        if(object != null) {
            Automaton automaton = (Automaton) object;
            File file = gui.openFileToSave("Automaton");
            if(file!=null) {
                AutomatonUtil.save(automaton, file);
            }
        }
    }

    @FXML
    public void saveCurrentPushDownAutomaton() {
        Object object = gui.getCli().objects.get(PushDownAutomaton.class);
        if(object != null) {
            PushDownAutomaton pda= (PushDownAutomaton) object;
            File file = gui.openFileToSave("PDA");
            if(file!=null) {
                PushDownAutomatonUtil.save(pda, file);
            }
        }
    }

    @FXML
    public void toGraphViz() {
        Object object = gui.getCli().objects.get(Automaton.class);
        if(object != null) {
            Automaton automaton = (Automaton) object;
            File file = gui.openFileToSave("Automaton");
            if(file!=null) {
                AutomatonUtil.toGraphViz(automaton, file);
            }
        }
    }

    @FXML
    public void switchWorkspace() {



    }

    @FXML
    public void newGrammar() {
        Grammar grammar = new Grammar();
        Storable storable = new Grammar(grammar.getStartSymbol(),grammar.getRules(),"newGrammar", (Grammar) grammar.getPreviousVersion());
        Class clazz = Grammar.class;
        String name = storable.getName();
        if (storable != null) {
            gui.getCli().objects.put(clazz, storable);
            gui.getCli().store.get(clazz).put(name,storable);
            gui.refresh(storable);
            gui.refresh();
        }
    }

    @FXML
    public void newPDA() {
        PushDownAutomaton pda = new PushDownAutomaton();
        Storable storable = new PushDownAutomaton(pda.getStates(),pda.getStartState(),pda.getInitalStackLetter(),pda.getRules(),pda.getCurrentState(),"newPDA", (PushDownAutomaton) pda.getPreviousVersion());
        Class clazz = PushDownAutomaton.class;
        String name = storable.getName();
        if (storable != null) {
            gui.getCli().objects.put(clazz, storable);
            gui.getCli().store.get(clazz).put(name,storable);
            gui.refresh(storable);
            gui.refresh();
        }
    }

    @FXML
    public void newAutomaton() {
        Storable storable = new Automaton();
        Class clazz = Automaton.class;
        String name = storable.getName();
        if (storable != null) {
            gui.getCli().objects.put(clazz, storable);
            gui.getCli().store.get(clazz).put(name,storable);
            gui.refresh(storable);
            gui.refresh();
        }
    }




    public void setGui(GUI gui) {
        this.gui = gui;
    }
}

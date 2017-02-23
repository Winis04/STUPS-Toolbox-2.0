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





public class RootController {



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
    @SuppressWarnings({"EmptyMethod", "unused"})
    @FXML
    private void initialize() {
    }

    @FXML
    private void setTooltips() {
        gui.getStateController().setTooltips(!gui.getStateController().isTooltips());
        gui.refresh();
        gui.refresh();
    }
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
        if(result.isPresent()) {
            if (result.get() == epsilon) {
                GUI.nameOfNullSymbol = "\u03B5";
                gui.setNullsymbol("epsilon");

            } else if (result.get() == lambda) {
                GUI.nameOfNullSymbol = "\u03BB";
                gui.setNullsymbol("lambda");


            }
        } //TODO else-case

        TreeItem<String> selectedItem = gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem();

        if(selectedItem != null) {
            String parent = selectedItem.getParent().getValue().toLowerCase();
            // we get the parents (and the child's class) by looking in the lookup table
            Class parentClass = gui.getContent().getLookUpTable().get(parent);


            // now we can get the matching storable object
            Storable selectedStorable = gui.getContent().getStore().get(parentClass).get(selectedItem.getValue());
            // put it as the current grammar/automaton/..

            gui.getContent().getObjects().put(parentClass, selectedStorable);
            gui.switchDisplayGui(parentClass);
            gui.refresh(selectedStorable);
        }

    }


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
        Object object = gui.getContent().getObjects().get(Grammar.class);
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
        Object object = gui.getContent().getObjects().get(Automaton.class);
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

   private String chooseName(Class clazz) {
       TextInputDialog dialog = new TextInputDialog("new"+clazz.getSimpleName());
       dialog.setTitle("Choose name");
       dialog.setHeaderText("Please choose a name for the new "+clazz.getSimpleName());
       dialog.setContentText("Please enter the name:");

       Alert alert = new Alert(Alert.AlertType.ERROR);
       alert.setTitle("error");
       alert.setHeaderText("Name already taken!");


// Traditional way to get the response value.
       Optional<String> result = dialog.showAndWait();
       if(result.isPresent()) {
           while(result.isPresent() && gui.getContent().getStore().get(clazz).get(result.get()) != null) {
               //name already taken
               alert.showAndWait();
               result = dialog.showAndWait();
           }
           if(result.isPresent()) {
               return result.get();
           }
       }
       return null;
   }


    @FXML
    public void newGrammar() {
        Grammar grammar = new Grammar();
        Class clazz = Grammar.class;
        String name = chooseName(clazz);
        if(name != null) {
            Storable storable = new Grammar(grammar.getStartSymbol(), grammar.getRules(), name, (Grammar) grammar.getPreviousVersion());
            gui.getContent().getObjects().put(clazz, storable);
            gui.getContent().getStore().get(clazz).put(name, storable);
            gui.refresh(storable);
            gui.refresh();
        }
    }


    @FXML
    public void newPDA() {
        PushDownAutomaton pda = new PushDownAutomaton();
        Class clazz = PushDownAutomaton.class;
        String name = chooseName(clazz);
        if(name != null) {
            Storable storable = new PushDownAutomaton(pda.getStartState(), pda.getInitialStackLetter(), pda.getRules(), name, (PushDownAutomaton) pda.getPreviousVersion());
            gui.getContent().getObjects().put(clazz, storable);
            gui.getContent().getStore().get(clazz).put(name, storable);
            gui.refresh(storable);
            gui.refresh();
        }
    }


    @FXML
    public void newAutomaton() {
        Automaton automaton = new Automaton();
        Class clazz = Automaton.class;
        String name = chooseName(clazz);
        if(name != null) {
            Storable storable = new Automaton(automaton.getStates(), automaton.getStartState(), automaton.getAllInputs(), name);
            gui.getContent().getObjects().put(clazz, storable);
            gui.getContent().getStore().get(clazz).put(name, storable);
            gui.refresh(storable);
            gui.refresh();
        }
    }





    public void setGui(GUI gui) {
        this.gui = gui;
    }
}

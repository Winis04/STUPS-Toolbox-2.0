package Main.view;


import AutomatonSimulator.Automaton;
import AutomatonSimulator.AutomatonUtil;
import CLIPlugins.PrintModePlugin;
import GrammarParser.lexer.LexerException;
import GrammarParser.parser.ParserException;
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
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("latex","*.tex");
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
            File file = gui.openFile("Grammar");
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
            File file = gui.openFile("Automaton");
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
            File file = gui.openFile("PDA");
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
            File file = gui.openFile("Automaton");
            if(file!=null) {
                AutomatonUtil.toGraphViz(automaton, file);
            }
        }
    }

    @FXML
    public void switchWorkspace() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("choose workspace");
        File file = fileChooser.showDialog(gui.getPrimaryStage());
        if(file!=null) {
            gui.getCli().switchWorkspace(file.getAbsolutePath());
        }
        gui.refresh();


    }




    public void setGui(GUI gui) {
        this.gui = gui;
    }
}

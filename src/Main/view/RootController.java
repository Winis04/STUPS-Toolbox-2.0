package Main.view;


import AutomatonSimulator.Automaton;
import AutomatonSimulator.AutomatonUtil;
import CLIPlugins.PrintModePlugin;
import java.util.Arrays;
import GrammarParser.lexer.LexerException;
import GrammarParser.parser.ParserException;
import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Main.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.util.ArrayList;


import java.io.*;


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


            if (showLatexDialog) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "display this information again?", ButtonType.YES, ButtonType.NO);
                alert.setHeaderText("You are now in Latex-mode. Every blue-squared command writes in the file you just choose until you end the latex-mode!");

                alert.showAndWait();
                if (alert.getResult() == ButtonType.NO) {
                    showLatexDialog = false;
                }
            }


            PrintModePlugin printModePlugin = new PrintModePlugin();
            printModePlugin.execute(null, new String[]{"latex", path, "--force"});
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
    }


    @FXML
    public void about() {

    }

    @FXML
    public void save() {

    }

    @FXML
    public void loadGrammar() {
        File file = gui.loadFile("Grammar");
        if(file!=null) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                String string = "";
                String line;
                while ((line = reader.readLine()) != null) {
                    string = string + line + "\n";
                }
                Grammar grammar = GrammarUtil.parse(string);
                gui.getCli().objects.put(Grammar.class, grammar);
                gui.switchDisplayGui(Grammar.class);
                gui.refresh(grammar);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (LexerException e) {
                e.printStackTrace();
            } catch (ParserException e) {
                e.printStackTrace();
            }
        }

    }
    @FXML
    public void loadAutomaton() {
        File file = gui.loadFile("Automaton");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String string = "";
            String line;
            while ((line = reader.readLine()) != null) {
                string = string + line + "\n";
            }
            Automaton automaton = AutomatonUtil.parse(string);
            gui.getCli().objects.put(Automaton.class, automaton);
            gui.switchDisplayGui(Automaton.class);
            gui.refresh(automaton);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AutomatonParser.lexer.LexerException e) {
            e.printStackTrace();
        } catch (AutomatonParser.parser.ParserException e) {
            e.printStackTrace();
        }

    }
    @FXML
    public void loadPushDownAutomaton() {

    }

    @FXML
    public void saveCurrentGrammar() {
        Object object = gui.getCli().objects.get(Grammar.class);
        if(object != null) {
            Grammar grammar = (Grammar) object;
            File file = gui.openFile("Grammar");
            GrammarUtil.save(grammar,file);
        }
    }

    @FXML
    public void saveCurrentAutomaton() {
        Object object = gui.getCli().objects.get(Automaton.class);
        if(object != null) {
            Automaton automaton = (Automaton) object;
            File file = gui.openFile("Automaton");
            AutomatonUtil.save(automaton,file);
        }
    }

    @FXML
    public void saveCurrentPushDownAutomaton() {
        
    }

    @FXML
    public void toGraphViz() {
        Object object = gui.getCli().objects.get(Automaton.class);
        if(object != null) {
            Automaton automaton = (Automaton) object;
            File file = gui.openFile("Automaton");
            AutomatonUtil.toGraphViz(automaton,file);
        }
    }




    public void setGui(GUI gui) {
        this.gui = gui;
    }
}

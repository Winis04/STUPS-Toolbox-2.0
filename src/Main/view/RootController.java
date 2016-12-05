package Main.view;


import AutomatonSimulator.Automaton;
import AutomatonSimulator.AutomatonUtil;
import CLIPlugins.PrintModePlugin;
import Console.Storable;
import GrammarParser.lexer.LexerException;
import GrammarParser.parser.ParserException;
import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Main.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.*;

/**
 * Created by Isabel on 28.11.2016.
 */
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
        borderPane.setStyle("-fx-border-color: blue; -fx-border-width: 4;");
        PrintModePlugin printModePlugin = new PrintModePlugin();
        printModePlugin.execute(null,new String[]{"latex","test/gui_test.tex","--force"});
    }
    @FXML
    private void latexModeOff() {
        borderPane.setStyle("-fx-border-witdth: 0;");
        PrintModePlugin printModePlugin = new PrintModePlugin();
        printModePlugin.execute(null,new String[]{"no"});
    }

    @FXML
    public void startLatex() {

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

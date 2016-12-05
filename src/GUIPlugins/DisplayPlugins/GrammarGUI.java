package GUIPlugins.DisplayPlugins;

import CLIPlugins.CLIPlugin;
import GUIPlugins.DisplayPlugins.GrammarTabs.EditTab;
import GUIPlugins.DisplayPlugins.GrammarTabs.FirstFollowTab;
import GUIPlugins.DisplayPlugins.GrammarTabs.GrammarTab;
import GUIPlugins.DisplayPlugins.GrammarTabs.LLParsingTableTab;
import GrammarParser.lexer.LexerException;
import GrammarParser.parser.ParserException;
import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import GrammarSimulator.Nonterminal;
import GrammarSimulator.Symbol;
import Main.GUI;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * Created by fabian on 13.08.16.
 */
public class GrammarGUI implements DisplayPlugin {

    /**
     * The grammar, that is being displayed.
     */
    private Grammar grammar;

    /**
     * This GUI's root pane.
     */
    private TabPane rootPane;

    /**
     * Contains all {@link GrammarTab}s.
     */
    private HashSet<GrammarTab> tabs;

    /**
     * Maps each GUI-Tab to its {@link GrammarTab}.
     */
    private HashMap<Tab, GrammarTab> tabMap = new HashMap<>();

    private GUI gui;

    @Override
    public Node display(Object object) {
        grammar = (Grammar) object;

        rootPane = new TabPane();

        tabs = new HashSet<>();

        rootPane.getTabs().clear();
        Tab edit =new Tab("edit");
        edit.setContent(new EditTab(gui).getFxNode(grammar));
        edit.setClosable(false);
        rootPane.getTabs().add(edit);

        return rootPane;
    }

    public void firstfollow(Grammar grammar) {
        String name="First Follow";
        if(rootPane.getTabs().stream().anyMatch(tab -> tab.getText().equals(name))) {
            rootPane.getTabs().stream().forEach(tab -> {
                if(tab.getText().equals(name)) {
                    rootPane.getSelectionModel().select(tab);
                }
            });
        } else {
            Tab ff = new Tab(name);
            ff.setContent(new FirstFollowTab().getFxNode(grammar));
            ff.setClosable(true);
            rootPane.getTabs().add(ff);
        }
    }
    public void llParsingTableTab(Grammar grammar) {
        String name="LL-parsing Table";
        if(rootPane.getTabs().stream().anyMatch(tab -> tab.getText().equals(name))) {
            rootPane.getTabs().stream().forEach(tab -> {
                if(tab.getText().equals(name)) {
                    rootPane.getSelectionModel().select(tab);
                }
            });
        } else {
            Tab ll = new Tab(name);
            ll.setContent(new LLParsingTableTab().getFxNode(grammar));
            ll.setClosable(true);
            rootPane.getTabs().add(ll);
        }
    }

    @Override
    public Node refresh(Object object) {
        grammar = (Grammar) object;
        return this.display(grammar);
    }

    @Override
    public Object newObject() {
        return new Grammar();
    }

    @Override
    public Object openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file");
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if(selectedFile == null) {
            return null;
        }
        String file = "";
        try {
            BufferedReader automatonReader = new BufferedReader(new FileReader(selectedFile.getPath()));
            String line;
            while ((line = automatonReader.readLine()) != null) {
                file = file + line + "\n";
            }
            return GrammarUtil.parse(file);
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("STUPS-Toolbox");
            alert.setHeaderText("File not found!");
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("STUPS-Toolbox");
            alert.setHeaderText("Unable to write to file!");
            alert.showAndWait();
        } catch (LexerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("STUPS-Toolbox");
            alert.setHeaderText("Error while parsing the file!");
            alert.showAndWait();
        } catch (ParserException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("STUPS-Toolbox");
            alert.setHeaderText("Error while parsing the file!");
            alert.showAndWait();
        }

        return  null;
    }

    @Override
    public void saveFile(Object object) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        File selectedFile = fileChooser.showSaveDialog(new Stage());
        if(selectedFile != null) {
            GrammarUtil.save((Grammar) object, selectedFile.getAbsolutePath());
        }
    }

    @Override
    public HashSet<Menu> menus(Object object, Node node) {
        return new HashSet<>();
    }

    @Override
    public String getName() {
        return "Grammar";
    }

    @Override
    public Class displayType() {
        return Grammar.class;
    }

    @Override
    public void setGUI(GUI gui) {
        this.gui=gui;
    }
}

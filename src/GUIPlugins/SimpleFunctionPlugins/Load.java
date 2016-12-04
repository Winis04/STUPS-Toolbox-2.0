package GUIPlugins.SimpleFunctionPlugins;

import AutomatonSimulator.Automaton;
import AutomatonSimulator.AutomatonUtil;
import GrammarParser.lexer.LexerException;
import GrammarParser.parser.ParserException;
import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Console.Storable;
import javafx.stage.FileChooser;

import java.io.*;

/**
 * Created by Isabel on 04.12.2016.
 */
public class Load extends SimpleFunctionPlugin {
    @Override
    public Object execute(Object object) {
        if(gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem().getValue().equals("Grammar")) {

            try {
                String string = readFile("Grammar");
                Grammar grammar = GrammarUtil.parse(string);
                load(Grammar.class,grammar);

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
        if(gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem().getValue().equals("Automaton")) {

            try {
                String string = readFile("Automaon");
                Automaton automaton = AutomatonUtil.parse(string);
                load(Automaton.class,automaton);

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
        return null;
    }

    private String readFile(String titel) throws IOException {
        File file = gui.loadFile(titel);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String string = "";
        String line;
        while ((line = reader.readLine()) != null) {
            string = string + line + "\n";
        }
        return string;
    }

    private void load(Class typ, Storable storable) {
        gui.getCli().objects.put(typ, storable);
        gui.switchDisplayGui(typ);
        gui.refresh(storable);
    }

    @Override
    String getName() {
        return "load ...";
    }

    @Override
    public Class inputType() {
        return null;
    }

    @Override
    Class outputType() {
        return null;
    }

    @Override
    public boolean operatesOnSuperClass() {
        return true;
    }
}

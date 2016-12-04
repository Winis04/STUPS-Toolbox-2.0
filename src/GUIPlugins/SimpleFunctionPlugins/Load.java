package GUIPlugins.SimpleFunctionPlugins;

import GrammarParser.lexer.LexerException;
import GrammarParser.parser.ParserException;
import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import javafx.stage.FileChooser;

import java.io.*;

/**
 * Created by Isabel on 04.12.2016.
 */
public class Load extends SimpleFunctionPlugin {
    @Override
    public Object execute(Object object) {
        if(gui.getOverviewController().getTreeView().getSelectionModel().getSelectedItem().getValue().equals("Grammar")) {
            File file = gui.loadFile("Grammar");
            try {
                BufferedReader grammarReader = new BufferedReader(new FileReader(file));
                String string = "";
                String line;
                while ((line = grammarReader.readLine()) != null) {
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

        return null;
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

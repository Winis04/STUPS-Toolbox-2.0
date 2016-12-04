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
        String name = gui.getOverviewController().getClassOfCurrentSelected().getSimpleName();
        File file = gui.loadFile(name);
        if(name.equals("Grammar")) {
            try {
                BufferedReader grammarReader = new BufferedReader(new FileReader(file));
                String string = "";
                String line;
                while ((line = grammarReader.readLine()) != null) {
                    string = string + line + "\n";
                }
                Grammar grammar = GrammarUtil.parse(string);
                return grammar;
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
        return Grammar.class;
    }

    @Override
    public boolean operatesOnSuperClass() {
        return true;
    }
}

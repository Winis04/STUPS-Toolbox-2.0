package CLIPlugins;

import GrammarParser.lexer.LexerException;
import GrammarParser.parser.ParserException;
import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Main.Storable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Loads a {@link Grammar} from a text file.
 * @author fabian
 * @since 11.08.16
 */


public class GrammarLoadPlugin extends CLIPlugin {

    private boolean errorFlag = false;

    @Override
    public String[] getNames() {
        return new String[]{"lg", "load-grammar"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        if(parameters.length < 1) {
            System.out.println("Please enter a filename as parameter for this command!");
            return false;
        }
        return true;
    }

    @Override
    public String getHelpText() {
        return "Loads a grammar from a text file. Takes a path to a file as parameter.";
    }

    @Override
    public Storable execute(Object object, String[] parameters) {
        errorFlag = false;
        Grammar grammar = null;
        try {
            String filename = parameters[0];
            System.out.println(filename);
            BufferedReader grammarReader = new BufferedReader(new FileReader(filename));
            String file = "";
            String line;
            while ((line = grammarReader.readLine()) != null) {
                file = file + line + "\n";
            }
            grammar = GrammarUtil.parse(file,filename);

        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            errorFlag = true;
        } catch (IOException e) {
            System.out.println("Unable to read file!");
            errorFlag = true;
        } catch (ParserException | LexerException e) {
            System.out.println("Error while parsing the file!");
            e.printStackTrace();
            errorFlag = true;
        }
        return grammar;
    }

    @Override
    public Class inputType() {
        return null;
    }

    @Override
    public Class outputType() {
        return Grammar.class;
    }

    @Override
    public boolean errorFlag() {
        return errorFlag;
    }
}

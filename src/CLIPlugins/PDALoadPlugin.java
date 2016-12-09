package CLIPlugins;

import GrammarParser.lexer.LexerException;
import GrammarParser.parser.ParserException;
import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import PushDownAutomatonSimulator.PushDownAutomaton;
import PushDownAutomatonSimulator.PushDownAutomatonUtil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by fabian on 11.08.16.
 */
public class PDALoadPlugin implements CLIPlugin {

    private boolean errorFlag = false;

    @Override
    public String[] getNames() {
        return new String[]{"lpda", "load-pda"};
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
        return "Loads a pda from a text file. Takes a file as parameter.";
    }

    @Override
    public Object execute(Object object, String[] parameters) {
        errorFlag = false;
        PushDownAutomaton pda = null;
        try {
            String filename = parameters[0];
            BufferedReader grammarReader = new BufferedReader(new FileReader(filename));
            String file = "";
            String line;
            while ((line = grammarReader.readLine()) != null) {
                file = file + line + "\n";
            }
            pda = PushDownAutomatonUtil.parse(file);
            System.out.println("bla");


        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            errorFlag = true;
        } catch (IOException e) {
            System.out.println("Unable to read file!");
            errorFlag = true;
        } catch (PushDownAutomatonParser.lexer.LexerException e) {
            e.printStackTrace();
        } catch (PushDownAutomatonParser.parser.ParserException e) {
            e.printStackTrace();
        }
        return pda;
    }

    @Override
    public Class inputType() {
        return null;
    }

    @Override
    public Class outputType() {
        return PushDownAutomaton.class;
    }

    @Override
    public boolean errorFlag() {
        return errorFlag;
    }
}

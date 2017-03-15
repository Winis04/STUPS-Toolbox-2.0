package CLIPlugins;

import Main.Storable;
import PushDownAutomatonSimulator.PushDownAutomaton;
import PushDownAutomatonSimulator.PushDownAutomatonUtil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Loads a {@link PushDownAutomaton} from a {@link java.io.File}.
 * @author fabian
 * @since 11.08.16
 */


public class PDALoadPlugin extends CLIPlugin {

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
    public Storable execute(Storable object, String[] parameters) {
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
            pda = PushDownAutomatonUtil.parse(file,filename);


        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            errorFlag = true;
        } catch (IOException e) {
            System.out.println("Unable to read file!");
            errorFlag = true;
        } catch (PushDownAutomatonParser.lexer.LexerException | PushDownAutomatonParser.parser.ParserException e) {
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

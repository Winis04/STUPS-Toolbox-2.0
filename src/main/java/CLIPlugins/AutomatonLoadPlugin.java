package CLIPlugins;

import AutomatonParser.lexer.LexerException;
import AutomatonParser.parser.ParserException;
import AutomatonSimulator.Automaton;
import AutomatonSimulator.AutomatonUtil;

import java.io.*;

/**
 * @author fabian
 * @since 15.06.16
 */
public class AutomatonLoadPlugin implements CLIPlugin{

    private boolean errorFlag = false;

    @Override
    public String[] getNames() {
        return new String[]{"la", "load-automaton"};
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
        return "Loads an automaton from a text file. Takes a file as parameter.";
    }

    @Override
    public Object execute(Object object, String[] parameters) {
        errorFlag = false;
        Automaton automaton = null;
        try {
            String filename = parameters[0];
            BufferedReader automatonReader = new BufferedReader(new FileReader(filename));
            String file = "";
            String line;
            while ((line = automatonReader.readLine()) != null) {
                file = file + line + "\n";
            }
            automaton = AutomatonUtil.parse(file,filename);
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
        return automaton;
    }

    @Override
    public Class inputType() {
        return null;
    }

    @Override
    public Class outputType() {
        return Automaton.class;
    }

    @Override
    public boolean errorFlag() {
        return errorFlag;
    }
}

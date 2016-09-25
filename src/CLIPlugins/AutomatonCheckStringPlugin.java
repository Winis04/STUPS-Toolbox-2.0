package CLIPlugins;

import AutomatonSimulator.Automaton;
import AutomatonSimulator.AutomatonUtil;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by fabian on 15.06.16.
 */
public class AutomatonCheckStringPlugin implements CLIPlugin{

    private boolean errorFlag = false;

    @Override
    public String[] getNames() {
        return new String[]{"csa", "check-string-automaton"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        if(parameters.length < 1) {
            System.out.println("Please enter a string as parameter for this command!");
            return false;
        }
        return true;
    }

    @Override
    public String getHelpText() {
        return "Checks if a given string is accepted by the loaded automaton. Takes a string surrounded by quotation marks as parameter.";
    }

    @Override
    public Object execute(Object object, String[] parameters) {
        errorFlag = false;
        if(object == null) {
            System.out.println("Please use 'a', or 'automaton' to load an automaton before using this command!");
            errorFlag = true;
            return null;
        }

        Automaton automaton = (Automaton) object;
        ArrayList<String> automatonInput = new ArrayList<>();
        for(String parameter : parameters) {
            parameter = parameter.replaceAll("\\s+","");
            parameter = parameter.replaceAll("\"", "");
            if(parameter.length() > 0) {
                automatonInput.add(parameter);
            }
        }

        boolean accepted = AutomatonUtil.checkInput(automaton, automatonInput);
        Stack<String> takenWay = AutomatonUtil.getTakenWay();

        String word = "";
        int i = 0;
        for(String string : takenWay) {
            if(!string.contains("epsilon") && !string.contains("lambda")) {
                word += automatonInput.get(i) + " ";
                i++;
            }
            if(word.endsWith(" ")) {
                System.out.println(string + "     \"" + word.substring(0, word.length() - 1) + "\"");
            } else {
                System.out.println(string + "     \"" + word + "\"");
            }
        }

        if (accepted) {
            System.out.println("\nThe automaton accepts the given input!");
        } else {
            System.out.println("\nThe automaton doesn't accept the given input!");
        }
        return null;
    }

    @Override
    public Class inputType() {
        return Automaton.class;
    }

    @Override
    public Class outputType() {
        return null;
    }

    @Override
    public boolean errorFlag() {
        return errorFlag;
    }
}

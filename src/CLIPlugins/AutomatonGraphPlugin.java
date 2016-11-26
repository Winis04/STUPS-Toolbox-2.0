package CLIPlugins;

import AutomatonSimulator.Automaton;
import AutomatonSimulator.AutomatonUtil;

/**
 * Created by fabian on 15.06.16.
 */
public class AutomatonGraphPlugin implements CLIPlugin {

    private boolean errorFlag = false;

    @Override
    public String[] getNames() {
        return new String[]{"ga", "graph-automaton"};
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
        return "Writes the loaded automaton into a .dot file. Takes a file as parameter.";
    }

    @Override
    public Object execute(Object object, String[] parameters) {
        errorFlag = false;
        if(object == null) {
            System.out.println("Please use 'la', or 'load-automaton' to load an automaton before using this command!");
            errorFlag = true;
            return null;
        }
        Automaton automaton = (Automaton) object;
        AutomatonUtil.toGraphViz(automaton, parameters[0]);
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

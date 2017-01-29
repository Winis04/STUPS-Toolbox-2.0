package CLIPlugins;

import AutomatonSimulator.Automaton;
import AutomatonSimulator.AutomatonUtil;

/**
 * Turns an {@link Automaton} into one without epsilon transitions.
 * @author fabian
 * @since 15.06.16
 */
@SuppressWarnings("ALL")
public class AutomatonRemoveEpsilonStatesPlugin implements CLIPlugin {

    private boolean errorFlag = false;

    @Override
    public String[] getNames() {
        return new String[]{"ret", "remove-epsilon-transitions"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return true;
    }

    @Override
    public String getHelpText() {
        return "Turns the loaded automaton into one without epsilon transitions. Doesn't take any parameters.";
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
        return AutomatonUtil.removeEpsilonTransitions(automaton);
    }

    @Override
    public Class inputType() {
        return Automaton.class;
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

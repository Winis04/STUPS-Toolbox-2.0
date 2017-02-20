package CLIPlugins;

import AutomatonSimulator.Automaton;
import AutomatonSimulator.AutomatonUtil;
import Main.Storable;

/**
 * Renames the {@link AutomatonSimulator.State}s of an {@link Automaton}.
 * @author fabian
 * @since 24.07.16
 */


public class AutomatonRenameStatesPlugin extends CLIPlugin {

    private boolean errorFlag = false;

    @Override
    public String[] getNames() {
        return new String[]{"rs", "rename-states"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return true;
    }

    @Override
    public String getHelpText() {
        return "Renames the states of the loaded automaton. Doesn't take any parameters.";
    }

    @Override
    public Storable execute(Object object, String[] parameters) {
        errorFlag = false;
        if(object == null) {
            System.out.println("Please use 'la', or 'load-automaton' to load an automaton before using this command!");
            errorFlag = true;
            return null;
        }
        Automaton automaton = (Automaton) object;
        return AutomatonUtil.renameStates(automaton);
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
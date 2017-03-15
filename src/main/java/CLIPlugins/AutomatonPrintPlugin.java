package CLIPlugins;

import AutomatonSimulator.Automaton;
import Main.Storable;
import Print.Printer;

/**
 * Prints an {@link Automaton} to the console.
 * @author fabian
 * @since 20.06.16
 */


public class AutomatonPrintPlugin extends CLIPlugin {

    private boolean errorFlag = false;

    @Override
    public String[] getNames() {
        return new String[]{"pa", "print-automaton"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return true;
    }

    @Override
    public String getHelpText() {
        return "Prints the automaton on the console. Doesn't take any parameters.";
    }

    @Override
    public Storable execute(Storable object, String[] parameters) {
        errorFlag = false;
        if(object == null) {
            System.out.println("Please use 'la', or 'load-automaton' to load an automaton before using this command!");
            errorFlag = true;
            return null;
        }
        Automaton automaton = (Automaton) object;
        Printer.print(automaton);
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


    @Override
    public boolean createsOutput() {
        return true;
    }
}

package CLIPlugins;

import AutomatonSimulator.Automaton;
import Print.Printer;

/**
 * @author fabian
 * @since 20.06.16
 */
@SuppressWarnings("ALL")
public class AutomatonPrintPlugin implements CLIPlugin {

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
    public Object execute(Object object, String[] parameters) {
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
}

package CLIPlugins;

import Print.Printer;
import PushDownAutomatonSimulator.PushDownAutomaton;

/**
 * Prints the current {@link GrammarSimulator.Grammar} to the console.
 * @author Isabel
 * @since 29.10.2016
 */
@SuppressWarnings("ALL")
public class PDAPrintPlugin extends CLIPlugin {
    private boolean errorFlag;
    @Override
    public String[] getNames() {
        return new String[]{"print-push-down-automaton","ppda"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return parameters.length==0;
    }

    @Override
    public String getHelpText() {
        return "prints the current PDA";
    }

    @Override
    public Object execute(Object object, String[] parameters) {
        errorFlag = false;
        if(object == null) {
            System.out.println("Please load a grammar before using this command!");
            errorFlag = true;
            return null;
        }
        PushDownAutomaton pushDownAutomaton = (PushDownAutomaton) object;
        Printer.print(pushDownAutomaton);
        return null;
    }

    @Override
    public Class inputType() {
        return PushDownAutomaton.class;
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

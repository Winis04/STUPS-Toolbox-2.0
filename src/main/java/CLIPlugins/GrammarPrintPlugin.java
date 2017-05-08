package CLIPlugins;

import GrammarSimulator.Grammar;
import Main.Storable;
import Print.Printer;

/**
 * Prints the current {@link Grammar}.
 * @author fabian
 * @since 11.08.16
 */


public class GrammarPrintPlugin extends CLIPlugin {

    private boolean errorFlag = false;

    @Override
    public String[] getNames() {
        return new String[]{"pg", "print-grammar"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return true;
    }

    @Override
    public String getHelpText() {
        return "Prints the loaded grammar. Doesn't take any parameters.";
    }

    @Override
    public Storable execute(Storable object, String[] parameters) {
        errorFlag = false;
        if(object == null) {
            System.out.println("Please load a grammar before using this command!");
            errorFlag = true;
            return null;
        }
        Grammar grammar = (Grammar) object;
        Printer.printWithTitle(grammar.getName(),grammar);
        return null;
    }

    @Override
    public Class inputType() {
        return Grammar.class;
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

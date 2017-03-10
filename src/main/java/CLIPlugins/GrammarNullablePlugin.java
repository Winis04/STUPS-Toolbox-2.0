package CLIPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import GrammarSimulator.Nonterminal;
import Main.Storable;
import Print.Printable;
import Print.PrintableSet;
import Print.Printer;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Prints the nullable-set of the current {@link Grammar}.
 * @author fabian
 * @since 22.08.16
 */


public class GrammarNullablePlugin extends CLIPlugin {

    private boolean errorFlag = false;

    @Override
    public String[] getNames() {
        return new String[]{"nullable"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return true;
    }

    @Override
    public String getHelpText() {
        return "Prints the Nullable-set of the loaded grammar. Doesn't take any parameters.";
    }

    @Override
    public Storable execute(Object object, String[] parameters) {
        errorFlag = false;
        if(object == null) {
            System.out.println("Please load a grammar before using this command!");
            errorFlag = true;
            return null;
        }
        Grammar grammar = (Grammar) object;
        PrintableSet result = GrammarUtil.calculateNullableAsPrintable(grammar);
        Printer.print(result);

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


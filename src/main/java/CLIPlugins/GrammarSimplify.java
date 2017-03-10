package CLIPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import GrammarSimulator.Rule;
import Main.Storable;
import Print.Printer;
import Print.StringLiterals;

/**
 * Eliminates unit {@link Rule}s of the current {@link Grammar}.
 * @author Isabel
 * @since 22.10.2016
 */


public class GrammarSimplify extends CLIPlugin {
    private boolean errorFlag = false;


    @Override
    public String[] getNames() {
        return new String[]{"sim","simplify"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return parameters.length==0;
    }

    @Override
    public String getHelpText() {
        return "Simplifies a grammar.";
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
        Printer.printEnumeration(GrammarUtil.simplifyAsPrintable(grammar), StringLiterals.SIMPLIFY_POINT_DESCRIPTIONS,StringLiterals.SIMPLIFY_TEXTS,StringLiterals.SIMPLIFY_TITLE);
        return GrammarUtil.simplify(grammar);
    }

    @Override
    public Class inputType() {
        return Grammar.class;
    }

    @Override
    public Class outputType() {
        return Grammar.class;
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

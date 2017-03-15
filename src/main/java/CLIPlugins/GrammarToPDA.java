package CLIPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Main.Storable;
import Print.Printer;
import Print.StringLiterals;
import PushDownAutomatonSimulator.PushDownAutomaton;

/**
 * Transforms a {@link Grammar} into a {@link PushDownAutomaton}.
 * @author Isabel
 * @since 29.10.2016
 */


public class GrammarToPDA extends CLIPlugin {
    private boolean errorFlag;
    @Override
    public String[] getNames() {
        return new String[]{"toPDA"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return parameters.length==0;
    }

    @Override
    public String getHelpText() {
        return "Transforms the current grammar into a pda.";
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
        Printer.printEnumeration(GrammarUtil.toPDAAsPrintables(grammar), StringLiterals.TOPDA_POINT_DESCRIPTIONS,StringLiterals.TOPDA_TEXTS,StringLiterals.TOPDA_TITLE);
        return GrammarUtil.toPDA(grammar);
    }

    @Override
    public Class inputType() {
        return Grammar.class;
    }

    @Override
    public Class outputType() {
        return PushDownAutomaton.class;
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

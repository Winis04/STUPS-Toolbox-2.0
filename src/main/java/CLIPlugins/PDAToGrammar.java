package CLIPlugins;

import GrammarSimulator.Grammar;

import Main.Storable;
import PushDownAutomatonSimulator.PushDownAutomaton;
import PushDownAutomatonSimulator.PushDownAutomatonUtil;
import Print.Printer;
import Print.StringLiterals;

/**
 * Transforms a {@link Grammar} into a {@link PushDownAutomaton}.
 * @author Isabel
 * @since 29.10.2016
 */


public class PDAToGrammar extends CLIPlugin {
    private boolean errorFlag;
    @Override
    public String[] getNames() {
        return new String[]{"toGrammar"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return parameters.length==0;
    }

    @Override
    public String getHelpText() {
        return "Transforms the current pda into a grammar.";
    }

    @Override
    public Storable execute(Storable object, String[] parameters) {
        errorFlag = false;
        if(object == null) {
            System.out.println("Please load a pda before using this command!");
            errorFlag = true;
            return null;
        }
        PushDownAutomaton pda = (PushDownAutomaton) object;
        Printer.printEnumeration(PushDownAutomatonUtil.toGrammarAsPrintables(pda), StringLiterals.TOGRAMMAR_POINT_DESCRIPTIONS, StringLiterals.TOGRAMMAR_TEXTS, StringLiterals.TOGRAMMAR_TITLE);

        return PushDownAutomatonUtil.toGrammar(pda);
    }

    @Override
    public Class outputType() {
        return Grammar.class;
    }

    @Override
    public Class inputType() {
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

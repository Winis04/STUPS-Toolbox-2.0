package CLIPlugins;

import GrammarSimulator.*;
import Main.Storable;
import Print.Printer;
import Print.StringLiterals;

/**
 * Eliminates unit {@link Rule}s of the current {@link Grammar}.
 * @author Isabel
 * @since 22.10.2016
 */


public class GrammarEliminateUnitRules extends CLIPlugin {
    private boolean errorFlag = false;


    @Override
    public String[] getNames() {
        return new String[]{"eur","eliminate-unit-rules"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return parameters.length==0;
    }

    @Override
    public String getHelpText() {
        return "Eliminates unit Rules (A -> B) from the loaded grammar.";
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
        if(!GrammarUtil.hasUnitRules(grammar)) {
            System.out.println("This grammar is already without unit rules");
            return null;
        }

        Printer.printEnumeration(GrammarUtil.eliminateUnitRulesAsPrintables(grammar), StringLiterals.EUR_POINT_DESCRIPTIONS, StringLiterals.EUR_TEXTS,StringLiterals.EUR_TITLE);

        return GrammarUtil.eliminateUnitRules(grammar);
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

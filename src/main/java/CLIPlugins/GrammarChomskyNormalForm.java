package CLIPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Main.Storable;
import Print.Printer;
import Print.StringLiterals;

/**
 * Calculates the chomsky normal form of the current {@link Grammar}.
 * @author isabel
 * @since 25.10.16
 */


public class GrammarChomskyNormalForm extends CLIPlugin {
    private boolean errorFlag;

    @Override
    public String[] getNames() {
        return new String[]{"cnf","chomsky-formal-form"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return parameters.length==0;
    }

    @Override
    public String getHelpText() {
        return "calculates the chomsky normal form.";
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
        if(!GrammarUtil.isLambdaFree(grammar)) {
           System.out.println("Please remove lambda rules first by calling rlr");
            errorFlag = true;
            return null;
        }
        if(GrammarUtil.hasUnitRules(grammar)) {
            System.out.println("Please eliminate unit rules first by calling eur");
            errorFlag = true;
            return null;
        }
        if(GrammarUtil.isInChomskyNormalForm(grammar)) {
            System.out.println("This grammar is already in chomsky normal form");
            return null;
        }



        Printer.printEnumeration(GrammarUtil.chomskyNormalFormAsPrintables(grammar), StringLiterals.CNF_POINT_DESCRIPTIONS,StringLiterals.CNF_TEXTS,StringLiterals.CNF_TITLE);
        return GrammarUtil.chomskyNormalForm(grammar);

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

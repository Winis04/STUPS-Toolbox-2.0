package CLIPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Print.Printer;

/**
 * Calculates the chomsky normal form of the current {@link Grammar}.
 * @author isabel
 * @since 25.10.16
 */
@SuppressWarnings("ALL")
public class GrammarChomskyNormalForm implements CLIPlugin {
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
        return "calculates the chomsky normal form";
    }

    @Override
    public Object execute(Object object, String[] parameters) {
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

        String[] texts=new String[]{"","rules, that point only on one Nonterminal are already in chomsky normal form and we keep them",
                "in all other rules replace every appearance of Terminal a through a new Nonterminal",
                "in all rules that contain more than two nonterminals, add a new nonterminal that points to the end of the rule."};

        String[] point_descriptions=new String[]{"Before","Step 1", "Step 2", "Step 3"};

        Printer.printEnumeration(GrammarUtil.chomskyNormalFormAsPrintables(grammar),point_descriptions,texts,"Chomsky - Normal - Form");
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
}

package CLIPlugins;

import GrammarSimulator.Explanation;
import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Print.Printer;

/**
 * Created by isabel on 25.10.16.
 */
public class GrammarChomskyNormalForm implements CLIPlugin {
    private boolean errorFlag;
    private Explanation type;
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
        return "calculates the chomsky normal form. Takes the strings 'no', 'short' or 'long' as a parameter, which determine the level of explanation.";
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

        String[] texts=new String[]{"","rules, that point only on one Nonterminal are already in chomsky normal form and we keep them.\\\\ \n",
                "in all other rules replace every appearance of Terminal a through a new Nonterminal.\\\\ \n",
                "in all rules that contain more than two nonterminals, add a new nonterminal that points to the end of the rule.\\\\ \n"};
        String[] point_descriptions=new String[]{"Before","Step 1", "Step 2", "Step 3"};
<<<<<<< 4cb370f0971701e19e59e4b125439e9095d4a09d
        Printer.print(GrammarUtil.chomskyNormalForm(grammar),point_descriptions,texts,"Chomsky - Normal - Form");

=======
        Printer.printEnumeration(GrammarUtil.chomskyNormalForm(grammar),point_descriptions,texts,"Chomsky - Normal - Form");
>>>>>>> first step making writer private
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
}

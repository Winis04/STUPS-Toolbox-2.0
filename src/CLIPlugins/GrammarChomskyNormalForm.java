package CLIPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;

/**
 * Created by isabel on 25.10.16.
 */
public class GrammarChomskyNormalForm implements CLIPlugin {
    private boolean errorFlag;
    @Override
    public String[] getNames() {
        return new String[]{"cnf","chomsky-formal-form"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return true;
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
        GrammarUtil.replaceLambda(grammar); //TODO: do in the load grammar method
        //step 1: take rules in form A --> a in new rule set
        //step 2: replace every instance of terminal a through new Nonterminal X_a and add rule X_a -->
        //step 3: remove more than two Nonterminals

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

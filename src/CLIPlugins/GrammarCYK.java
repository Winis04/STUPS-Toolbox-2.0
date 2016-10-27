package CLIPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import GrammarSimulator.Matrix;

/**
 * Created by Isabel on 26.10.2016.
 */
public class GrammarCYK implements CLIPlugin {
    private boolean errorFlag;
    @Override
    public String[] getNames() {
        return new String[]{"cyk"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return parameters.length==1;
    }

    @Override
    public String getHelpText() {
        return "does the cyk-algorithm. Takes a word of the language as a parameter";
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
        if(!GrammarUtil.isInChomskyNormalForm(grammar)) {
            GrammarUtil.chomskyNormalFormWithNoOutput(grammar);
        }
        if(GrammarUtil.cykWithLongOutput(grammar,parameters[0])) {
            System.out.println("L(G) contains " + parameters[0] + ".");
        } else {
            System.out.println("L(G) does not contain " + parameters[0] + ".");
        }
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

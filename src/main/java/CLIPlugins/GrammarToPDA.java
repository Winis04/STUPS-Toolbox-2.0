package CLIPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import PushDownAutomatonSimulator.PushDownAutomaton;

/**
 * translates a grammar to a pda
 * @author Isabel
 * @since 29.10.2016
 */
public class GrammarToPDA implements CLIPlugin {
    boolean errorFlag;
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
        return "transforms the current grammar into a pda";
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
}

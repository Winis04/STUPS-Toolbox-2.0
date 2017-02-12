package GUIPlugins.SimpleFunctionPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Main.Storable;

public class RemoveUnreachable extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Object object) {
        if(object != null) {
            Grammar grammar = (Grammar) object;
            return GrammarUtil.removeUnreachableNonterminals(grammar);
        }
        return null;
    }

    @Override
   public String getName() {
        return "remove unreachable";
    }

    @Override
    public Class inputType() {
        return Grammar.class;
    }

    @Override
    Class outputType() {
        return Grammar.class;
    }

    @Override
    public boolean createsOutput() {
        return true;
    }
}

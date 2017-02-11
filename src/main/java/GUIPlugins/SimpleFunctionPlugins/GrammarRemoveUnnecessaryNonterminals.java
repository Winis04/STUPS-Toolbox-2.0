package GUIPlugins.SimpleFunctionPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Main.Storable;

public class GrammarRemoveUnnecessaryNonterminals extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Object object) {
        if(object != null) {
            Grammar grammar = (Grammar) object;
            return GrammarUtil.removeDeadEnds(grammar);
        }
        return null;
    }

    @Override
   public String getName() {
        return "remove dead ends";
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

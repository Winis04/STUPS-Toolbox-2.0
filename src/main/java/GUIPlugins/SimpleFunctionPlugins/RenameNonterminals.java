package GUIPlugins.SimpleFunctionPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Main.Storable;


@SuppressWarnings("unused")
public class RenameNonterminals extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Object object) {
        if(object != null) {
            Grammar grammar = (Grammar) object;
            return GrammarUtil.renameNonterminals(grammar);
        }
        return null;
    }

    @Override
   public String getName() {
        return "Rename Nonterminals";
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

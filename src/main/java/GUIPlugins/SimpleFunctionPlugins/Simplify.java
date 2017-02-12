package GUIPlugins.SimpleFunctionPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Main.Storable;
import Print.Printer;


public class Simplify extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Object object) {
        if(object == null) {
            gui.errorDialog("Please load a grammar before using this command!");
            return null;
        }
        Grammar grammar = (Grammar) object;
        Grammar res = GrammarUtil.simplify(GrammarUtil.removeDeadEnds(GrammarUtil.removeUnreachableNonterminals(grammar)));
        res= GrammarUtil.simplify(GrammarUtil.removeDeadEnds(GrammarUtil.removeUnreachableNonterminals(res)));
        return new Grammar(res.getStartSymbol(),res.getRules(),res.getName(),grammar);
    }

    @Override
    public String getName() {
        return "Simplify";
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

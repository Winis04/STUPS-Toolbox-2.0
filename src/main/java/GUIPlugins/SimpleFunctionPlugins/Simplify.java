package GUIPlugins.SimpleFunctionPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Main.Storable;
import Print.Printer;
import Print.StringLiterals;


public class Simplify extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Object object) {
        if(object == null) {
            gui.errorDialog("Please load a grammar before using this command!");
            return null;
        }
        Grammar grammar = (Grammar) object;
        Printer.printEnumeration(GrammarUtil.simplifyAsPrintable(grammar), StringLiterals.SIMPLIFY_POINT_DESCRIPTIONS,StringLiterals.SIMPLIFY_TEXTS,StringLiterals.SIMPLIFY_TITLE);
        return GrammarUtil.simplify(grammar);
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

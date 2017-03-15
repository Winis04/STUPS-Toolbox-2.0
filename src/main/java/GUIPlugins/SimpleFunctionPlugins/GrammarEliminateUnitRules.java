package GUIPlugins.SimpleFunctionPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Main.Storable;
import Print.Printer;
import Print.StringLiterals;



public class GrammarEliminateUnitRules extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Storable object) {
        if(object == null) {
            gui.errorDialog("Please load a grammar before using this command!");
            return null;
        }
        Grammar grammar = (Grammar) object;
        if(!GrammarUtil.hasUnitRules(grammar)) {
            gui.infoDialog("this grammar has no unit rules!");
        } else {

            Printer.printEnumeration(GrammarUtil.eliminateUnitRulesAsPrintables(grammar), StringLiterals.EUR_POINT_DESCRIPTIONS,StringLiterals.EUR_TEXTS,StringLiterals.EUR_TITLE);
            return GrammarUtil.eliminateUnitRules(grammar);
        }
        return grammar;
    }

    @Override
    public String getName() {
        return "Eliminate Unit Rules";
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

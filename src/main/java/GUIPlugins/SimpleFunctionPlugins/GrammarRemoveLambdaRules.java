package GUIPlugins.SimpleFunctionPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Main.GUI;
import Main.Storable;
import Print.Printable;
import Print.Printer;
import Print.StringLiterals;

import java.util.ArrayList;

/**
 * removes lambda rules
 * @author Isabel
 * @since 03.12.2016
 */


public class GrammarRemoveLambdaRules extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Storable object) {
        if(object == null) {
            gui.errorDialog("Please load a grammar before using this command!");
            return null;
        }
        Grammar grammar = (Grammar) object;
        if(GrammarUtil.isLambdaFree(grammar)) {
            gui.infoDialog("this grammar is already lambda-free");
            return null;
        }
        ArrayList<Printable> printables=GrammarUtil.removeLambdaRulesAsPrintables(grammar);

        if(printables.size()==4) {
            Printer.printEnumeration(printables, StringLiterals.RLR_POINT_DESCRIPTIONS_SHORT,StringLiterals.RLR_TEXTS_SHORT,StringLiterals.RLR_TITLE);
        } else if(printables.size()==5) {
            Printer.printEnumeration(printables, StringLiterals.RLR_POINT_DESCRIPTIONS_LONG,StringLiterals.RLR_TEXTS_LONG,StringLiterals.RLR_TITLE);
        } else {
            System.err.println("an error has occurred");
        }

        return GrammarUtil.removeLambdaRules(grammar);
    }

    @Override
    public String getName() {
        return "Remove "+ GUI.nameOfNullSymbol + "-Rules";
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

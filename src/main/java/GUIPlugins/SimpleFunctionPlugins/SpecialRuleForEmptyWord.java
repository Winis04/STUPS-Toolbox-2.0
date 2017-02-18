package GUIPlugins.SimpleFunctionPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Main.Storable;
import Print.Printer;
import Print.StringLiterals;


@SuppressWarnings("unused")
public class SpecialRuleForEmptyWord extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Object object) {
        if(object == null) {
            gui.errorDialog("Please load a grammar before using this command!");
            return null;
        }
        Grammar grammar = (Grammar) object;
        if(!GrammarUtil.languageContainsLambda(grammar)) {
            gui.infoDialog("no need for special rule");
        } else {
            Printer.printWithTitle("Special Rule for empty Word",GrammarUtil.specialRuleForEmptyWord(grammar,grammar));
           return GrammarUtil.specialRuleForEmptyWord(grammar,grammar);
        }
        return grammar;
    }

    @Override
    public String getName() {
        return "Special Rule";
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

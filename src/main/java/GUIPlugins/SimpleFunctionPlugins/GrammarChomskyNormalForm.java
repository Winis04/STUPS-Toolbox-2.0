package GUIPlugins.SimpleFunctionPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Main.Storable;
import Print.Printer;
import Print.StringLiterals;


@SuppressWarnings("unused")
public class GrammarChomskyNormalForm extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Object object) {
        Grammar grammar = (Grammar) object;
        if(object == null) {
            gui.errorDialog("Please load a Grammar before using this command");
            return null;
        }
        if(!GrammarUtil.isLambdaFree(grammar)) {
            gui.errorDialog("Can't do this! The grammar has lambda-rules!");
        } else if(GrammarUtil.hasUnitRules(grammar)) {
            gui.errorDialog("Can't do this! The grammar has unit-rules");
        } else if(GrammarUtil.isInChomskyNormalForm(grammar)) {
            gui.infoDialog("Can't do this! The grammar is already in chomsky normal-form");
        } else {


            Printer.printEnumeration(GrammarUtil.chomskyNormalFormAsPrintables(grammar), StringLiterals.CNF_POINT_DESCRIPTIONS,StringLiterals.CNF_TEXTS,StringLiterals.CNF_TITLE);
            return GrammarUtil.chomskyNormalForm(grammar);
        }
        return grammar;
    }

    @Override
   public String getName() {
        return "Chomsky Normal Form";
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

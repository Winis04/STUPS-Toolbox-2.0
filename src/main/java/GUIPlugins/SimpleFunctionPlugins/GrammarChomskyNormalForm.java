package GUIPlugins.SimpleFunctionPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Main.Storable;
import Print.Printer;

/**
 * @author Isabel
 * @since 03.12.2016
 */
@SuppressWarnings("ALL")
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
            String[] texts=new String[]{"","rules, that point only on one Nonterminal are already in chomsky normal form and we keep them",
                    "in all other rules replace every appearance of Terminal a through a new Nonterminal",
                    "in all rules that contain more than two nonterminals, add a new nonterminal that points to the end of the rule."};

            String[] point_descriptions=new String[]{"Before","Step 1", "Step 2", "Step 3"};

            Printer.printEnumeration(GrammarUtil.chomskyNormalFormAsPrintables(grammar),point_descriptions,texts,"Chomsky - Normal - Form");
            return GrammarUtil.chomskyNormalForm(grammar);
        }
        return grammar;
    }

    @Override
    String getName() {
        return "chomsky normal form";
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

package GUIPlugins.SimpleFunctionPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Main.GUI;
import Main.Storable;
import Print.Printable;
import Print.Printer;

import java.util.ArrayList;

/**
 * removes lambda rules
 * @author Isabel
 * @since 03.12.2016
 */

@SuppressWarnings("unused")
public class GrammarRemoveLambdaRules extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Object object) {
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
        String texts[];
        String point_descriptions[];
        if(printables.size()==4) {
            point_descriptions=new String[]{"Before","Step 1","Step 2","Step 3"};
            texts=new String[]{"",
                    "calculate the nullable set",
                    "for every rule that contains a nullable nonterminal, add that rule without this nonterminal",
                    "remove lambda-rules"};
        } else if(printables.size()==5) {
            point_descriptions=new String[]{"Before","Special Rule for Empty Word","Step 1","Step 2","Step 3"};
            texts=new String[]{"",
                    "add a nonterminal according to the special rule for empty word",
                    "calculate the nullable set",
                    "for every rule that contains a nullable nonterminal, add that rule without this nonterminal",
                    "remove lambda-rules"};
        } else {
            point_descriptions=new String[]{""};
            texts=new String[]{""};
        }
        Printer.printEnumeration(printables,point_descriptions,texts,"Remove Lambda-Rules");
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

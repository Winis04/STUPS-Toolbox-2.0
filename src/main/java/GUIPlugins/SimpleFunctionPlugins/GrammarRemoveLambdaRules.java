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
@SuppressWarnings("ALL")
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
            texts=new String[]{"","nullables","",
                    "All lambda-rules are removed and all nonterminals, that do not appear on any right side."};
        } else if(printables.size()==5) {
            point_descriptions=new String[]{"Before","Step 1","Step 2","Step 3","Special Rule for Empty Word"};
            texts=new String[]{"","add new Symbol","nullables","All lambda-rules are removed and all nonterminals, that do not appear on any right side.",""};
        } else {
            point_descriptions=new String[]{""};
            texts=new String[]{""};
        }
        Printer.printEnumeration(printables,point_descriptions,texts,"Remove Lambda-Rules");
        return GrammarUtil.removeLambdaRules(grammar);
    }

    @Override
    String getName() {
        return "remove "+ GUI.nameOfNullSymbol + "-Rules";
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

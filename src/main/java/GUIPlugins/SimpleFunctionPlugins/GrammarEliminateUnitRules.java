package GUIPlugins.SimpleFunctionPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Main.Storable;
import Print.Printer;




@SuppressWarnings("unused")
public class GrammarEliminateUnitRules extends SimpleFunctionPlugin {
    @Override
    public Storable execute(Object object) {
        if(object == null) {
            gui.errorDialog("Please load a grammar before using this command!");
            return null;
        }
        Grammar grammar = (Grammar) object;
        if(!GrammarUtil.hasUnitRules(grammar)) {
            gui.infoDialog("this grammar has no unit rules!");
        } else {
            String texts[]=new String[]{"",
                    "remove circles",
                    "number the nonterminals and remove unit rules beginning by the highest number."};
            String[] point_descriptions=new String[]{"Before","Step 1","Step 2"};
            Printer.printEnumeration(GrammarUtil.eliminateUnitRulesAsPrintables(grammar),point_descriptions,texts,"Eliminate Unit Rules");
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

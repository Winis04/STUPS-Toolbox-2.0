package CLIPlugins;

import GrammarSimulator.*;
import Print.Printer;

/**
 * Created by Isabel on 22.10.2016.
 */
public class GrammarEliminateUnitRules implements CLIPlugin {
    private boolean errorFlag = false;
    private Explanation type;


    @Override
    public String[] getNames() {
        return new String[]{"eur","eliminate-unit-rules"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return parameters.length==0;
    }

    @Override
    public String getHelpText() {
        return "eliminates unit Rules (A -> B) from the loaded grammar. Takes the strings 'no', 'short' or 'long' as a parameter, which determine the level of explanation.";
    }

    @Override
    public Object execute(Object object, String[] parameters) {
        errorFlag = false;
        if(object == null) {
            System.out.println("Please load a grammar before using this command!");
            errorFlag = true;
            return null;
        }


        Grammar grammar = (Grammar) object;
        if(!GrammarUtil.hasUnitRules(grammar)) {
            System.out.println("This grammar is already without unit rules");
            return null;
        }
        String texts[]=new String[]{"",
                "remove circles \\\\ \n",
                "number the nonterminals and remove unit rules beginning by the highest number. \\\\ \n"};
        String[] point_descriptions=new String[]{"Before","Step 1","Step 2"};
        Printer.printEnumeration(GrammarUtil.eliminateUnitRules(grammar),point_descriptions,texts,"Eliminate Unit Rules");

        return null;
    }

    @Override
    public Class inputType() {
        return Grammar.class;
    }

    @Override
    public Class outputType() {
        return null;
    }

    @Override
    public boolean errorFlag() {
        return errorFlag;
    }
}

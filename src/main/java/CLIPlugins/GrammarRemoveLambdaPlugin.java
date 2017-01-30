package CLIPlugins;

import GrammarSimulator.*;
import Print.Printable;
import Print.Printer;

import java.util.*;


/**
 * removes lambda-rules of the current {@link Grammar}.
 * @author isabel
 * @since 20.10.16
 */
@SuppressWarnings("ALL")
public class GrammarRemoveLambdaPlugin extends CLIPlugin {

    private boolean errorFlag = false;
    @Override
    public String[] getNames() {
        return new String[]{"rlr", "remove-lambda-rules"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return parameters.length==0;
    }

    @Override
    public String getHelpText() {
        return "removes lambda-rules of the loaded grammar. Doesn't take any parameters";
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
        if(GrammarUtil.isLambdaFree(grammar)) {
            System.out.println("This grammar is already lambda-free");
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
    public Class inputType() {
        return Grammar.class;
    }

    @Override
    public Class outputType() {
        return Grammar.class;
    }

    @Override
    public boolean errorFlag() {
        return errorFlag;
    }
}

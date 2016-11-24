package CLIPlugins;

import GrammarSimulator.*;
import Print.Printable;
import Print.Printer;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by isabel on 20.10.16.
 */
public class GrammarRemoveLambdaPlugin implements CLIPlugin {

    private boolean errorFlag = false;
    private Explanation type;
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
        return "removes lambda-rules. Takes the strings 'no', 'short' or 'long' as a parameter, which determine the level of explanation.";
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
       ArrayList<Printable> printables=GrammarUtil.removeLambdaRules(grammar);
        String texts[];
        if(printables.size()==4) {
            texts=new String[]{"Before","nullables","",
            "All lambda-rules are removed and all nonterminals, that do not appear on any right side."};
        } else if(printables.size()==5) {
            texts=new String[]{"Before","add new Symbol $S#§","nullables","","All lambda-rules are removed and all nonterminals, that do not appear on any right side."};
        } else {
            texts=new String[]{""};
        }
        Printer.print(printables,texts,"Remove Lambda-Rules");


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

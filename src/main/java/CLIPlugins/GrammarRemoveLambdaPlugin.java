package CLIPlugins;

import GrammarSimulator.*;
import Main.Storable;
import Print.Printable;
import Print.Printer;
import Print.StringLiterals;

import java.util.*;


/**
 * removes lambda-rules of the current {@link Grammar}.
 * @author isabel
 * @since 20.10.16
 */

@SuppressWarnings("unused")
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
    public Storable execute(Object object, String[] parameters) {
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

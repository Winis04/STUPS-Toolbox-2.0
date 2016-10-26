package CLIPlugins;

import GrammarSimulator.*;

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
        if(parameters.length==1) {
            if(parameters[0].equals("no")) {
                type=Explanation.NO;
                return true;
            }
            if(parameters[0].equals("short")) {
                type=Explanation.SHORT;
                return true;
            }
            if(parameters[0].equals("long")) {
                type=Explanation.LONG;
                return true;
            }
        }
        return false;
    }

    @Override
    public String getHelpText() {
        return "removes lambda-rules. Does not take any parameters";
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
        switch (type) {
            case NO:
                GrammarUtil.removeLambdaRulesWithNoOutput(grammar);
                break;
            case SHORT:
                GrammarUtil.removeLambdaRulesWithShortOutput(grammar);
                break;
            case LONG:
                GrammarUtil.removeLambdaRulesWithLongOutput(grammar);
                break;

        }
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

package CLIPlugins;

import GrammarSimulator.*;

import java.util.ArrayList;
import java.util.HashSet;

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
        return "eliminates unit Rules (A -> B) from the loaded grammar";
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
        switch (type) {
            case NO:
                GrammarUtil.eliminateUnitRulesWithNoOutput(grammar);
                break;
            case SHORT:
                GrammarUtil.eliminateUnitRulesWithShortOutput(grammar);
                break;
            case LONG:
                GrammarUtil.eliminateUnitRulesWithLongutput(grammar);
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

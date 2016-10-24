package CLIPlugins;

import GrammarSimulator.*;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Isabel on 22.10.2016.
 */
public class GrammarEliminateUnitRules implements CLIPlugin {
    private boolean errorFlag = false;



    @Override
    public String[] getNames() {
        return new String[]{"eur","eliminate-unit-rules"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return true;
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
        //first step

        System.out.println("Step 1: remove Circle");
        HashSet<Node> unitRules=GrammarUtil.removeCircleRules(grammar);
        GrammarUtil.print(grammar);


     //   GrammarUtil.print(grammar);

        System.out.println("Step 2: number the nonterminals");
        ArrayList<Node> list=GrammarUtil.removeUnitRules(unitRules,grammar);
        list.stream().forEach(x -> System.out.printf("%s: %d\n",x.getName(),x.getNumber()));
        System.out.println("Step 3: remove unit rules");
        GrammarUtil.print(grammar);
       // unitRules.stream().forEach(x -> System.out.printf("Node %s: %d\n",x.getName(),x.getNumber()));
        //second step

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

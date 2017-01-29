package CLIPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import GrammarSimulator.Nonterminal;
import GrammarSimulator.Terminal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @author fabian
 * @since 23.08.16
 */
@SuppressWarnings("ALL")
public class GrammarFollowPlugin implements CLIPlugin {

    private boolean errorFlag = false;

    @Override
    public String[] getNames() {
        return new String[]{"follow"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return true;
    }

    @Override
    public String getHelpText() {
        return "Prints the Follow-sets of the loaded grammar. Doesn't take any parameters.";
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
        HashMap<Nonterminal, HashSet<Terminal>> result = GrammarUtil.calculateFollow(grammar);

        for(Nonterminal nonterminal : GrammarUtil.getNonterminalsInOrder(grammar)) {
            System.out.print("Follow(" + nonterminal.getName() + ") = {");

            Iterator<Terminal> it = result.get(nonterminal).iterator();
            while(it.hasNext()) {
                Terminal currentTerminal = it.next();
                System.out.print(currentTerminal.getName());

                if(it.hasNext()) {
                    System.out.print(", ");
                }
            }

            System.out.println("}");
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

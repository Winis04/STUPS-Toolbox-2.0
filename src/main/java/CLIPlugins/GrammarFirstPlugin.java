package CLIPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import GrammarSimulator.Nonterminal;
import GrammarSimulator.Terminal;
import Main.Storable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Prints the Firsts-sets of the current {@link Grammar}.
 * @author fabian
 * @since 20.08.16
 */

@SuppressWarnings("unused")
public class GrammarFirstPlugin extends CLIPlugin {

    private boolean errorFlag = false;

    @Override
    public String[] getNames() {
        return new String[]{"first"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return true;
    }

    @Override
    public String getHelpText() {
        return "Prints the First-sets of the loaded grammar. Doesn't take any parameters.";
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
        HashMap<Nonterminal, HashSet<Terminal>> result = GrammarUtil.calculateFirst(grammar);

        for(Nonterminal nonterminal : GrammarUtil.getNonterminalsInOrder(grammar)) {
            System.out.print("First(" + nonterminal.getName() + ") = {");

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

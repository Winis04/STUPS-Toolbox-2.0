package CLIPlugins;

import GrammarSimulator.Grammar;
import Main.Storable;

/**
 * names a {@link Grammar}.
 * @author Isabel
 * @since 19.11.2016
 */


public class GrammarNamePlugin extends CLIPlugin {
    private boolean errorFlag;
    @Override
    public String[] getNames() {
        return new String[]{"ng","name-grammar"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return parameters.length==1;
    }

    @Override
    public String getHelpText() {
        return "Sets the name of the current grammar to the parameter";
    }

    @Override
    public Storable execute(Storable object, String[] parameters) {
        errorFlag = false;
        if(object == null) {
            System.out.println("Please load a grammar before using this command!");
            errorFlag = true;
            return null;
        }
        Grammar grammar=(Grammar) object;
        return new Grammar(grammar.getStartSymbol(),grammar.getRules(),parameters[0], (Grammar) grammar.getPreviousVersion());
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

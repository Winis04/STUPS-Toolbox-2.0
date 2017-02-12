package CLIPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import GrammarSimulator.Matrix;
import Main.Storable;
import Print.Printer;

import java.util.Arrays;

/**
 * Runs the CYK-Algorithm on the current {@link Grammar}.
 * @author Isabel
 * @since 26.10.2016
 */

@SuppressWarnings("unused")
public class GrammarCYK extends CLIPlugin {
    private boolean errorFlag;

    @Override
    public String[] getNames() {
        return new String[]{"cyk"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return parameters.length==1;
    }

    @Override
    public String getHelpText() {
        return "does the cyk-algorithm. Takes a word of the language as a parameter";
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
        if(!GrammarUtil.isInChomskyNormalForm(grammar)) {
            System.out.println("Is not in chomsky normal form");
            return null;
        }
        Matrix matrix=GrammarUtil.cyk(grammar, Arrays.asList(parameters[0].split(",")));
        if(matrix != null) {
            Printer.print(matrix);
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

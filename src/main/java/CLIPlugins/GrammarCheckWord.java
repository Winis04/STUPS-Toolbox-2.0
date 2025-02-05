package CLIPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Main.Storable;
import Print.Printer;

import java.util.Arrays;

/**
 * checks, if the current {@link Grammar}'s language contains the given word.
 * @author Isabel
 * @since 12.12.2016
 */


public class GrammarCheckWord extends CLIPlugin {
    private boolean errorFlag=false;
    @Override
    public String[] getNames() {
        return new String[]{"csg","check-string-grammar"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return parameters.length==1;
    }

    @Override
    public String getHelpText() {
        return "Checks, if the current grammar contains the word that is given as the first parameter.";
    }

    @Override
    public Storable execute(Storable object, String[] parameters) {
        if(object == null) {
            System.out.println("please load a grammar before using this command");
            errorFlag = true;
            return null;
        }
        Grammar grammar = (Grammar) object;
        boolean contains= GrammarUtil.languageContainsWord(grammar, Arrays.asList(parameters[0].split(",")));
        if(contains) {
            Printer.print("the grammar "+grammar.getName()+ " contains the word "+parameters[0] + ".");
        } else {
            Printer.print("the grammar "+grammar.getName()+ " doesn't contain the word "+parameters[0] + ".");
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


    @Override
    public boolean createsOutput() {
        return true;
    }
}

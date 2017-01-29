package CLIPlugins;

import GrammarSimulator.Grammar;
import GrammarSimulator.GrammarUtil;
import Print.Printer;

import java.util.Arrays;

/**
 * @author Isabel
 * @since 12.12.2016
 */
@SuppressWarnings("ALL")
public class GrammarCheckWord implements CLIPlugin {
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
        return "checks, if the current Grammar contains the word that is given as the second parameter";
    }

    @Override
    public Object execute(Object object, String[] parameters) {
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
            Printer.print("the grammar "+grammar.getName()+ " doesn't contains the word "+parameters[0] + ".");
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

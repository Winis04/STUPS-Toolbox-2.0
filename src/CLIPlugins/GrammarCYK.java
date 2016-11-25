package CLIPlugins;

import GrammarSimulator.*;
import Print.Printer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * Created by Isabel on 26.10.2016.
 */
public class GrammarCYK implements CLIPlugin {
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
    public Object execute(Object object, String[] parameters) {
        errorFlag = false;
        if(object == null) {
            System.out.println("Please load a grammar before using this command!");
            errorFlag = true;
            return null;
        }
        Grammar grammar = (Grammar) object;
        Matrix matrix=GrammarUtil.cyk(grammar,parameters[0]);
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

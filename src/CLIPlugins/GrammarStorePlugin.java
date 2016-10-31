package CLIPlugins;

import GrammarSimulator.Grammar;

/**
 * Created by Isabel on 31.10.2016.
 */
public class GrammarStorePlugin implements CLIPlugin {
    private boolean errorFlag;
    @Override
    public String[] getNames() {
        return new String[]{"st","store-grammar"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return parameters.length==0;
    }

    @Override
    public String getHelpText() {
        return null;
    }

    @Override
    public Object execute(Object object, String[] parameters) {
        errorFlag = false;
        if(object == null) {
            System.out.println("Please load a grammar before using this command!");
            errorFlag = true;
            return null;
        }
        return null;
    }

    @Override
    public Class inputType() {
        return Grammar[].class;
    }

    @Override
    public Class outputType() {
        return Grammar[].class;
    }

    @Override
    public boolean errorFlag() {
        return errorFlag;
    }
}

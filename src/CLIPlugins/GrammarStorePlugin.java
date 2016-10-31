package CLIPlugins;

/**
 * Created by Isabel on 31.10.2016.
 */
public class GrammarStorePlugin implements CLIPlugin {
    @Override
    public String[] getNames() {
        return new String[0];
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return false;
    }

    @Override
    public String getHelpText() {
        return null;
    }

    @Override
    public Object execute(Object object, String[] parameters) {
        return null;
    }

    @Override
    public Class inputType() {
        return null;
    }

    @Override
    public Class outputType() {
        return null;
    }

    @Override
    public boolean errorFlag() {
        return false;
    }
}

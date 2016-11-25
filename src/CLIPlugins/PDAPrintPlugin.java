package CLIPlugins;

import PushDownAutomatonSimulator.PushDownAutomaton;
import PushDownAutomatonSimulator.PushDownAutomatonUtil;

/**
 * Created by Isabel on 29.10.2016.
 */
public class PDAPrintPlugin implements CLIPlugin {
    private boolean errorFlag;
    @Override
    public String[] getNames() {
        return new String[]{"printEnumeration-push-down-automaton","ppda"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return parameters.length==0;
    }

    @Override
    public String getHelpText() {
        return "prints the current PDA";
    }

    @Override
    public Object execute(Object object, String[] parameters) {
        errorFlag = false;
        if(object == null) {
            System.out.println("Please use 'a', or 'automaton' to load an automaton before using this command!");
            errorFlag = true;
            return null;
        }
        PushDownAutomaton pda = (PushDownAutomaton) object;
        PushDownAutomatonUtil.print(pda);
        return null;
    }

    @Override
    public Class inputType() {
        return PushDownAutomaton.class;
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

package CLIPlugins;

import PushDownAutomatonSimulator.InputLetter;
import PushDownAutomatonSimulator.PushDownAutomaton;
import PushDownAutomatonSimulator.PushDownAutomatonUtil;

import java.util.ArrayList;

/**
 * Created by Isabel on 29.10.2016.
 */
public class PDAGoThroughInputPlugin implements CLIPlugin {
    private boolean errorFlag;
    @Override
    public String[] getNames() {
        return new String[]{"gti","go-through-input"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return parameters.length==1;
    }

    @Override
    public String getHelpText() {
        return "doesn't work yet!";
    }

    @Override
    public Object execute(Object object, String[] parameters) {
        errorFlag = false;
        if(object == null) {
            System.out.println("Please load a PDA before using this command");
            errorFlag = true;
            return null;
        }

        PushDownAutomaton pda = (PushDownAutomaton) object;
        if(pda.getCurrentInput().isEmpty()) {
            System.out.println("Please load an input in the pda");
            errorFlag = true;
            return null;
        }
        int j=Integer.parseInt(parameters[0]);

        if(j < 0) {
            System.out.println("Number negative!");
            errorFlag = true;
            return null;
        }

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

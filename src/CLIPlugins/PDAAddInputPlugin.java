package CLIPlugins;

import PushDownAutomatonSimulator.InputLetter;
import PushDownAutomatonSimulator.PushDownAutomaton;
import PushDownAutomatonSimulator.PushDownAutomatonUtil;

import java.util.ArrayList;

/**
 * Created by Isabel on 29.10.2016.
 */
public class PDAAddInputPlugin implements CLIPlugin {
    private boolean errorFlag;
    @Override
    public String[] getNames() {
        return new String[]{"ai","add-input"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return parameters.length==1;
    }

    @Override
    public String getHelpText() {
        return null;
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
        String string=parameters[0];
        ArrayList<InputLetter> list=new ArrayList<>();
        for(int i=0;i<string.length();i++) {
            InputLetter tmp=PushDownAutomatonUtil.getInputLetterWithName(string.substring(i,i+1),pda);
            if(tmp==null) {
                System.out.println("This input is not allowed!");
                errorFlag = true;
                return null;
            } else {
                list.add(tmp);
            }
        }
        pda.setCurrentInput(list);
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

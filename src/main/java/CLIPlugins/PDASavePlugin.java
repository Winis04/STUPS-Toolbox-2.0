package CLIPlugins;

import Main.Storable;
import PushDownAutomatonSimulator.PushDownAutomaton;
import PushDownAutomatonSimulator.PushDownAutomatonUtil;

/**
 * Saves a {@link PushDownAutomaton} to a file.
 * @author Isabel
 * @since 29.01.2017
 */
public class PDASavePlugin extends CLIPlugin{

        private boolean errorFlag = false;

        @Override
        public String[] getNames() {
            return new String[]{"spda", "save-pda"};
        }

        @Override
        public boolean checkParameters(String[] parameters) {
            if(parameters.length < 1) {
                System.out.println("Please enter a filename as parameter for this command!");
                return false;
            }
            return true;
        }

        @Override
        public String getHelpText() {
            return "Writes the loaded pda into a text file, which can later be reloaded by this program. Takes a file as parameter.";
        }

        @Override
        public Storable execute(Object object, String[] parameters) {
            errorFlag = false;
            if(object == null) {
                System.out.println("Please load a pda before using this command!");
                errorFlag = true;
                return null;
            }
            PushDownAutomaton pda = (PushDownAutomaton) object;
            PushDownAutomatonUtil.save(pda, parameters[0]);
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


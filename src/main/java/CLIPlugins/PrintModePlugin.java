package CLIPlugins;

import Main.Storable;
import Print.Printer;

import java.io.*;


/**
 * Sets the Print-Mode of the {@link Printer}.
 * @author Isabel
 * @since 18.11.2016
 */

public class PrintModePlugin extends CLIPlugin {
    private boolean errorFlag;
    @Override
    public String[] getNames() {
        return new String[]{"print-mode","pm"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return parameters.length < 4;
    }

    @Override
    public String getHelpText() {
        return "Sets the print-mode. 'no' for no output, 'latex' and 'path-to-file' for latex output in file (and --force if the file already exists and should be overwritten), 'console' for output in the terminal. To end an Latex file, change print mode.";
    }

    @Override
    public Storable execute(Object object, String[] parameters) {
        errorFlag = false;
        if(parameters.length==1) {


            switch (parameters[0]) {
                case "no":
                    Printer.setPrintMode_No();
                    break;
                case "console":
                    Printer.setPrintMode_Console();
                    break;
                default:
                    errorFlag = true;
                    System.out.println("not a valid parameter");
                    return null;
            }

        } else {
            if(parameters[0].equals("latex")) {
                if(new File(parameters[1]).exists()) {
                    if(!(parameters.length==3 && parameters[2].equals("--force"))) {
                        System.out.println("this file already exists");
                        Printer.setPrintMode_Console();
                        errorFlag=true;
                        return null;
                    }
                }
                Printer.setPrintMode_Latex(new File(parameters[1]));

            } else {
                errorFlag=true;
                System.out.println("not a valid parameter");
                return null;
            }
        }
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
        return errorFlag;
    }
}

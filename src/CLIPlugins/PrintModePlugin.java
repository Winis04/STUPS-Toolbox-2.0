package CLIPlugins;

import Print.PrintMode;
import Print.Printer;

import java.io.PrintWriter;

/**
 * Created by Isabel on 18.11.2016.
 */
public class PrintModePlugin implements CLIPlugin {
    private boolean errorFlag;
    @Override
    public String[] getNames() {
        return new String[]{"print-mode","pm"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return parameters.length==1 || parameters.length==2;
    }

    @Override
    public String getHelpText() {
        return "sets the print-modus. 'no' for no output, 'latex' and 'path-to-file' for latex output in file, 'terminal' for output in the terminal";
    }

    @Override
    public Object execute(Object object, String[] parameters) {
        errorFlag = false;
        if(parameters.length==1) {
            if(parameters[0].equals("no")) {
                Printer.printmode= PrintMode.NO;
            } else if(parameters[0].equals("terminal")) {
                Printer.printmode=PrintMode.TERMINAL;
            } else {
                errorFlag=true;
                System.out.println("not a valid parameter");
                return null;
            }
        } else {
            if(parameters[0].equals("latex")) {
                Printer.printmode=PrintMode.LATEX;
                try{
                    PrintWriter writer = new PrintWriter(parameters[1], "UTF-8");
                    writer.close();
                } catch (Exception e) {
                    errorFlag=true;
                    System.out.println("not a valid path");
                    return null;
                }
                Printer.currentFile=parameters[1];

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

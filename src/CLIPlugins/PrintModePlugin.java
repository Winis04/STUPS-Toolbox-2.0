package CLIPlugins;

import Print.PrintMode;
import Print.Printer;

import java.io.*;

import static Print.Printer.writer;

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
        return parameters.length < 4;
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
            } else if(parameters[0].equals("console")) {
                Printer.printmode = PrintMode.CONSOLE;
            } else if(parameters[0].equals("close") &&Printer.printmode==PrintMode.LATEX && Printer.writer!=null) {
                Printer.printEndOfLatex(Printer.writer);
                try {
                    Printer.writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Printer.printmode=PrintMode.CONSOLE;

            } else {
                errorFlag=true;
                System.out.println("not a valid parameter");
                return null;
            }
        } else {
            if(parameters[0].equals("latex")) {
                Printer.printmode=PrintMode.LATEX;
                if(Printer.writer!=null) {
                    Printer.printEndOfLatex(writer);
                }
                if(new File(parameters[1]).exists()) {
                    if(parameters.length==3 && parameters[2].equals("--force")) {

                    } else {
                        System.out.println("this file already exists");
                        errorFlag=true;
                        return null;
                    }
                }
                Printer.currentFile=parameters[1];
                try {
                    writer=new BufferedWriter(new FileWriter(Printer.currentFile));
                    Printer.printStartOfLatex(writer);
                } catch (IOException e) {
                    e.printStackTrace();
                }

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

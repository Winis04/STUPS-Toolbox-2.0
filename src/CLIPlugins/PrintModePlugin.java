package CLIPlugins;

import Print.PrintMode;
import Print.Printer;

import java.io.*;


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
        if(Printer.printmode==PrintMode.LATEX && !Printer.writerIsNull()) {
            Printer.printEndOfLatex();
            Printer.closeWriter();
            Printer.setWriter(null);
        }
        if(parameters.length==1) {


            if(parameters[0].equals("no")) {
                Printer.printmode= PrintMode.NO;
                Printer.setWriter(null);
            } else if(parameters[0].equals("console")) {
                Printer.setWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
                Printer.printmode = PrintMode.CONSOLE;
            } else {
                errorFlag=true;
                System.out.println("not a valid parameter");
                return null;
            }

        } else {
            if(parameters[0].equals("latex")) {
                Printer.printmode=PrintMode.LATEX;
                if(new File(parameters[1]).exists()) {
                    if(parameters.length==3 && parameters[2].equals("--force")) {

                    } else {
                        System.out.println("this file already exists");
                        Printer.printmode=PrintMode.CONSOLE;
                        errorFlag=true;
                        return null;
                    }
                }
                Printer.currentFile=parameters[1];
                try {
                    Printer.setWriter(new BufferedWriter(new FileWriter(Printer.currentFile)));
                    Printer.printStartOfLatex();
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

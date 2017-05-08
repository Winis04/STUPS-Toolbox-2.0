package Print;



import Main.GUI;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;


/**
 * A class used for Printing on the console and in latex files.
 */
public class Printer {
    /**
     * the current print mode. Is an Instance of enum {@link PrintMode}
     */
    private static PrintMode printmode=PrintMode.CONSOLE;

    /**
     * the indentation of the latex file
     */
    private static int deepness =0;

    /**
     * current writer
     */
    private static BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(System.out));

    /**
     * Prints a {@link Printable} object.
     * @param printable the {@link Printable} that should be printed
     */
    public static void print(Printable printable) {
        switch (printmode) {
            case NO:
                break;
            case LATEX:
                printable.printLatex(getSpace(deepness));
                break;
            case CONSOLE:
                printable.printConsole();
                break;
        }
    }

    /**
     * prints a String using the current {@link #writer}
     * @param string the {@link String} which should be printed
     */
    public static void print(String string) {
        switch (printmode) {
            case NO:
                break;
            case LATEX:
                printLatex(string);
                break;
            case CONSOLE:
                printConsole(string);
                break;
        }
    }

    /**
     * prints a String using the given writer
     * @param string the {@link String} which should be printed
     */
    private static void printConsole(String string) {
        try {
            writer.write(string);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void printLatex(String string) {
        try {
            writer.write(string);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Used for printing an enumeration of text and {@link Printable}s.
     * @param printables the {@link Printable}s that should be printed
     * @param point_descriptions the titles of the different items of the enumeration
     * @param texts the texts printed before the printable object
     * @param title the title of the enumeration
     */
    public static void printEnumeration(List<Printable> printables, String[] point_descriptions, String[] texts, String title) {
        switch(printmode) {
            case NO:
                break;
            case CONSOLE:
                printEnumerationConsole(printables,point_descriptions,texts);
                break;
            case LATEX:
                printEnumerationLatex(printables,point_descriptions,texts,title);
                break;
        }
    }

    /**
     * transforms the Strings of an Array such that it is conform with latex
     * @param arr the array with string which need to be transformed
     * @return an Array with transformed strings
     */
    private static String[] toLatex(String[] arr) {
        String[] res = new String[arr.length];
        for(int i=0;i<arr.length;i++) {
            res[i] = toLatex(arr[i]);
        }
        return res;
    }

    /**
     * transforms a String such that it can be used in latex
     * @param string the String that should be transformed
     * @return the transformed string
     */
    public static String toLatex(String string) {
        if(string.length()==1) {
            return checkIfLatexSpecial(string);
        }

        //replaces "->" through the latex rightarrow symbol
        String res = Arrays.stream(string.split("->")).collect(joining(" $\\rightarrow$ "));
        // split at "_"
        String[] tmp = res.split("_");
        //replaces every "_" except the first through "-"
        if(tmp.length > 1) {
            String[] arr = new String[tmp.length - 1];
            System.arraycopy(tmp, 1, arr, 0, tmp.length - 1);
            //detects a latex special character
            res = tmp[0] + "_" + Arrays.stream(arr).map(Printer::checkIfLatexSpecial).collect(joining("-"));
        }
        tmp = res.split("_");
        if(tmp.length > 1) {
            String[] tmp2 = tmp[1].split(" ");
            String[] arr = new String[tmp2.length - 1];
            System.arraycopy(tmp2, 1, arr, 0, tmp2.length - 1);
            res = tmp[0]+"_{"+checkIfLatexSpecial(tmp2[0])+"} "+Arrays.stream(arr).map(Printer::checkIfLatexSpecial).collect(joining(" "));
        }
        return /**makeToGreek(**/res;//);

    }

    /**
     * prints a {@link Printable} with a title
     * @param title the title
     * @param printable the {@link Printable}
     */
    public static void printWithTitle(String title, Printable printable) {
        switch(printmode) {
            case NO:
                break;
            case CONSOLE:
                printWithTitleConsole(title,printable);
                break;
            case LATEX:
                printWithTitleLatex(title, printable);
                break;
        }

    }

    /**
     * prints a String} with a title
     * @param title the title
     * @param string the print that should be printed
     */
    public static void printWithTitle(String title, String string) {
        switch(printmode) {
            case NO:
                break;
            case CONSOLE:
                printWithTitleConsole(title,string);
                break;
            case LATEX:
                printWithTitleLatex(toLatex(title), string);
                break;
        }

    }




    private static void printWithTitleConsole(String title, Printable printable) {
        Printer.printConsole(title+"\n\n");
        printable.printConsole();
    }

    private static void printWithTitleConsole(String title, String string) {
        Printer.print(title+"\n\n");
        Printer.print(string);
    }


    /** LATEX **/

    private static void printWithTitleLatex(String title, Printable printable) {
        Printer.print("\\section{$"+toLatex(title)+"$}\n\n");
        printable.printLatex(getSpace(deepness));

    }

    private static void printWithTitleLatex(String title, String string) {
        Printer.print("\\section{$"+toLatex(title)+"$}\n\n");
        Printer.print(getSpace(deepness)+string);

    }

    private static void printEnumerationLatex(List<Printable> printables, String[] point_descriptions, String[] texts, String title) {
        if(printables.size()!=texts.length || printables.size()!=point_descriptions.length) {
            return;
        }
        Printer.printLatex("\\section{"+toLatex(title)+"}\n\n");
        Printer.printLatex("\\begin{description}\n");
        Printer.deepness++;
        for(int i=0;i<printables.size();i++) {
            writeItem(toLatex(point_descriptions[i]),toLatex(texts[i]));
            printables.get(i).printLatex(getSpace(deepness));

        }
        Printer.deepness--;
        Printer.printLatex("\\end{description}\n\n");

    }

    /**
     * prints the beginning of a latex document
     */
    private static void printStartOfLatex() {
        Printer.printLatex("%this document was generated by the STUPS Toolbox 2.0\n");
        Printer.printLatex("\\documentclass{article}\n\\" +
                "usepackage{amssymb}\n\\" +
                "usepackage{amsmath,amsthm}\n\\" +
                "usepackage[ngerman,english]{babel}\n\\" +
                "usepackage{tikz}\n\\" +
                "usetikzlibrary{automata,positioning}\n\n\\" +
                "begin{document}\n\n");
    }

    /**
     * prints the end of a latex document
     */
    private static void printEndOfLatex() {
        if(printmode==PrintMode.LATEX) {
            printLatex("\\end{document}");
            if(Printer.writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * transforms "epsilon" and "lambda" to greek letters in LaTex
     * @param string a String
     * @return a greek letter in LaTex
     */
    public static String makeToGreek(String string) {
        if(string.equals("epsilon")||string.equals("lambda")) {
            return "\\"+string;

        } else if(string.equals(GUI.nameOfNullSymbol)) {
            return "\\"+"epsilon";

        } else {
            return string;
        }
    }



    private static void writeItem(String title, String subtitle) {
        String s="";
        for(int i = 0; i<Printer.deepness; i++) {
            s+="\t";
        }
        if(subtitle.isEmpty()) {
            Printer.printLatex(s+"\\item["+title+"] \\hfill \\\\ \n"+s+subtitle);
        } else {
            Printer.printLatex(s+"\\item["+title+"] \\hfill \\\\ \n"+s+subtitle+"\\\\ \n");
        }

    }


    /** CONSOLE **/



    private static void printEnumerationConsole(List<Printable> printables, String[] point_description, String[] texts) {
        if(printables.size()!=texts.length || printables.size()!=point_description.length) {
            return;
        }

        for(int i=0;i<printables.size();i++) {
            Printer.printConsole("\n"+point_description[i]+": "+texts[i]+"\n");

            printables.get(i).printConsole();

        }

    }


    /** PRIVATE **/

    private static String getSpace(int x) {
        String res="";
        for(int i=0;i<x;i++) {
            res+="\t";
        }
        return res;
    }


    /**
     * checks if a string is a special character in latex
     * @param string the string that should be checked
     * @return a string that is ok in LaTex
     */
    public static String checkIfLatexSpecial(String string) {
        if(string.length() > 1) {
            if (string.equals("lambda") || string.equals("epsilon")) {
                return "\\" + string;
            }
        } else {
            if (!string.matches("[0-9a-zA-Z]")) {
                return "\\" + string;
            }
        }
        return string;
        /**
        String[] special = new String[]{"#","epsilon","lambda","alpha","beta","$","%","{","}","&","_",""};
        List<String> list = Arrays.asList(special);
        if(Printer.printmode==PrintMode.LATEX && list.contains(string)) {
            return "\\"+string;
        } else {
            return string;
        } **/
    }

    public static String fill(String s, int n) {
        String res=s;
        if(n > s.length()) {
            int k = n-s.length();
            for(int i=0;i<k;i++) {
                res+=" ";
            }
        }
        return res;
    }


    /* GETTER AND SETTER */

    public static PrintMode getPrintmode() {
        return printmode;
    }

    public static void setPrintMode_No() {
        initiatePrintModeAndWriter(PrintMode.NO,null);
    }

    public static void setPrintMode_Console() {
        Printer.initiatePrintModeAndWriter(PrintMode.CONSOLE,new BufferedWriter(new OutputStreamWriter(System.out)));
    }

    public static void setPrintMode_Latex(File file) {
        try {
            Printer.initiatePrintModeAndWriter(PrintMode.LATEX,new BufferedWriter(new FileWriter(file)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void setPrintMode(PrintMode printmode) {
        Printer.printmode = printmode;
    }
    private static void initiatePrintModeAndWriter(PrintMode printmode, BufferedWriter writer) {
        if(Printer.printmode==PrintMode.LATEX && Printer.writer != null) {
            Printer.printEndOfLatex();
           // Printer.closeWriter();
        }
        Printer.printmode = printmode;
        Printer.writer = writer;
        if(printmode == PrintMode.LATEX) {
            Printer.printStartOfLatex();
        }
    }

    public static String replace_underscore(String s) {
       return Arrays.stream(s.split("_")).collect(Collectors.joining(""));
    }


}

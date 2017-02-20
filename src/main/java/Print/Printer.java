package Print;



import GUIPlugins.SimpleFunctionPlugins.Print;
import Main.GUI;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.joining;


@SuppressWarnings("unused")
public class Printer {
    /**
     * the print mode
     */
    @SuppressWarnings("unused")
    public static PrintMode printmode=PrintMode.CONSOLE;
    /**
     * the current file that should be used
     */
    @SuppressWarnings("unused")
    public static String currentFile;
    //BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

    @SuppressWarnings("unused")
    private static int deepness =0;
    @SuppressWarnings("unused")
    private static BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(System.out));

    @SuppressWarnings("unused")
    public static void print(Printable printable) {
        switch (printmode) {
            case NO:
                break;
            case LATEX:
                printable.printLatex(writer,getSpace(deepness));
                break;
            case CONSOLE:
                printable.printConsole(writer);
                break;
        }
    }
    @SuppressWarnings("unused")
    public static void print(String string) {
        print(string,Printer.writer);
    }
    @SuppressWarnings("unused")
    public static void print(String string, BufferedWriter writer) {
        try {
            writer.write(string);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unused")
    public static void println(String string, BufferedWriter writer) {
        try {
            writer.write(string+"\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unused")
    public static void print(int i, BufferedWriter writer) {
        try {
            writer.write(i+"");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public static void printEnumeration(ArrayList<Printable> printables, String[] point_descriptions, String[] texts, String title) {
        switch(printmode) {
            case NO:
                break;
            case CONSOLE:
                printEnumerationConsole(printables,point_descriptions,texts);
                break;
            case LATEX:
                printEnumerationLatex(printables,toLatex(point_descriptions),toLatex(texts),toLatex(title));
                break;
        }
    }

    public static String[] toLatex(String[] arr) {
        String[] res = new String[arr.length];
        for(int i=0;i<arr.length;i++) {
            res[i] = toLatex(arr[i]);
        }
        return res;
    }

    public static String toLatex(String string) {
        String res = Arrays.stream(string.split("->")).collect(joining(" $\\rightarrow$ "));
        String[] tmp = res.split("_");
        if(tmp.length > 1) {
            String[] arr = new String[tmp.length - 1];
            System.arraycopy(tmp, 1, arr, 0, tmp.length - 1);
            res = tmp[0] + "_" + Arrays.stream(arr).collect(joining("-"));
        }
        tmp = res.split("_");
        if(tmp.length > 1) {
            String[] tmp2 = tmp[1].split(" ");
            String[] arr = new String[tmp2.length - 1];
            System.arraycopy(tmp2, 1, arr, 0, tmp2.length - 1);
            res = tmp[0]+"_{"+tmp2[0]+"} "+Arrays.stream(arr).collect(joining(" "));
        }
        return makeToGreek(res);

    }

    @SuppressWarnings("unused")
    public static void printWithTitle(String title, Printable printable) {
        switch(printmode) {
            case NO:
                break;
            case CONSOLE:
                printWithTitleConsole(title,printable);
                break;
            case LATEX:
                printWithTitleLatex(toLatex(title), printable);
                break;
        }

    }

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


    @SuppressWarnings("unused")
    private static void printWithTitleLatex(String title, Printable printable) {
        Printer.print("\\section{"+title+"}\n\n",writer);
        printable.printLatex(writer,getSpace(deepness));

    }

    private static void printWithTitleLatex(String title, String string) {
        Printer.print("\\section{"+title+"}\n\n",writer);
        Printer.print(getSpace(deepness)+string);

    }

    @SuppressWarnings("unused")
    private static void printWithTitleConsole(String title, Printable printable) {
        Printer.print(title+"\n\n",writer);
        printable.printConsole(writer);
    }

    private static void printWithTitleConsole(String title, String string) {
        Printer.print(title+"\n\n",writer);
        Printer.print(string);
    }


    @SuppressWarnings("unused")
    public static void setWriter(BufferedWriter writer) {
        Printer.writer = writer;
    }

    @SuppressWarnings("unused")
    public static void closeWriter() {
        try {
            Printer.writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unused")
    public static boolean writerIsNotNull() {
        return writer != null;
    }


    /** LATEX **/
    @SuppressWarnings("unused")
    private static void printEnumerationLatex(ArrayList<Printable> printables, String[] point_descriptions, String[] texts, String title) {
        if(printables.size()!=texts.length || printables.size()!=point_descriptions.length) {
            return;
        }
        Printer.print("\\section{"+title+"}\n\n",writer);
        Printer.print("\\begin{description}\n",writer);
        Printer.deepness++;
        for(int i=0;i<printables.size();i++) {
            writeItem(point_descriptions[i],texts[i]);
            printables.get(i).printLatex(writer,getSpace(deepness));

        }
        Printer.deepness--;
        Printer.print("\\end{description}\n\n",writer);

    }
    @SuppressWarnings("unused")
    public static void printStartOfLatex() {
        Printer.println("%this document was generated by the STUPS Toolbox 2.0",writer);
        Printer.print("\\documentclass{article}\n\\" +
                "usepackage{amssymb}\n\\" +
                "usepackage{amsmath,amsthm}\n\\" +
                "usepackage[ngerman,english]{babel}\n\\" +
                "usepackage{tikz}\n\\" +
                "usetikzlibrary{automata,positioning}\n\n\\" +
                "begin{document}\n\n",writer);
    }
    @SuppressWarnings("unused")
    public static void printEndOfLatex() {
        if(printmode==PrintMode.LATEX) {
            print("\\end{document}",writer);
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @SuppressWarnings("unused")
    public static String makeToGreek(String string) {
        if(string.equals("epsilon")||string.equals("lambda")) {
            return "\\"+string;

        } else if(string.equals(GUI.nameOfNullSymbol)) {
            return "\\"+"epsilon";

        } else {
            return string;
        }
    }


    @SuppressWarnings("unused")
    private static void writeItem(String title, String subtitle) {
        String s="";
        for(int i = 0; i<Printer.deepness; i++) {
            s+="\t";
        }
        if(subtitle.isEmpty()) {
            Printer.print(s+"\\item["+title+"] \\hfill \\\\ \n"+s+subtitle,writer);
        } else {
            Printer.print(s+"\\item["+title+"] \\hfill \\\\ \n"+s+subtitle+"\\\\ \n",writer);
        }

    }


    /** CONSOLE **/


    @SuppressWarnings("unused")
    private static void printEnumerationConsole(ArrayList<Printable> printables, String[] point_description, String[] texts) {
        if(printables.size()!=texts.length || printables.size()!=point_description.length) {
            return;
        }

        for(int i=0;i<printables.size();i++) {
            Printer.print(point_description[i]+": "+texts[i]+"\n",writer);

            printables.get(i).printConsole(writer);

        }

    }


    /** PRIVATE **/
    @SuppressWarnings("unused")
    private static String getSpace(int x) {
        String res="";
        for(int i=0;i<x;i++) {
            res+="\t";
        }
        return res;
    }

    @SuppressWarnings("unused")
    public static String checkIfLatexSpecial(String string) {
        String[] special = new String[]{"#","epsilon","lambda","alpha","beta","$","%","{","}","&","_",""};
        List<String> list = Arrays.asList(special);
        if(Printer.printmode==PrintMode.LATEX && list.contains(string)) {
            return "\\"+string;
        } else {
            return string;
        }
    }




}

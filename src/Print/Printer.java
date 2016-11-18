package Print;

import GrammarSimulator.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * Created by Isabel on 18.11.2016.
 */
public class Printer {
    /**
     * the print mode
     */
    public static PrintMode printmode=PrintMode.CONSOLE;
    /**
     * the current file that should be used
     */
    public static String currentFile;
    //BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

    public static BufferedWriter writer=null;
    /**
     * Prints a given grammar depending on {@Link printmode}
     *
     * @param grammar The grammar.
     */
    public static void printGrammar(Grammar grammar) {
        switch(printmode) {
            case NO:
                break;
            case LATEX:
                printGrammarLatex(grammar);
                break;
            case CONSOLE:
                printGrammarConsole(grammar);
                break;
        }


    }

    public static void printCNF(Grammar grammar) {
        switch(printmode) {
            case NO:
                break;
            case LATEX:
                printCNFLatex(grammar);
                break;
            case CONSOLE:
                printCNFConsole(grammar);
                break;
        }
    }
    private static void printCNFConsole(Grammar grammar) {
        BufferedWriter writer1=new BufferedWriter(new OutputStreamWriter(System.out));
        try {
            writer1.write("Before:\n");
            writer1.flush();
            printGrammar(grammar);
            writer1.write("Step 1: rules in form of A --> a are already in chomsky normal form and we keep them.\n");
            writer1.flush();
            writer1.write("Step 2: in all other rules replace every appearance of Terminal a through a new Nonterminal X_a and add the rule X_a --> a\n");
            writer1.flush();
            printGrammar(grammar);
            GrammarUtil.chomskyNormalForm_StepOne(grammar);
            writer1.write("Step 3: in all rules that contain more than two nonterminals, add a new nonterminal that points to the end of the rule\n");
            writer1.flush();
            GrammarUtil.chomskyNormalForm_StepTwo(grammar);
            printGrammar(grammar);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static void printCNFLatex(Grammar grammar) {
        try {
            writer.write("\\section{Chomsky - Normal - Form}\n");
            writer.write("\\begin{description}\n");
            writer.write("\\item[Before]\n");
            writer.flush();
            printGrammar(grammar);
            writer.write("\\item[Step 1] rules in form of $A \\rightarrow a$ are already in chomsky normal form and we keep them.\n");
            writer.flush();
            writer.write("\\item[Step 2] in all other rules replace every appearance of Terminal a through a new Nonterminal $X_a$ and add the rule $X_a \\rightarrow a$.\n");
            writer.flush();

            GrammarUtil.chomskyNormalForm_StepOne(grammar);
            printGrammar(grammar);
            writer.write("\\item[Step 3] in all rules that contain more than two nonterminals, add a new nonterminal that points to the end of the rule\n");
            writer.flush();
            GrammarUtil.chomskyNormalForm_StepTwo(grammar);
            printGrammar(grammar);
            writer.write("\\end{description}\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static void printGrammarConsole(Grammar grammar) {
        BufferedWriter writer1=new BufferedWriter(new OutputStreamWriter(System.out));
        try {

            ArrayList<String>[] header=getHeader(grammar);

            writer1.write("{");
            writer1.write(header[0].stream().collect(joining(", ")));
            writer1.flush();
            writer1.write("; ");

            writer1.write(header[1].stream().collect(joining(", ")));
            writer1.flush();
            writer1.write("; ");
            writer1.write(header[2].get(0));
            writer1.flush();
            writer1.write("}\n\n");
            writer1.flush();

            for(Nonterminal nt : GrammarUtil.getNonterminalsInOrder(grammar)) {
                writer1.write(nt.getName() + " --> ");
                HashSet<ArrayList<String>> tmp=getRulesToNonterminal(grammar,nt);
                writer1.write(tmp.stream().map(list -> list.stream().collect(joining(""))).collect(joining(" | ")));
                writer1.write("\n");
                writer1.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printGrammarLatex(Grammar grammar) {

        try {


            ArrayList<String>[] header=getHeader(grammar);
            writer.write("$");
            writer.write("G=\\left(\\{");
            writer.write(header[0].stream().map(string -> makeToGreek(string)).collect(joining(", ")));

            writer.write("\\},\\;\\{ ");

            writer.write(header[1].stream().collect(joining(", ")));
            writer.write("\\},\\;");

            writer.write(header[2].get(0));

            writer.write(",\\;P\\right)");
            writer.write("$ with\n");
            writer.write("\\begin{align*}\n");
            writer.write("P=\\{");


            writer.write(GrammarUtil.getNonterminalsInOrder(grammar).stream().
                    map(nonterminal -> {
                        String start="\t"+nonterminal.getName() + " &\\rightarrow ";
                        HashSet<ArrayList<String>> tmp=getRulesToNonterminal(grammar,nonterminal);
                        start+=tmp.stream().
                                map(list -> list.stream().
                                        map(string -> makeToGreek(string)).
                                        collect(joining(""))).
                                collect(joining("\\;|\\;"));
                        return start;
                    }).collect(joining(", \\\\ \n")));

            writer.write("\\}\n");
            writer.write("\\end{align*}\n");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static ArrayList<String>[] getHeader(Grammar grammar) {
        ArrayList<String>[] header=new ArrayList[3];
        header[0]=getTerminalsAsStrings(grammar);
        header[1]=getNonterminalsAsStrings(grammar);
        ArrayList<String> tmp=new ArrayList<>();
        tmp.add(grammar.getStartSymbol().getName());
        header[2]=tmp;
        return header;
    }


    private static ArrayList<String> getTerminalsAsStrings(Grammar grammar) {
        //Get all of the grammar's terminals in order of their appearance in the rules.
        ArrayList<Terminal> terminals = GrammarUtil.getTerminalsInOrder(grammar);
        return (ArrayList<String>) terminals.stream().map(terminal -> terminal.getName()).collect(Collectors.toList());
    }

    private static ArrayList<String> getNonterminalsAsStrings(Grammar grammar) {
        ArrayList<Nonterminal> nonterminals = GrammarUtil.getNonterminalsInOrder(grammar);
        return (ArrayList<String>) nonterminals.stream().map(nonterminal -> nonterminal.getName()).collect(Collectors.toList());
    }
    private static HashSet<ArrayList<String>> getRulesToNonterminal(Grammar grammar, Nonterminal nonterminal) {
        HashSet<ArrayList<Symbol>> lists=nonterminal.getSymbolLists();
        HashSet<ArrayList<String>> result=new HashSet<>();
        for(ArrayList<Symbol> list : lists) {
            result.add((ArrayList<String>)list.stream().map(symbol -> symbol.getName()).collect(toList()));
        }
        return result;
    }
    public static void printStartOfLatex(BufferedWriter writer) {
        try {
            writer.write("%%this document was generated by the STUPS Toolbox 2.0\n");
            writer.write("\\documentclass{article}\n\\usepackage{amssymb}\n\\usepackage{amsmath,amsthm}\n\\usepackage[ngerman,english]{babel}\n\n\\begin{document}\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void printEndOfLatex(BufferedWriter writer) {
        try {
            writer.write("\\end{document}");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String makeToGreek(String string) {
        if(string.equals("epsilon")||string.equals("lambda")) {
            return "\\"+string;
        } else {
            return string;
        }
    }
}

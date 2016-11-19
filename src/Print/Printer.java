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
                printGrammarLatex(grammar,0);
                break;
            case CONSOLE:
                printGrammarConsole(grammar);
                break;
        }


    }
    public static void printGrammar(Grammar grammar, int x) {
        switch(printmode) {
            case NO:
                break;
            case LATEX:
                printGrammarLatex(grammar,x);
                break;
            case CONSOLE:
                printGrammarConsole(grammar);
                break;
        }


    }

    public static void printCNF(Grammar grammar) {
        switch(printmode) {
            case NO:
                GrammarUtil.chomskyNormalForm(grammar);
                break;
            case LATEX:
                printCNFLatex(grammar);
                break;
            case CONSOLE:
                printCNFConsole(grammar);
                break;
        }
    }


    public static void printEliminateUnitRules(Grammar grammar) {
        switch(printmode) {
            case NO:
                GrammarUtil.eliminateUnitRules(grammar);
                break;
            case LATEX:
                printEliminateUnitRulesLatex(grammar);
                break;
            case CONSOLE:
                printEliminateUnitRulesConsole(grammar);
                break;
        }
    }

    public static void printRemoveLambdaRules(Grammar grammar) {
        switch(printmode) {
            case NO:
                GrammarUtil.removeLambdaRules(grammar);
                break;
            case LATEX:
                printRemoveLambdaRulesLatex(grammar);
                break;
            case CONSOLE:
                printRemoveLambdaRulesConsole(grammar);
                break;
        }
    }
    private static void printRemoveLambdaRulesLatex(Grammar grammar) {
        try {
            writer.write("\\section{Remove lambda-rules}\n");
            writer.write("\\begin{description}\n");
            writer.write("\t\\item[Before] \\hfill \\\\ \n");
            Printer.printGrammar(grammar,1);
            grammar.modifyName();
            if(GrammarUtil.specialRuleForEmptyWord(grammar)) {
                writer.write("\t\\item[Step 0] add new Symbol $S#§: \\\\ \n");
                Printer.printGrammar(grammar,1);
                grammar.modifyName();
            }
            //first step: calculate the Nullable set
            HashSet<Nonterminal> nullable= GrammarUtil.calculateNullable(grammar);
            writer.write("\t\\item[Step 1] nullable = \\{"+ nullable.stream().map(nt -> nt.getName()).collect(Collectors.joining(", "))+"\\}\n");
            writer.write("\t\\item[Step 2]\n");
            //second step: for every rule with a nullable nonterminal, add that rule without this nonterminal
            GrammarUtil.removeLambdaRules_StepTwo(grammar,nullable);
            GrammarUtil.removeUnneccesaryEpsilons(grammar);
            Printer.printGrammar(grammar,1);
            grammar.modifyName();
            writer.write("\t\\item[Step 3] All lambda-rules are removed and all nonterminals, that do not appear on any right side. \\\\ \n");
            GrammarUtil.removeLambdaRules_StepThree(grammar,true);
            Printer.printGrammar(grammar,1);
            grammar.clearName();
            writer.write("\\end{description}\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void printRemoveLambdaRulesConsole(Grammar grammar) {
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(System.out));
            try {
                if(GrammarUtil.specialRuleForEmptyWord(grammar)) {
                    writer.write("added new symbol S#:\n");
                    writer.flush();
                    Printer.printGrammar(grammar);
                }
                //first step: calculate the Nullable set
                HashSet<Nonterminal> nullable= GrammarUtil.calculateNullable(grammar);
                writer.write("Step 1:\nnullable = {"+ nullable.stream().map(nt -> nt.getName()).collect(Collectors.joining(", "))+"}\n");
                writer.flush();
                writer.write("Step 2:\n");
                writer.flush();
                //second step: for every rule with a nullable nonterminal, add that rule without this nonterminal
                GrammarUtil.removeLambdaRules_StepTwo(grammar,nullable);
                GrammarUtil.removeUnneccesaryEpsilons(grammar);
                Printer.printGrammar(grammar);
                writer.write("Step 3: All lambda-rules are removed and all nonterminals, that do not appear on any right side\n");
                writer.flush();
                GrammarUtil.removeLambdaRules_StepThree(grammar,true);
                Printer.printGrammar(grammar);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    private static void printEliminateUnitRulesLatex(Grammar grammar) {
        try {
            writer.write("\\section{Eliminate unit rules}\n");
            writer.write("\\begin{description}\n");
            writer.write("\t\\item[Before] \n");
            Printer.printGrammar(grammar,1);
            grammar.modifyName();
            HashSet<Node> unitRules=GrammarUtil.removeCircleRules(grammar);
            writer.write("\t\\item[Step 1] remove circles: \\\\ \n");

            Printer.printGrammar(grammar,1);
            grammar.modifyName();
            writer.write("\t\\item[Step 2] number the nonterminals:\n");

            ArrayList<Node> list=GrammarUtil.removeUnitRules(unitRules,grammar);
            writer.write("\t\\begin{align*}\n");

            writer.write(list.stream().map(node -> "\t\t"+node.getName()+ "&: "+ node.getNumber()).collect(joining("\\\\\n")));

            writer.write("\n\t\\end{align*}\n");

            writer.write("\t\\item[Step 3] remove unit rules beginning by the highest number: \\\\ \n");

            Printer.printGrammar(grammar,1);
            grammar.clearName();
            writer.write("\\end{description}\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void printEliminateUnitRulesConsole(Grammar grammar) {
        BufferedWriter writer1=new BufferedWriter(new OutputStreamWriter(System.out));

        try {
            HashSet<Node> unitRules=GrammarUtil.removeCircleRules(grammar);
            writer1.write("Step 1: remove circles\n");
            writer1.flush();
            Printer.printGrammar(grammar);
            writer1.write("Step 2: number the nonterminals\n");
            writer1.flush();
            ArrayList<Node> list=GrammarUtil.removeUnitRules(unitRules,grammar);
            list.stream().forEach(x -> {
                try {
                    writer1.write(x.getName() + ": " + x.getNumber() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer1.write("Step 3: remove unit rules beginning by the highest number\n");
            writer1.flush();
            Printer.printGrammar(grammar);
        } catch (IOException e) {
            e.printStackTrace();
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
            writer.write("\t\\item[Before]\n");

            printGrammar(grammar,1);
            grammar.modifyName();
            writer.write("\t\\item[Step 1] rules in form of $A \\rightarrow a$ are already in chomsky normal form and we keep them.\n");

            writer.write("\t\\item[Step 2] in all other rules replace every appearance of Terminal a through a new Nonterminal $X_a$ and add the rule $X_a \\rightarrow a$.\n");


            GrammarUtil.chomskyNormalForm_StepOne(grammar);
            printGrammar(grammar,1);
            grammar.modifyName();
            writer.write("\t\\item[Step 3] in all rules that contain more than two nonterminals, add a new nonterminal that points to the end of the rule.\n");

            GrammarUtil.chomskyNormalForm_StepTwo(grammar);
            printGrammar(grammar,1);
            grammar.clearName();

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

    private static void printGrammarLatex(Grammar grammar, int x) {
        String s="";
        for(int i=0;i<x;i++) {
            s+="\t";
        }
        final String space=s;
        try {


            ArrayList<String>[] header=getHeader(grammar);
            writer.write(space+"$"+grammar.getName()+"=\\left(\\{");
            writer.write(space+header[0].stream().map(string -> makeToGreek(string)).collect(joining(", ")));

            writer.write("\\},\\;\\{ ");

            writer.write(header[1].stream().collect(joining(", ")));
            writer.write("\\},\\;");

            writer.write(header[2].get(0));

            writer.write(",\\;"+grammar.getRuleSetName()+"\\right)");
            writer.write("$ with\n");
            writer.write(space+"\\begin{align*}\n");

            writer.write(space+"\t"+grammar.getRuleSetName()+"=\\{");


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
                    }).collect(joining(", \\\\ \n"+space)));

            writer.write("\\}\n");
            writer.write("\t\\end{align*}\n");

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

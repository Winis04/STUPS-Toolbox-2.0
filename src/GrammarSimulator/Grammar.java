package GrammarSimulator;


import Print.Printable;
import Print.Printer;

import java.util.*;

import static Print.Printer.makeToGreek;

import static java.util.stream.Collectors.joining;

/**
 * Created by fabian on 06.08.16.
 */
public class Grammar implements Printable {

    /**
     * The grammar's terminal symbols.
     */
    private HashSet<Terminal> terminals;

    /**
     * The grammar's nonterminal symbols.
     */
    private HashSet<Nonterminal> nonterminals;

    /**
     * The grammar's start symbol.
     */
    private Nonterminal startSymbol;
    /**
     * the grammars name (optional)
     */
    private String name=null;
    /**
     * a suffix for the name;
     */
    private String suffix="";

<<<<<<< 450016293caed50ee11b68c31056124c6b74ffff
=======


>>>>>>> on the right way!
    /**
     * The constructor for an empty grammar.
     */
    public Grammar() {
        Terminal terminal = new Terminal("a");
        ArrayList<Symbol> symbolList = new ArrayList(Arrays.asList(terminal));
        this.startSymbol = new Nonterminal("S", new HashSet<>(Arrays.asList(symbolList)));
        this.terminals = new HashSet<>(Arrays.asList(terminal));
        this.nonterminals = new HashSet<>(Arrays.asList(startSymbol));
    }

    /**
     * The constructor for a grammar with a given set of terminals and nonterminals.
     *
     * @param terminals The grammar's set of terminals.
     * @param nonterminals The grammar's set of nonterminals.
     * @param startSymbol The grammar's start symbol.
     */
    public Grammar(HashSet<Terminal> terminals, HashSet<Nonterminal> nonterminals, Nonterminal startSymbol) {
        this.terminals = terminals;
        this.nonterminals = nonterminals;
        this.startSymbol = startSymbol;
    }

    /**
     * deep copy constructor
     * @param old the grammar that should be copied
     */
    public Grammar(Grammar old, boolean newName) {
        this.terminals=new HashSet<>();
        this.nonterminals=new HashSet<>();
        for(Terminal t : old.getTerminals()) { //adds the Terminals
            this.terminals.add(new Terminal(t.getName()));
        }
        for(Nonterminal nt : old.getNonterminals()) { //adds the Nonterminals, but without rules
            this.nonterminals.add(new Nonterminal(nt.getName(),new HashSet<ArrayList<Symbol>>()));
        }
        for(Nonterminal oldNt : old.getNonterminals()) {
            Nonterminal newNt=this.getNonterminal(oldNt.getName()); //the matching Nonterminal in the new grammar
            for(ArrayList<Symbol> list : oldNt.getSymbolLists()) { // copy the lists
                ArrayList<Symbol> tmp=new ArrayList<>();
                for(Symbol symbol : list) {
                    if(symbol instanceof Terminal) {
                        tmp.add(this.getTerminal(symbol.getName()));
                    } else {
                        tmp.add(this.getNonterminal(symbol.getName()));
                    }
                }
                newNt.getSymbolLists().add(tmp);
            }
        }
        this.name=old.getNameWithoutSuffix();
        this.suffix=old.getSuffix();
        if(newName) {
            this.modifyName();
        }
        this.startSymbol=this.getNonterminal(old.getStartSymbol().getName());
    }


    /**
     * Getter-method for {@link #terminals}.
     *
     * @return {@link #terminals}
     */
    public HashSet<Terminal> getTerminals() {
        return terminals;
    }

    /**
     * Getter-method for {@link #nonterminals}.
     *
     * @return {@link #nonterminals}
     */
    public HashSet<Nonterminal> getNonterminals() {
        return nonterminals;
    }

    /**
     * Getter-method for {@link #startSymbol}.
     *
     * @return {@link #startSymbol}
     */
    public Nonterminal getStartSymbol() {
        return startSymbol;
    }

    /**
     * Setter-method for {@link #startSymbol}.
     *
     * @param startSymbol The new value for {@link #startSymbol}.
     */
    public void setStartSymbol(Nonterminal startSymbol) {
        this.startSymbol = startSymbol;
    }
<<<<<<< 450016293caed50ee11b68c31056124c6b74ffff

<<<<<<< 902536a7405d30a6ffbb9aa2b90253ca9144eed0
=======

    private static void printGrammarLatex(Grammar grammar, int x) {
        String s="";
        for(int i=0;i<x;i++) {
            s+="\t";
        }
        final String space=s;


        ArrayList<String>[] header= Printer.getHeader(grammar);
        Printer.print(space+"$"+grammar.getNameWithSuffix()+"=\\left(\\{");
        Printer.print(space+header[0].stream().map(string -> makeToGreek(string)).collect(joining(", ")));

        Printer.print("\\},\\;\\{ ");

        Printer.print(header[1].stream().collect(joining(", ")));
        Printer.print("\\},\\;");

        Printer.print(header[2].get(0));

        Printer.print(",\\;"+grammar.getRuleSetName()+"\\right)");
        Printer.print("$ with\n");
        Printer.print(space+"\\begin{align*}\n");

        Printer.print(space+"\t"+grammar.getRuleSetName()+"=\\{");


        Printer.print(GrammarUtil.getNonterminalsInOrder(grammar).stream().
                map(nonterminal -> {
                    String start="\t"+nonterminal.getName() + " &\\rightarrow ";
                    HashSet<ArrayList<Symbol>> tmp=nonterminal.getSymbolLists();
                    start+=tmp.stream().
                            map(list -> list.stream().
                                    map(symbol -> symbol.getName()).
                                    map(string -> makeToGreek(string)).
                                    collect(joining(""))).
                            collect(joining("\\;|\\;"));
                    return start;
                }).collect(joining(", \\\\ \n"+space)));

        Printer.print("\\}\n");
        Printer.print("\t\\end{align*}\n");


    }
    private static void printGrammarConsole(Grammar grammar) {

        Printer.print(grammar.getNameWithSuffix()+"\n");

        ArrayList<String>[] header=Printer.getHeader(grammar);

        Printer.print("{");
        Printer.print(header[0].stream().collect(joining(", ")));

        Printer.print("; ");

        Printer.print(header[1].stream().collect(joining(", ")));

        Printer.print("; ");
        Printer.print(header[2].get(0));

        Printer.print("}\n\n");


        for(Nonterminal nt : GrammarUtil.getNonterminalsInOrder(grammar)) {
            Printer.print(nt.getName() + " --> ");
            HashSet<ArrayList<Symbol>> tmp=nt.getSymbolLists();
            Printer.print(tmp.stream()
                    .map(list -> list.stream().map(symbol -> symbol.getName()).collect(joining("")))
                    .collect(joining(" | ")));
           // HashSet<ArrayList<String>> tmp=getRulesToNonterminal(grammar,nt);
            //Printer.print(tmp.stream().map(list -> list.stream().collect(joining(""))).collect(joining(" | ")));
            Printer.print("\n");

        }

    }
    @Override
    public void print() {
        switch(Printer.printmode) {
            case NO:
                break;
            case LATEX:
                printGrammarLatex(this,Printer.deepnes);
                break;
            case CONSOLE:
                printGrammarConsole(this);
                break;
        }
    }



>>>>>>> on the right way!
    public Terminal getTerminal(String name) {
        for(Terminal t : this.terminals) {
            if(t.getName().equals(name)) {
                return t;
            }
        }
        return null;
    }
    public Nonterminal getNonterminal(String name) {
        for(Nonterminal nt : this.nonterminals) {
            if(nt.getName().equals(name)) {
                return nt;
            }
        }
        return null;
    }

    public String getNameWithSuffix() {
        if(name==null) {
            return "G"+suffix;
        } else {
            return name+suffix;
        }
    }
    public String getNameWithoutSuffix() {
        if(name==null) {
            return "G";
        } else {
            return name;
        }
    }

    public void setName(String name) {
        this.name = name;
    }
    public void modifyName() {
        this.suffix+="'";
    }
    public void clearName() {
        this.suffix="";
    }

    public String getRuleSetName() {
        return "R"+suffix;
=======
    @Override
    public void print() {

    }

    @Override
    public void setText(String newText) {
        this.help_text=newText;
    }

    @Override
    public String getText() {
        return this.help_text;
>>>>>>> added printable interface
    }

    public String getSuffix() {
        return suffix;
    }
}

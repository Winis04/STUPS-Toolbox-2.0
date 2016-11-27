package GrammarSimulator;


import Main.Storable;
import Print.Printable;
import Print.Printer;

import java.io.BufferedWriter;
import java.util.*;

import static Print.Printer.makeToGreek;

import static java.util.stream.Collectors.joining;

/**
 * Created by fabian on 06.08.16.
 */
public class Grammar implements Printable, Storable {

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

    @Override
    public void printLatex(BufferedWriter writer, String space) {
        ArrayList<String>[] header= GrammarUtil.getHeader(this);
        Printer.print(space+"$"+this.getNameWithSuffix()+"=\\left(\\{",writer);
        Printer.print(space+header[0].stream().map(string -> makeToGreek(string)).collect(joining(", ")),writer);

        Printer.print("\\},\\;\\{ ",writer);

        Printer.print(header[1].stream().collect(joining(", ")),writer);
        Printer.print("\\},\\;",writer);

        Printer.print(header[2].get(0),writer);

        Printer.print(",\\;"+this.getRuleSetName()+"\\right)",writer);
        Printer.print("$ with\n",writer);
        Printer.print(space+"\\begin{align*}\n",writer);

        Printer.print(space+"\t"+this.getRuleSetName()+"=\\{",writer);


        Printer.print(GrammarUtil.getNonterminalsInOrder(this).stream().
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
                }).collect(joining(", \\\\ \n"+space)),writer);

        Printer.print("\\}\n",writer);
        Printer.print(space+"\\end{align*}\n",writer);


    }
    @Override
    public void printConsole(BufferedWriter writer) {

        Printer.print(this.getNameWithSuffix()+"\n",writer);

        ArrayList<String>[] header=GrammarUtil.getHeader(this);

        Printer.print("{",writer);
        Printer.print(header[0].stream().collect(joining(", ")),writer);

        Printer.print("; ",writer);

        Printer.print(header[1].stream().collect(joining(", ")),writer);

        Printer.print("; ",writer);
        Printer.print(header[2].get(0),writer);

        Printer.print("}\n\n",writer);


        for(Nonterminal nt : GrammarUtil.getNonterminalsInOrder(this)) {
            Printer.print(nt.getName() + " --> ",writer);
            HashSet<ArrayList<Symbol>> tmp=nt.getSymbolLists();
            Printer.print(tmp.stream()
                    .map(list -> list.stream().map(symbol -> symbol.getName()).collect(joining("")))
                    .collect(joining(" | ")),writer);
           // HashSet<ArrayList<String>> tmp=getRulesToNonterminal(grammar,nt);
            //Printer.print(tmp.stream().map(list -> list.stream().collect(joining(""))).collect(joining(" | ")));
            Printer.print("\n",writer);

        }

    }


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
        return "R" + suffix;
    }
    public String getSuffix() {
        return suffix;
    }

    @Override
    public Storable deep_copy(Storable old) {
        return new Grammar((Grammar) old, false);
    }
}

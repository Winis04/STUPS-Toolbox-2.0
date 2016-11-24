package GrammarSimulator;


import Print.Printable;

import java.util.*;

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
    public Grammar(Grammar old) {
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

<<<<<<< 902536a7405d30a6ffbb9aa2b90253ca9144eed0
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

    public String getName() {
        if(name==null) {
            return "G"+suffix;
        } else {
            return name+suffix;
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
}

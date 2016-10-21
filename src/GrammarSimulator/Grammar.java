package GrammarSimulator;

import java.util.*;

/**
 * Created by fabian on 06.08.16.
 */
public class Grammar {

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
     * the grammar's null Symbol
     */
    private Terminal nullSymbol;
    /**
     * The constructor for an empty grammar.
     */
    public Grammar() {
        Terminal terminal = new Terminal("a");
        ArrayList<Symbol> symbolList = new ArrayList<Symbol>(Arrays.asList(terminal));
        this.startSymbol = new Nonterminal("S", new HashSet(symbolList));
        this.terminals = new HashSet<>(Arrays.asList(terminal));
        this.nullSymbol=new Terminal("epsilon");
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
        this.nullSymbol=new Terminal("epsilon");
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

    /**
     * Getter-method for {@link #nullSymbol}
     *
     * @return nullSymbol The nullsymbol of this grammar
     */
    public Terminal getNullSymbol() {
        for(Terminal t: terminals) {
            if(t.getName().equals("epsilon")) {
                return t;
            }
        }
        return null;
    }
    public HashSet<ArrayList<Symbol>> getSymbolListsWithoutEmptyRules(Nonterminal nt) {
        HashSet<ArrayList<Symbol>> tmp=nt.getSymbolLists();
        HashSet<ArrayList<Symbol>> res=new HashSet<>();
        for(ArrayList<Symbol> list : tmp) {
            boolean allNull=true;
            for(Symbol sym : list) {
                if(sym.equals(this.getNullSymbol())) {
                    allNull=allNull & true;
                } else {
                    allNull=false;
                }
            }
            if(allNull==false) {
                res.add(list);
            }
        }
        return res;
    }
}

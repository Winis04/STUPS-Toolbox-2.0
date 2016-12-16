package GrammarSimulator;

import Print.Printable;
import Print.Printer;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashSet;

import static Print.Printer.printmode;


/**
 * Created by fabian on 06.08.16.
 */
public class Nonterminal implements Symbol, Printable {

    /**
     * The nonterminal's name.
     */
    private String name;

    /**
     * The set of all the lists of {@link Symbol}s to which this nonterminal points.
     */
    private HashSet<ArrayList<Symbol>> symbolLists;

    /**
     * The constructor.
     *
     * @param name The nonterminal's name.
     * @param symbolLists The set of all the lists of {@link Symbol}s to which this nonterminal points.
     */
    public Nonterminal(String name, HashSet<ArrayList<Symbol>> symbolLists) {
        this.name = name;
        this.symbolLists = symbolLists;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter-method for {@link #symbolLists}.
     *
     * @return {@link #symbolLists}
     */
    public HashSet<ArrayList<Symbol>> getSymbolLists() {
        return symbolLists;
    }

    public void setSymbolLists(HashSet<ArrayList<Symbol>> symbolLists) {
        this.symbolLists = symbolLists;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Nonterminal && ((Nonterminal)o).getName().equals(name);
    }

    @Override
    public void printLatex(BufferedWriter writer, String space) {
        Printer.print(space+this.getName(),writer);
    }

    @Override
    public void printConsole(BufferedWriter writer) {
        Printer.print(this.getName(),writer);
    }
}

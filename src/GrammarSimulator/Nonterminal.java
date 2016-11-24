package GrammarSimulator;

import Print.Printable;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;

import static Print.Printer.printmode;
import static Print.Printer.writer;

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
    public void print() {
        switch (printmode) {
            case NO:
                break;
            case CONSOLE:
                BufferedWriter writer1=new BufferedWriter(new OutputStreamWriter(System.out));
                try {
                    writer1.write(this.getName()+" ");
                    writer1.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case LATEX:
                try {
                    writer.write(this.getName()+" ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}

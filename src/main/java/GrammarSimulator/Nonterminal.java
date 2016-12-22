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

    private boolean isStart;

    /**
     * The nonterminal's name.
     */
    private String name;


    /**
     * The constructor.
     *
     * @param name The nonterminal's name.
     * @param symbolLists The set of all the lists of {@link Symbol}s to which this nonterminal points.
     */
    public Nonterminal(String name) {
        this.name = name;
        this.isStart = false;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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

    public boolean isStart() {
        return  isStart;
    }
    public void markAsStart() {
        this.isStart = true;
    }
}

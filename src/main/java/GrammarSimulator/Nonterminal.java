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
     * The constructor.
     *
     * @param name The nonterminal's name.
     */
    public Nonterminal(String name) {
        this.name = name;
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
    public void printLatex(BufferedWriter writer, String space) {
        Printer.print(space+this.getName(),writer);
    }

    @Override
    public void printConsole(BufferedWriter writer) {
        Printer.print(this.getName(),writer);
    }


    @Override
    public boolean equals(Object o) {
        if(o instanceof Nonterminal) {
            Nonterminal other = (Nonterminal) o;
            if(other.name.equals(this.name)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }
}

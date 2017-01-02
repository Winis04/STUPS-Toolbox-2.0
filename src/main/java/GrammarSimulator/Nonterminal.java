package GrammarSimulator;

import Print.Printable;
import Print.Printer;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashSet;

import static Print.Printer.printmode;


/**
 * Created by fabian on 06.08.16.
 */
public final class Nonterminal implements Symbol, Printable {


    /**
     * The nonterminal's name.
     */
    private final String name;


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
    public void printLatex(BufferedWriter writer, String space) {
        Printer.print(space+this.getName(),writer);
    }

    @Override
    public void printConsole(BufferedWriter writer) {
        Printer.print(this.getName(),writer);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Nonterminal rhs = (Nonterminal) obj;
        return new EqualsBuilder()
                .append(name, rhs.name)
                .isEquals();
    }


    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
                // if deriving: appendSuper(super.hashCode()).
                        append(name).
                        toHashCode();
    }

}

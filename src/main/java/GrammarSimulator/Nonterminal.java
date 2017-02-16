package GrammarSimulator;

import Print.Printable;
import Print.Printer;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.BufferedWriter;
import java.util.Arrays;
import java.util.stream.Collectors;


/**
 * An element of a {@link Grammar}, which has further derivation. It is therefore non-terminal.
 * @author fabian
 * @since 06.08.16
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
        if(!validName(name)) {
            this.name = makeValid(name);
        } else {
            this.name = name;
        }
    }


    @Override
    public String getName() {


        return name;
    }

    @Override
    public String getDisplayName() {
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

    public static boolean validName(String name) {

       return name.matches("[a-zA-Z_](\\w*)");
    }



    public static String makeValid(String name) {

        StringBuilder res = new StringBuilder();
        String start = Character.toString(name.charAt(0));
        if(start.matches("[a-zA-Z_]")) {
            res.append(start);
        } else {
            res.append((int)name.charAt(0));
        }
        name.substring(1).chars().forEach(c -> {
            if(c=='_' || Character.toString((char) c).matches("(\\w)")) {
                res.append((char) c);
            } else {
                res.append(c);
            }
        });
        return res.toString();

    }

    public String nameToLatex() {
        String[] splitted = name.split("_");
        String res;
        if(splitted.length>1) {
            res = splitted[0] + "_";
            res += "{" + splitted[1] + "}";
            if(splitted.length > 2) {
                res += "\\_";
                String[] subarray = new String[splitted.length - 2];
                System.arraycopy(splitted, 2, subarray, 0, splitted.length - 2);
                res+= Arrays.stream(subarray).collect(Collectors.joining("\\_"));
            }

        } else {
            res=name;
        }
        return res;
    }

}

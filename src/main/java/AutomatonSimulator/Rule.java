package AutomatonSimulator;

import Print.Printable;
import Print.Printer;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.joining;

/**
 * A production rule of a {@link Automaton}. Describes a transition of {@link Automaton} from one {@link State}
 * to another {@link State} {@link #goingTo} by reading a input {@link String} {@link #acceptedInputs}.
 * @author fabian
 * @since 21.04.16
 */
public class Rule implements Printable{
    /**
     * The state, to which the rule points.
     */
    private State goingTo;

    private State comingFrom;

    /**
     * The input-strings, that are valid for this rule.
     */
    private final HashSet<String> acceptedInputs;


    /**
     * the automaton is printed from left to right in alphanumeric order and length give the length of the edge
     * for example, 1 if the edge goes to a neighbor.
     */
    private int length=1;

    private int maxLength=1;
    /**
     * The constructor.
     *
     * @param goingTo The state, to which the rule is pointing.
     * @param  comingFrom the state, from which the rule starts
     * @param acceptedInputs The inputs, which this rule accepts.
     */
    public Rule(State comingFrom, State goingTo, Set<String> acceptedInputs) {
        this.comingFrom = comingFrom;
        this.goingTo = goingTo;
        this.acceptedInputs = new HashSet<>(acceptedInputs);
        this.length=1;
    }

    /**
     * Getter-method for {@link #goingTo}.
     *
     * @return {@link #goingTo}
     */
    public State getGoingTo() {
        return goingTo;
    }

    /**
     * Getter-method for {@link #acceptedInputs}.
     *
     * @return {@link #acceptedInputs}
     */
    public HashSet<String> getAcceptedInputs() {
        return acceptedInputs;
    }

    /**
     * Setter-method for {@link #goingTo}.
     *
     * @param goingTo The new value for {@link #goingTo}.
     */
    public void setGoingTo(State goingTo) {
        this.goingTo = goingTo;
    }


    /**
     * the automaton is printed from left to right in alphanumeric order
     * @param space a string of tabs for nice printing
     */
    @Override
    public void printLatex(String space) {
        Printer.print(" edge ");
        if(this.isLoop()) {
            Printer.print("[loop below] ");
        } else {
            int arc;
            if(maxLength==1) {
                arc=45;
            } else {
                arc = (90*length)/maxLength;
            }
            String s=""+arc;
              Printer.print("[bend left="+s+"] ");
        }
        Printer.print("node {"+acceptedInputs.stream().map(Printer::checkIfLatexSpecial).collect(joining(", "))+"}\t(");
        Printer.print(Printer.remove_underscore(goingTo.getName()));
        Printer.print(")\n");
    }

    @Override
    public void printConsole() {
        Printer.print(" --'"+acceptedInputs.stream().collect(joining("', '"))+"'--> ");
        Printer.print(goingTo);

    }

    /**
     * Getter-Method for {@link #length}
     * @return {@link #length}
     */
    public int getLength() {
        return length;
    }

    /**
     * Setter-Method for {@link #length}
     * @param length the new value for {@link #length}
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Setter-method for {@link #maxLength}
     * @param maxLength the new value for {@link #maxLength}
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    private boolean isLoop() {
        return comingFrom.getName().equals(goingTo.getName());
    }
}

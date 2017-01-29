package AutomatonSimulator;

import Print.Printable;
import Print.Printer;

import java.io.BufferedWriter;
import java.util.HashSet;

import static java.util.stream.Collectors.joining;

/**
 * @author fabian
 * @since 21.04.16
 */
public class Rule implements Printable{
    /**
     * The state, to which the rule points.
     */
    private State goingTo;

    /**
     * The input-strings, that are valid for this rule.
     */
    private final HashSet<String> acceptedInputs;

    /**
     * is this rule a loop?
     */
    private boolean isLoop;

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
     * @param acceptedInputs The inputs, which this rule accepts.
     */
    public Rule(State goingTo, HashSet<String> acceptedInputs) {
        this.goingTo = goingTo;
        this.acceptedInputs = acceptedInputs;
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
     * @param writer the output-write
     * @param space a string of tabs for nice printing
     */
    @Override
    public void printLatex(BufferedWriter writer, String space) {
        Printer.print(" edge ",writer);
        if(isLoop) {
            Printer.print("[loop below] ",writer);
        } else {
            int arc;
            if(maxLength==1) {
                arc=45;
            } else {
                arc = (90*length)/maxLength;
            }
            String s=""+arc;
              Printer.print("[bend left="+s+"] ",writer);
        }
        Printer.print("node {"+acceptedInputs.stream().collect(joining(", "))+"}\t(",writer);
        Printer.print(goingTo);
        Printer.println(")",writer);
    }

    @Override
    public void printConsole(BufferedWriter writer) {
        Printer.print(" --'"+acceptedInputs.stream().collect(joining("', '"))+"'--> ",writer);
        Printer.print(goingTo);

    }

    void setLoop(boolean loop) {
        isLoop = loop;
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
}

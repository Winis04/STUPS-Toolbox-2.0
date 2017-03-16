package PushDownAutomatonSimulator;

import Print.Printable;
import Print.Printer;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Describes a production rule of an {@link PushDownAutomaton}.
 */
public class PDARule implements Printable{
    /**
     * the state that rule comes from
     */
    private final State comingFrom;
    /**
     * the current Letter to be read in
     */

    private final InputLetter readIn;
    /**
     * the old top of stack
     */

    private final StackLetter oldToS;
    /**
     * the start this rule points to
     */

    private final State goingTo;
   /**
    * the new ToS. newTos[0] is the topmost element!
    */

    private final List<StackLetter> newToS;


    /**
     * Constructs a rule of a pda.
     * @param comingFrom the old {@link State} of the {@link PushDownAutomaton}
     * @param goingTo the new {@link State} of the {@link PushDownAutomaton}
     * @param readIn the {@link InputLetter} that this rule reads in
     * @param oldToS the {@link StackLetter} on top of the stack
     * @param newToS the {@link StackLetter}s this rule will push onto the stack
s     */
    public PDARule(State comingFrom, State goingTo, InputLetter readIn, StackLetter oldToS, List<StackLetter> newToS) {
        this.comingFrom = comingFrom;
        this.goingTo = goingTo;
        this.readIn = readIn;
        this.oldToS = oldToS;
        this.newToS = newToS;
    }


    /**
     * constructs an empty PDARule
     */
    public PDARule() {
        this.comingFrom = new State("z0");
        this.goingTo = new State("z0");
        this.readIn = new InputLetter("a");
        this.oldToS = new StackLetter("a");
        this.newToS = new ArrayList<>();
    }


    /**
     * Getter-Method for {@link #goingTo}
     * @return {@link #goingTo}
     */
    public State getGoingTo() {
        return goingTo;
    }

    /**
     * Getter-Method for {@link #readIn}
     * @return {@link #readIn}
     */
    public InputLetter getReadIn() {
        return readIn;
    }

    /**
     * Getter-Method for {@link #oldToS}
     * @return {@link #oldToS}
     */
    public StackLetter getOldToS() {
        return oldToS;
    }

    /**
     * Getter-Method for {@link #newToS}
     * @return a unmodifiableList of the elements of {@link #newToS}
     */
    public List<StackLetter> getNewToS() {
        return Collections.unmodifiableList(new ArrayList<>(newToS));
    }

    /**
     * Getter-Method for {@link #comingFrom}
     * @return {@link #comingFrom}
     */
    public State getComingFrom() {
        return comingFrom;
    }


    @Override
    public void printLatex(String space) {
        Printer.print(space+"$"+this.getComingFrom().getName());
        Printer.print(Printer.checkIfLatexSpecial(this.getReadIn().getName()));
        Printer.print(Printer.checkIfLatexSpecial(this.getOldToS().getName()));
        Printer.print(" \\rightarrow ");
        Printer.print(Printer.checkIfLatexSpecial(this.getGoingTo().getName()));
        Printer.print(this.getNewToS().stream().map(t -> Printer.checkIfLatexSpecial(t.getName())).collect(Collectors.joining(""))+"$");
    }


    @Override
    public void printConsole() {
        Printer.print(this.getComingFrom().getName());
        Printer.print(this.getReadIn().getName());
        Printer.print(this.getOldToS().getName());
        Printer.print(" --> ");
        Printer.print(this.getGoingTo().getName());
        Printer.print(this.getNewToS().stream().map(StackLetter::getName).collect(Collectors.joining(""))+"\n");
    }


    /**
     * returns the rule as a String
     * @return a String describing the rule
     */
    public String asString() {
        String s="";
        s += this.getComingFrom().getName();
        s += " " + this.getReadIn().getDisplayName();
        s+=" "+this.getOldToS().getDisplayName();
        s+=" \u2192";
        s+=" "+this.getGoingTo().getName();
        s+=" "+this.getNewToS().stream().map(StackLetter::getDisplayName).collect(Collectors.joining(" "));


        return s;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        PDARule rhs = (PDARule) obj;
        if(this.newToS.size() != rhs.newToS.size()) {
            return false;
        }
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(comingFrom, rhs.comingFrom)
                .append(readIn,rhs.readIn)
                .append(oldToS,rhs.oldToS)
                .append(goingTo,rhs.goingTo);
        for(int i=0;i<newToS.size();i++) {
            builder.append(this.newToS.get(i),rhs.newToS.get(i));
        }
        return builder.isEquals();
    }



    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(17,31);
        builder.append(comingFrom)
                .append(readIn)
                .append(oldToS)
                .append(goingTo);
        newToS.forEach(builder::append);
        return builder.toHashCode();
    }
}

package PushDownAutomatonSimulator;

import Print.Printable;
import Print.Printer;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


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

    public PDARule(State comingFrom, State goingTo, InputLetter readIn, StackLetter oldToS, List<StackLetter> newToS) {
        this.comingFrom = comingFrom;
        this.goingTo = goingTo;
        this.readIn = readIn;
        this.oldToS = oldToS;
        this.newToS = newToS;
    }

    public PDARule() {
        this.comingFrom = new State("z0");
        this.goingTo = new State("z0");
        this.readIn = new InputLetter("a");
        this.oldToS = new StackLetter("a");
        this.newToS = new ArrayList<>();
    }

    public State getGoingTo() {
        return goingTo;
    }

    public InputLetter getReadIn() {
        return readIn;
    }

    public StackLetter getOldToS() {
        return oldToS;
    }

    public List<StackLetter> getNewToS() {
        return Collections.unmodifiableList(new ArrayList<>(newToS));
    }

    public State getComingFrom() {
        return comingFrom;
    }

    @Override
    public void printLatex(BufferedWriter writer, String space) {
        Printer.print(space+"$"+this.getComingFrom().getName(),writer);
        Printer.print(Printer.checkIfLatexSpecial(this.getReadIn().getName()),writer);
        Printer.print(Printer.checkIfLatexSpecial(this.getOldToS().getName()),writer);
        Printer.print(" \\rightarrow ",writer);
        Printer.print(Printer.checkIfLatexSpecial(this.getGoingTo().getName()),writer);
        Printer.print(this.getNewToS().stream().map(t -> Printer.checkIfLatexSpecial(t.getName())).collect(Collectors.joining(""))+"$",writer);
    }

    @Override
    public void printConsole(BufferedWriter writer) {
        Printer.print(this.getComingFrom().getName(),writer);
        Printer.print(this.getReadIn().getName(),writer);
        Printer.print(this.getOldToS().getName(),writer);
        Printer.print(" --> ",writer);
        Printer.print(this.getGoingTo().getName(),writer);
        Printer.print(this.getNewToS().stream().map(StackLetter::getName).collect(Collectors.joining(""))+"\n",writer);
    }

    public String asString(boolean onGUI) {
        String s="";
        if(onGUI) {
            s += this.getComingFrom().getName();
            s += " " + this.getReadIn().getDisplayName();
            s+=" "+this.getOldToS().getDisplayName();
            s+=" \u2192";
            s+=" "+this.getGoingTo().getName();
            s+=" "+this.getNewToS().stream().map(StackLetter::getDisplayName).collect(Collectors.joining(""));
        } else {
            s += this.getComingFrom().getName();
            s += " " + this.getReadIn().getName();
            s+=" "+this.getOldToS().getName();
            s+=" \u2192";
            s+=" "+this.getGoingTo().getName();
            s+=" "+this.getNewToS().stream().map(StackLetter::getName).collect(Collectors.joining(""));
        }


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

package PushDownAutomatonSimulator;

import Print.Printable;
import Print.Printer;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by Isabel on 29.10.2016.
 */
public class Rule implements Printable{
    /**
     * the state that rule comes from
     */

    private State comingFrom;
    /**
     * the current Letter to be read in
     */
    private InputLetter readIn;
    /**
     * the old top of stack
     */
    private StackLetter oldToS;
    /**
     * the start this rule points to
     */
    private State goingTo;
   /**
    * the new ToS. newTos[0] is the topmost element
    */
    private ArrayList<StackLetter> newToS;

    public Rule(State goingTo, InputLetter readIn, StackLetter oldToS, ArrayList<StackLetter> newToS) {
        this.goingTo = goingTo;
        this.readIn = readIn;
        this.oldToS = oldToS;
        this.newToS = newToS;
    }

    public Rule(State comingFrom, InputLetter readIn, StackLetter oldToS, State goingTo, ArrayList<StackLetter> newToS) {
        this.comingFrom = comingFrom;
        this.readIn = readIn;
        this.oldToS = oldToS;
        this.goingTo = goingTo;
        this.newToS = newToS;
    }

    public Rule() {
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

    public ArrayList<StackLetter> getNewToS() {
        return newToS;
    }

    public State getComingFrom() {
        return comingFrom;
    }

    public void setComingFrom(State comingFrom) {
        this.comingFrom = comingFrom;
    }

    public void setReadIn(InputLetter readIn) {
        this.readIn = readIn;
    }

    public void setOldToS(StackLetter oldToS) {
        this.oldToS=oldToS;
    }

    public void setGoingTo(State goingTo) {
        this.goingTo = goingTo;
    }

    public void setNewToS(ArrayList<StackLetter> newToS) {
        this.newToS = newToS;
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
        Printer.print(this.getNewToS().stream().map(t -> t.getName()).collect(Collectors.joining(""))+"\n",writer);
    }

    public String asString() {
        String s=this.getComingFrom().getName();
        s+=this.getReadIn().getName();
        s+=this.getOldToS().getName();
        s+=" --> ";
        s+=this.getGoingTo().getName();
        s+=this.getNewToS().stream().map(t -> t.getName()).collect(Collectors.joining(""));
        return s;
    }
}

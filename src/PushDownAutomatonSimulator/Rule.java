package PushDownAutomatonSimulator;

import java.util.ArrayList;

/**
 * Created by Isabel on 29.10.2016.
 */
public class Rule {
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
    private StackLetter OldToS;
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
        OldToS = oldToS;
        this.newToS = newToS;
    }

    public State getGoingTo() {
        return goingTo;
    }

    public InputLetter getReadIn() {
        return readIn;
    }

    public StackLetter getOldToS() {
        return OldToS;
    }

    public ArrayList<StackLetter> getNewToS() {
        return newToS;
    }

    public State getComingFrom() {
        return comingFrom;
    }
}

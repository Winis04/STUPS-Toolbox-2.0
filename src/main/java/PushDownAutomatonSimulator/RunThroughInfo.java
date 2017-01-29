package PushDownAutomatonSimulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * this class describes a single step of a run through a {@link PushDownAutomaton}.
 * @author Isabel
 * @since 04.01.2017
 */
public class RunThroughInfo {

    /** the current stack **/
    private final Stack<StackLetter> stack;
    /** the current {@link InputLetter} **/
    private final List<InputLetter> input;
    /** the current {@link State} of the {@link PushDownAutomaton} **/
    private final State currentState;
    /** the previous configuration **/
    private final RunThroughInfo previous;
    /** the {@link PushDownAutomaton} this RunThroughInfo belongs to **/
    private final PushDownAutomaton myPDA;

    RunThroughInfo(Stack<StackLetter> stack, List<InputLetter> input, State currentState, RunThroughInfo previous, PushDownAutomaton pda) {
        this.stack = stack;
        this.input = input;
        this.currentState = currentState;
        this.previous = previous;
        this.myPDA=pda;
    }


    /**
     * Getter-Method for Stack. Works with immutable, because a copy is returned.
     * @return a {@link Stack} which is a copy of the stack of this object.
     */
    public Stack<StackLetter> getStack() {
        List<StackLetter> list = Collections.unmodifiableList(stack);
        Stack<StackLetter> res = new Stack<>();
        list.forEach(res::push);
        return res;
    }



    /**
     * Getter-Method for {@link #input}
     * @return a {@link List} of {@link InputLetter}s
     */
    public List<InputLetter> getInput() {

        return Collections.unmodifiableList(new ArrayList<>(input));
    }

    /**
     * Getter-Method for {@link #currentState}
     * @return the {@link #currentState} of the RunTrough.
     */
    public State getCurrentState() {
        return currentState;
    }

    /**
     * Getter-Method for {@link #previous}
     * @return the {@link #previous} RunThroughInfo
     */
    public RunThroughInfo getPrevious() {
        return previous;
    }

    PushDownAutomaton getMyPDA() {
        return myPDA;
    }

}

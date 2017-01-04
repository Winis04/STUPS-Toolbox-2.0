package PushDownAutomatonSimulator;

import java.util.List;
import java.util.Stack;

/**
 * Created by Isabel on 04.01.2017.
 */
public class RunThroughInfo {

    private final Stack<StackLetter> stack;
    private final List<InputLetter> input;
    private final State currentState;
    private final RunThroughInfo previous;
    private final PushDownAutomaton myPDA;

    public RunThroughInfo(Stack<StackLetter> stack, List<InputLetter> input, State currentState, RunThroughInfo previous, PushDownAutomaton pda) {
        this.stack = stack;
        this.input = input;
        this.currentState = currentState;
        this.previous = previous;
        this.myPDA=pda;
    }

    public Stack<StackLetter> getStack() {
        return stack;
    }

    public List<InputLetter> getInput() {
        return input;
    }

    public State getCurrentState() {
        return currentState;
    }

    public RunThroughInfo getPrevious() {
        return previous;
    }

    public PushDownAutomaton getMyPDA() {
        return myPDA;
    }
}

package PushDownAutomatonSimulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import static java.util.stream.Collectors.joining;

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

    public String asString() {
        String cS = currentState.getName();
        String in = InputLetter.NULLSYMBOL.getName();
        if(!input.isEmpty()) {
            in = input.stream().map(s -> s.getName()).collect(joining(""));
        }
        String st = StackLetter.NULLSYMBOL.getName();
        if(!stack.isEmpty()) {
            ArrayList<StackLetter> list = new ArrayList<>();
            list.addAll(stack);
            Collections.reverse(list);
            st = list.stream().map(s -> s.getName()).collect(joining(""));
        }
        return "("+cS+", "+in+", "+st+")";
    }
}

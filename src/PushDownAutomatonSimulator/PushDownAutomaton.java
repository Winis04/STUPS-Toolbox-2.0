package PushDownAutomatonSimulator;

import java.util.HashSet;
import java.util.Stack;

/**
 * Created by Isabel on 29.10.2016.
 */
public class PushDownAutomaton {
    /**
     * contains the states of this PDA
     */
    private HashSet<State> states;
    /**
     * the input alphabet
     */
    private HashSet<InputLetter> inputAlphabet;
    /**
     * the stack alphabet;
     */
    private HashSet<StackLetter> stackAlphabet;
    /**
     * the PDA's start state
     */
    private State startState;
    /**
     * the initial stack letter
     */
    private StackLetter initalStackLetter;
    /**
     * the rules of this pda
     */
    private HashSet<Rule> rules;

    public PushDownAutomaton(HashSet<State> states, HashSet<InputLetter> inputAlphabet, HashSet<StackLetter> stackAlphabet, State startState, StackLetter initalStackLetter, HashSet<Rule> rules) {
        this.states = states;
        this.inputAlphabet = inputAlphabet;
        this.stackAlphabet = stackAlphabet;
        this.startState = startState;
        this.initalStackLetter = initalStackLetter;
        this.rules = rules;
    }

    public PushDownAutomaton() {
    }

    public HashSet<State> getStates() {
        return states;
    }

    public HashSet<InputLetter> getInputAlphabet() {
        return inputAlphabet;
    }

    public HashSet<StackLetter> getStackAlphabet() {
        return stackAlphabet;
    }

    public State getStartState() {
        return startState;
    }

    public StackLetter getInitalStackLetter() {
        return initalStackLetter;
    }
}

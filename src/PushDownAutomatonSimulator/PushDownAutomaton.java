package PushDownAutomatonSimulator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
    private ArrayList<Rule> rules;
    /**
     * the stack;
     */
    private Stack<StackLetter> stack;
    /**
     * the current state of this automaton
     */
    private State currentState;
    /**
     * the current Input
     */
    private ArrayList<InputLetter> currentInput;
    public PushDownAutomaton(HashSet<State> states, HashSet<InputLetter> inputAlphabet, HashSet<StackLetter> stackAlphabet, State startState, StackLetter initalStackLetter, ArrayList<Rule> rules) {
        this.states = states;
        this.inputAlphabet = inputAlphabet;
        this.stackAlphabet = stackAlphabet;
        this.startState = startState;
        this.initalStackLetter = initalStackLetter;
        this.rules = rules;
        this.stack=new Stack<>();
        this.stack.add(initalStackLetter);
        this.currentInput=new ArrayList<>();
        this.currentState=startState;
    }

    public PushDownAutomaton() {

        this.states=new HashSet<>();
        this.inputAlphabet=new HashSet<>();
        this.stackAlphabet=new HashSet<>();
        this.rules=new ArrayList<>();
        this.stack=new Stack<>();
        this.currentInput=new ArrayList<>();
        this.currentState=startState;
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

    public ArrayList<Rule> getRules() {
        return rules;
    }

    public StackLetter getInitalStackLetter() {
        return initalStackLetter;
    }

    public void setStates(HashSet<State> states) {
        this.states = states;
    }

    public void setInputAlphabet(HashSet<InputLetter> inputAlphabet) {
        this.inputAlphabet = inputAlphabet;
    }

    public void setStackAlphabet(HashSet<StackLetter> stackAlphabet) {
        this.stackAlphabet = stackAlphabet;
    }

    public void setStartState(State startState) {
        this.startState = startState;
    }

    public void setInitalStackLetter(StackLetter initalStackLetter) {
        if(stack.isEmpty()) {
            stack.add(initalStackLetter);
        }
        this.initalStackLetter = initalStackLetter;
    }

    public void setRules(ArrayList<Rule> rules) {
        this.rules = rules;
    }

    public Stack<StackLetter> getStack() {
        return stack;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public ArrayList<InputLetter> getCurrentInput() {
        return currentInput;
    }

    public void setCurrentInput(ArrayList<InputLetter> currentInput) {
        this.currentInput = currentInput;
    }
}

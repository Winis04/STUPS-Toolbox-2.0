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
        this.states=new HashSet<>();
        this.inputAlphabet=new HashSet<>();
        this.stackAlphabet=new HashSet<>();
        this.rules=new HashSet<>();
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

    public HashSet<Rule> getRules() {
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
        this.initalStackLetter = initalStackLetter;
    }

    public void setRules(HashSet<Rule> rules) {
        this.rules = rules;
    }
    public boolean addToStackAlphabet(StackLetter st) {
        if(!this.stackAlphabet.stream().anyMatch(letter -> letter.getName().equals(st.getName()))) {
            stackAlphabet.add(st);
            return true;
        } else {
            return false;
        }

    }
    public boolean addToInputAlphabet(InputLetter ip) {
        if(!this.inputAlphabet.stream().anyMatch(letter -> letter.getName().equals(ip.getName()))) {
            inputAlphabet.add(ip);
            return true;
        } else {
            return false;
        }
    }
    public StackLetter getStackLetter(String s) {
        for(StackLetter st : this.stackAlphabet) {
            if(st.getName().equals(s)) {
                return st;
            }
        }
        return null;
    }
    public InputLetter getInputLetter(String s) {
        for(InputLetter ip : this.inputAlphabet) {
            if(ip.getName().equals(s)) {
                return ip;
            }
        }
        return null;
    }
    public State getStateWithName(String name) {
        for(State s : this.states) {
            if(s.getName().equals(name)) {
                return s;
            }
        }
        return null;
    }
}

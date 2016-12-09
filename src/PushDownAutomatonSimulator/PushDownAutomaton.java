package PushDownAutomatonSimulator;

import Console.Storable;
import Print.Printable;

import java.io.BufferedWriter;
import java.io.File;
import java.util.*;

import static java.util.stream.Collectors.joining;

/**
 * Created by Isabel on 29.10.2016.
 */
public class PushDownAutomaton implements Printable, Storable{
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

    public PushDownAutomaton(HashSet<State> states, HashSet<InputLetter> inputAlphabet, HashSet<StackLetter> stackAlphabet, State startState, StackLetter initalStackLetter) {
        this.states = states;
        this.inputAlphabet = inputAlphabet;
        this.stackAlphabet = stackAlphabet;
        this.startState = startState;
        this.initalStackLetter = initalStackLetter;
        this.stack=new Stack<>();
        this.stack.add(initalStackLetter);
        this.currentInput=new ArrayList<>();
        this.currentState=startState;

    }

    public PushDownAutomaton() {

        this.states=new HashSet<>();
        this.inputAlphabet=new HashSet<>();
        this.stackAlphabet=new HashSet<>();
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


    @Override
    public Storable deep_copy() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public void printToSave(String path) {

    }

    @Override
    public Storable restoreFromFile(File file) {
        return null;
    }

    @Override
    public void printLatex(BufferedWriter writer, String space) {

    }

    @Override
    public void printConsole(BufferedWriter writer) {
        System.out.println("States: ");
        System.out.println(this.getStates().stream().map(state -> state.getName()).collect(joining(", ")));
        System.out.println("Start State");
        System.out.println(this.getStartState().getName());
        System.out.println("Input Alphabet: ");
        System.out.println(this.getInputAlphabet().stream().map(letter -> letter.getName()).collect(joining(", ")));
        System.out.println("Stack Alphabet: ");
        System.out.println(this.getStackAlphabet().stream().map(letter -> letter.getName()).collect(joining(", ")));
        System.out.println("initial stack symbol:");
        System.out.println(this.getInitalStackLetter().getName());
        System.out.println("rules:");
        this.getStates().stream().forEach(state -> {
            if(state.getRules()!=null && !state.getRules().isEmpty()) {
                state.getRules().stream().forEach(rule -> {
                    System.out.printf("%s, %s, %s --> ",rule.getComingFrom().getName(),rule.getReadIn().getName(),rule.getOldToS().getName());
                    System.out.printf("%s, %s\n",rule.getGoingTo().getName(),rule.getNewToS().stream().map(sym -> sym.getName()).collect(joining(", ")));
                });
            }
        });

    }
}

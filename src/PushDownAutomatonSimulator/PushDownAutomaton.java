package PushDownAutomatonSimulator;

import Console.Storable;
import Print.Printable;
import Print.Printer;
import PushDownAutomatonParser.lexer.LexerException;
import PushDownAutomatonParser.parser.ParserException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.util.stream.Collectors.joining;

/**
 * Created by Isabel on 29.10.2016.
 */
public class PushDownAutomaton implements Printable, Storable{
    /**
     * contains the states of this PDA
     */
    private HashMap<String, State> states;
    /**
     * the input alphabet
     */
    private HashMap<String, InputLetter> inputAlphabet;
    /**
     * the stack alphabet;
     */
    private HashMap<String, StackLetter> stackAlphabet;
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

    /**
     * the name of the pda
     */
    private String name="P";
    private ArrayList<Rule> rules = new ArrayList<>();
    public PushDownAutomaton(HashMap<String, State> states, HashMap<String, InputLetter> inputAlphabet, HashMap<String, StackLetter> stackAlphabet, State startState, StackLetter initalStackLetter) {
        this.states = states;
        this.inputAlphabet = inputAlphabet;
        this.stackAlphabet = stackAlphabet;
        this.startState = startState;
        this.initalStackLetter = initalStackLetter;
        this.stack=new Stack<>();
        this.stack.add(initalStackLetter);
        this.currentInput=new ArrayList<>();
        this.currentState=startState;
        this.states.values().stream().forEach(state -> state.getRules().stream().forEach(rule -> rules.add(rule)));

    }


    public PushDownAutomaton(PushDownAutomaton pda) {
        /** copy the states **/
        HashMap<String, State> stateNEW = new HashMap<>();
        pda.getStates().keySet().stream().forEach(key -> {
            //create new state
            stateNEW.put(key, new State(pda.getStates().get(key).isStart(),key));
        });
        this.states=stateNEW;
        /** copy the input alphabet **/
        HashMap<String, InputLetter> inputAlphabetNEW = new HashMap<>();
        pda.getInputAlphabet().keySet().stream().forEach(key -> {
            inputAlphabetNEW.put(key,new InputLetter(key));
        });
        this.inputAlphabet = inputAlphabetNEW;
        /** copy the stack alphabet **/
        HashMap<String, StackLetter> stackAlphabetNEW = new HashMap<>();
        pda.getStackAlphabet().keySet().stream().forEach(key ->{
            stackAlphabetNEW.put(key,new StackLetter(key));
        });
        this.stackAlphabet = stackAlphabetNEW;
        /** copy the start state **/
        this.startState = this.states.get(pda.getStartState().getName());
        /** copy the initial stack letter **/
        this.initalStackLetter = this.stackAlphabet.get(pda.getInitalStackLetter().getName());
        /** stack and current state **/
        this.stack = new Stack<>();
        this.currentInput = new ArrayList<>();
        this.currentState = startState;
        /** copy the rules **/
        pda.getStates().keySet().stream().forEach(key -> {
            State old = pda.getStates().get(key);
            State current = this.states.get(key);
            old.getRules().stream().forEach(rule -> {
                Rule tmp = new Rule();
                tmp.setComingFrom(current);
                tmp.setGoingTo(this.states.get(rule.getGoingTo().getName()));
                tmp.setOldToS(this.stackAlphabet.get(rule.getOldToS().getName()));
                tmp.setReadIn(this.inputAlphabet.get(rule.getReadIn().getName()));
                ArrayList<StackLetter> list = new ArrayList<StackLetter>();
                rule.getNewToS().stream().forEach(stackLetter -> {
                    list.add(this.stackAlphabet.get(stackLetter.getName()));
                });
                tmp.setNewToS(list);
                current.getRules().add(tmp);
            });
        });
        this.states.values().stream().forEach(state -> state.getRules().stream().forEach(rule -> rules.add(rule)));
    }
    public PushDownAutomaton() {

        this.states=new HashMap<>();
        this.inputAlphabet=new HashMap<>();
        this.stackAlphabet=new HashMap<>();
        this.stack=new Stack<>();
        this.currentInput=new ArrayList<>();
        this.currentState=startState;
    }

    @Override
    public Storable deep_copy() {
        return new PushDownAutomaton(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name=name;
    }


    @Override
    public void printToSave(String path) {
        PushDownAutomatonUtil.save(this,path);
    }

    @Override
    public Storable restoreFromFile(File file) {
        try {
            return PushDownAutomatonUtil.parse(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LexerException e) {
            e.printStackTrace();
        } catch (ParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void printLatex(BufferedWriter writer, String space) {
        Printer.print(space+"\\begin{table}[h!]\n",writer);
        Printer.print(space+"\t\\centering\n",writer);
        Printer.print(space+"\t\\caption{PDA}\n",writer);
        String s="|lcl|lcl|";
        Printer.print(space+"\t\\begin{tabular}{"+s+"}\n",writer);
        //1 & 2 & 3\\
        //\hline
        int ruleNumber=this.rules.size();
        int half = ruleNumber/2;
        if(2*half < ruleNumber) {
            half++;
        }
        Rule current;
        for(int i=0;i<ruleNumber;i+=2) {
            Printer.print(space+"\t\t\\hline\n",writer);
            current = this.rules.get(i);
            Printer.print(space+"\t\t $",writer);
            Printer.print(Printer.checkIfLatexSpecial(current.getComingFrom().getName()),writer);
            Printer.print(Printer.checkIfLatexSpecial(current.getReadIn().getName()),writer);
            Printer.print(Printer.checkIfLatexSpecial(current.getOldToS().getName()),writer);
            Printer.print("$ & $ \\rightarrow $ & $",writer);
            Printer.print(Printer.checkIfLatexSpecial(current.getGoingTo().getName()),writer);
            Printer.print(current.getNewToS().stream().map(t -> Printer.checkIfLatexSpecial(t.getName())).collect(joining("")),writer);

            if(i+1<ruleNumber) {
                Printer.print("$ & $",writer);
                current = this.rules.get(i + 1);
                Printer.print(Printer.checkIfLatexSpecial(current.getComingFrom().getName()), writer);
                Printer.print(Printer.checkIfLatexSpecial(current.getReadIn().getName()), writer);
                Printer.print(Printer.checkIfLatexSpecial(current.getOldToS().getName()), writer);
                Printer.print("$ & $ \\rightarrow $ & $", writer);
                Printer.print(Printer.checkIfLatexSpecial(current.getGoingTo().getName()), writer);
                Printer.print(current.getNewToS().stream().map(t -> Printer.checkIfLatexSpecial(t.getName())).collect(joining("")), writer);
                Printer.print("$ \\\\\n",writer);
            } else {
                Printer.print("$ & & &\\\\\n");
            }
        }
        Printer.print(space+"\t\t\\hline\n",writer);
        /** begin table **/

        Printer.print(space+"\t\\end{tabular}\n",writer);
        Printer.print(space+"\\end{table}\n\n",writer);

    }

    @Override
    public void printConsole(BufferedWriter writer) {
        Printer.print(this.name+"\n",writer);
        Printer.print("States: \n",writer);
        Printer.print(this.getStates().values().stream().map(state -> state.getName()).collect(joining(", "))+"\n",writer);
        Printer.print("Start State"+"\n",writer);
        Printer.print(this.getStartState().getName()+"\n",writer);
        Printer.print("Input Alphabet: "+"\n",writer);
        Printer.print(this.getInputAlphabet().values().stream().map(letter -> letter.getName()).collect(joining(", "))+"\n",writer);
        Printer.print("Stack Alphabet: "+"\n",writer);
        Printer.print(this.getStackAlphabet().values().stream().map(letter -> letter.getName()).collect(joining(", "))+"\n",writer);
        Printer.print("initial stack symbol:"+"\n",writer);
        Printer.print(this.getInitalStackLetter().getName()+"\n",writer);
        Printer.print("rules:"+"\n",writer);
        this.getStates().values().stream().forEach(state -> {
            if(state.getRules()!=null && !state.getRules().isEmpty()) {
                state.getRules().stream().forEach(rule -> {
                    Printer.print(rule.getComingFrom().getName()+", ",writer);
                    Printer.print(rule.getReadIn().getName()+", ",writer);
                    Printer.print(rule.getOldToS().getName()+" --> ",writer);
                    Printer.print(rule.getGoingTo().getName()+", ",writer);
                    Printer.print(rule.getNewToS().stream().map(sym -> sym.getName()).collect(joining(", "))+"\n",writer);
                });
            }
        });

    }

    /** GETTER AND SETTER **/
    public HashMap<String, State> getStates() {
        return states;
    }

    public HashMap<String, InputLetter> getInputAlphabet() {
        return inputAlphabet;
    }

    public HashMap<String, StackLetter> getStackAlphabet() {
        return stackAlphabet;
    }

    public State getStartState() {
        return startState;
    }


    public StackLetter getInitalStackLetter() {
        return initalStackLetter;
    }

    public void setStates(HashMap<String, State> states) {
        this.states = states;
    }

    public void setInputAlphabet(HashMap<String, InputLetter> inputAlphabet) {
        this.inputAlphabet = inputAlphabet;
    }

    public void setStackAlphabet(HashMap<String, StackLetter> stackAlphabet) {
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


}

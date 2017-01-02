package PushDownAutomatonSimulator;

import Main.Storable;
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
    private final HashSet<State> states;
    /**
     * the input alphabet
     */
    private final HashSet<InputLetter> inputAlphabet;
    /**
     * the stack alphabet;
     */
    private final HashSet<StackLetter> stackAlphabet;
    /**
     * the PDA's start state
     */
    private final State startState;
    /**
     * the initial stack letter
     */
    private final StackLetter initalStackLetter;

    /**
     * the name of the pda
     */
    private final String name;
    private final PushDownAutomaton previousPDA;
    private final List<Rule> rules;


    /** the following field are mutable, but they also play no rule regarding equal and hashCode
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


    public PushDownAutomaton(HashSet<State> states, HashSet<InputLetter> inputAlphabet, HashSet<StackLetter> stackAlphabet, State startState, StackLetter initalStackLetter, List<Rule> rules, String name, PushDownAutomaton previousPDA) {
        this.states = states;
        this.inputAlphabet = inputAlphabet;
        this.stackAlphabet = stackAlphabet;
        this.startState = startState;
        this.initalStackLetter = initalStackLetter;

        this.rules = rules;
        this.name = name;
        this.previousPDA = previousPDA;
        // mutable:
        this.stack=new Stack<>();
        this.stack.add(initalStackLetter);
        this.currentInput=new ArrayList<>();
        this.currentState=startState;

    }

    public PushDownAutomaton() {
        this.states=new HashSet<>();
        this.inputAlphabet=new HashSet<>();
        this.stackAlphabet=new HashSet<>();
        this.startState = new State("a");
        this.initalStackLetter = new StackLetter("p");
        this.stack=new Stack<>();
        this.currentInput=new ArrayList<>();
        this.currentState = startState;
        this.rules = new ArrayList<>();
        this.name="G";
        this.previousPDA = null;

    }

    @Override
    public Storable deep_copy() {
        return new PushDownAutomaton(this.states,this.inputAlphabet, this.stackAlphabet, this.startState, this.initalStackLetter, this.rules, this.name, this.previousPDA);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Storable otherName(String name) {
        return new PushDownAutomaton(this.states,this.inputAlphabet,this.stackAlphabet,this.startState,this.initalStackLetter,this.rules,name,this.previousPDA);
    }



    @Override
    public void printToSave(String path) {
        PushDownAutomatonUtil.save(this,path);
    }

    @Override
    public Storable restoreFromFile(File file) {
        try {
            return PushDownAutomatonUtil.parse(file);
        } catch (IOException | LexerException | ParserException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Storable getPreviousVersion() {
        return previousPDA;
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
        Printer.print(this.states.stream().map(State::getName).collect(joining(", "))+"\n",writer);
        Printer.print("Start State"+"\n",writer);
        Printer.print(this.getStartState().getName()+"\n",writer);
        Printer.print("Input Alphabet: "+"\n",writer);
        Printer.print(this.inputAlphabet.stream().map(InputLetter::getName).collect(joining(", "))+"\n",writer);
        Printer.print("Stack Alphabet: "+"\n",writer);
        Printer.print(this.stackAlphabet.stream().map(StackLetter::getName).collect(joining(", "))+"\n",writer);
        Printer.print("initial stack symbol:"+"\n",writer);
        Printer.print(this.initalStackLetter.getName()+"\n",writer);
        Printer.print("rules:"+"\n",writer);
        this.states.forEach(state -> {
            if (state.getRules() != null && !state.getRules().isEmpty()) {
                state.getRules().forEach(rule -> {
                    Printer.print(rule.getComingFrom().getName() + ", ", writer);
                    Printer.print(rule.getReadIn().getName() + ", ", writer);
                    Printer.print(rule.getOldToS().getName() + " --> ", writer);
                    Printer.print(rule.getGoingTo().getName() + ", ", writer);
                    Printer.print(rule.getNewToS().stream().map(StackLetter::getName).collect(joining(", ")) + "\n", writer);
                });
            }
        });

    }

    /** GETTER AND SETTER **/
    public Set<State> getStates() {
        return Collections.unmodifiableSet(new HashSet<>(states));
    }

    public Set<InputLetter> getInputAlphabet() {
        return Collections.unmodifiableSet(new HashSet<>(inputAlphabet));
    }

    public Set<StackLetter> getStackAlphabet() {
        return Collections.unmodifiableSet(new HashSet<>(stackAlphabet));
    }

    public State getStartState() {
        return startState;
    }


    public StackLetter getInitalStackLetter() {
        return initalStackLetter;
    }

    public List<Rule> getRules() {
        return Collections.unmodifiableList(new ArrayList<>(rules));
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

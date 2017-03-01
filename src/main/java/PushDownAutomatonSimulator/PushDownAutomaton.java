package PushDownAutomatonSimulator;

import Main.Storable;
import Print.Printable;
import Print.Printer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;




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

    private final StackLetter initialStackLetter;

    /**
     * the name of the pda
     */

    private final String name;

    /** the previous PDA **/

    private final PushDownAutomaton previousPDA;

    /** the rules **/

    private final List<PDARule> rules;

    /**
     * the current state of this automaton
     */

    private final State currentState;




    public PushDownAutomaton(State startState, StackLetter initialStackLetter, List<PDARule> rules, String name, PushDownAutomaton previousPDA) {
        this.states = new HashSet<>(rules.stream().map(PDARule::getComingFrom).collect(Collectors.toSet()));
        states.addAll(new HashSet<>(rules.stream().map(PDARule::getGoingTo).collect(Collectors.toSet())));
        states.add(startState);
        this.inputAlphabet = new HashSet<>();
        rules.stream().map(PDARule::getReadIn).forEach(inputAlphabet::add);
        this.stackAlphabet = new HashSet<>();
        rules.stream().map(PDARule::getOldToS).forEach(stackAlphabet::add);
        rules.stream().map(PDARule::getNewToS).forEach(list -> list.forEach(stackAlphabet::add));
        this.startState = startState;
        this.initialStackLetter = initialStackLetter;
        //unnecessary
        stackAlphabet.add(initialStackLetter);
        this.rules = rules;
        this.name = name;
        this.previousPDA = previousPDA;
        // mutable:
        this.currentState=startState;


    }
    public PushDownAutomaton(Set<InputLetter> input, Set<StackLetter> stack,State startState, StackLetter initialStackLetter, List<PDARule> rules, String name, PushDownAutomaton previousPDA) {
        this.states = new HashSet<>(rules.stream().map(PDARule::getComingFrom).collect(Collectors.toSet()));
        states.addAll(new HashSet<>(rules.stream().map(PDARule::getGoingTo).collect(Collectors.toSet())));
        states.add(startState);
        this.inputAlphabet = new HashSet<>(input);
        rules.stream().map(PDARule::getReadIn).forEach(inputAlphabet::add);
        this.stackAlphabet = new HashSet<>(stack);
        rules.stream().map(PDARule::getOldToS).forEach(stackAlphabet::add);
        rules.stream().map(PDARule::getNewToS).forEach(list -> list.forEach(stackAlphabet::add));
        this.startState = startState;
        this.initialStackLetter = initialStackLetter;
        //unnecessary
        stackAlphabet.add(initialStackLetter);
        this.rules = rules;
        this.name = name;
        this.previousPDA = previousPDA;
        // mutable:
        this.currentState=startState;


    }


    public PushDownAutomaton() {
        this.states=new HashSet<>();
        this.inputAlphabet=new HashSet<>();
        this.stackAlphabet=new HashSet<>();
        this.startState = new State("a");
        this.initialStackLetter = new StackLetter("p");


        this.currentState = startState;

        this.rules = new ArrayList<>();
        List<StackLetter> tmp = new ArrayList<>();
        tmp.add(new StackLetter("epsilon"));
        this.rules.add(new PDARule(startState,startState,new InputLetter("b"), initialStackLetter,tmp));
        this.name="G";
        this.previousPDA = null;


    }


    @Override
    public Storable deep_copy() {
        return new PushDownAutomaton(this.startState, this.initialStackLetter, this.rules, this.name, this.previousPDA);
    }


    @Override
    public String getName() {
        return name;
    }


    @Override
    public Storable otherName(String name) {
        return new PushDownAutomaton(this.startState,this.initialStackLetter,this.rules, name,this.previousPDA);
    }




    @Override
    public void printToSave(String path) throws IOException {
        PushDownAutomatonUtil.save(this,path);
    }


    @Override
    public Storable restoreFromFile(File file) throws Exception {

        return PushDownAutomatonUtil.parse(file);

    }



    @Override
    public Storable getPreviousVersion() {
        return previousPDA;
    }


    @Override
    public void savePreviousVersion() {

    }


    @Override
    public void printLatex(String space) {
        String in = "\\{ "+inputAlphabet.stream()
                .filter(ia -> !ia.equals(InputLetter.NULLSYMBOL))
                .map(InputLetter::getName)
                .map(Printer::checkIfLatexSpecial).collect(joining(", "))+" \\}";
        String stack = "\\{ "+stackAlphabet.stream()
                .filter(sa -> !sa.equals(StackLetter.NULLSYMBOL))
                .map(StackLetter::getName)
                .map(Printer::checkIfLatexSpecial).collect(joining(", "))+" \\}";
        String stateNames = "\\{ "+states.stream()
                .map(State::getName)
                .map(Printer::checkIfLatexSpecial).collect(joining(", "))+" \\}";
        String start = Printer.checkIfLatexSpecial(startState.getName());
        String init = Printer.checkIfLatexSpecial(initialStackLetter.getName());
        String modName;
        if(this.getName().contains("_")) {
            String[] splitted = this.getName().split("_");
            String[] subArray = new String[splitted.length-1];
            System.arraycopy(splitted, 1, subArray, 0, splitted.length - 1);
            modName=splitted[0]+"_";
            modName+= Arrays.stream(subArray).map(s -> "{"+s).collect(joining("_"));
            for (String aSubarray : subArray) {
                modName += "}";
            }
        } else {
            modName = this.getName();
        }
        Printer.print(space+"$"+modName+"=\\left( "+in+", "+stack+", "+ stateNames +", \\delta , "+start+", "+init+" \\right)$ ");

        if(!this.rules.isEmpty()) {
            Printer.print(space+"with \n");
            Printer.print(space + "\\begin{table}[h!]\n");
            Printer.print(space + "\t\\centering\n");
            Printer.print(space + "\t\\caption{$\\delta$}\n");
            String s = "|lcl|lcl|";
            Printer.print(space + "\t\\begin{tabular}{" + s + "}\n");
            //1 & 2 & 3\\
            //\hline
            int ruleNumber = this.rules.size();

            PDARule current;
            for (int i = 0; i < ruleNumber; i += 2) {
                Printer.print(space + "\t\t\\hline\n");
                current = this.rules.get(i);
                Printer.print(space + "\t\t $");
                Printer.print(Printer.checkIfLatexSpecial(current.getComingFrom().getName()) + " ");
                Printer.print(Printer.checkIfLatexSpecial(current.getReadIn().getName()) + " ");
                Printer.print(Printer.checkIfLatexSpecial(current.getOldToS().getName()) + " ");
                Printer.print("$ & $ \\rightarrow $ & $");
                Printer.print(Printer.checkIfLatexSpecial(current.getGoingTo().getName()) + " ");
                Printer.print(current.getNewToS().stream().map(t -> Printer.checkIfLatexSpecial(t.getName())).collect(joining(" ")));

                if (i + 1 < ruleNumber) {
                    Printer.print("$ & $");
                    current = this.rules.get(i + 1);
                    Printer.print(Printer.checkIfLatexSpecial(current.getComingFrom().getName()) + " ");
                    Printer.print(Printer.checkIfLatexSpecial(current.getReadIn().getName()) + " ");
                    Printer.print(Printer.checkIfLatexSpecial(current.getOldToS().getName()) + " ");
                    Printer.print("$ & $ \\rightarrow $ & $");
                    Printer.print(Printer.checkIfLatexSpecial(current.getGoingTo().getName()) + " ");
                    Printer.print(current.getNewToS().stream().map(t -> Printer.checkIfLatexSpecial(t.getName())).collect(joining(" ")));
                    Printer.print("$ \\\\\n");
                } else {
                    Printer.print("$ & & &\\\\\n");
                }
            }
            Printer.print(space + "\t\t\\hline\n");
        /* begin table **/

            Printer.print(space + "\t\\end{tabular}\n");
            Printer.print(space + "\\end{table}\n\n");
        }

    }


    @Override
    public void printConsole() {
        Printer.print(this.name+"\n");
        Printer.print("States: \n");
        Printer.print(this.states.stream().map(State::getName).collect(joining(", "))+"\n");
        Printer.print("Start State"+"\n");
        Printer.print(this.getStartState().getName()+"\n");
        Printer.print("Input Alphabet: "+"\n");
        Printer.print(this.inputAlphabet.stream().map(InputLetter::getName).collect(joining(", "))+"\n");
        Printer.print("Stack Alphabet: "+"\n");
        Printer.print(this.stackAlphabet.stream().map(StackLetter::getName).collect(joining(", "))+"\n");
        Printer.print("initial stack symbol:"+"\n");
        Printer.print(this.initialStackLetter.getName()+"\n");
        Printer.print("rules:"+"\n");
        this.rules.forEach(rule -> {
            Printer.print(rule.getComingFrom().getName() + ", ");
            Printer.print(rule.getReadIn().getName() + ", ");
            Printer.print(rule.getOldToS().getName() + " --> ");
            Printer.print(rule.getGoingTo().getName() + ", ");
            Printer.print(rule.getNewToS().stream().map(StackLetter::getName).collect(joining(", ")) + "\n");
        });
    }



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



    public StackLetter getInitialStackLetter() {
        return initialStackLetter;
    }


    public List<PDARule> getRules() {
        return Collections.unmodifiableList(new ArrayList<>(rules));
    }



    public State getCurrentState() {
        return currentState;
    }




}

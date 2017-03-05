package AutomatonSimulator;

import Main.Storable;
import Print.Printable;
import Print.Printer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

/**
 * the model for formal automatons.
 * @author fabian
 * @since 21.04.16
 */
public class Automaton implements Printable, Storable {

    private final String name;
    /**
     * Contains all the states of the automaton.
     */
    private final HashSet<State> states;

    /**
     * The initial state.
     */
    private State startState;

    /**
     * Contains all the input string, that are used for the transitions of this automaton.
     */
    private final HashSet<String> allInputs;

    /**
     * true, if there are cyclic epsilon-transitions in this automaton.
     */
    private boolean epsilonCycle = false;

    private Automaton previousAutomaton;

    /**
     * The constructor for an automaton with a given set of states and rules.
     *
     * @param states A set of states.
     * @param startState The initial states.
     * @param allInputs A set of input-strings.
     * @param name name of the automaton
     */
    public Automaton(HashSet<State> states, State startState, HashSet<String> allInputs, String name) {
        this.states = states;
        this.startState = startState;
        this.allInputs = allInputs;
        removeEpsilonCycles();
        if(epsilonCycle) {
            System.out.println("Epsilon-cycles in this automaton have been removed automatically!");
        }
        this.name=name;

    }

    /**
     * The constructor for an empty automaton.
     * It just contains an initial state called "z0".
     */
    public Automaton() {
        this.startState = new State("z0", true, true, new HashSet<>());
        this.states = new HashSet<>();
        this.states.add(this.startState);
        this.allInputs = new HashSet<>();
        this.allInputs.add("a");
        Rule rule = new Rule(startState,startState,allInputs);
        startState.getRules().add(rule);
        this.name="A";

    }

    /**
     * The constructor, that copies the passed automaton
     * @param oldAutomaton the automaton that is copied
     */
    public Automaton(Automaton oldAutomaton) {
        this.states= new HashSet<>();

        //adds the states, but without Rules.
        oldAutomaton.states.forEach(state -> this.states.add(new State(state.getName(), state.isStart(), state.isFinal(), new HashSet<>())));
        this.states.forEach(state -> {
            if (state.isStart()) {
                this.startState = state;
            }
        });
        // for every old State, find the matching new State and copy the rules, so the new rules will
        // have references to the new states.
        oldAutomaton.states.forEach(oldState -> {
            State newState = this.getState(oldState.getName());
            oldState.getRules().forEach(rule -> newState.getRules().add(new Rule(newState,this.getState(rule.getGoingTo().getName()), rule.getAcceptedInputs())));
        });
        this.allInputs=oldAutomaton.allInputs;
        this.previousAutomaton = (Automaton) oldAutomaton.getPreviousVersion();
        this.name=oldAutomaton.getName();
    }

    /**
     * Gets rid of any cyclic epsilon-transitions.
     */
    private void removeEpsilonCycles() {

        //First, check if there any states, that have a rule going to themselves with the empty word as input.
        //If so, these rules can just be removed.
        for(State state : states) {
            //Make a local copy of the rules, so we don't get a "ConcurrentModificationException", while removing rules.
            HashSet<Rule> localRules = new HashSet<>(state.getRules());
            localRules.stream().filter(rule -> (rule.getAcceptedInputs().contains("lambda") || rule.getAcceptedInputs().contains("epsilon")) && rule.getGoingTo().equals(state)).forEach(rule -> state.getRules().remove(rule));
        }

        //For every state, call a method that checks if this state is part of a epsilon-cycle and if so, gets rid of this cycle.
        HashSet<State> localStates = new HashSet<>(states);
        for(State state : localStates) {
            removeEpsilonCycles(state, new HashSet<>(), false);
        }
    }

    /**
     * Checks, if we can get from a given state into a cycle of epsilon states.
     * If so, the cycle will be removed.
     *
     * @param startState The state, from which we start.
     * @param visitedStates Contains all states, that have already been visited. It is empty initially.
     * @param cycleFound true, if an epsilon-cycle has been found.
     */
    private void removeEpsilonCycles(State startState, HashSet<State> visitedStates, boolean cycleFound) {
        //Get all states, that are reachable with an empty word as input, from startState and startState to visitedStates.
        HashSet<State> nextStates = AutomatonUtil.nextStates(startState, "lambda");
        visitedStates.add(startState);


        for(State state : nextStates) {
            //If visitedStates contains the current state, we obviously found an epsilon-cycle.
            if(visitedStates.contains(state)) {
                HashSet<State> cycleStates = new HashSet<>();
                //If cycleFound is false, we call this method recursively with cycleFound = true and new empty HashSet for visitedStates.
                //That way, visitedStates will be calculated a second time.
                //This is necessary, because right now, there might still be states in it, that are not part of the cycle
                //Example: z0 --epsilon-> z1; z1 --epsilon--> z2; z2 --epsilon--> z1;
                //After that the code for removing the cycle gets executed.
                if(!cycleFound) {
                    removeEpsilonCycles(state, cycleStates, true);
                    epsilonCycle = true;
                }

                //This code removes an epsilon-cycle by merging all states that the cycle contains into one.
                if(cycleStates.size() > 0) {
                    String name = "";
                    boolean isStart = false;
                    boolean isFinal = false;
                    HashSet<Rule> rules = new HashSet<>();
                    //Iterate over all states of the cycle and merge their names into one name.
                    //Also, determine if the merged state is a start state and/or a final state.
                    for (State cycleState : cycleStates) {
                        name += cycleState.getName();
                        if (cycleState.isStart()) {
                            isStart = true;
                        }
                        if (cycleState.isFinal()) {
                            isFinal = true;
                        }
                        rules.addAll(cycleState.getRules().stream().filter(rule -> !cycleStates.contains(rule.getGoingTo())).collect(Collectors.toList()));
                    }

                    State newState = new State(name, isStart, isFinal, rules);

                    //Let all rules that pointed to one of the old states point to the newly created state.
                    for (State state1 : states) {
                        state1.getRules().stream().filter(rule -> cycleStates.contains(rule.getGoingTo())).forEach(rule -> rule.setGoingTo(newState));
                    }

                    //Add the new state to the automaton and remove the old states.
                    states.add(newState);
                    states.removeAll(cycleStates);
                    if (isStart) {
                        this.startState = newState;
                    }
                }
                break;
            }
            removeEpsilonCycles(state, visitedStates, cycleFound);
        }
    }

    /**
     * Getter-method for {@link #states}.
     *
     * @return {@link #states}
     */
    public HashSet<State> getStates() {
        return states;
    }

    /**
     * Getter-method for {@link #startState}.
     *
     * @return {@link #startState}
     */
    public State getStartState() {
        return startState;
    }

    /**
     * Getter-method for {@link #allInputs}.
     *
     * @return {@link #allInputs}
     */
    public HashSet<String> getAllInputs() {
        return allInputs;
    }

    /**
     * Setter-method for {@link #startState}.
     *
     * @param startState The new value for {@link #startState}.
     */
    public void setStartState(State startState) {
        this.startState = startState;
    }


    @Override
    public void printLatex(String space) {
        AutomatonUtil.fillLengthsOfEdges(this);
        ArrayList<State> statesSorted=AutomatonUtil.getStatesSorted(this);
        Printer.print("\\begin{tikzpicture}[shorten >=1pt,node distance=2cm,on grid,auto]\n\n");
        String space1=space+"\t";
        String before="";
        for(State state : statesSorted) {
            Printer.print(space1+"\\node[state");
            if(state.isStart()) {
                Printer.print(",initial");
            }
            if(state.isFinal()) {
                Printer.print(",accepting");
            }
            Printer.print("]\t (");
            Printer.print(state);
            Printer.print(")\t");
            //position:
            if(!before.isEmpty()) {
                Printer.print("[right of="+before+"]\t");
                before=state.getName();
            } else {
                before=statesSorted.get(0).getName();
            }
            Printer.print("\t{");
            Printer.print(state);
            Printer.print("};\n");
          //  \node[state,initial] (q_0)   {$q_0$};
        }
        Printer.print( space+"\\path[->]\n");

        for(State s : statesSorted) {
            Printer.print(space+"(");
            Printer.print(s);
            Printer.print(") \t");
            s.getRules().forEach(Printer::print);
        }
        Printer.print(";\n");
        Printer.print("\\end{tikzpicture}\n\n\n");

    }
    /**
     * Prints a given automaton to stdout.
     */
    @Override
    public void printConsole() {
        ArrayList<State> statesSorted=AutomatonUtil.getStatesSorted(this);
        Printer.print("{");
        this.printAllStates_Console(statesSorted,false);
        Printer.print("; ");
        Printer.print(this.startState.getName()+"; ");
        this.printAllStates_Console(statesSorted,true);
        Printer.print("}\n");

        for(State s : statesSorted) {
            for(Rule r : s.getRules()) {
                Printer.print(s);
                Printer.print(r);
                Printer.print("\n");
            }
        }

        Printer.print("\n");
        Printer.print(AutomatonUtil.getStatesSorted(this).stream().map(State::getName).collect(joining(", "))+"\n");
    }

    private void printAllStates_Console(ArrayList<State> statesSorted, boolean onlyFinal) {
        ArrayList<State> states;
        if(onlyFinal) {
            states=(ArrayList<State>) statesSorted.stream().filter(State::isFinal).collect(Collectors.toList());
        } else {
            states=(ArrayList<State>) statesSorted.stream().collect(Collectors.toList());
        }
        Iterator<State> iterator=states.iterator();
        while(iterator.hasNext()) {
            State state=iterator.next();
            Printer.print(state);
            if(iterator.hasNext()) {
                Printer.print(", ");
            }
        }
    }

    @Override
    public Storable deep_copy() {
        return new Automaton(this);
    }

    private State getState(String name) {
        final State[] res = {null};
        states.forEach(state -> {
            if (state.getName().equals(name)) {
                res[0] = state;
            }
        });
        return res[0];
    }

    public String getName() {
        return name;
    }


    @Override
    public Storable otherName(String name) {

        return new Automaton(new HashSet<>(this.states),new State(this.startState.getName(),this.startState.isStart(),this.startState.isFinal(),this.startState.getRules()),new HashSet<>(this.allInputs),name);
    }


    @Override
    public void printToSave(String path) throws IOException {
        AutomatonUtil.save(this,path);
    }

    @Override
    public Storable restoreFromFile(File file) throws Exception {
        return AutomatonUtil.parse(file);

    }

    public void setPreviousAutomaton(Automaton a) {
        this.previousAutomaton = a;
    }

    @Override
    public Storable getPreviousVersion() {
        return previousAutomaton;
    }
}

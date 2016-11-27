package AutomatonSimulator;

import AutomatonParser.Visitor;
import AutomatonParser.lexer.Lexer;
import AutomatonParser.lexer.LexerException;
import AutomatonParser.node.Start;
import AutomatonParser.parser.Parser;
import AutomatonParser.parser.ParserException;
import javafx.util.Pair;

import java.io.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * Created by fabian on 20.06.16.
 */
public class AutomatonUtil {

    /**
     * This variable is used by {@link #checkInput(ArrayList, State)} to determine,
     * whether the given input is accepted, or not.
     */
    private static boolean accepted;

    /**
     * This variable is used to store the recursion depth of {@link #checkInput(ArrayList, State)}.
     */
    private static int recDepth;

    /**
     * This stack stores the way that {@link #checkInput(ArrayList, State)} has taken.
     */
    private static Stack<String> takenWay;

    /**
     * A backup of {@link #takenWay}. It will store the longest way that {@link #checkInput(ArrayList, State)} can take
     * if the given input string is not accepted by the automaton.
     */
    private static ArrayList<String> longestWay;

    /**
     * This stack is used by {@link #checkInput(ArrayList, State)} to temporarily store {@link #recDepth},
     * whenever it comes to branch of possible rules to take.
     */
    private static Stack<Integer> branches;

    /**
     * States, that have a rule with the empty word as accepted input pointing to them, will be added to this list.
     * This is needed by {@link #checkInput(ArrayList, State)}.
     */
    private static HashSet<State> emptyWordStates = new HashSet<>();


    /******************************************************************************************************************
     * ---------------------------------------------------------------------------------------------------------------*
     * -                      First some private methods. Scroll down to see the public methods.                     -*
     * ---------------------------------------------------------------------------------------------------------------*
     ******************************************************************************************************************/

    /**
     * Returns an ArrayList that contains all states, that are somehow reachable from the start state,
     * in order of their appearance in the automaton's rules.
     *
     * @param comingFrom This should be the automaton' start state initially.
     * @param states Holds the added states. Should be empty initally.
     * @return The ArrayList.
     */
    private static ArrayList<State> getStatesInOrder(State comingFrom, ArrayList<State> states) {
        HashSet<State> nextStates = new HashSet<>();

        //Determine, whether comingFrom's name should be added, or not.
        //If yes, add it to the list.
        if (!states.contains(comingFrom)) {
            states.add(comingFrom);
        }

        //Do the same for all states, that comingFrom points to.
        for(Rule rule : comingFrom.getRules()) {
            State currentState = rule.getGoingTo();
            if (!states.contains(currentState)) {
                states.add(currentState);
                nextStates.add(currentState);
            }
        }

        //Call this method recursively with all states in nextStates as comingFrom.
        for(State goingTo : nextStates) {
            getStatesInOrder(goingTo, states);
        }

        return states;
    }

    /**
     * Writes all of the automaton's states through a given BufferedWriter.
     *
     * @param writer The BufferedWriter.
     * @param automaton The automaton.
     * @param onlyFinalStates If true, only final states will be written.
     */
    private static void writeStates(BufferedWriter writer, Automaton automaton, boolean onlyFinalStates) {
        //Get all states in order of their appearance in the automaton's rules.
        ArrayList<State> states = getStatesInOrder(automaton);
        boolean comma = false;

        //Write the states.
        try {
            for(State currentState : states) {
                if (!onlyFinalStates || onlyFinalStates && currentState.isFinal()) {
                    if(comma) {
                        writer.write(", ");
                    }
                    comma = true;
                    writer.write(currentState.getName());
                }
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes all of the automaton's rules through a given BufferedWriter.
     *
     * @param writer The BufferedWriter.
     * @param automaton The automaton.
     */
    private static void writeRules(BufferedWriter writer, Automaton automaton) {
        //Get all states in order of their appearance in the automatons rules.
        ArrayList<State> states = getStatesInOrder(automaton);
        boolean comma = false;

        //Iterate through all rules of every state and print their rules.
        try {
            for(State state : states) {
                for (Rule rule : state.getRules()) {
                    writer.write(state.getName() + " --");

                    Iterator it = rule.getAcceptedInputs().iterator();
                    while (it.hasNext()) {
                        writer.write("'" + it.next() + "'");
                        if (it.hasNext()) {
                            writer.write(",");
                        }
                    }

                    writer.write("--> " + rule.getGoingTo().getName() + "\n");
                }
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes all of the automaton's rules through a given BufferedWriter using the GraphViz-Syntax.
     *
     * @param writer The BufferedWriter.
     * @param comingFrom Should be the automaton's start state initially.
     * @param visitedStates Should be empty initially.
     */
    private static void writeRulesGraphViz(BufferedWriter writer, State comingFrom, HashSet<State> visitedStates) {
        HashSet<State> nextStates = new HashSet<>();

        try {
            if (!visitedStates.contains(comingFrom)) {
                for (Rule rule : comingFrom.getRules()) {
                    //Write the rule.
                    writer.write(comingFrom.getName() + " -> " + rule.getGoingTo().getName() + "[label = \"");

                    //Label the rule with its' accepted inputs.
                    Iterator it = rule.getAcceptedInputs().iterator();
                    while (it.hasNext()) {
                        String currentInput = (String)it.next();
                        if(currentInput.equals("epsilon") || currentInput.equals("lambda")) {
                            writer.write("&" + currentInput + ";");
                        } else {
                            writer.write(currentInput);
                        }
                        if (it.hasNext()) {
                            writer.write(",");
                        }
                    }
                    //End the line.
                    writer.write("\"];\n");

                    nextStates.add(rule.getGoingTo());
                }
            }

            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        visitedStates.add(comingFrom);

        //Call this method recursively with all states in visitedStates as comingFrom.
        for (State goingTo : nextStates) {
            writeRulesGraphViz(writer, goingTo, visitedStates);
        }
    }

    /**
     * Calculates all states, that can be reached from a given state, with just the empty word.
     *
     * @param currentState The given state.
     * @param returnSet Should be empty initially.
     * @return A HashSet, that contains the reachable states.
     */
    private static HashSet<State> epsilonReachableStates(State currentState, HashSet<State> returnSet) {
        //Get all states, that can be reached with just one epsilon-, or lambda-transition.
        HashSet<State> nextStates = nextStates(currentState, "lambda");
        returnSet.addAll(nextStates);
        
        //Now go through every of these states and call this method recursively.
        //This way, states that can be reached with more than one epsilon-, or lambda-transition
        //will also be added to returnSet.
        for(State state : nextStates) {
            epsilonReachableStates(state, returnSet);
        }
        return returnSet;
    }
    
    /**
     * Calculates the which states can be reached from a given state with only one input symbol.
     * The result is stored in a HashMap, that maps every possible input to all states,
     * that are reachable with this input from the current state.
     * This determines the transitions for the new epsilon-free automaton.
     *
     * @param automaton The automaton.
     * @param currentState The state.
     * @return A HashMap containing the result.
     */
    private static HashMap<String, HashSet<State>> epsilonFreeTransitions(Automaton automaton, State currentState) {
        //Get all states, that can be reached from currentState with just the empty word.
        HashSet<State> epsilonStates = epsilonReachableStates(currentState, new HashSet<>());
        epsilonStates.add(currentState);

        //Initialize the HashMap with empty HashSets.
        HashMap<String, HashSet<State>> transitionMap = new HashMap<>();
        for(String input : automaton.getAllInputs()) {
            if(!input.equals("lambda") && !input.equals("epsilon")) {
                transitionMap.put(input, new HashSet<>());
            }
        }

        //Go through state and map each input to a set of states, that can be reached from that state.
        for(State state : epsilonStates) {
            for(String input : automaton.getAllInputs()) {
                if(!input.equals("lambda") && !input.equals("epsilon")) {
                    for(Rule rule : state.getRules()) {
                        if (rule.getAcceptedInputs().contains(input)) {
                            transitionMap.get(input).add(rule.getGoingTo());
                            transitionMap.get(input).addAll(epsilonReachableStates(rule.getGoingTo(), new HashSet<>()));
                        }
                    }
                }
            }
        }

        return transitionMap;
    }

    /**
     * Checks, if a given input is accepted by the automaton.
     *
     * @param input The input.
     * @param startState The automaton's start state.
     * @return True, if the input is accepted.
     */
    private static boolean checkInput(ArrayList<String> input, State startState) {
        //Increase the recursion depth and create a local copy of input.
        recDepth++;
        ArrayList<String> localInput = new ArrayList<>(input);

        //Check, if the input-list is empty.
        if(localInput.isEmpty()) {
            //If yes, and the given state is a final state, return true.
            if(startState.isFinal()) {
                recDepth--;
                return true;
            } else {
                //If it is not a final state, try to go on with a lambda transition.
                if(!nextStates(startState, "lambda").isEmpty()) {
                    localInput.add("lambda");
                } else {
                    //If this fails, pop everything from takenWay since the last branch and return false.
                    if(branches.size() > 0) {
                        for(int i = branches.peek(); i < recDepth; i++) {
                            if(takenWay.size() > longestWay.size()) {
                                longestWay = new ArrayList<>(takenWay);
                            }
                            takenWay.pop();
                        }
                    }
                    recDepth--;
                    return false;
                }
            }
        }

        //If the input-list is not empty, take the first entry, and get all states,
        //that are reachable from the current state with this input.
        String currentInput = localInput.get(0);
        HashSet<State> currentStates = nextStates(startState, currentInput);

        //If there is more than one state to go to, push the current recDepth to the branches-stack.
        //This way, we know how many entries we need to remove from takenWay, if we took the wrong way.
        if(currentStates.size() > 1) {
            branches.push(recDepth);
        }

        //If there is at least one state to go to, remove the first entry from the input list.
        if(!currentStates.isEmpty()) {
            localInput.remove(0);
        }
        else {
            //If there is no state to go to, pop everything from takenWay since the last branch and return false.
            if (branches.size() > 0) {
                for (int i = branches.peek(); i < recDepth; i++) {
                    if(takenWay.size() > longestWay.size()) {
                        longestWay = new ArrayList<>(takenWay);
                    }
                    takenWay.pop();
                }
            }
            recDepth--;
            return false;
        }

        //Go through all possible next states and call this method recursively with them as a parameter.
        for(State currentState : currentStates) {
            if(accepted) {
                break;
            }

            //If we can reach the next state with a lambda-transition, and this transition does not point to the current state itself,
            //we can put the removed input back to the front of our input-list and call this method recursively.
            if(emptyWordStates.contains(currentState) && !currentState.equals(startState) && !currentInput.equals("lambda") && !currentInput.equals("epsilon")) {
                ArrayList<String> newLocalInput = new ArrayList<>(Arrays.asList(currentInput));
                newLocalInput.addAll(localInput);
                takenWay.push(startState.getName() + " --lambda--> " + currentState.getName());
                accepted = checkInput(newLocalInput, currentState);
            } else {
                //Else, we can just call this method recursively.
                takenWay.push(startState.getName() + " --" + currentInput + "--> " + currentState.getName());
                accepted = checkInput(localInput, currentState);
            }
        }

        if(currentStates.size() > 1 && !accepted) {
            branches.pop();
            if(takenWay.size() > 0) {
                if(takenWay.size() > longestWay.size()) {
                    longestWay = new ArrayList<>(takenWay);
                }
                takenWay.pop();
            }
        }

        recDepth--;
        return accepted;
    }

    private static HashMap<Pair<State, State>, Boolean> getMinimizeMap(Automaton automaton) {
        HashMap<Pair<State, State>, Boolean> statePairs = new HashMap<>();
        for(State state1 : automaton.getStates()) {
            for(State state2 : automaton.getStates()) {
                if(!state1.equals(state2) && !statePairs.keySet().contains(new Pair<>(state2, state1))) {
                    statePairs.put(new Pair<>(state1, state2), false);
                }
            }
        }

        for(Pair<State, State> statePair : statePairs.keySet()) {
            if(statePair.getKey().isFinal() && !statePair.getValue().isFinal() || !statePair.getKey().isFinal() && statePair.getValue().isFinal()) {
                statePairs.put(statePair, true);
            }
        }

        boolean changed = true;
        while(changed) {
            changed = false;
            for(Pair<State, State> statePair : statePairs.keySet()) {
                if(!statePairs.get(statePair)) {
                    for(String input : automaton.getAllInputs()) {
                        Pair<State, State> transitivePair;
                        try {
                            State transitiveState1 = null;
                            State transitiveState2 = null;
                            for(State state : nextStates(statePair.getKey(), input)) {
                                transitiveState1 = state;
                            }
                            for(State state : nextStates(statePair.getValue(), input)) {
                                transitiveState2 = state;
                            }
                            transitivePair = new Pair<>(transitiveState1, transitiveState2);
                            if(!transitivePair.getKey().equals(transitivePair.getValue())) {
                                if (!statePairs.keySet().contains(transitivePair)) {
                                    transitivePair = new Pair<>(transitivePair.getValue(), transitivePair.getKey());
                                }
                                if (!statePairs.get(statePair) && statePairs.get(transitivePair)) {
                                    statePairs.put(statePair, true);
                                    changed = true;
                                }
                            }
                        } catch (NullPointerException e) {
                        }
                    }
                }
            }
        }

        return statePairs;
    }

    /******************************************************************************************************************
     * ---------------------------------------------------------------------------------------------------------------*
     * -                                The public methods follow after this comment.                                -*
     * ---------------------------------------------------------------------------------------------------------------*
     ******************************************************************************************************************/

    /**
     * Takes an input-string and parses it into an automaton. Typically the input-string comes from a file,
     * that has been loaded completely into that string.
     *
     * @param fileInput The input-string.
     * @return The automaton.
     * @throws ParserException {@link ParserException}.
     * @throws LexerException {@link LexerException}.
     * @throws IOException {@link IOException}.
     */
    public static Automaton parse(String fileInput) throws ParserException, LexerException, IOException {
        StringReader reader = new StringReader(fileInput);
        PushbackReader r = new PushbackReader(reader, 100);
        Lexer l = new Lexer(r);
        Parser parser = new Parser(l);
        Start start = parser.parse();
        Visitor visitor = new Visitor();
        start.apply(visitor);
        return setIsLoop(visitor.getAutomaton());
    }

    private static Automaton setIsLoop(Automaton automaton) {
        automaton.getStates().stream().forEach(state -> {
            state.getRules().stream().forEach(rule -> {
                if(rule.getGoingTo().getName().equals(state.getName())) {
                    rule.setLoop(true);
                } else {
                    rule.setLoop(false);
                }
            });
        });
        return automaton;
    }
    /**
     * Returns an ArrayList that contains all states in order of their appearance in the automaton's rules.
     *
     * @param automaton The automaton.
     * @return The ArrayList.
     */
    public static ArrayList<State> getStatesInOrder(Automaton automaton) {
        ArrayList<State> states = getStatesInOrder(automaton.getStartState(), new ArrayList<>());
        HashSet<State> missingStates = new HashSet<>(automaton.getStates());
        missingStates.removeAll(states);
        states.addAll(missingStates);
        return states;
    }

    /**
     * Prints a given automaton to stdout.
     * It does the same as {@link #save(Automaton, String)}, but uses System.out instead of a filename for the BufferedWriter.
     *
     * @param automaton The automaton.
     */
    public static void print(Automaton automaton) {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

        try {
            writer.write("{");

            writeStates(writer, automaton, false);
            writer.write("; ");

            writer.write(automaton.getStartState().getName() + "; ");

            writeStates(writer, automaton, true);
            writer.write("}\n");
            writer.newLine();

            writeRules(writer, automaton);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes a given automaton to a file, so it can be loaded again later.
     *
     * @param automaton The automaton.
     * @param fileName The filename.
     */
    public static void save(Automaton automaton, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

            writer.write("{");

            writeStates(writer, automaton, false);
            writer.write("; ");

            writer.write(automaton.getStartState().getName() + "; ");

            writeStates(writer, automaton, true);

            writer.write("}\n\n");

            writeRules(writer, automaton);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves a given automaton to a .dot-file.
     *
     * @param automaton The automaton.
     * @param fileName The filename.
     */
    public static void toGraphViz(Automaton automaton, String fileName) {
        try {
            //Write the header.
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write("digraph Automaton {\n");
            writer.write("graph[rankdir=LR];\n");

            //Write the automaton's rules.
            writeRulesGraphViz(writer, automaton.getStartState(), new HashSet<>());

            //Mark the start state with an arrow pointing towards it.
            writer.write("start [shape = none];\n");
            writer.write("start -> " + automaton.getStartState().getName() + "\n");

            //Mark all final states with double circles.
            for(State state : automaton.getStates()) {
                if(state.isFinal()) {
                    writer.write(state.getName() + "[shape = doublecircle];");
                    writer.newLine();
                }
            }

            //Close the file.
            writer.write("}");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculates all states, that are reachable from a given state with just one transition, for a certain input.
     *
     * @param currentState The state.
     * @param input The input.
     * @return A HashSet, that contains the reachable states.
     */
    public static HashSet<State> nextStates(State currentState, String input) {
        HashSet<State> nextStates = new HashSet<>();
        emptyWordStates = new HashSet<>();

        //Go through all the state's rules and check, if input is contained in the set of their accepted inputs.
        //If yes, add the state, to which the current rule points, to nextStates.
        //Then check if the current rule is an epsilon-transition.
        for(Rule rule : currentState.getRules()) {
            if(rule.getAcceptedInputs().contains(input)) {
                nextStates.add(rule.getGoingTo());
            }
            if((rule.getAcceptedInputs().contains("lambda") || rule.getAcceptedInputs().contains("epsilon")) && !currentState.equals(rule.getGoingTo())) {
                nextStates.add(rule.getGoingTo());
                emptyWordStates.add(rule.getGoingTo());
            }
        }
        return  nextStates;
    }

    /**
     * This is just a simple-to-use wrapper method for {@link #checkInput(ArrayList, State)}.
     * It returns true, if a given automaton accepts a given input-
     *
     * @param automaton The automaton.
     * @param input The input-list.
     * @return True, if the automaton accepts the input.
     */
    public static boolean checkInput(Automaton automaton, ArrayList<String> input) {
        recDepth = 0;
        takenWay = new Stack<>();
        longestWay = new ArrayList<>();
        branches = new Stack<>();
        boolean ret = checkInput(input, automaton.getStartState());
        if(!accepted) {
            takenWay = new Stack<>();
            for(String s : longestWay) {
                takenWay.push(s);
            }
        }
        accepted = false;
        return ret;
    }

    /**
     * Renames the states of a given automaton, so that have short and ordered names.
     *
     * @param automaton The automaton.
     * @return The changed automaton.
     */
    public static Automaton renameStates(Automaton automaton) {
        //Initialize a HashMap, that maps a state to a boolean, to keep track of which states have already been renamed.
        HashMap<State, Boolean> visitedStates = new HashMap<>();
        for(State state : automaton.getStates()) {
            visitedStates.put(state, false);
        }

        int counter = 0;
        ArrayList<State> states = new ArrayList<>();
        states.add(automaton.getStartState());

        //Rename states, as long as there are still false-entries in visitedStates.
        while(visitedStates.values().contains(false)) {
            ArrayList<State> nextStates = new ArrayList<>();

            //Go through every entry in visited states and rename it, if it has not already been renamed.
            //Then add all its successors to nextStates.
            for(int i = 0; i < states.size(); i++) {
                if(!visitedStates.get(states.get(i))) {
                    states.get(i).setName("z" + counter);
                    visitedStates.put(states.get(i), true);
                    for (Rule rule : states.get(i).getRules()) {
                        nextStates.add(rule.getGoingTo());
                    }
                    counter++;
                }
            }
            states = nextStates;
        }

        return automaton;
    }

    /**
     * Turns a given automaton into a one without epsilon transitions.
     *
     * @param automaton The automaton.
     * @return The epsilon-free automaton
     */
    public static Automaton removeEpsilonTransitions(Automaton automaton) {
        if(isEpsilonFree(automaton)) {
            return  automaton;
        }

        //Reset branches, so checkInput works properly.
        branches = new Stack<>();

        //If a final state can be reached with just the empty word as input,
        //set the start state to be final.
        if(checkInput(new ArrayList<>(), automaton.getStartState())) {
            automaton.getStartState().setFinal(true);
        }

        //Go through each state, and calculate it's new rules.
        for(State state : automaton.getStates()) {
            //Calculate the new transitions and remove all old transitions from the current state.
            HashMap<String, HashSet<State>> newTransitions = epsilonFreeTransitions(automaton, state);
            state.getRules().clear();

            //Go through each entry in newTransitions and add each new transition to the current state.
            for(String input : newTransitions.keySet()) {
                for(State newState : newTransitions.get(input)) {
                    boolean newRule = true;
                    //If rules to newState already exist, add the current input to this rule.
                    //Else, a new rule will be added in the code below.
                    for(Rule rule : state.getRules()) {
                        if(rule.getGoingTo().equals(newState)) {
                            rule.getAcceptedInputs().add(input);
                            newRule = false;
                        }
                    }

                    //Add a new rule to newState if necessary.
                    if(newRule) {
                        state.getRules().add(new Rule(newState, new HashSet<>(Arrays.asList(input))));
                    }
                }
            }
        }

        automaton.getAllInputs().remove("epsilon");
        automaton.getAllInputs().remove("lambda");
        return automaton;
    }

    /**
     * Takes an automaton and converts it to a DFA.
     *
     * @param automaton The automaton.
     * @return The changed automaton (DFA).
     */
    public static Automaton convertToDFA(Automaton automaton) {
        if(isDFA(automaton)) {
            return automaton;
        }

        //First, we need to make sure, that there are no epsilon-transitions in this automaton.
        Automaton epsilonFreeAutomaton = removeEpsilonTransitions(automaton);


        HashMap<HashSet<State>, Boolean> stateMap = new HashMap<>();
        State startState = new State(epsilonFreeAutomaton.getStartState().getName(), true, epsilonFreeAutomaton.getStartState().isFinal(), epsilonFreeAutomaton.getStartState().getRules());
        stateMap.put(new HashSet<>(Arrays.asList(startState)), false);

        HashMap<String, State> newStates = new HashMap<>();
        newStates.put(epsilonFreeAutomaton.getStartState().getName(), new State(epsilonFreeAutomaton.getStartState().getName(), true, epsilonFreeAutomaton.getStartState().isFinal(), new HashSet<>()));

        while(stateMap.values().contains(false)) {
            HashSet<HashSet<State>> keySetCopy = new HashSet<>(stateMap.keySet());
            for(HashSet<State> states : keySetCopy) {
                if(!stateMap.get(states)) {
                    for(String input : epsilonFreeAutomaton.getAllInputs()) {
                        HashSet<State> newState = new HashSet<>();
                        boolean isFinal = false;
                        for(State state : states) {
                            for (Rule rule : state.getRules()) {
                                if (rule.getAcceptedInputs().contains(input)) {
                                    newState.add(rule.getGoingTo());
                                    if(rule.getGoingTo().isFinal()) {
                                        isFinal = true;
                                    }
                                }
                            }
                        }
                        if(!newState.isEmpty()) {
                            String goingToName = "";
                            String comingFromName = "";
                            for(State state : states) {
                                comingFromName += state.getName();
                            }
                            for(State state : newState) {
                                goingToName += state.getName();
                            }

                            char goingToNameArray[] = goingToName.toCharArray();
                            char startStateNameArray[] = startState.getName().toCharArray();
                            Arrays.sort(goingToNameArray);
                            Arrays.sort(startStateNameArray);
                            boolean containsGoingTo = false;
                            boolean equalsStartState = Arrays.equals(goingToNameArray, startStateNameArray);;
                            for(String name : newStates.keySet()) {
                                char nameArray[] = name.toCharArray();
                                Arrays.sort(nameArray);
                                if(Arrays.equals(goingToNameArray, nameArray)) {
                                    containsGoingTo = true;
                                    break;
                                }
                            }

                            if (!containsGoingTo) {
                                newStates.put(goingToName, new State(goingToName, false, isFinal, new HashSet<>()));
                            }
                            newStates.get(comingFromName).getRules().add(new Rule(newStates.get(goingToName), new HashSet<>(Arrays.asList(input))));



                            if (!stateMap.keySet().contains(newState) && !equalsStartState) {
                                stateMap.put(newState, false);
                            }
                        }
                    }
                    stateMap.put(states, true);
                }
            }
        }

        return new Automaton(new HashSet<>(newStates.values()), newStates.get(startState.getName()), epsilonFreeAutomaton.getAllInputs());
    }

    /**
     * Turns a given automaton into a DFA with a complete transition function.
     * @param automaton The automaton.
     * @return The changed automaton (DFA).
     */
    public static Automaton completeDFA(Automaton automaton) {
        if(isComplete(automaton)) {
            return automaton;
        }

        //First, we need to convert the automaton into a DFA.
        Automaton dfa = convertToDFA(automaton);

        //Initialize the trash state and let it point to itself for all inputs.
        State trashState = new State("zt", false, false, new HashSet<>());
        trashState.getRules().add(new Rule(trashState, dfa.getAllInputs()));

        boolean addedRule = false;

        //Go through every state, check if there are possible inputs for that the state has no transition,
        //and add a transition to the trashState with these inputs.
        for(State state : dfa.getStates()) {
            //This rule will point from the current state to the trash state.
            Rule toTrashRule = new Rule(trashState, new HashSet<>());

            //Go through every possible input, and check if the current state has a rule, that accepts the current input.
            for(String input : dfa.getAllInputs()) {
                boolean toTrashState = true;
                for(Rule rule : state.getRules()) {
                    if(rule.getAcceptedInputs().contains(input)) {
                        toTrashState = false;
                    }
                }

                //If there is no rule that accepts the current input, add it to the trash rule.
                if(toTrashState) {
                    toTrashRule.getAcceptedInputs().add(input);
                }
            }

            //If the trash-rule's input-list is not empty, add it to the current state.
            if(!toTrashRule.getAcceptedInputs().isEmpty()) {
                state.getRules().add(toTrashRule);
                addedRule = true;
            }
        }

        //If any rules have been added to the automaton, we need to add the trash state.
        if(addedRule) {
            dfa.getStates().add(trashState);
        }

        return dfa;
    }

    /**
     * Takes an automaton and converts it to a minimized DFA.
     * @param automaton The automaton.
     * @return The changed automaton.
     */
    public static Automaton minimizeDFA(Automaton automaton) {
        if(isMinimal(automaton)) {
            return automaton;
        }

        Automaton dfa = completeDFA(renameStates(convertToDFA(removeEpsilonTransitions(automaton))));
        State startState = dfa.getStartState();

        HashMap<Pair<State, State>, Boolean> statePairs = getMinimizeMap(dfa);

        HashSet<State> states = new HashSet<>();
        HashMap<State, HashSet<State>> mergedStates = new HashMap<>();

        for(Pair<State, State> statePair : statePairs.keySet()) {
            if(statePairs.get(statePair)) {
                states.add(statePair.getKey());
                states.add(statePair.getValue());
            } else {
                HashSet<State> mergeStates = new HashSet<>();
                mergeStates.add(statePair.getKey());
                mergeStates.add(statePair.getValue());
                boolean addedState = true;
                while(addedState) {
                    addedState = false;
                    for (Pair<State, State> mergePair : statePairs.keySet()) {
                        if(!statePairs.get(mergePair) && (mergeStates.contains(mergePair.getKey()) || mergeStates.contains(mergePair.getValue()))) {
                            mergeStates.add(mergePair.getKey());
                            mergeStates.add(mergePair.getValue());
                        }
                    }
                }
                String name = "";
                boolean isStart = false;
                boolean isFinal = true;
                HashSet<Rule> rules = new HashSet<>();
                for(State mergeState : mergeStates) {
                    name += mergeState.getName();
                    if(mergeState.isStart()) {
                        isStart = true;
                    }
                    if(mergeState.isFinal()) {
                        isFinal = true;
                    }

                    for(Rule rule1 : mergeState.getRules()) {
                        boolean alreadyContainsRule = false;
                        for(Rule rule2 : rules) {
                            if(rule1.getGoingTo().equals(rule2.getGoingTo())) {
                                rule2.getAcceptedInputs().addAll(rule1.getAcceptedInputs());
                                alreadyContainsRule = true;
                            }
                        }
                        if(!alreadyContainsRule) {
                            rules.add(rule1);
                        }
                    }
                }
                State newState = new State(name, isStart, isFinal, rules);
                states.add(newState);
                mergedStates.put(newState, mergeStates);
                startState = newState;
            }
        }

        for(State state : mergedStates.keySet()) {
            for(State mergeState : mergedStates.get(state)) {
                for(State state1 : states) {
                    for(Rule rule1 : state1.getRules()) {
                        boolean duplicate = false;
                        for(Rule rule2 : state1.getRules()) {
                            if(!(rule1 == rule2) && rule1.getGoingTo().equals(rule2.getGoingTo()) && rule1.getAcceptedInputs().equals(rule2.getAcceptedInputs())) {
                                duplicate = true;
                            }
                        }
                        if(!duplicate && rule1.getGoingTo().equals(mergeState)) {
                            rule1.setGoingTo(state);
                        }
                    }
                }
                states.remove(mergeState);
            }
        }

        return new Automaton(new HashSet<>(states), startState, dfa.getAllInputs());
    }

    /**
     * Checks if a given automaton is free of epsilon transitions.
     * @param automaton The automaton.
     * @return true, if the automaton has no epsilon transitions.
     */
    public static boolean isEpsilonFree(Automaton automaton) {
        for(State state : automaton.getStates()) {
            for(Rule rule : state.getRules()) {
                for(String string : rule.getAcceptedInputs()) {
                    if(string.equals("epsilon") || string.equals("lambda")) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Checks if a given automaton is a DFA.
     * @param automaton The automaton.
     * @return true, if the automaton is a DFA.
     */
    public static boolean isDFA(Automaton automaton) {
        for(State state : automaton.getStates()) {
            HashSet<String> inputs = new HashSet<>();
            for(Rule rule : state.getRules()) {
                for(String string : rule.getAcceptedInputs()) {
                    if(inputs.contains(string)) {
                        return false;
                    }
                    inputs.add(string);
                }
            }
        }

        return true;
    }

    /**
     * Checks if a given automaton is a DFA with a complete transition function.
     * @param automaton The automaton.
     * @return true, if the automaton is a DFA.
     */
    public static boolean isComplete(Automaton automaton) {
        for(String input : automaton.getAllInputs()) {
            for(State state : automaton.getStates()) {
                HashSet<String> stateInputs = new HashSet<>();
                for(Rule rule : state.getRules()) {
                    stateInputs.addAll(rule.getAcceptedInputs());
                }
                if(!stateInputs.equals(automaton.getAllInputs())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if a given automaton is a minimal DFA.
     * @param automaton The automaton.
     * @return true, if the automaton is a minimal DFA.
     */
    public static boolean isMinimal(Automaton automaton) {
        HashMap<Pair<State, State>, Boolean> statePairs = getMinimizeMap(automaton);

        return !statePairs.values().contains(false);
    }

    /**
     * Getter-method for {@link #takenWay}.
     * @return {@link #takenWay}
     */
    public static Stack<String> getTakenWay() {
        return takenWay;
    }

    public static void fillLengthsOfEdges(Automaton automaton) {
        ArrayList<State> statesSorted=getStatesSorted(automaton);
        for(int i=0;i<statesSorted.size();i++) {
            State current=statesSorted.get(i);
            for(Rule rule : current.getRules()) {
                int j=statesSorted.indexOf(rule.getGoingTo());
                rule.setLength(Math.abs(i-j));
            }
        }
        int max=0;
        for(State state : automaton.getStates()) {
            for(Rule rule : state.getRules()) {
                if(rule.getLength()>max) {
                    max=rule.getLength();
                }
            }
        }
        for(int i=0;i<statesSorted.size();i++) {
            State current=statesSorted.get(i);
            for(Rule rule : current.getRules()) {
                int j=statesSorted.indexOf(rule.getGoingTo());
                rule.setMaxLength(max);
            }
        }

    }
    public static ArrayList<State> getStatesSorted(Automaton automaton) {
        return (ArrayList<State>) automaton.getStates().stream().sorted((s1,s2) -> s1.getName().compareTo(s2.getName())).collect(toList());
    }


}
package AutomatonSimulator;



import AutomatonParser.analysis.DepthFirstAdapter;
import AutomatonParser.node.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * @author fabian
 * @since 24.04.16
 */
class Visitor extends DepthFirstAdapter {
    private Automaton automaton;
    private State startState;
    private final HashMap<String, State> states = new HashMap<>();
    private final HashSet<String> allInputs = new HashSet<>();

    @Override
    public void inAStates(AStates node) {
        for(TIdentifier state : node.getAllStates()) {
            states.put(state.getText(), new State(state.getText(), false, false, new HashSet<>()));
        }

        states.get(node.getStartState().getText()).setStart(true);
        startState = states.get(node.getStartState().getText());

        for(TIdentifier state : node.getEndStates()) {
            states.get(state.getText()).setFinal(true);
        }
    }

    @Override
    public void inARule(ARule node) {
        HashSet<String> acceptedInputs = new HashSet<>();
        for(TListArg input : node.getListArg()) {
            String acceptedInput = input.toString().substring(1, input.getText().length() - 1);
            acceptedInputs.add(acceptedInput);
            if(!allInputs.contains(acceptedInput)) {
                allInputs.add(acceptedInput);
            }
        }
        states.get(node.getComingFrom().getText()).getRules().add(new Rule(states.get(node.getGoingTo().getText()), acceptedInputs));
    }

    @Override
    public void outARoot(ARoot node) {
        HashSet<State> stateList = states.keySet().stream().map(states::get).collect(Collectors.toCollection(HashSet::new));
        automaton = new Automaton(stateList, startState, allInputs);
    }

    public Automaton getAutomaton(String name) {
        return (Automaton) automaton.otherName(name);
    }
}

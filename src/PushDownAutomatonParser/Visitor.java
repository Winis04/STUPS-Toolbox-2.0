package PushDownAutomatonParser;


import PushDownAutomatonParser.analysis.DepthFirstAdapter;
import PushDownAutomatonParser.node.*;
import PushDownAutomatonSimulator.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


/**
 * Created by Isabel on 09.12.2016.
 */
public class Visitor extends DepthFirstAdapter {
    private PushDownAutomaton pushDownAutomaton;
    private State start_state;
    private StackLetter initialStackLetter;
    private HashMap<String, State> states = new HashMap<>();
    private HashMap<String, InputLetter> inputAlphabet = new HashMap<>();
    private HashMap<String, StackLetter> stackAlphabet = new HashMap<>();
    @Override
    public void inASymbols(ASymbols node) {

        for(TSymbol inputLetter : node.getInputletters()) {
            String name = inputLetter.getText().replaceAll("'","");
            inputAlphabet.put(name, new InputLetter(name));
        }
        for(TSymbol stackLetter : node.getStackletters()) {
            String name = stackLetter.getText().replaceAll("'","");
            stackAlphabet.put(name, new StackLetter(name));
        }
        for(TIdentifier state : node.getStates()) {
            String name = state.getText().replaceAll("'","");
            states.put(name,new State(name));
        }
        start_state = states.get(node.getStartState().getText().replaceAll("'",""));
        start_state.setStart(true);
        initialStackLetter = stackAlphabet.get(node.getBottom().getText().replaceAll("'",""));
    }

    @Override
    public void inARule(ARule node) {

        State comingFrom;
        InputLetter input;
        StackLetter tos;
        State goingTo;
        ArrayList<StackLetter> newStack = new ArrayList<>();
        List<TSymbol> list = node.getComingFrom();
        comingFrom = states.get(list.get(0).getText().replaceAll("'",""));
        input = inputAlphabet.get(list.get(1).getText().replaceAll("'",""));
        tos = stackAlphabet.get(list.get(2).getText().replaceAll("'",""));

        List<TSymbol> list2 = node.getGoingTo();
        goingTo = states.get(list2.get(0).getText().replaceAll("'",""));
        list2.remove(0);
        for(TSymbol symbol : list2) {
            newStack.add(stackAlphabet.get(symbol.getText().replaceAll("'","")));
        }

        Rule rule = new Rule(comingFrom,input,tos,goingTo,newStack);
        comingFrom.getRules().add(rule);

    }
    @Override
    public void outARoot(ARoot node) {

        pushDownAutomaton = new PushDownAutomaton(toHashSet(states),toHashSet(inputAlphabet),toHashSet(stackAlphabet),start_state,initialStackLetter);

    }

    public PushDownAutomaton getPushDownAutomaton() {

        return pushDownAutomaton;
    }

    public HashSet toHashSet(HashMap map) {
        HashSet set = new HashSet<>();
        map.values().stream().forEach(elem -> set.add(elem));
        return set;
    }

}

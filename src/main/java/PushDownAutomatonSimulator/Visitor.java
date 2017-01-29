package PushDownAutomatonSimulator;


import PushDownAutomatonParser.analysis.DepthFirstAdapter;
import PushDownAutomatonParser.node.*;

import java.util.*;


/**
 * @author Isabel
 * @since 09.12.2016
 */
class Visitor extends DepthFirstAdapter {
    private PushDownAutomaton pushDownAutomaton;
    private State start_state;
    private StackLetter initialStackLetter;
    private HashSet<State> states = new HashSet<>();
    private HashSet<InputLetter> inputAlphabet = new HashSet<>();
    private HashSet<StackLetter> stackAlphabet = new HashSet<>();
    private ArrayList<PDARule> rules = new ArrayList<>();
    @Override
    public void inASymbols(ASymbols node) {

        for(TSymbol inputLetter : node.getInputletters()) {
            String name = inputLetter.getText().replaceAll("'","");
            if(name.equals("epsilon") || name.equals("lambda")) {
                inputAlphabet.add(InputLetter.NULLSYMBOL);
            } else {
                inputAlphabet.add(new InputLetter(name));
            }
        }
        inputAlphabet.add(InputLetter.NULLSYMBOL);
        for(TSymbol stackLetter : node.getStackletters()) {
            String name = stackLetter.getText().replaceAll("'","");
            if(name.equals("epsilon") || name.equals("lambda")) {
                stackAlphabet.add(StackLetter.NULLSYMBOL);
            } else {
                stackAlphabet.add(new StackLetter(name));
            }
        }
        stackAlphabet.add(StackLetter.NULLSYMBOL);

        for(TIdentifier state : node.getStates()) {
            String name = state.getText().replaceAll("'","");
            states.add(new State(name));
        }
        start_state = new State(node.getStartState().getText().replaceAll("'",""));
        initialStackLetter = new StackLetter(node.getBottom().getText().replaceAll("'",""));

    }

    @Override
    public void inARule(ARule node) {

        State comingFrom;
        InputLetter input;
        StackLetter tos;
        State goingTo;
        ArrayList<StackLetter> newStack = new ArrayList<>();
        List<TSymbol> list = node.getComingFrom();
        comingFrom = new State(list.get(0).getText().replaceAll("'",""));
        if(list.get(1).getText().replaceAll("'","").equals("epsilon")||list.get(1).getText().replaceAll("'","").equals("lambda")) {
            input = InputLetter.NULLSYMBOL;
        } else {

             input = new InputLetter(list.get(1).getText().replaceAll("'",""));
        }

        if(list.get(2).getText().replaceAll("'","").equals("epsilon")||list.get(2).getText().replaceAll("'","").equals("lambda"))  {
            tos = StackLetter.NULLSYMBOL;
        } else {
            tos = new StackLetter(list.get(2).getText().replaceAll("'", ""));
        }

        List<TSymbol> list2 = node.getGoingTo();
        goingTo =new State(list2.get(0).getText().replaceAll("'",""));
        list2.remove(0);
        for(TSymbol symbol : list2) {
            if(symbol.getText().replaceAll("'","").equals("epsilon") || symbol.getText().replaceAll("'","").equals("lambda")) {
                newStack.add(StackLetter.NULLSYMBOL);
            } else {
                newStack.add(new StackLetter(symbol.getText().replaceAll("'", "")));
            }
        }
   //     Collections.reverse(newStack);
        PDARule rule = new PDARule(comingFrom,goingTo,input,tos,newStack);
        rules.add(rule);

    }
    @Override
    public void outARoot(ARoot node) {

        pushDownAutomaton = new PushDownAutomaton(states,start_state,initialStackLetter,rules,start_state,"G",null);

    }

    PushDownAutomaton getPushDownAutomaton(String name) {
        PushDownAutomaton p = pushDownAutomaton;
        return new PushDownAutomaton(p.getStates(),p.getStartState(),p.getInitalStackLetter(),p.getRules(),start_state,name,null);

    }


}

package Main;

import AutomatonSimulator.Automaton;
import GrammarSimulator.Grammar;
import PushDownAutomatonSimulator.PushDownAutomaton;

import java.util.HashMap;

/**
 * @author Isabel
 * @since 30.01.2017
 */
public class Content {
    private GUI gui;
    private CLI cli;
    private HashMap<String, Class> lookUpTable = new HashMap<>();
    public Content(GUI gui, CLI cli) {
        lookUpTable.put("grammar", Grammar.class);
        lookUpTable.put("automaton", Automaton.class);
        lookUpTable.put("pda", PushDownAutomaton.class);
        lookUpTable.put("pushdownautomaton",PushDownAutomaton.class);
    }
}

package PushDownAutomatonSimulator;

import java.util.HashSet;

/**
 * Created by Isabel on 29.10.2016.
 */
public class State {
    /**
     * true, if the state is a start state
     */
    private final boolean isStart;
    /**
     * the state's name
     */
    private final String name;
    /**
     * the rules going away from this state
     */
    private HashSet<Rule> rules;

    public State(boolean isStart, String name, HashSet<Rule> rules) {
        this.isStart = isStart;
        this.name = name;
        this.rules = rules;
    }

    public State(boolean isStart, String name) {
        this.isStart = isStart;
        this.name = name;
        this.rules=new HashSet<>();
    }

    public State(String name) {
        this.isStart= false;
        this.name = name;
        this.rules=new HashSet<>();
    }

    public boolean isStart() {
        return isStart;
    }

    public String getName() {
        return name;
    }

    public HashSet<Rule> getRules() {
        return rules;
    }

    public void setRules(HashSet<Rule> rules) {
        this.rules = rules;
    }
}

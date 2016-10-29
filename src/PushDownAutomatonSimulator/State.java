package PushDownAutomatonSimulator;

import java.util.HashSet;

/**
 * Created by Isabel on 29.10.2016.
 */
public class State {
    /**
     * true, if the state is a start state
     */
    private boolean isStart;
    /**
     * the state's name
     */
    private String name;
    /**
     * the rules going away from this state
     */
    private HashSet<Rule> rules;

    public State(boolean isStart, String name, HashSet<Rule> rules) {
        this.isStart = isStart;
        this.name = name;
        this.rules = rules;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public String getName() {
        return name;
    }
}

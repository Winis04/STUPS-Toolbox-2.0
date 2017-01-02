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




    public State(boolean isStart, String name) {
        this.isStart = isStart;
        this.name = name;
    }

    public State(String name) {
        this.isStart= false;
        this.name = name;
    }

    public boolean isStart() {
        return isStart;
    }

    public String getName() {
        return name;
    }

}

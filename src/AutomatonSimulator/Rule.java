package AutomatonSimulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by fabian on 21.04.16.
 */
public class Rule {
    /**
     * The state, to which the rule points.
     */
    private State goingTo;

    /**
     * The input-strings, that are valid for this rule.
     */
    private HashSet<String> acceptedInputs;

    /**
     * The constructor.
     *
     * @param goingTo The state, to which the rule is pointing.
     * @param acceptedInputs The inputs, which this rule accepts.
     */
    public Rule(State goingTo, HashSet<String> acceptedInputs) {
        this.goingTo = goingTo;
        this.acceptedInputs = acceptedInputs;
    }

    /**
     * Getter-method for {@link #goingTo}.
     *
     * @return {@link #goingTo}
     */
    public State getGoingTo() {
        return goingTo;
    }

    /**
     * Getter-method for {@link #acceptedInputs}.
     *
     * @return {@link #acceptedInputs}
     */
    public HashSet<String> getAcceptedInputs() {
        return acceptedInputs;
    }

    /**
     * Setter-method for {@link #goingTo}.
     *
     * @param goingTo The new value for {@link #goingTo}.
     */
    public void setGoingTo(State goingTo) {
        this.goingTo = goingTo;
    }
}

package AutomatonSimulator;

import Print.Printable;
import Print.Printer;

import java.util.HashSet;
import java.util.Set;

/**
 * A state of an {@link Automaton}.
 * @author fabian
 * @since 21.04.16
 */
public class State implements Printable{
    /**
     * The state's name.
     */
    private String name;

    /**
     * True, if the state is a start state.
     */
    private boolean isStart;

    /**
     * True, if the state is a final state.
     */
    private boolean isFinal;

    /**
     * The rules, that are pointing away from this state.
     */
    private final HashSet<Rule> rules;

    /**
     * The constructor.
     *
     * @param name The state's name.
     * @param isStart True, if this state is a start state.
     * @param isFinal True, if this state is a final state.
     * @param rules The state's rules.
     */
    public State(String name, boolean isStart, boolean isFinal, Set<Rule> rules) {
        this.name = name;
        this.isStart = isStart;
        this.isFinal = isFinal;
        this.rules = new HashSet<>(rules);
    }

    /**
     * Getter-method for {@link #name}.
     *
     * @return {@link #name}
     */
    public String getName() {
        return name;
    }

    /**
     * Getter-method for {@link #isFinal}.
     *
     * @return {@link #isFinal}
     */
    public boolean isFinal() {
        return isFinal;
    }

    /**
     * Getter-method for {@link #isStart}.
     *
     * @return {@link #isStart}
     */
    public boolean isStart() {
        return isStart;
    }

    /**
     * Getter-method for {@link #rules}.
     *
     * @return {@link #rules}
     */
    public HashSet<Rule> getRules() {
        return rules;
    }

    /**
     * Setter-method for {@link #isStart}.
     *
     * @param start The new value for {@link #isStart}.
     */
    public void setStart(boolean start) {
        isStart = start;
    }

    /**
     * Setter-method for {@link #isFinal}.
     *
     * @param aFinal The new value for {@link #isFinal}.
     */
    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    /**
     * Setter-method for {@link #name}.
     *
     * @param name The new value for {@link #name}.
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void printLatex(String space) {

        Printer.print(this.name);
    }

    @Override
    public void printConsole() {
        Printer.print(this.name);
    }
}

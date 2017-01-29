package PushDownAutomatonSimulator;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * describe a state of an {@link PushDownAutomaton}
 * @author Isabel
 * @since 29.10.2016
 */
public class State {
    /**
     * the state's name
     */
    private final String name;


    /**
     * constructor
     * @param name the name of the state
     */
    public State(String name) {
        this.name = name;
    }

    /**
     * Getter-Method for {@link #name}
     * @return the {@link #name}
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
       State rhs = (State) obj;
        return new EqualsBuilder()
                .append(name, rhs.name)
                .isEquals();
    }


    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
                append(name).
                toHashCode();
    }
}

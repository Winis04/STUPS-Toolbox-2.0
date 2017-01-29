package PushDownAutomatonSimulator;

import Main.GUI;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * This class is there for the symbol you put on the stack of a {@link PushDownAutomaton}
 * @author Isabel
 * @since 29.10.2016
 */
public class StackLetter {
    private final String name;

    /**
     * global null symbol for the stack
     */
    public static final StackLetter NULLSYMBOL = new StackLetter("epsilon");

    /**
     * the constructor.
     * The given parameter is the name of the StackLetter
     * @param name the name of the StackLetter.
     */
    public StackLetter(String name) {
        this.name = name;
    }

    /**
     * Getter-Method for {@link #name}
     * @return the {@link #name} of this StackLetter
     */
    public String getName() {
        if(name.equals("epsilon")) {
            return GUI.nameOfNullSymbol;
        }
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        StackLetter rhs = (StackLetter) obj;
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

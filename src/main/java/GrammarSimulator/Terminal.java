package GrammarSimulator;

import Main.GUI;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author fabian
 * @since 06.08.16
 */
public final class Terminal implements Symbol {

    /**
     * The terminal's name.
     */
    private final String name;

    public static final Terminal NULLSYMBOL=new Terminal("epsilon");

    /**
     * The constructor.
     *
     * @param name The terminal's name.
     */
    public Terminal(String name) {
        this.name = name;
    }

    @Override
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
        Terminal rhs = (Terminal) obj;
        return new EqualsBuilder()
                .append(name, rhs.name)
                .isEquals();
    }


    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
                // if deriving: appendSuper(super.hashCode()).
                        append(name).
                        toHashCode();
    }
}

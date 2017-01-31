package PushDownAutomatonSimulator;



import Main.GUI;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * every symbol or string a {@link PushDownAutomaton} reads in, is an instance of this class.
 * @author Isabel
 * @since 29.10.2016
 */
public final class InputLetter {

    /**
     * global null symbol
     */
    public static final InputLetter NULLSYMBOL = new InputLetter("epsilon");
    private final String name;

    /**
     * constructor for an InputLetter with {@link #name} "name"
     * @param name the name of this InputLetter
     */
    public InputLetter(String name) {
        this.name = name;
    }

    /**
     * Getter-Method for {@link #name}
     * @return {@link #name}
     */
    public String getName() {
        return name;
    }

    public String getDisplayName() {
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
        InputLetter rhs = (InputLetter) obj;
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

package PushDownAutomatonSimulator;



import Main.GUI;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author Isabel
 * @since 29.10.2016
 */
public final class InputLetter {

    public static final InputLetter NULLSYMBOL = new InputLetter("epsilon");
    private final String name;

    public InputLetter(String name) {
        this.name = name;
    }

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

package PushDownAutomatonSimulator;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Stack;

/**
 * Created by Isabel on 29.10.2016.
 */
public class StackLetter {
    private String name;

    public static final StackLetter NULLSYMBOL = new StackLetter("epsilon");

    public StackLetter(String name) {
        this.name = name;
    }

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

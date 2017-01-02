package PushDownAutomatonSimulator;

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
    public boolean equals(Object o) {
        if(o instanceof StackLetter) {
            StackLetter other = (StackLetter) o;
            if(other.name.equals(this.name)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

}

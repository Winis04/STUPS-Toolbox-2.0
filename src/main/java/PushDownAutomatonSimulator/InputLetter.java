package PushDownAutomatonSimulator;


import GrammarSimulator.Nonterminal;
import jdk.internal.util.xml.impl.Input;

/**
 * Created by Isabel on 29.10.2016.
 */
public final class InputLetter {

    public static final InputLetter NULLSYMBOL = new InputLetter("epsilon");
    private final String name;

    public InputLetter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof InputLetter) {
            InputLetter other = (InputLetter) o;
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

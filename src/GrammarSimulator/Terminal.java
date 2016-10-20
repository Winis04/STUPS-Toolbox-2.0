package GrammarSimulator;

import java.io.Serializable;

/**
 * Created by fabian on 06.08.16.
 */
public class Terminal implements Symbol, Serializable {

    /**
     * The terminal's name.
     */
    private String name;

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
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}

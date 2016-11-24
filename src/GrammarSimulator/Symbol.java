package GrammarSimulator;

import Print.Printable;

/**
 * Created by fabian on 06.08.16.
 */
public interface Symbol {
    /**
     * Returns the symbol's name.
     *
     * @return The symbol's name.
     */
    String getName();

    /**
     * Sets the symbol's name.
     *
     * @param name The symbol's name.
     */
    void setName(String name);

    boolean equals(Object o);
}

package GrammarSimulator;


/**
 * Created by fabian on 06.08.16.
 */
public interface Symbol  {
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

    /**
     * checks if the symbols equals another symbol
     * @param s the other symbol
     * @return true, if the two are equal
     */
    boolean equals(Symbol s);
}

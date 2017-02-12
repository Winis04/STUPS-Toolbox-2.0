package GrammarSimulator;

/**
 * A symbol is an element of a {@link Grammar}. It is either a {@link Nonterminal}, which has further derivations,
 * or a {@link Terminal}, which is terminal.
 * @author fabian
 * @since 06.08.16
 */
public interface Symbol {
    /**
     * Returns the symbol's name.
     *
     * @return The symbol's name.
     */
    String getName();

    /**
     * Returns the symbol's name for the screen on the gui
     * @return the symbol's name
     */
    String getDisplayName();

}

package CLIPlugins;

import Main.Storable;

/**
 * Plugins are executable commands for the terminal. The plugin operates on the currently loaded object
 * of the type {@link #inputType()}. Returns an object of type {@link #outputType()}.
 * @author fabian
 * @since 15.06.16
 */
public abstract class CLIPlugin {

    /**
     * Returns the commands that execute this plugin.
     *
     * @return A string-array, containing the commands.
     */
    public abstract String[] getNames();

    /**
     * Evaluates, whether the command has been called with the right parameters, or not.
     *
     * @param parameters The parameters, that the user has entered.
     * @return true, if the parameters are okay.
     */
    public abstract boolean checkParameters(String[] parameters);

    /**
     * Returns a short description-string that is printed, when the user enters "help".
     *
     * @return The string.
     */
    public abstract String getHelpText();

    /**
     * Takes an object, does something with it, and returns the changed object.
     * For example: If a plugin executes an algorithm on an automaton, the object needs to be casted to {@link AutomatonSimulator.Automaton}.
     *              Then, the algorithm can be executed on it, and then this method returns the changed automaton.
     *              It is also possible, to return an object, that is of a completely different type, than the input object.
     *
     * @param storable The object.
     * @param parameters The parameters, that the user has entered.
     * @return The changed object.
     */
    public abstract Storable execute(Storable storable, String[] parameters);

    /**
     * Returns the desired object-type, needed by {@link #execute(Storable, String[])}.
     * For example: If {@link #execute(Storable, String[])} needs an automaton, this method returns {@link AutomatonSimulator.Automaton}.class.
     *
     * @return The object-type.
     */
   public abstract Class inputType();

    /**
     * Returns the type of the object that {@link #execute(Storable, String[])} returns.
     * For example: If {@link #execute(Storable, String[])} returns an automaton, this method returns {@link AutomatonSimulator.Automaton}.class.
     *
     * @return The object-type.
     */
    public abstract Class outputType();

    /**
     * After calling {@link #execute(Storable, String[])}, the main program calls this method to evaluate, whether an error has occurred, or not.
     *
     * @return true, if an error has occurred, during the execution of {@link #execute(Storable, String[])}.
     */
    public abstract boolean errorFlag();

    public boolean createsOutput() {
        return false;
    }


}

package CLIPlugins;

import Main.CLI;
import org.junit.runner.manipulation.Sortable;

/**
 * Plugins are executable commands for the terminal. The plugin operates on the currently loaded object
 * of the type {@link #inputType()}. Returns an object of type {@link #outputType()}.
 * @author fabian
 * @since 15.06.16
 */
public abstract class CLIPlugin implements Comparable{

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
     * @param object The object.
     * @param parameters The parameters, that the user has entered.
     * @return The changed object.
     */
    public abstract Object execute(Object object, String[] parameters);

    /**
     * Returns the desired object-type, needed by {@link #execute(Object, String[])}.
     * For example: If {@link #execute(Object, String[])} needs an automaton, this method returns {@link AutomatonSimulator.Automaton}.class.
     *
     * @return The object-type.
     */
   public abstract Class inputType();

    /**
     * Returns the type of the object that {@link #execute(Object, String[])} returns.
     * For example: If {@link #execute(Object, String[])} returns an automaton, this method returns {@link AutomatonSimulator.Automaton}.class.
     *
     * @return The object-type.
     */
    public abstract Class outputType();

    /**
     * After calling {@link #execute(Object, String[])}, the main program calls this method to evaluate, whether an error has occurred, or not.
     *
     * @return true, if an error has occurred, during the execution of {@link #execute(Object, String[])}.
     */
    public abstract boolean errorFlag();

    @Override
    public int compareTo(Object o) {
        if(o instanceof CLIPlugin) {
            CLIPlugin x = (CLIPlugin) o;
           return this.getNames()[0].compareTo(x.getNames()[0]);
        } else {
            return -100;
        }
    }
}

package GUIPlugins.SimpleFunctionPlugins;

/**
 * Created by fabian on 18.06.16.
 */
public interface SimpleFunctionPlugin {

    /**
     * Takes an object, does something with it, and returns the changed object.
     * For example: If a plugin executes an algorithm on an automaton, the object needs to be casted to {@link AutomatonSimulator.Automaton}.
     *              Then, the algorithm can be executed on it, and then this method returns the changed automaton.
     *              It is also possible, to return an object, that is of a completely different type, than the input object.
     *
     * @param object The object.
     * @return The changed object.
     */
    Object execute(Object object);

    /**
     * Returns the plugin's name.
     *
     * @return The plugin's name.
     */
    String getName();

    /**
     * Returns the desired object-type, needed by {@link #execute(Object)}.
     * For example: If {@link #execute(Object)} needs an automaton, this method returns {@link AutomatonSimulator.Automaton}.class.
     *
     * @return The object-type.
     */
    Class inputType();

    /**
     * Returns the type of the object that {@link #execute(Object)} returns.
     * For example: If {@link #execute(Object)} returns an automaton, this method returns {@link AutomatonSimulator.Automaton}.class.
     *
     * @return The object-type.
     */
    Class outputType();
}

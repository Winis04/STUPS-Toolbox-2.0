package Main;

import Print.Printable;

import java.io.File;
import java.io.IOException;

/**
 * An Interface, which has to be implemented by classes that should be displayed and handled by this ToolBox.
 * The Toolbox has a workspace, a "store". Every Grammar, Automaton, etc. on which some kind of algorithm should
 * be run is of type "storable".
 * @author Isabel
 * @since 27.11.2016
 */


public interface Storable extends Printable {
    /**
     * a deep-copy of the object.
     * Becomes redundant once automaton are immutable too
     * @return a instance of class Storable, which has the same subclass as the original
     */

    Storable deep_copy();

    /**
     * Getter-Method for the name of the object
     * @return the name of the storable object
     */

    String getName();

    /**
     * just like {@link #deep_copy()}, but with a new name
     * @param name the new name of the copied object
     * @return a copy of the original, but with a different name
     */

    Storable otherName(String name);

    /**
     *
     * prints the object in a certain form, so it can be saved in a file
     * @param path the path to the save file
     * @throws IOException input/output Exception
     */

    void printToSave(String path) throws IOException;

    /**
     * restores a Storable from a file
     * @param file the source file
     * @return a Storable object
     * @throws Exception every Exception
     */
    Storable restoreFromFile(File file) throws Exception;

    /**
     * the previous version of this object.
     * @return a Storable of the same subclass as the original
     */
    Storable getPreviousVersion();


    @Override
    void printLatex(String space);

    @Override
    void printConsole();
    
}

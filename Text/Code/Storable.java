
public interface Storable extends Printable {
   
    //a deep-copy of the object.
    Storable deep_copy();

    String getName();

    
    // just like {@link #deep_copy()}, but with a new name
    Storable otherName(String name);


    //prints the object in a certain form, so it can be saved in a file
    void printToSave(String path) throws IOException;

    //restores a Storable from a file
    Storable restoreFromFile(File file) throws Exception;

    Storable getPreviousVersion();

    @Override
    void printLatex(String space);

    @Override
    void printConsole();
}

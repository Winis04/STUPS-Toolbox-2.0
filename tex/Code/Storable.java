public interface Storable extends Printable {
    //a deep-copy of the object.
    Storable deep_copy();
    //[...]
    String getName();

    void printToSave(String path) throws IOException;

    Storable restoreFromFile(File file) throws Exception;
	
    //returns the previous version of this object
    Storable getPreviousVersion();

    @Override
    void printLatex(String space);

    @Override
    void printConsole();
}

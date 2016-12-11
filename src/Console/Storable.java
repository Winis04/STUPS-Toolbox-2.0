package Console;

import java.io.File;

/**
 * Created by Isabel on 27.11.2016.
 */
public interface Storable {
    Storable deep_copy();
    String getName();
    void setName(String name);
    void printToSave(String path);
    Storable restoreFromFile(File file);
    void savePreviousVersion();
    Storable getPreviousVersion();

}

package Main;

import java.io.File;

/**
 * Created by Isabel on 27.11.2016.
 */
public interface Storable {
    Storable deep_copy();
    String getName();
    Storable otherName(String name);
    void printToSave(String path);
    Storable restoreFromFile(File file);
    Storable getPreviousVersion();
    void savePreviousVersion();

}

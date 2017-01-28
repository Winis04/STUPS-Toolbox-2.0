package Main;

import java.io.File;

/**
 * @author Isabel
 * @since 27.11.2016
 */
public interface Storable {
    Storable deep_copy();
    String getName();
    Storable otherName(String name);
    void printToSave(String path);
    Storable restoreFromFile(File file) throws Exception;
    Storable getPreviousVersion();
    void savePreviousVersion();

}

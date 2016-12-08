package Print;

import java.io.BufferedWriter;

/**
 * Created by isabel on 24.11.16.
 */
public interface Printable {
    void printLatex(BufferedWriter writer, String space);
    void printConsole(BufferedWriter writer);

}

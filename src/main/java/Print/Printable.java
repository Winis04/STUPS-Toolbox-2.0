package Print;

import java.io.BufferedWriter;

/**
 * @author isabel
 * @since 24.11.16
 */
public interface Printable {
    void printLatex(BufferedWriter writer, String space);
    void printConsole(BufferedWriter writer);

}

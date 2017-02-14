package Print;

import java.io.BufferedWriter;

/**
 * this Interface has to be implemented by classes that should be printed.
 * @author isabel
 * @since 24.11.16
 */
public interface Printable {
    /**
     * formats the text, so it can be written in a latex file
     * @param writer the {@link BufferedWriter} writer
     * @param space the deepness of the text written
     */
    void printLatex(BufferedWriter writer, String space);

    /**
     * formats the code such that it can be written on the terminal
     * @param writer the {@link BufferedWriter}
     */
    void printConsole(BufferedWriter writer);

}

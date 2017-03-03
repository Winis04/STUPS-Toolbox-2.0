package Print;



/**
 * this Interface has to be implemented by classes that should be printed.
 * @author isabel
 * @since 24.11.16
 */
public interface Printable {
    /**
     * formats the text, so it can be written in a latex file
     *
     * @param space the deepness of the text written
     */
    void printLatex(String space);

    /**
     * formats the code such that it can be written on the terminal
     */
    void printConsole();
}

package Print;


import java.io.BufferedWriter;

/**
 * A Dummy-class for printable objects. Used, if nothing should be printed
 * or a given string. The dummy class is a wrapper for a string.
 * @author Isabel
 * @since 25.11.2016
 */


public class Dummy implements Printable {

    private String s="";

    /**
     * empty constructor
     */

    public Dummy() {

    }

    /**
     * a dummy with the content of string
     * @param string the content of this dummy
     */

    public Dummy(String string) {
        this.s=string;
    }

    /**
     * The Dummy is nothing more than a wrapper for a string. This method
     * prints the string with the given indentation
     * @param writer the {@link BufferedWriter} writer
     * @param space the deepness of the text written
     */
    @Override
    public void printLatex(BufferedWriter writer, String space) {
        Printer.print(space+s,writer);
    }


    @Override
    public void printConsole(BufferedWriter writer) {
        Printer.print(s,writer);
    }
}

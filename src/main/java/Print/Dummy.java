package Print;


import java.io.BufferedWriter;

/**
 * A Dummy-class for printable objects. Used, if nothing should be printed.
 * @author Isabel
 * @since 25.11.2016
 */

@SuppressWarnings("unused")
public class Dummy implements Printable {
    @SuppressWarnings("unused")
    private String s="";

    /**
     * empty constructor
     */
    @SuppressWarnings("unused")
    public Dummy() {

    }

    /**
     * a dummy with the content of string
     * @param string the content of this dummy
     */
    @SuppressWarnings("unused")
    public Dummy(String string) {
        this.s=string;
    }

    @SuppressWarnings("unused")
    @Override
    public void printLatex(BufferedWriter writer, String space) {
        Printer.print(space+s,writer);
    }

    @SuppressWarnings("unused")
    @Override
    public void printConsole(BufferedWriter writer) {
        Printer.print(s,writer);
    }
}

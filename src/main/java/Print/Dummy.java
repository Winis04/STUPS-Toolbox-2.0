package Print;


import java.io.BufferedWriter;

/**
 * A Dummy-class for printable objects. Used, if nothing should be printed.
 * @author Isabel
 * @since 25.11.2016
 */
@SuppressWarnings("ALL")
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

    @Override
    public void printLatex(BufferedWriter writer, String space) {
        Printer.print(space+s,writer);
    }

    @Override
    public void printConsole(BufferedWriter writer) {
        Printer.print(s,writer);
    }
}

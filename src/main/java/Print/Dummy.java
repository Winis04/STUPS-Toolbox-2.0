package Print;


import java.io.BufferedWriter;

/**
 * A Dummy-class for printable objects. Used, if nothing should be printed.
 * @author Isabel
 * @since 25.11.2016
 */
public class Dummy implements Printable {
    String s="";
    public Dummy() {

    }
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

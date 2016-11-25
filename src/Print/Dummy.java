package Print;

/**
 * Created by Isabel on 25.11.2016.
 */

/**
 * A Dummy-class for printable objects. Used, if nothing should be printed.
 */
public class Dummy implements Printable {
    String s="";
    public Dummy() {

    }
    public Dummy(String string) {
        this.s=string;
    }
    @Override
    public void print() {
        if(!s.isEmpty()) {
            Printer.print(s);
        }
    }
}

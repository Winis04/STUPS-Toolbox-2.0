package Print;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Collection;

/**
 * My own {@link java.util.List} of {@link Printable}s. No Null-objects allowed.
 * @author Isabel
 * @singe 24.11.16
 */
public class PrintableList extends ArrayList<Printable> implements Printable {
    public PrintableList(int n) {
        super(n);
    }
    public PrintableList(Collection<Printable> collection) {
        super(collection);
    }

    @Override
    public boolean add(Printable p) {
        return p != null && super.add(p);
    }

    @Override
    public void printLatex(BufferedWriter writer, String space) {
        this.forEach(x -> {
            Printer.print(x);
            Printer.print("\n", writer);
        });
    }

    @Override
    public void printConsole(BufferedWriter writer) {
        this.forEach(x -> {
            Printer.print(x);
            Printer.print("\n", writer);
        });
    }
}

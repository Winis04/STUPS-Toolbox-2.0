package Print;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Collection;

/**
 * My own {@link java.util.List} of {@link Printable}s. No Null-objects allowed.
 * @author Isabel
 * @since 24.11.16
 */

@SuppressWarnings("unused")
public class PrintableList extends ArrayList<Printable> implements Printable {
    @SuppressWarnings("unused")
    public PrintableList(int n) {
        super(n);
    }
    @SuppressWarnings("unused")
    public PrintableList(Collection<Printable> collection) {
        super(collection);
    }

    @SuppressWarnings("unused")
    @Override
    public boolean add(Printable p) {
        return p != null && super.add(p);
    }

    @SuppressWarnings("unused")
    @Override
    public void printLatex(BufferedWriter writer, String space) {
        this.forEach(x -> {
            Printer.print(x);
            Printer.print("\n", writer);
        });
    }

    @SuppressWarnings("unused")
    @Override
    public void printConsole(BufferedWriter writer) {
        this.forEach(x -> {
            Printer.print(x);
            Printer.print("\n", writer);
        });
    }
}

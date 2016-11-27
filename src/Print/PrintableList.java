package Print;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by isabel on 24.11.16.
 * My own List of Printables. No Null-objects allowed.
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
        if(p==null) return false;
        return super.add(p);
    }

    @Override
    public void printLatex(BufferedWriter writer, String space) {
        this.stream().forEach(x -> {
            Printer.print(x);
            Printer.print("\n",writer);
        });
    }

    @Override
    public void printConsole(BufferedWriter writer) {
        this.stream().forEach(x -> {
            Printer.print(x);
            Printer.print("\n",writer);
        });
    }
}

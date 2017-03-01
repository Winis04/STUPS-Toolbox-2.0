package Print;

import java.io.BufferedWriter;
import java.util.HashSet;
import java.util.Iterator;


/**
 * A {@link HashSet} of {@link Printable}s. Used, if you want to have a set you can print.
 * @author isabel
 * @since 24.11.16
 */
public class PrintableSet extends HashSet<Printable> implements Printable {
    /**
     * constructor
     * @param n number of elements
     */
    public PrintableSet(int n) {
        super(n);
    }


    public boolean add(Printable p) {
        return p != null && super.add(p);
    }

    @Override
    public void printConsole() {

        Printer.print("{");
        Iterator<Printable> printableIterator=this.iterator();
        while(printableIterator.hasNext()) {
            Printable p=printableIterator.next();
            Printer.print(p);
            if(printableIterator.hasNext()) {
                Printer.print(", ");
            }
        }


        Printer.print("}\n");

    }
    @Override
    public void printLatex(String space) {

        Printer.print(space+"$\\{");
        Iterator<Printable> printableIterator=this.iterator();
        while(printableIterator.hasNext()) {
            Printable p=printableIterator.next();
            Printer.print(p);
            if(printableIterator.hasNext()) {
                Printer.print(", ");
            }
        }
        Printer.print("\\}$\n");


    }

    public PrintableSet clone() {
        return (PrintableSet) super.clone();
    }
}

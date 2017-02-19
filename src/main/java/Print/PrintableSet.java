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
    public void printConsole(BufferedWriter writer) {

        Printer.print("{",writer);
        Iterator<Printable> printableIterator=this.iterator();
        while(printableIterator.hasNext()) {
            Printable p=printableIterator.next();
            Printer.print(p);
            if(printableIterator.hasNext()) {
                Printer.print(", ",writer);
            }
        }


        Printer.print("}\n",writer);

    }
    @Override
    public void printLatex(BufferedWriter writer, String space) {

        Printer.print(space+"$\\{",writer);
        Iterator<Printable> printableIterator=this.iterator();
        while(printableIterator.hasNext()) {
            Printable p=printableIterator.next();
            Printer.print(p);
            if(printableIterator.hasNext()) {
                Printer.print(", ",writer);
            }
        }
        Printer.print("\\}$\n",writer);


    }

    public PrintableSet clone() {
        return (PrintableSet) super.clone();
    }
}

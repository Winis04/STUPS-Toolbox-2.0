package Print;

import java.io.BufferedWriter;
import java.util.HashSet;
import java.util.Iterator;


/**
 * @author isabel
 * @since 24.11.16
 */
@SuppressWarnings("unused")
public class PrintableSet extends HashSet<Printable> implements Printable {
    /**
     * constructor
     * @param n number of elements
     */
    @SuppressWarnings("unused")
    public PrintableSet(int n) {
        super(n);
    }

    @SuppressWarnings("unused")
    @Override
    public boolean add(Printable p) {
        return p != null && super.add(p);
    }
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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


    @SuppressWarnings("unused")
    public PrintableSet clone() {
        return (PrintableSet) super.clone();
    }
}

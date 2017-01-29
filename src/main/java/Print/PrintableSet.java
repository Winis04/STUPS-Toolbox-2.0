package Print;

import java.io.BufferedWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * @author isabel
 * @since 24.11.16
 */
public class PrintableSet extends HashSet<Printable> implements Printable {
    private String help_text;
    public PrintableSet(int n) {
        super(n);
    }
    public PrintableSet(Collection<Printable> collection) {
        super(collection);
    }
    public PrintableSet(Set<Printable> set) {
        super(set);
    }

    @Override
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


}

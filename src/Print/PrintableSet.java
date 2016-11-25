package Print;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;


/**
 * Created by isabel on 24.11.16.
 */
public class PrintableSet extends HashSet<Printable> implements Printable {
    private String help_text;
    public PrintableSet(int n) {
        super(n);
    }
    public PrintableSet(Collection<Printable> collection) {
        super(collection);
    }

    @Override
    public void print() {
        switch (Printer.printmode) {
            case NO:
                break;
            case CONSOLE:
                this.printConsole();
                break;
            case LATEX:
                this.printLatex();
        }
    }
    @Override
    public boolean add(Printable p) {
        if(p==null) return false;
        return super.add(p);
    }

    private void printConsole() {

        Printer.print("{");
        Iterator<Printable> printableIterator=this.iterator();
        while(printableIterator.hasNext()) {
            Printable p=printableIterator.next();
            p.print();
            if(printableIterator.hasNext()) {
                Printer.print(", ");
            }
        }


        Printer.print("}\n");

    }
    private void printLatex() {
        Printer.print("$\\{");
        Iterator<Printable> printableIterator=this.iterator();
        while(printableIterator.hasNext()) {
            Printable p=printableIterator.next();
            p.print();
            if(printableIterator.hasNext()) {
                Printer.print(", ");
            }
        }
        Printer.print("\\}$\n");


    }
}

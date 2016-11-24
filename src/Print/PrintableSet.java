package Print;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static Print.Printer.writer;

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
    private void printConsole() {
        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(System.out));
        try {
            writer.write("{");
            writer.flush();
            this.stream().forEach(x -> x.print());
            writer.flush();
            writer.write("}\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void printLatex() {
        try {
            writer.write("$\\{");
            this.stream().forEach(x -> x.print());
            writer.write("\\}$\n");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

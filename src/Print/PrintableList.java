package Print;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by isabel on 24.11.16.
 */
public class PrintableList extends ArrayList<Printable> implements Printable {
    public PrintableList(int n) {
        super(n);
    }
    public PrintableList(Collection<Printable> collection) {
        super(collection);
    }
    @Override
    public void print() {
        this.stream().forEach(x -> x.print());
    }
}

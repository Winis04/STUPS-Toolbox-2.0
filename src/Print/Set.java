package Print;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by isabel on 24.11.16.
 */
public class Set extends ArrayList<Printable> implements Printable {
    private String help_text;
    public Set(int n) {
        super(n);
    }
    public Set(Collection<Printable> collection) {
        super(collection);
    }

    @Override
    public void print() {
        //TODO: print help_text in right mode
        this.stream().forEach(x -> x.print());
    }

    @Override
    public void setText(String text) {
        this.help_text=text;
    }

    @Override
    public String getText() {
        return this.help_text;
    }
}

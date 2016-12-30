package GrammarSimulator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Isabel on 27.12.2016.
 */
public class ComparableList<E> extends ArrayList<E> {
    public ComparableList(List<E> rightSide) {
        this.addAll(rightSide);

    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof ComparableList) {
            ComparableList other = (ComparableList) o;
            if(this.size()==other.size()) {
                boolean all = true;
                for(int i=0;i<this.size();i++) {
                    all &= this.get(i).equals(other.get(i));
                }
                return all;
            }
        }
        return false;
    }
    @Override
    public boolean contains(Object o) {
        return this.stream().anyMatch(elem -> elem.equals(o));
    }
}

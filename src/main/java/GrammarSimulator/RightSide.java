package GrammarSimulator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Isabel on 27.12.2016.
 */
public class RightSide<E> extends ArrayList<E> {
    public RightSide(List<E> rightSide) {
        this.addAll(rightSide);

    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof RightSide) {
            RightSide other = (RightSide) o;
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

package GrammarSimulator;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Isabel on 22.10.2016.
 */
public class UnitRule implements Symbol {
    private HashSet<Nonterminal> follower;
    private String name;
    public  UnitRule(Nonterminal s, HashSet<ArrayList<Symbol>> right) {
        this.name=s.getName();
        follower=new HashSet<>();
        for(ArrayList<Symbol> list : right) {
            if(list.size()==1 && list.get(0) instanceof Nonterminal) {
                follower.add((Nonterminal) list.get(0));
            }
        }
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {

    }
}

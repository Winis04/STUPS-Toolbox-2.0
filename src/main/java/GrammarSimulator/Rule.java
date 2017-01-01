package GrammarSimulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by isabel on 22.12.16.
 */
public final class Rule {
    private final Nonterminal comingFrom;
    private final List<Symbol> rightSide;

    public Rule(Nonterminal comingFrom, List<Symbol> rightSide) {
        this.comingFrom = comingFrom;

        this.rightSide = rightSide;

    }

    public Rule(Rule old) {
        this.comingFrom = new Nonterminal(old.getComingFrom().getName());

        List<Symbol> list =old.getRightSide().stream().map(symbol ->{
            if (symbol instanceof Nonterminal) {
                return new Nonterminal(symbol.getName());
            } else {
                return new Terminal(symbol.getName());
            }
        }).collect(Collectors.toList());
       this.rightSide = new ArrayList<>(list);
    }

    public Nonterminal getComingFrom() {
        return comingFrom;
    }


    public List<Symbol> getRightSide() {
        return Collections.unmodifiableList(new ArrayList<>(rightSide));
    }



    public Rule copy() {
        return new Rule(this);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Rule) {
            Rule other = (Rule) o;
            if(this.comingFrom.equals(other.comingFrom) && this.rightSide.equals(other.rightSide)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.comingFrom.hashCode();
    }
}

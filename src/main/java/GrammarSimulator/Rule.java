package GrammarSimulator;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by isabel on 22.12.16.
 */
public final class Rule {
    private final Nonterminal comingFrom;
    private final ComparableList<Symbol> comparableList;

    public Rule(Nonterminal comingFrom, List<Symbol> rightSide) {
        this.comingFrom = comingFrom;

        this.comparableList = new ComparableList<>(rightSide);

    }

    public Rule(Rule old) {
        this.comingFrom = new Nonterminal(old.getComingFrom().getName());

        List<Symbol> list =old.getComparableList().stream().map(symbol ->{
            if (symbol instanceof Nonterminal) {
                return new Nonterminal(symbol.getName());
            } else {
                return new Terminal(symbol.getName());
            }
        }).collect(Collectors.toList());
       this.comparableList = new ComparableList<>(list);
    }

    public Nonterminal getComingFrom() {
        return comingFrom;
    }


    public List<Symbol> getComparableList() {
        return Collections.unmodifiableList(comparableList);
    }



    public Rule copy() {
        return new Rule(this);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Rule) {
            Rule other = (Rule) o;
            if(this.comingFrom.equals(other.comingFrom) && this.comparableList.equals(other.comparableList)) {
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

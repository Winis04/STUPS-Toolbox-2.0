package GrammarSimulator;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

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
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
       Rule rhs = (Rule) obj;
        if(rhs.rightSide.size() != this.rightSide.size()) {
            return false;
        }
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(rhs.comingFrom,this.comingFrom);
        for(int i=0;i<this.rightSide.size();i++) {
            builder.append(rhs.rightSide.get(i),this.rightSide.get(i));
        }
        return builder.isEquals();
    }


    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(17,31);
        builder.append(this.comingFrom);
        this.rightSide.forEach(sym -> builder.append(sym));
        return builder.toHashCode();
    }
}

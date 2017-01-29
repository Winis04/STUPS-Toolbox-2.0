package GrammarSimulator;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * this class describes a production rule of a formal {@link Grammar}.
 * Every rule contains a {@link Nonterminal} on the left side, and a list of {@link Symbol}s on the right side.
 * That means, the toolbox is only capable of describing context free grammars //TODO
 * @author isabel
 * @since 22.12.16
 */
public final class Rule {
    private final Nonterminal comingFrom;
    private final List<Symbol> rightSide;

    /**
     * the constructor for the rule
     * @param comingFrom the left side of the rule
     * @param rightSide the right side of the rule
     */
    public Rule(Nonterminal comingFrom, List<Symbol> rightSide) {
        this.comingFrom = comingFrom;

        this.rightSide = rightSide;

    }


    /**
     * Getter-Method for {@link #comingFrom}
     * @return a {@link Nonterminal}, the left side of the rule
     */
    public Nonterminal getComingFrom() {
        return comingFrom;
    }


    /**
     * Getter-Method for {@link #rightSide}
     * @return a {@link java.util.Collections.UnmodifiableList} of {@link Symbol}s, the right side of the rule.
     */
    public List<Symbol> getRightSide() {
        return Collections.unmodifiableList(new ArrayList<>(rightSide));
    }


    /**
     * copy-method
     * @return a copy of this rule
     */
    public Rule copy() {
        return new Rule(this.comingFrom,this.rightSide);
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
        this.rightSide.forEach(builder::append);
        return builder.toHashCode();
    }
}

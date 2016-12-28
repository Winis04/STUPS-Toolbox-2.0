package GrammarSimulator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isabel on 22.12.16.
 */
public class Rule {
    private Nonterminal comingFrom;
    private RightSide<Symbol> rightSide;

    public Rule(Nonterminal comingFrom) {
        this.comingFrom = comingFrom;
        this.rightSide = new RightSide<>();
    }

    public Rule(Nonterminal comingFrom, ArrayList<Symbol> rightSide) {
        this.comingFrom = comingFrom;

        this.rightSide = new RightSide<>();
        this.rightSide.addAll(rightSide);

    }

    public Rule(Rule old) {
        this.comingFrom = new Nonterminal(old.getComingFrom().getName());
        this.rightSide = new RightSide<>();
        old.getRightSide().forEach(symbol -> {
            if (symbol instanceof Nonterminal) {
                this.rightSide.add(new Nonterminal(symbol.getName()));
            } else {
                this.rightSide.add(new Terminal(symbol.getName()));
            }
        });
    }

    public Nonterminal getComingFrom() {
        return comingFrom;
    }

    public void setComingFrom(Nonterminal comingFrom) {
        this.comingFrom = comingFrom;
    }

    public RightSide<Symbol> getRightSide() {
        return rightSide;
    }

    public void setRightSide(List<Symbol> rightSide) {
        this.rightSide = new RightSide<>();
        this.rightSide.addAll(rightSide);
    }
    public void setRightSide(RightSide<Symbol> rightSide) {
        this.rightSide = rightSide;
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

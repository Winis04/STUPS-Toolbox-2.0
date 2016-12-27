package GrammarSimulator;

import java.util.ArrayList;

/**
 * Created by isabel on 22.12.16.
 */
public class Rule {
    private Nonterminal comingFrom;
    private ArrayList<Symbol> rightSide;

    public Rule(Nonterminal comingFrom) {
        this.comingFrom = comingFrom;
        this.rightSide = new ArrayList<>();
    }

    public Rule(Nonterminal comingFrom, ArrayList<Symbol> rightSide) {
        this.comingFrom = comingFrom;

        this.rightSide = rightSide;

    }

    public Rule(Rule old) {
        this.comingFrom = new Nonterminal(old.getComingFrom().getName());
        this.rightSide = new ArrayList<>();
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

    public ArrayList<Symbol> getRightSide() {
        return rightSide;
    }

    public void setRightSide(ArrayList<Symbol> rightSide) {
        this.rightSide = rightSide;
    }

    public Rule copy() {
        return new Rule(this);
    }
}

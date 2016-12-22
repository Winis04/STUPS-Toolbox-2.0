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
        rightSide = new ArrayList<>();
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
}

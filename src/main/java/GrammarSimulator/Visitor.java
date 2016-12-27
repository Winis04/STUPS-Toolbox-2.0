package GrammarSimulator;

import GrammarParser.analysis.DepthFirstAdapter;
import GrammarParser.node.*;
import GrammarSimulator.Grammar;
import GrammarSimulator.Nonterminal;
import GrammarSimulator.Symbol;
import GrammarSimulator.Terminal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by fabian on 06.08.16.
 */
public class Visitor extends DepthFirstAdapter {

    private Grammar grammar;
    private Nonterminal startSymbol;
    private HashMap<String, Terminal> terminals;
    private HashMap<String, Nonterminal> nonterminals;
    private HashSet<Rule> rules;

    @Override
    public void inASymbols(ASymbols node) {
        terminals = new HashMap<>();
        nonterminals = new HashMap<>();
        rules = new HashSet<>();

        for(TIdentifier nonterminal : node.getNonterminals()) {
            nonterminals.put(nonterminal.getText(), new Nonterminal(nonterminal.getText()));
        }

        for(TSymbol terminal : node.getTerminals()) {
            terminals.put(terminal.getText().replaceAll("'", ""), new Terminal(terminal.getText().replaceAll("'", "")));
        }

        startSymbol = new Nonterminal(node.getStartSymbol().getText());
        nonterminals.put(startSymbol.getName(), startSymbol);
    }

    /**
     * checks, if the occurring symbols are in the Terminal or the nonterminal set
     * An exception is epsilon, which is added to the terminal alphabet by this method.
     *
     * @author Fabian Ruhland, Isabel Wingen
     * @param node
     */
    @Override
    public void inARule(ARule node) {
        if(!nonterminals.containsKey(node.getComingFrom().getText())) {
            System.out.println("Nonterminal " + node.getComingFrom().getText() + " has not been defined!");
            System.exit(1);
        }

        Rule rule = new Rule(new Nonterminal(node.getComingFrom().getText()));
        ArrayList<Symbol> rightSide = new ArrayList<>();

        for(TSymbol symbol : node.getGoingTo()) {
            if(terminals.containsKey(symbol.getText().replaceAll("'", ""))) {
                rightSide.add(new Terminal(symbol.getText().replaceAll("'", "")));
            }
            else if((nonterminals.containsKey(symbol.getText().replaceAll("'", "")))) {
                rightSide.add(new Nonterminal(symbol.getText().replaceAll("'", "")));
            }
            else {
                //TODO: fix this. if it is an epsilon, add it to the set
                System.out.println("Symbol " + symbol.getText() + " has not been defined!");
                System.exit(1);
            }
        }
        rule.setRightSide(rightSide);
        rules.add(rule);
    }

    @Override
    public void outARoot(ARoot node) {
        startSymbol.markAsStart();
        grammar = new Grammar(new HashSet<>(terminals.values()), new HashSet<>(nonterminals.values()), startSymbol,rules);

    }

    public Grammar getGrammar() {

        return  grammar;
    }
}

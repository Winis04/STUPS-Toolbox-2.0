package GrammarSimulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by isabel on 19.01.17.
 */
public class Configuration {
    private final List<Symbol> config;
    private final Configuration previous;
    private final Grammar grammar;
    public Configuration(List<Symbol> config, Configuration previous, Grammar grammar) {
        this.previous = previous;
        this.config = config;
        this.grammar = grammar;
    }

    public List<Symbol> getConfig() {
        return Collections.unmodifiableList(new ArrayList<>(config));
    }

    public Configuration getPrevious() {
        return previous;
    }

    public HashSet<Configuration> getChildren() {
        HashSet<Configuration> result = new HashSet<>();
        int j=0;
        for(int i=0;i<config.size();i++) {
            if(config.get(i) instanceof Nonterminal) {
                j=i;
                break;
            }
        }
        Nonterminal first = (Nonterminal) config.get(j); //this is the first Nonterminal in the configuration
        List<List<Symbol>> childrenRules = grammar.getRules()
                .stream()
                .filter(rule -> rule.getComingFrom().equals(first))
                .map(Rule::getRightSide)
                .collect(Collectors.toList()); //contains every Rule that is an ancestor of the first Nonterminal
        for(List<Symbol> list : childrenRules) { //replace the first Nonterminal through its ancestors and add the new config to the result
            List<Symbol> tmp = new ArrayList<>();
            config.forEach(sym -> {
                if (sym.equals(first)) {
                    tmp.addAll(list);
                } else {
                    tmp.add(sym);
                }
            });
            result.add(new Configuration(tmp,this,grammar));
        }
        return result;

    }
}

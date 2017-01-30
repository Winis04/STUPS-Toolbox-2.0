package GrammarSimulator;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A "Configuration" is one step in a derivation of a {@link Grammar}.
 * @author isabel
 * @since 19.01.17
 */
public class Configuration {
    private final List<Symbol> config;
    private final Configuration previous;
    private final Grammar grammar;
    Configuration(List<Symbol> config, Configuration previous, Grammar grammar) {
        this.previous = previous;
        this.config = config;
        this.grammar = grammar;


    }

    /**
     * returns a list with the symbols of this configuration
     * @return a {@link List}. This list contains every symbol of this configuration
     */
    public List<Symbol> getConfig() {
        return Collections.unmodifiableList(new ArrayList<>(config));
    }

    Configuration getPrevious() {
        return previous;
    }



    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Configuration rhs = (Configuration) obj;
        EqualsBuilder equalsBuilder = new EqualsBuilder();
        if(rhs.getConfig().size() != getConfig().size()) {
            return false;
        }
        for(int i=0;i<rhs.config.size();i++) {
            equalsBuilder.append(rhs.config.get(i),this.config.get(i));
        }
        return equalsBuilder.isEquals();
    }


    @Override
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(17,31);
        this.config.forEach(hashCodeBuilder::append);
        return hashCodeBuilder.hashCode();
    }

    /**
     * computes the children of this config. The children are exactly the configuration we can obtain if we
     * derive the first nonterminal
     * @return a {@link HashSet} of Configuration
     */
    public HashSet<Configuration> getChildren() {
        HashSet<Configuration> result = new HashSet<>();
        int j=0;
        if(config.stream().allMatch(sym -> sym.equals(Terminal.NULLSYMBOL))) {
            return result;
        }
        for(int i=0;i<config.size();i++) {
            if(config.get(i) instanceof Nonterminal) {
                j=i;
                break;
            }
        }
        int k=j;
        Symbol first = config.get(j);
        grammar.getRules().stream()
                .filter(rule -> rule.getComingFrom().equals(first))
                .map(Rule::getRightSide)
                .forEach(list -> {
                    List<Symbol> tmp = new ArrayList<>();
                    tmp.addAll(config);
                    tmp.remove(k);
                    if (!list.stream().allMatch(sym -> sym.equals(Terminal.NULLSYMBOL))) {
                        list = list.stream().filter(x -> !x.equals(Terminal.NULLSYMBOL)).collect(Collectors.toList());
                        tmp.addAll(k,list);
                    }
                    result.add(new Configuration(tmp,this,grammar));
                });
        return result;
    }
}

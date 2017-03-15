package GrammarSimulator;


import Main.Storable;
import Print.Printable;
import Print.Printer;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.util.stream.Collectors.joining;

/**
 * The Model for formal grammars.
 * @author fabian
 * @since 06.08.16
 */
public final class Grammar implements Printable, Storable {


    /**
     * The grammar's terminal symbols.
     */
    private final HashSet<Terminal> terminals;

    /**
     * The grammar's nonterminal symbols.
     */
    private final HashSet<Nonterminal> nonterminals;

    /**
     * The grammar's start symbol.
     */
    private final Nonterminal startSymbol;

    /**
     * the grammars rule
     */
    private final HashSet<Rule> rules;
    /**
     * the grammars name (optional)
     */
    private final String name;


    private final Grammar previousVersion;

    /**
     * The Constructor with every field.
     * Grammars are immutable, so the fields can't be filled later.
     * @param startSymbol the start{@link Symbol} of the Grammar
     * @param rules a {@link HashSet} of production {@link Rule}s
     * @param name the name of the grammar
     * @param previousVersion the previous version of this grammar (null if there isn't any)
     */
    public Grammar(Nonterminal startSymbol, HashSet<Rule> rules, String name, Grammar previousVersion) {
        this.startSymbol = startSymbol;
        this.rules = rules;
        this.name = name;
        this.previousVersion = previousVersion;
        this.terminals = new HashSet<>();
        this.nonterminals = new HashSet<>();
        this.rules.stream().map(Rule::getRightSide).forEach(list -> list.forEach(sym -> {
            if (sym instanceof Terminal) {
                terminals.add((Terminal) sym);
            } else {
                nonterminals.add((Nonterminal) sym);
            }
        }));
        nonterminals.add(startSymbol);
        this.rules.stream().map(Rule::getComingFrom).forEach(nonterminals::add);
    }
    /**
     * The Constructor with every field.
     * Grammars are immutable, so the fields can't be filled later.
     * @param startSymbol the start{@link Symbol} of the Grammar
     * @param rules a {@link Set} of production {@link Rule}s
     * @param name the name of the grammar
     * @param previousVersion the previous version of this grammar (null if there isn't any)
     */
    public Grammar(Nonterminal startSymbol, Set<Rule> rules, String name, Grammar previousVersion) {
        this.startSymbol = startSymbol;
        this.rules = new HashSet<>(rules);
        this.name = name;
        this.previousVersion = previousVersion;
        this.terminals = new HashSet<>();
        this.nonterminals = new HashSet<>();
        this.rules.stream().map(Rule::getRightSide).forEach(list -> list.forEach(sym -> {
            if (sym instanceof Terminal) {
                terminals.add((Terminal) sym);
            } else {
                nonterminals.add((Nonterminal) sym);
            }
        }));
        this.rules.stream().map(Rule::getComingFrom).forEach(nonterminals::add);
    }


    /**
     * an empty constructor. Use only if you truly want a minimal grammar
     */
    public Grammar() {
        this.name="G";
        this.previousVersion=null;
        this.rules = new HashSet<>();
        this.nonterminals = new HashSet<>();
        this.terminals = new HashSet<>();
        nonterminals.add(new Nonterminal("S"));
        terminals.add(new Terminal("a"));
        List<Symbol> tmp = new ArrayList<>();
        tmp.add(new Terminal("a"));
        rules.add(new Rule(new Nonterminal("S"),tmp));
        this.startSymbol = new Nonterminal("S");
    }

    @Override
    public void printLatex(String space) {
        ArrayList<ArrayList<String>> header= GrammarUtil.getHeader(this,true);
        Printer.print(space+"$"+Printer.toLatex(this.getName())+"=(\\{");
        Printer.print(space+header.get(0).stream().map(Printer::makeToGreek).collect(joining(", ")));

        Printer.print("\\},\\;\\{ ");

        Printer.print(header.get(1).stream().collect(joining(", ")));
        Printer.print("\\},\\;");

        Printer.print(header.get(2).get(0));

        Printer.print(",\\;"+"R)");
        Printer.print("$ with\n");
        Printer.print(space+"\\begin{align*}\n");

        Printer.print(space+"\t"+"R"+"=\\{");


        Printer.print(GrammarUtil.getNonterminalsInOrder(this).stream().
                map(nonterminal -> {
                    String start="\t"+Printer.toLatex(nonterminal.getName()) + " &\\rightarrow ";
                    start += getRules().stream().filter(rule -> rule.getComingFrom().equals(nonterminal))
                            .map(Rule::getRightSide)
                            .map(list -> list.stream()
                                    .map(Symbol::getName)
                                    .map(Printer::toLatex)
                                    .collect(joining("")))
                            .collect(joining("\\;|\\;"));
                    return start;
                }).collect(joining(", \\\\ \n"+space)));

        Printer.print("\\}\n");
        Printer.print(space+"\\end{align*}\n");


    }
    @Override
    public void printConsole() {

        Printer.print(this.getName()+"\n");

        ArrayList<ArrayList<String>> header=GrammarUtil.getHeader(this,false);

        Printer.print("{");
        Printer.print(header.get(0).stream().collect(joining(", ")));

        Printer.print("; ");

        Printer.print(header.get(1).stream().collect(joining(", ")));

        Printer.print("; ");
        Printer.print(header.get(2).get(0));

        Printer.print("}\n\n");

        OptionalInt max = this.nonterminals.stream().mapToInt(nt -> nt.getName().length()).max();

        for(Nonterminal nt : GrammarUtil.getNonterminalsInOrder(this)) {
            if(max.isPresent()) {
                Printer.print(Printer.fill(nt.getName(),max.getAsInt()) + " --> ");
            } else {
                Printer.print(nt.getName() + " --> ");
            }
            Printer.print(getRules().stream().filter(rule -> rule.getComingFrom().equals(nt))
                    .map(Rule::getRightSide)
                    .map(list -> list.stream().map(Symbol::getName).collect(joining(" ")))
                    .collect(joining(" | ")));
//            HashSet<ArrayList<Symbol>> tmp=nt.getSymbolLists();
           // HashSet<ArrayList<String>> tmp=getRulesToNonterminal(grammar,nt);
            //Printer.print(tmp.stream().map(list -> list.stream().collect(joining(""))).collect(joining(" | ")));
            Printer.print("\n");

        }

    }




    public String getName() {
        return this.name;
    }

    @Override
    public Storable otherName(String name) {
        return new Grammar(this.getStartSymbol(),this.getRules(),name,this);
    }


    @Override
    public Storable deep_copy() {

        return new Grammar(this.startSymbol,this.rules,this.name,this.previousVersion);
    }

    @Override
    public void printToSave(String path) throws IOException {
        GrammarUtil.save(this,path);
    }

    @Override
    public Storable restoreFromFile(File file) throws Exception {
        return GrammarUtil.parse(file);

    }


    @Override
    public Storable getPreviousVersion() {
        if(previousVersion != null) {
            return previousVersion;
        } else {
            return null;
        }
    }



    /* GETTER **/
    /**
     * Getter-method for {@link #terminals}.
     *
     * @return {@link #terminals}
     */
    public Set<Terminal> getTerminals() {
        return Collections.unmodifiableSet(new HashSet<>(terminals));
    }

    /**
     * Getter-method for {@link #nonterminals}.
     *
     * @return {@link #nonterminals}
     */
    public Set<Nonterminal> getNonterminals() {
        return Collections.unmodifiableSet(new HashSet<>(nonterminals));
    }

    /**
     * Getter-method for {@link #startSymbol}.
     *
     * @return {@link #startSymbol}
     */
    public Nonterminal getStartSymbol() {
        return startSymbol;
    }


    /**
     * Getter-method for {@link #rules}
     *
     * @return {@link #rules}
     */
    public Set<Rule> getRules() {
        return Collections.unmodifiableSet(new HashSet<>(rules));
    }




}

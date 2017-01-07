package GrammarSimulator;


import Main.Storable;
import GrammarParser.lexer.LexerException;
import GrammarParser.parser.ParserException;
import Print.Printable;
import Print.Printer;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static Print.Printer.makeToGreek;

import static java.util.stream.Collectors.joining;

/**
 * Created by fabian on 06.08.16.
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
        this.rules.stream().map(Rule::getComingFrom).forEach(nonterminals::add);
    }

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


    //DO NOT USE
    public Grammar() {
        this.name="G";
        this.previousVersion=null;
        this.rules = new HashSet<>();
        this.nonterminals = new HashSet<>();
        this.terminals = new HashSet<>();
        this.startSymbol = new Nonterminal("S");
    }

    @Override
    public void printLatex(BufferedWriter writer, String space) {
        ArrayList<String>[] header= GrammarUtil.getHeader(this);
        Printer.print(space+"$"+this.getName()+"=\\left(\\{",writer);
        Printer.print(space+header[0].stream().map(string -> makeToGreek(string)).collect(joining(", ")),writer);

        Printer.print("\\},\\;\\{ ",writer);

        Printer.print(header[1].stream().collect(joining(", ")),writer);
        Printer.print("\\},\\;",writer);

        Printer.print(header[2].get(0),writer);

        Printer.print(",\\;"+this.getRuleSetName()+"\\right)",writer);
        Printer.print("$ with\n",writer);
        Printer.print(space+"\\begin{align*}\n",writer);

        Printer.print(space+"\t"+this.getRuleSetName()+"=\\{",writer);


        Printer.print(GrammarUtil.getNonterminalsInOrder(this).stream().
                map(nonterminal -> {
                    String start="\t"+nonterminal.getName() + " &\\rightarrow ";
                    start += getRules().stream().filter(rule -> rule.getComingFrom().equals(nonterminal))
                            .map(Rule::getRightSide)
                            .map(list -> list.stream()
                                    .map(Symbol::getName)
                                    .map(Printer::makeToGreek)
                                    .collect(joining("")))
                            .collect(joining("\\;|\\;"));
                    return start;
                }).collect(joining(", \\\\ \n"+space)),writer);

        Printer.print("\\}\n",writer);
        Printer.print(space+"\\end{align*}\n",writer);


    }
    @Override
    public void printConsole(BufferedWriter writer) {

        Printer.print(this.getName()+"\n",writer);

        ArrayList<String>[] header=GrammarUtil.getHeader(this);

        Printer.print("{",writer);
        Printer.print(header[0].stream().collect(joining(", ")),writer);

        Printer.print("; ",writer);

        Printer.print(header[1].stream().collect(joining(", ")),writer);

        Printer.print("; ",writer);
        Printer.print(header[2].get(0),writer);

        Printer.print("}\n\n",writer);


        for(Nonterminal nt : GrammarUtil.getNonterminalsInOrder(this)) {
            Printer.print(nt.getName() + " --> ",writer);
            Printer.print(getRules().stream().filter(rule -> rule.getComingFrom().equals(nt))
                    .map(Rule::getRightSide)
                    .map(list -> list.stream().map(Symbol::getName).collect(joining("")))
                    .collect(joining(" | ")),writer);
//            HashSet<ArrayList<Symbol>> tmp=nt.getSymbolLists();
           // HashSet<ArrayList<String>> tmp=getRulesToNonterminal(grammar,nt);
            //Printer.print(tmp.stream().map(list -> list.stream().collect(joining(""))).collect(joining(" | ")));
            Printer.print("\n",writer);

        }

    }


    public Terminal getTerminal(String name) {
        for(Terminal t : this.terminals) {
            if(t.getName().equals(name)) {
                return t;
            }
        }
        return null;
    }
    public Nonterminal getNonterminal(String name) {
        for(Nonterminal nt : this.nonterminals) {
            if(nt.getName().equals(name)) {
                return nt;
            }
        }
        return null;
    }


    public String getName() {
        return this.name;
    }

    @Override
    public Storable otherName(String name) {
        return new Grammar(this.getStartSymbol(),this.getRules(),name,this);
    }

    public String getRuleSetName() {
        return "R";
    }

    @Override
    public Storable deep_copy() {

        return new Grammar(this.startSymbol,this.rules,this.name,this.previousVersion);
    }

    @Override
    public void printToSave(String path) {

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

    @Override
    public void savePreviousVersion() {

    }

    /** GETTER **/
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


    public Set<Rule> getRules() {
        return Collections.unmodifiableSet(new HashSet<>(rules));
    }




}

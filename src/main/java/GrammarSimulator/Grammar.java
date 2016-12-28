package GrammarSimulator;


import Main.Storable;
import GrammarParser.lexer.LexerException;
import GrammarParser.parser.ParserException;
import Print.Printable;
import Print.Printer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static Print.Printer.makeToGreek;

import static java.util.stream.Collectors.joining;

/**
 * Created by fabian on 06.08.16.
 */
public class Grammar implements Printable, Storable {


    /**
     * The grammar's terminal symbols.
     */
    private HashSet<Terminal> terminals;

    /**
     * The grammar's nonterminal symbols.
     */
    private HashSet<Nonterminal> nonterminals;

    /**
     * The grammar's start symbol.
     */
    private Nonterminal startSymbol;

    /**
     * the grammars rule
     */
    private HashSet<Rule> rules;
    /**
     * the grammars name (optional)
     */
    private String name=null;
    /**
     * a suffix for the name;
     */
    private String suffix="";

    private Grammar previousVersion=null;


    /**
     * The constructor for an empty grammar.
     */
    public Grammar() {
        Terminal terminal = new Terminal("a");
        this.name="G";
        ArrayList<Symbol> symbolList = new ArrayList(Arrays.asList(terminal));
        this.startSymbol = new Nonterminal("S");
        this.terminals = new HashSet<>(Collections.singletonList(terminal));
        this.nonterminals = new HashSet<>(Collections.singletonList(startSymbol));
        this.rules = new HashSet<>();
    }

    /**
     * The constructor for a grammar with a given set of terminals and nonterminals.
     *
     * @param terminals The grammar's set of terminals.
     * @param nonterminals The grammar's set of nonterminals.
     * @param startSymbol The grammar's start symbol.
     */
    public Grammar(HashSet<Terminal> terminals, HashSet<Nonterminal> nonterminals, Nonterminal startSymbol) {
        this.terminals = terminals;
        this.nonterminals = nonterminals;
        this.startSymbol = startSymbol;
        this.rules = new HashSet<>();
    }

    /**
     * The constructor for a grammar with a given set of terminals and nonterminals.
     *
     * @param terminals The grammar's set of terminals.
     * @param nonterminals The grammar's set of nonterminals.
     * @param startSymbol The grammar's start symbol.
     */
    public Grammar(HashSet<Terminal> terminals, HashSet<Nonterminal> nonterminals, Nonterminal startSymbol, HashSet<Rule> rules) {
        this.terminals = terminals;
        this.nonterminals = nonterminals;
        this.startSymbol = startSymbol;
        this.rules = rules;
    }

    /**
     * deep copy constructor
     * @param old the grammar that should be copied
     */
    public Grammar(Grammar old, boolean newName) {
        this.terminals=new HashSet<>();
        this.nonterminals=new HashSet<>();
        for(Terminal t : old.getTerminals()) { //adds the Terminals
            this.terminals.add(new Terminal(t.getName()));
        }
        for(Nonterminal nt : old.getNonterminals())  {
            this.nonterminals.add(new Nonterminal(nt.getName()));
        }
        this.name=old.getNameWithoutSuffix();
        this.suffix=old.getSuffix();
        if(newName) {
            this.modifyName();
        }
        this.startSymbol=new Nonterminal(old.getStartSymbol().getName());
        this.rules = new HashSet<>();
        for(Rule rule : old.getRules()) {
            this.rules.add(rule.copy());
        }
        this.previousVersion = (Grammar) old.getPreviousVersion();

    }




    @Override
    public void printLatex(BufferedWriter writer, String space) {
        ArrayList<String>[] header= GrammarUtil.getHeader(this);
        Printer.print(space+"$"+this.getNameWithSuffix()+"=\\left(\\{",writer);
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

        Printer.print(this.getNameWithSuffix()+"\n",writer);

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

    public String getNameWithSuffix() {
        if(name==null) {
            return "G"+suffix;
        } else {
            return name+suffix;
        }
    }
    public String getNameWithoutSuffix() {
        if(name==null) {
            return "G";
        } else {
            return name;
        }
    }
    public String getName() {
        return getNameWithoutSuffix();
    }

    public void setName(String name) {
        this.name = name;
    }
    public void modifyName() {
        this.suffix+="'";
    }
    public void clearName() {
        this.suffix="";
    }

    public String getRuleSetName() {
        return "R" + suffix;
    }
    public String getSuffix() {
        return suffix;
    }

    @Override
    public Storable deep_copy() {
        return new Grammar(this, false);
    }

    @Override
    public void printToSave(String path) {
      GrammarUtil.save(this,path);
    }

    @Override
    public Storable restoreFromFile(File file) {
        try {
            return GrammarUtil.parse(file);
        } catch (ParserException e) {
            e.printStackTrace();
        } catch (LexerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }




    @Override
    public void savePreviousVersion() {
        this.previousVersion = (Grammar) this.deep_copy();
    }

    @Override
    public Storable getPreviousVersion() {
        if(previousVersion != null) {
            return previousVersion;
        } else {
            return null;
        }
    }

    /** GETTER AND SETTER **/
    /**
     * Getter-method for {@link #terminals}.
     *
     * @return {@link #terminals}
     */
    public HashSet<Terminal> getTerminals() {
        return terminals;
    }

    /**
     * Getter-method for {@link #nonterminals}.
     *
     * @return {@link #nonterminals}
     */
    public HashSet<Nonterminal> getNonterminals() {
        return nonterminals;
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
     * Setter-method for {@link #startSymbol}.
     *
     * @param startSymbol The new value for {@link #startSymbol}.
     */
    public void setStartSymbol(Nonterminal startSymbol) {
        this.startSymbol = startSymbol;
    }

    public HashSet<Rule> getRules() {
        return rules;
    }

    public void setRules(HashSet<Rule> rules) {
        this.rules = rules;
    }
}

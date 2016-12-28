package GrammarSimulator;

import GrammarParser.lexer.Lexer;
import GrammarParser.lexer.LexerException;
import GrammarParser.node.Start;
import GrammarParser.parser.Parser;
import GrammarParser.parser.ParserException;
import Print.Dummy;
import Print.Printable;
import Print.PrintableSet;
import Print.Printer;
import PushDownAutomatonSimulator.*;



import java.io.*;
import java.sql.Wrapper;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * Created by fabian on 06.08.16.
 */
public class GrammarUtil {

    /******************************************************************************************************************
     * ---------------------------------------------------------------------------------------------------------------*
     * -                      First some private methods. Scroll down to see the public methods.                     -*
     * ---------------------------------------------------------------------------------------------------------------*
     ******************************************************************************************************************/

    /**
     * Returns an ArrayList that contains all terminals, that are somehow reachable from the start symbol,
     * in order of their appearance in the grammars's rules.
     *
     * @param comingFrom For the initial call, this should be the grammar's start symbol.
     * @param terminals Holds the added terminals. Should initially be empty.
     * @param visitedSymbols Keeps track of which nonterminals have already been visited. Should initially be empty.
     * @return The ArrayList.
     */
    private static ArrayList<Terminal> getTerminalsInOrder(Nonterminal comingFrom, ArrayList<Terminal> terminals, HashSet<Nonterminal> visitedSymbols, Grammar grammar) {
        if (!visitedSymbols.contains(comingFrom)) {
            HashSet<Nonterminal> nextSymbols = new HashSet<>();

            //Add all terminals, that comingFrom points to to the list
            //and all nonterminals, that comingFrom points to, to nextSymbols.
            grammar.getRules().stream()
                    .filter(rule -> rule.getComingFrom().equals(comingFrom))
                    .map(rule -> rule.getRightSide()).forEach(rightSide -> {
                rightSide.stream().forEach(symbol -> {
                    if(symbol instanceof  Terminal && !terminals.contains(symbol)) {
                        terminals.add((Terminal) symbol);
                    } else if(symbol instanceof  Nonterminal) {
                        nextSymbols.add((Nonterminal) symbol);
                    }
                });
            });

            visitedSymbols.add(comingFrom);

            //Call this method recursively with all symbols in nextSymbols as comingFrom.
            for (Nonterminal goingTo : nextSymbols) {
                getTerminalsInOrder(goingTo, terminals, visitedSymbols,grammar);
            }
        }

        return terminals;
    }

    /**
     * Returns an ArrayList that contains all nonterminals, that are somehow reachable from the start symbol,
     * in order of their appearance in the grammars's rules.
     *
     * @param comingFrom For the initial call, this should be the grammar's start symbol.
     * @param nonterminals Should be empty initially.
     * @return The ArrayList.
     */
    private static ArrayList<Nonterminal> getNonterminalsInOrder(Nonterminal comingFrom, ArrayList<Nonterminal> nonterminals, Grammar grammar) {
        HashSet<Nonterminal> nextSymbols = new HashSet<>();

        try {
            //Determine, whether comingFrom's name should be add, or not.
            //If yes, add it to the list.
            if (!nonterminals.contains(comingFrom)) {
                nonterminals.add(comingFrom);
            }

            //Do the same for all symbols, that comingFrom points to.
            grammar.getRules().stream()
                    .filter(rule -> rule.getComingFrom().equals(comingFrom))
                    .map(rule -> rule.getRightSide())
                    .forEach(rightSide -> {
                        rightSide.stream().forEach(symbol -> {
                            if(symbol instanceof Nonterminal && !nonterminals.contains(symbol)) {
                                nonterminals.add((Nonterminal) symbol);
                                nextSymbols.add((Nonterminal) symbol);
                            }
                        });
                    });
        } catch(Exception e) {
            e.printStackTrace();
        }

        //Call this method recursively with all symbols in nextSymbols as comingFrom.
        for(Nonterminal goingTo : nextSymbols) {
            getNonterminalsInOrder(goingTo, nonterminals,grammar);
        }

        return nonterminals;
    }

    /**
     * Writes all of the grammars's terminal symbols through a given BufferedWriter.
     *
     * @param writer The BufferedWriter.
     * @param grammar The grammar.
     */
    private static void writeTerminals(BufferedWriter writer, Grammar grammar) {
        //Get all of the grammar's terminals in order of their appearance in the rules.
        ArrayList<Terminal> terminals = getTerminalsInOrder(grammar);
        Iterator<Terminal> it = terminals.iterator();

        //Write the terminals.
        try {
            while(it.hasNext()) {
                Terminal currentTerminal = it.next();
                writer.write("'" + currentTerminal.getName() + "'");
                if(it.hasNext()) {
                    writer.write(", ");
                }
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeNonterminals(BufferedWriter writer, Grammar grammar) {
        ArrayList<Nonterminal> nonterminals = getNonterminalsInOrder(grammar);
        Iterator<Nonterminal> it = nonterminals.iterator();

        try {
            while(it.hasNext()) {
                Nonterminal currentNonterminal = it.next();
                writer.write(currentNonterminal.getName());
                if(it.hasNext()) {
                    writer.write(", ");
                }
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes all of the grammars's nonterminal symbols through a given BufferedWriter.
     *
     * @param writer The BufferedWriter.
     * @param grammar The grammar.
     */
    private static void writeRules(BufferedWriter writer, Grammar grammar) {
        //Get all of the grammar's nonterminal symbols in order of their appearance in the rules.
        ArrayList<Nonterminal> nonterminals = getNonterminalsInOrder(grammar);

        //Iterate through all symbol lists of every nonterminal and print them.
        try {
            for (Nonterminal nonterminal : nonterminals) {
                grammar.getRules().stream()
                        .filter(rule -> rule.getRightSide().equals(nonterminal))
                        .map(rule -> rule.getRightSide())
                        .forEach(symbolList -> {
                            try {
                                writer.write(nonterminal.getName() + " --> ");
                                Iterator<Symbol> it = symbolList.iterator();
                                while (it.hasNext()) {
                                    Symbol currentSymbol = it.next();
                                    writer.write("'" + currentSymbol.getName() + "'");

                                    if (it.hasNext()) {
                                        writer.write(",");
                                    }
                                }
                                writer.newLine();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculates the first-set of a given list of symbols.
     * This is needed by {@link #llParsingTable(Grammar)}.
     *
     * @param symbolList The list of symbols.
     * @param nullable The grammar's nullabel-set
     * @param firsts The grammar's first-sets.
     * @param grammar The grammar.
     * @return The first-set.
     */
    private static HashSet<Terminal> calculateFirstOfList(ArrayList<Symbol> symbolList, HashSet<Nonterminal> nullable, HashMap<Nonterminal, HashSet<Terminal>> firsts, Grammar grammar) {
        HashSet<Terminal> result = new HashSet<>();

        for(int i = 0; i < symbolList.size(); i++) {
            if(symbolList.get(i) instanceof Terminal) {
                //If the current symbol is a terminal, add it to the result-set.
                result.add((Terminal) symbolList.get(i));
                if(!symbolList.get(i).getName().endsWith("epsilon") ) {
                    break;
                }
            } else {
                //If the current symbol is a nonterminal, add its first-set to the result-set.
                result.addAll(firsts.get(symbolList.get(i)));
                if(!nullable.contains(symbolList.get(i))) {
                    break;
                } else {
                    //If the current symbol is nullable, add the empty word symbol to the result-set.
                    for(Terminal terminal : grammar.getTerminals()) {
                        if(terminal.getName().equals("epsilon")) {
                            result.add(terminal);
                            break;
                        }
                    }
                }
            }
        }

        return result;
    }

    /******************************************************************************************************************
     * ---------------------------------------------------------------------------------------------------------------*
     * -                                The public methods follow after this comment.                                -*
     * ---------------------------------------------------------------------------------------------------------------*
     ******************************************************************************************************************/

    /**
     * Takes an input-string and parses it into a grammar. Typically the input-string comes from a file,
     * that has been loaded completely into that string.
     *
     * @param fileInput The input-string.
     * @return The grammar.
     * @throws ParserException {@link ParserException}.
     * @throws LexerException {@link LexerException}.
     * @throws IOException {@link IOException}.
     */
    public static Grammar parse(String fileInput) throws ParserException, LexerException, IOException {
        StringReader reader = new StringReader(fileInput);
        PushbackReader r = new PushbackReader(reader, 100);
        Lexer l = new Lexer(r);
        Parser parser = new Parser(l);
        Start start = parser.parse();
        Visitor visitor = new Visitor();
        start.apply(visitor);
        Grammar g = visitor.getGrammar();
        GrammarUtil.replaceLambda(g);
        return g;
    }

    public static Grammar parse(File file) throws IOException, LexerException, ParserException {
        String name = file.getName();
        Grammar grammar = null;
        BufferedReader grammarReader = new BufferedReader(new FileReader(file));
        String string = "";
        String line;
        while ((line = grammarReader.readLine()) != null) {
            string = string + line + "\n";
        }
        grammar = GrammarUtil.parse(string);
        grammar.setName(name);
        grammarReader.close();
        return grammar;

    }

    /**
     * Returns an ArrayList that contains all terminals in order of their appearance in the grammars's rules.
     *
     * @param grammar The grammar.
     * @return The ArrayList.
     */
    public static ArrayList<Terminal> getTerminalsInOrder(Grammar grammar) {
        ArrayList<Terminal> terminals = getTerminalsInOrder(grammar.getStartSymbol(), new ArrayList<>(), new HashSet<>(),grammar);
        HashSet<Terminal> missingTerminals = new HashSet<>(grammar.getTerminals());
        missingTerminals.removeAll(terminals);
        terminals.addAll(missingTerminals);
        return terminals;
    }

    /**
     * Returns an ArrayList that contains all nonterminals in order of their appearance in the grammars's rules.
     *
     * @param grammar The grammar.
     * @return The ArrayList.
     */
    public static ArrayList<Nonterminal> getNonterminalsInOrder(Grammar grammar) {
        ArrayList<Nonterminal> nonterminals = getNonterminalsInOrder(grammar.getStartSymbol(), new ArrayList<>(),grammar);
        HashSet<Nonterminal> missingTerminals = new HashSet<>(grammar.getNonterminals());
        missingTerminals.removeAll(nonterminals);
        nonterminals.addAll(missingTerminals);
        return nonterminals;
    }

    /**
     * Prints a given grammar to stdout.
     * It does the same as {@link #save(Grammar, String)}, but uses System.out instead of a filename for the BufferedWriter.
     *
     * @param grammar The grammar.
     */
    public static void print(Grammar grammar) {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

        try {
            writer.write("{");

            writeTerminals(writer, grammar);
            writer.write("; ");

            writeNonterminals(writer, grammar);
            writer.write("; ");

            writer.write(grammar.getStartSymbol().getName());

            writer.write("}\n\n");
            writer.flush();

            writeRules(writer, grammar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void save(Grammar grammar, File file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            writer.write("{");

            writeTerminals(writer, grammar);
            writer.write("; ");

            writeNonterminals(writer, grammar);
            writer.write("; ");

            writer.write(grammar.getStartSymbol().getName());

            writer.write("}\n\n");

            writeRules(writer, grammar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Writes a given grammar to a file, so it can be loaded again later.
     *
     * @param grammar The grammar.
     * @param fileName The filename.
     */
    public static void save(Grammar grammar, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

            writer.write("{");

            writeTerminals(writer, grammar);
            writer.write("; ");

            writeNonterminals(writer, grammar);
            writer.write("; ");

            writer.write(grammar.getStartSymbol().getName());

            writer.write("}\n\n");

            writeRules(writer, grammar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes either the grammar's terminals, nonterminals, or rules through a given BufferedWriter.
     * Only one of the three booleans is supposed to be set to true.
     *
     * @param writer The BufferedWriter.
     * @param grammar The grammar.
     * @param terminals If true, the grammar's terminals will be written.
     * @param nonterminals If true, the grammar's nonterminals will be written.
     * @param rules If true, the grammar's rules will be written.
     */
    public static void writePart(BufferedWriter writer, Grammar grammar, boolean terminals, boolean nonterminals, boolean rules) {
        //Write terminals.
        if(terminals) {
            writeTerminals(writer, grammar);
            try {
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        //Write nonterminals.
        if(nonterminals) {
            writeNonterminals(writer, grammar);
            try {
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        //Write rules.
        if(rules) {
            writeRules(writer, grammar);
            try {
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
    }

    /**
     * Calculates the nullable-set of a given grammar.
     *
     * @param grammar The grammar.
     * @return A HashSet containing all nullable nonterminals,
     */
    public static PrintableSet calculateNullableAsPrintable(Grammar grammar) {
        HashSet<Nonterminal> res = calculateNullable(grammar);
        PrintableSet result = new PrintableSet(res.size());
        res.forEach(nt -> result.add(nt));
        return  result;
    }
    /**
     * Calculates the nullable-set of a given grammar.
     *
     * @param grammar The grammar.
     * @return A HashSet containing all nullable nonterminals,
     */
    public static HashSet<Nonterminal> calculateNullable(Grammar grammar) {



        HashSet<Nonterminal> nullable = new HashSet<>();
        boolean changed=true;
        while(changed) {
            changed = false;
            HashSet<Nonterminal> newNullables = new HashSet<>();

            grammar.getRules().stream()
                    //all rules that point on lambda//nullables
                    .filter(rule -> rule.getRightSide().stream().allMatch(symbol -> nullable.stream().anyMatch(elem -> elem.equals(symbol)) || symbol.equals(Terminal.NULLSYMBOL)))
                    .map(Rule::getComingFrom)
                    .filter(nt -> !nullable.stream().anyMatch(elem -> elem.equals(nt))) //TODO: without this, it does not work
                    .forEach(newNullables::add);
            int sizeBefore = nullable.size();
            nullable.addAll(newNullables);
            int sizeAfter = nullable.size();
            if(sizeBefore!=sizeAfter) {
                changed = true;
            }


        }
        return nullable;
    }

    /**
     * Calculates the first-sets of a given grammar.
     *
     * @param grammar The grammar.
     * @return A HashMap, that has an entry for every of the grammar's nonterminals,
     *         which is mapped to a HashSet containing its first-set.
     */
    public static HashMap<Nonterminal, HashSet<Terminal>> calculateFirst(Grammar grammar) {
        HashMap<Nonterminal, HashSet<Terminal>> result = new HashMap<>();
        HashSet<Nonterminal> nullable = calculateNullable(grammar);

        //Initialize the result-map.
        for(Nonterminal nonterminal : grammar.getNonterminals()) {
            result.put(nonterminal, new HashSet<>());
        }
        ArrayList<Boolean> wrapper = new ArrayList<>();
        wrapper.set(0,new Boolean(true));
        while(wrapper.get(0)) {
            wrapper.set(0,new Boolean(false));
            for (Nonterminal nonterminal : grammar.getNonterminals()) {

                grammar.getRules().stream().filter(rule -> rule.getComingFrom().equals(nonterminal))
                        .map(rule -> rule.getRightSide()).forEach(symbolList -> {
                    for (int i = 0; i < symbolList.size(); i++) {
                        if (symbolList.get(i) instanceof Terminal) {
                            if (!symbolList.get(i).getName().equals("epsilon") ) {
                                if(!result.get(nonterminal).contains(symbolList.get(i))) {
                                    //If the current symbol is a terminal and not the empty word,
                                    //add it to the first-set of the current nonterminal.
                                    result.get(nonterminal).add((Terminal) symbolList.get(i));
                                    wrapper.set(0,new Boolean(true));
                                }
                                break;
                            }
                        } else if (symbolList.get(i) instanceof Nonterminal) {
                            if(!result.get(nonterminal).containsAll(result.get(symbolList.get(i)))) {
                                //If the current symbol is a nonterminal, add its first-set to the first-set of the current nonterminal.
                                result.get(nonterminal).addAll(result.get(symbolList.get(i)));
                                wrapper.set(0,new Boolean(true));
                            }
                            if (!nullable.contains(symbolList.get(i))) {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }); //
            }
        }

        return result;
    }

    /**
     * Calculates the follow-set of a given grammar.
     *
     * @param grammar The grammar.
     * @return A HashMap, that has an entry for every of the grammar's nonterminals,
     *         which is mapped to a HashSet containing its follow-set.
     */
    public static HashMap<Nonterminal, HashSet<Terminal>> calculateFollow(Grammar grammar) {
        HashMap<Nonterminal, HashSet<Terminal>> result = new HashMap<>();
        HashSet<Nonterminal> nullable = calculateNullable(grammar);
        HashMap<Nonterminal, HashSet<Terminal>> firsts = calculateFirst(grammar);
        ArrayList<Boolean> wrapper = new ArrayList<>();
        wrapper.add(new Boolean(true));

        //Initialize the result-map.
        for(Nonterminal nonterminal : grammar.getNonterminals()) {
            result.put(nonterminal, new HashSet<>());
        }
        result.get(grammar.getStartSymbol()).add(new Terminal("$"));

        while(wrapper.get(0)) {
            wrapper.set(0,new Boolean(false));
            for (Nonterminal nonterminal : grammar.getNonterminals()) {
                grammar.getRules().stream().filter(rule -> rule.getComingFrom().equals(nonterminal))
                        .map(rule -> rule.getRightSide()).forEach(symbolList -> {
                    //Go through each list of symbols and calculate the follow-set of each nonterminal in these lists.
                    for (int i = 0; i < symbolList.size(); i++) {
                        if (symbolList.get(i) instanceof Nonterminal) {
                            if (i == symbolList.size() - 1) {
                                if (symbolList.get(i) instanceof Nonterminal) {
                                    //If the last symbol of the current list is a nonterminal, the follow-set of this nonterminal
                                    //contains the follow-set of the current nonterminal.
                                    if (!result.get(symbolList.get(i)).containsAll(result.get(nonterminal))) {
                                        result.get(symbolList.get(i)).addAll(result.get(nonterminal));
                                        wrapper.set(0,new Boolean(true));
                                    }
                                }
                            } else {
                                //symbolList.get(i) is always the Nonterminal whose follow-set we are currently calculating.

                                int j = i + 1;
                                while ((symbolList.get(j).getName().equals("epsilon")) && j < symbolList.size()) {
                                    //Skip all empty words.
                                    j++;
                                }

                                if (symbolList.get(j) instanceof Nonterminal) {
                                    if (!result.get(symbolList.get(i)).containsAll(firsts.get(symbolList.get(j)))) {
                                        //If the current symbol is a nonterminal, we have to add its first-set
                                        //to the follow-set of symbolList.get(i).
                                        result.get(symbolList.get(i)).addAll(firsts.get(symbolList.get(j)));
                                        wrapper.set(0,new Boolean(true));
                                    }

                                    if (nullable.contains(symbolList.get(j)) && j < symbolList.size() - 1) {
                                        //If the current symbol is nullable, we have to go on, until we find a symbol,
                                        //that isn't nullable.
                                        int k = j + 1;
                                        do {
                                            if(symbolList.get(k) instanceof Terminal && (!symbolList.get(k).getName().equals("epsilon"))) {
                                                if(!result.get(symbolList.get(i)).contains(symbolList.get(k))) {
                                                    //If the current symbol is a terminal, add it to the follow-set of symbolList.get(i).
                                                    result.get(symbolList.get(i)).add((Terminal) symbolList.get(k));
                                                    wrapper.set(0,new Boolean(true));
                                                }
                                            } else if (symbolList.get(k) instanceof Nonterminal) {
                                                if(!result.get(symbolList.get(i)).containsAll(firsts.get(symbolList.get(k)))) {
                                                    //If the current symbol is a nonterminal, add its first-set to the follow-set of symbolList.get(i).
                                                    result.get(symbolList.get(i)).addAll(firsts.get(symbolList.get(k)));
                                                    wrapper.set(0,new Boolean(true));
                                                }
                                            }
                                            k++;
                                        } while (k < symbolList.size() && nullable.contains(symbolList.get(k)));

                                        if (k == symbolList.size() - 1 && nullable.contains(symbolList.get(k))) {
                                            if(!result.get(symbolList.get(i)).containsAll(result.get(nonterminal))) {
                                                //If the do-while-loop has reached the last symbol of the current list
                                                //and this symbol is a nonterminal, we have to add the follow-set of this nonterminal
                                                //to the follow-set of symbol-list.get(i);
                                                result.get(symbolList.get(i)).addAll(result.get(nonterminal));
                                                wrapper.set(0,new Boolean(true));
                                            }
                                        }
                                    } else if (j == symbolList.size() - 1 && nullable.contains(symbolList.get(j))) {
                                        if(!result.get(symbolList.get(i)).containsAll(result.get(nonterminal))) {
                                            //If result.get(j) has reached the last symbol of the current list
                                            //and this symbol is a nonterminal, we have to add the follow-set of this nonterminal
                                            //to the follow-set of symbol-list.get(i);
                                            result.get(symbolList.get(i)).addAll(result.get(nonterminal));
                                            wrapper.set(0,new Boolean(true));
                                        }
                                    }

                                } else {
                                    if(!result.get(symbolList.get(i)).contains(symbolList.get(j))) {
                                        //If symbolList.get(j) is a terminal, we can just add it to the follow-set of symbolList.get(i).
                                        result.get(symbolList.get(i)).add((Terminal) symbolList.get(j));
                                        wrapper.set(0,new Boolean(true));
                                    }
                                }
                            }
                        }
                    }
                });//
            }
        }

        return result;
    }

    /**
     * Calculates the LL-Parsing-Table for a given grammar.
     *
     * @param grammar The grammar.
     * @return A HashMap with a key-set of nonterminals, that contains another HashMap with a key-set of terminals.
     *         An entry in the table can be read by calling result.get(nonterminal).get(terminal).
     *         This will return a set of strings that represent this entry's rules.
     */
    public static HashMap<Nonterminal, HashMap<Terminal, HashSet<String>>> llParsingTable(Grammar grammar) {
        HashMap<Nonterminal, HashMap<Terminal, HashSet<String>>> result = new HashMap<>();
        HashSet<Nonterminal> nullable = calculateNullable(grammar);
        HashMap<Nonterminal, HashSet<Terminal>> firsts = calculateFirst(grammar);
        HashMap<Nonterminal, HashSet<Terminal>> follows = calculateFollow(grammar);

        Terminal eof = null;

        //Get the eof-character.
        for(Terminal terminal : follows.get(grammar.getStartSymbol())) {
            if(terminal.getName().equals("$")) {
                eof = terminal;
            }
        }

        //Initialize the result-map.
        for(Nonterminal nonterminal : grammar.getNonterminals()) {
            result.put(nonterminal, new HashMap<>());
            for(Terminal terminal : grammar.getTerminals()) {
                result.get(nonterminal).put(terminal, new HashSet<>());
            }
            result.get(nonterminal).put(eof, new HashSet<>());
        }

        //Iterate through every first-set of every rule.
        for(Nonterminal nonterminal : grammar.getNonterminals()) {
            grammar.getRules().stream().filter(rule -> rule.getComingFrom().equals(nonterminal))
                    .map(rule -> rule.getRightSide()).forEach(symbolList -> {
                HashSet<Terminal> currentFirst = calculateFirstOfList(symbolList, nullable, firsts, grammar);
                for(Terminal terminal : currentFirst) {
                    //Add the current rule to result.get(nonterminal).get(terminal).
                    StringBuilder sb = new StringBuilder(nonterminal.getName() + " --> ");
                    Iterator<Symbol> it = symbolList.iterator();

                    while(it.hasNext()) {
                        sb.append(it.next().getName());
                        if(it.hasNext()) {
                            sb.append(", ");
                        }
                    }

                    result.get(nonterminal).get(terminal).add(sb.toString());

                    //Check if the current rule is nullable.
                    boolean isNullable = false;
                    for(Terminal nullableTerminal : currentFirst) {
                        if(nullableTerminal.getName().equals("epsilon")) {
                            isNullable = true;
                        }
                    }

                    if(isNullable) {
                        //If the current rule is nullable, add an entry for every terminal
                        //in the follow-set of the current nonterminal.
                        for(Terminal followTerminal : follows.get(nonterminal)) {
                            result.get(nonterminal).get(followTerminal).add(sb.toString());
                        }
                    }
                }
            }); //
        }

        return result;
    }


    private static void removeUnneccesaryEpsilons(Grammar g) {
        g.getRules().forEach(rule -> {
            if (rule.getRightSide().size() != 1) {
                if(rule.getRightSide().stream().allMatch(symbol -> symbol.equals(Terminal.NULLSYMBOL))) {
                    List<Symbol> tmp = new RightSide<>();
                    tmp.add(Terminal.NULLSYMBOL);
                    rule.setRightSide(tmp);
                } else {
                    List<Symbol> tmp = new RightSide<>();
                    tmp = rule.getRightSide().stream()
                            .filter(symbol -> !symbol.equals(Terminal.NULLSYMBOL))
                            .collect(Collectors.toList());
                    rule.setRightSide(tmp);
                }
            }
        });

    }

    private static void replaceLambda(Grammar g) {
        g.getRules().forEach(rule -> {
            RightSide<Symbol> right = new RightSide<Symbol>();
            rule.getRightSide().forEach(sym -> {
                if(sym.getName().equals("epsilon")||sym.getName().equals("lambda")) {
                    right.add(Terminal.NULLSYMBOL);
                } else {
                    right.add(sym);
                }
            });
            rule.setRightSide(right);
        });
    }
    /******************************************************************************************************************
     * ---------------------------------------------------------------------------------------------------------------*
     * -                                Remove Lambda Rules                                                          -*
     * ---------------------------------------------------------------------------------------------------------------*
     ******************************************************************************************************************/
    public static ArrayList<Printable> removeLambdaRules(Grammar grammar) {
        grammar.savePreviousVersion();
        ArrayList<Printable> res=new ArrayList<>(4);
        //0. before Grammar
        Grammar grammar0=new Grammar(grammar,false);

        //0.5 special Rule
        Grammar grammar05=new Grammar(grammar0,false);
        boolean additional=specialRuleForEmptyWord(grammar05);
        if(additional) {
            grammar05.modifyName();
        }
        //1. Nullable Set
        PrintableSet nullable_and_printable=GrammarUtil.calculateNullableAsPrintable(grammar05);

        //2. step two && unneccesaryepsilons
        Grammar grammar2=new Grammar(grammar05,true);
        HashSet<Nonterminal> nullable=GrammarUtil.calculateNullable(grammar2);
        removeLambdaRules_StepTwo(grammar2,nullable);
        removeUnneccesaryEpsilons(grammar2);
        //3. step three
        Grammar grammar3=new Grammar(grammar2,true);
        removeLambdaRules_StepThree(grammar3,true);
        res.add(grammar0);
        if(additional) {
            res.add(grammar05);
        }
        res.add(nullable_and_printable);
        res.add(grammar2);
        res.add(grammar3);
        /** change original grammar **/
        specialRuleForEmptyWord(grammar);
        nullable=GrammarUtil.calculateNullable(grammar);
        removeLambdaRules_StepTwo(grammar,nullable);
        removeUnneccesaryEpsilons(grammar);
        removeLambdaRules_StepThree(grammar,true);
        return res;

    }

    /**
     * the third step of the algorithm to delete lambda-rules
     * all rules which have only epsilons on the right side are removed
     * furthermore, nonterminals, which now do not appear on the rigth side of any rule, are removed
     * @param g the grammar g
     * @param again first time calling: true. during the algorithm new lambda-rules can emerge, so that method
     *              has to be called again, but this time with "again" set to false
     */
    private static void removeLambdaRules_StepThree(Grammar g, boolean again) {
        boolean startSymbolPointsOnLamda=GrammarUtil.startSymbolPointsOnLambda(g);
        // delete lambda-rules
        HashSet<Rule> tmp2 = new HashSet<>();
        g.getRules().stream()
                .filter(rule -> !rule.getRightSide().stream().allMatch(symbol -> symbol.equals(Terminal.NULLSYMBOL)))
                .forEach(rule -> tmp2.add(rule));
        g.setRules(tmp2);
        //these nonterminals can be removed. Store them in toRemove
        List<Symbol> toRemove = new ArrayList<>();
        for(Nonterminal nt : g.getNonterminals()) {
            if(g.getRules().stream().allMatch(rule -> !rule.getComingFrom().equals(nt))) {
                toRemove.add(nt);
            }
        }
        // the symbols that can be removed are replaced by the nullsymbol
        g.getRules().stream().forEach(rule -> {
            RightSide<Symbol> tmpList = new RightSide<Symbol>();
            RightSide<Symbol> list = rule.getRightSide();
            for(int i=0;i<list.size();i++) {
                if(toRemove.contains(list.get(i))) {
                    tmpList.add(Terminal.NULLSYMBOL);
                } else {
                    tmpList.add(list.get(i));
                }
                rule.setRightSide((RightSide<Symbol>) tmpList);
            }
        });
        // the nonterminal set of the grammar needs to be updated
        ArrayList<Symbol> bla=new ArrayList<>();
        bla.addAll(g.getNonterminals());
        g.getNonterminals().clear();
        for(Symbol nonterminal : bla) {
            if(!toRemove.contains((Nonterminal) nonterminal)) {
                g.getNonterminals().add((Nonterminal) nonterminal);
            }
        }
        // to find any new lambdas and lambda-rules
        if(again) {
            GrammarUtil.removeUnneccesaryEpsilons(g);
            GrammarUtil.removeLambdaRules_StepThree(g,false);
            g.getTerminals().remove(Terminal.NULLSYMBOL);
        }
        if(startSymbolPointsOnLamda) {
            RightSide<Symbol> tmp=new RightSide<>();
            tmp.add(Terminal.NULLSYMBOL);
            g.getTerminals().add(Terminal.NULLSYMBOL);
            Rule rule = new Rule(g.getStartSymbol());
            rule.setRightSide(tmp);
            g.getRules().add(rule);
        }

    }

    private static boolean containsNullable(Rule rule, HashSet<Nonterminal> nullable) {
        return rule.getRightSide().stream()
                .anyMatch(symbol -> nullable.stream().anyMatch(elem -> elem.equals(symbol)));
    }
    /**
     * the second step of the algorithm to delete lambda-rules.
     * For every rule, that contains a nullable symbol on the right side, a rule with this symbol replaced
     * by "epsilon" is added to the ruleset
     * @author Isabel Wingen
     * @param grammar The Grammar
     * @param nullable The set, which contains all the nullable terminals
     */
    private static void removeLambdaRules_StepTwo(Grammar grammar, HashSet<Nonterminal> nullable) {
        boolean pointsOnLambda=GrammarUtil.startSymbolPointsOnLambda(grammar);
        GrammarUtil.removeUnneccesaryEpsilons(grammar);
        Queue<Rule> queue = new LinkedList<>();
        RightSide<Rule> alreadySeen = new RightSide<>();
        grammar.getRules().stream().filter(rule -> containsNullable(rule,nullable))
                .forEach(queue::add);
        if(!queue.isEmpty()) {
            boolean changed= true;
            while(changed) {
                System.out.println(": "+ queue.stream()
                        .map(rule ->
                                rule.getComingFrom().getName()+" --> "+rule.getRightSide().stream()
                                        .map(s -> s.getName()).collect(joining(", ")))
                        .collect(joining("||")));
                changed=false;
                Rule current = queue.poll();
                Rule fresh = new Rule(current.getComingFrom());
                RightSide<Symbol> freshRight = new RightSide<>();
                freshRight.addAll(current.getRightSide());
                fresh.setRightSide(freshRight);
                for(int i=0;i<current.getRightSide().size();i++) {
                    // if the i-th Symbol is a nullable symbol, remove it and replace it with lambda
                    int finalI = i;
                    if(nullable.stream().anyMatch(elem -> elem.equals(fresh.getRightSide().get(finalI)))) {
                        fresh.getRightSide().set(i,Terminal.NULLSYMBOL);
                        if(queue.stream().anyMatch(elem -> elem.equals(fresh)) || alreadySeen.contains(fresh)) {
                            // if the queue already contains this new Rule, undo the changes and go on with the rule
                            fresh.getRightSide().set(i,current.getRightSide().get(i));
                        } else {
                            queue.add(fresh);
                            queue.add(current);
                            changed = true; //--> change
                            break;
                        }
                    }
                }
                //if there can't be anything changed about this rule, remove it **/
                alreadySeen.add(current);
            }
            grammar.getRules().addAll(queue);
            grammar.getRules().addAll(alreadySeen);
        }

        /** old
        grammar.getNonterminals().forEach(nonterminal -> {
            //contains all rules for this nonterminal which need to be edited
            Queue<RightSide<Symbol>> queue = new LinkedList<>();

            queue.addAll(GrammarUtil.getSymbolListsWithoutEmptyRules(nonterminal, grammar));
            if (!queue.isEmpty()) {
                boolean changed = true;
                //contains every rule that is already edited
                HashSet<ArrayList<Symbol>> alreadySeen = new HashSet<>();
                while (changed && !queue.isEmpty()) { //stop, if there is no change anymore
                    changed = false;
                    // gets the current head of the queue and removes it
                    RightSide<Symbol> current = queue.poll();

                    RightSide<Symbol> newRightSide = new RightSide<>();
                    newRightSide.addAll(current);
                    for (int i = 0; i < current.size(); i++) {
                        // if the i-th Symbol is a nullable symbol, remove it and replace it with lambda
                        //if(nullable.stream().anyMatch(nullables -> nullables.getNameWithSuffix().equals(current.get(i).getNameWithSuffix())))
                        int finalI = i;
                        if (nullable.stream().anyMatch(elem -> elem.equals(current.get(finalI)))) {
                            newRightSide.set(i, Terminal.NULLSYMBOL);
                            if (queue.contains(newRightSide)) {
                                // if the queue already contains this new Rule, undo the changes and go on with the rule
                                newRightSide.set(i, current.get(i)); // --> no change
                            } else {
                                //if not, add the rule and after it the current rule. go on
                                queue.add(newRightSide);
                                queue.add(current);
                                // both rules are now added to the alreadySeen List
                                alreadySeen.add(newRightSide);
                                alreadySeen.add(current);
                                changed = true; //--> change
                                break;
                            }
                        }
                    }
                    // if nothing was changed, check if the rule was already seen
                    if (!changed) {
                        // if yes, add it at the end
                        if (alreadySeen.contains(current)) {
                            queue.add(current);
                        } else {
                            alreadySeen.add(current);
                            changed = true;
                        }

                    }
                }
                queue.forEach(list -> {
                    Rule rule = new Rule(nonterminal,list);
                    grammar.getRules().add(rule);
                });
                alreadySeen.stream().filter(list -> !queue.stream().anyMatch(elem -> elem.equals(list))).forEach(list -> {
                    grammar.getRules().add(new Rule(nonterminal,list));
                });
            }
        });
        if(pointsOnLambda) {
            ArrayList<Symbol> tmp=new ArrayList<>();
            tmp.add(Terminal.NULLSYMBOL);
            grammar.getTerminals().add(Terminal.NULLSYMBOL);
            grammar.getRules().add(new Rule(grammar.getStartSymbol(),tmp));
        } **/
    }

    /******************************************************************************************************************
     * ---------------------------------------------------------------------------------------------------------------*
     * -                                eliminate Unit Rules                                                         -*
     * ---------------------------------------------------------------------------------------------------------------*
     ******************************************************************************************************************/

    public static ArrayList<Printable> eliminateUnitRules(Grammar grammar) {
        grammar.savePreviousVersion();
        ArrayList<Printable> res=new ArrayList<>(3);


        Grammar grammar1=new Grammar(grammar,true);
        GrammarUtil.removeCircleRulesAndGetUnitRules(grammar1);


        Grammar grammar2=new Grammar(grammar1,true);
        HashSet<Node> unitRules=GrammarUtil.removeCircleRulesAndGetUnitRules(grammar2);
        GrammarUtil.removeUnitRules(unitRules,grammar2);

        res.add(grammar);
        res.add(grammar1);
        res.add(grammar2);
        /** change original grammar **/
        HashSet<Node> unitRules2= removeCircleRulesAndGetUnitRules(grammar);
        removeUnitRules(unitRules2,grammar);
        return res;

    }


    /**
     * removes circles in the grammar rules
     * @author Isabel Wingen
     * @param grammar
     * @return
     */
    private static HashSet<Node> removeCircleRulesAndGetUnitRules(Grammar grammar) {
        ArrayList<Node> tmp;

        HashSet<Node> unitRules= GrammarUtil.findUnitRules(grammar);
        GrammarUtil.dfs(unitRules);
        tmp=GrammarUtil.findBackwardsEdge(unitRules);
        while(tmp!=null) {
            GrammarUtil.replaceNonterminal(tmp.get(0).getValue(), tmp.get(1).getValue(), grammar);
            unitRules= GrammarUtil.findUnitRules(grammar);
            GrammarUtil.dfs(unitRules);
            tmp=GrammarUtil.findBackwardsEdge(unitRules);
        }
        return unitRules;



    }

    /**
     * makes the nonterminals to nodes. the children are the nonterminals on which this nonterminal points
     * @autor Isabel Wingen
     * @param g
     * @return a HashSet with the Nonterminals as Nodes connected in a graph
     */
    private static HashSet<Node> findUnitRules(Grammar g) {
        HashSet<Node> result=new HashSet<>();
        g.getNonterminals().stream().
                filter(nonterminal -> GrammarUtil.isLeftSideOfUnitRule(nonterminal,g) || GrammarUtil.isRightSideOfUnitRule(nonterminal,g)).
                forEach(x -> result.add(new Node(x)));


        for(Node node : result) {
            g.getRules().stream().filter(rule -> rule.getComingFrom().equals(node.getValue()))
                    .map(rule -> rule.getRightSide()).forEach(list -> {
                if(list.size()==1 && list.get(0) instanceof Nonterminal) {
                    Nonterminal nt = (Nonterminal) list.get(0);
                    result.stream().forEach(child -> {
                        if (child.getName().equals(nt.getName())) {
                            node.getChildren().add(child);
                        }
                    });
                }
            });
        }
        return result;
    }
    private static boolean isLeftSideOfUnitRule(Nonterminal nonterminal, Grammar grammar) {
        return grammar.getRules().stream()
                .anyMatch(rule -> rule.getComingFrom().equals(nonterminal) && rule.getRightSide().size()==1 && rule.getRightSide().get(0) instanceof  Nonterminal);
    }
    private static boolean isRightSideOfUnitRule(Nonterminal right, Grammar g) {
        return g.getRules().stream()
                .anyMatch(rule -> rule.getRightSide().size()==1 && rule.getRightSide().get(0).equals(right));
    }

    /**
     * find backward edges after a dept-first-search
     * @param unitRules
     * @return
     */
    private static ArrayList<Node> findBackwardsEdge(HashSet<Node> unitRules) {
        if(unitRules.stream().allMatch(rule -> rule.getDfe()!=0)) {
            for (Node node : unitRules) {
                for (Node child : node.getChildren()) {
                    if (child.getDfs() < node.getDfs() && child.getDfe() > node.getDfe()) {
                        //backward edge found!
                        ArrayList<Node> tmp = new ArrayList<>();
                        tmp.add(node);
                        tmp.add(child);
                        return tmp;
                    }
                }
            }
        }
        return null;
    }

    /**
     * start of the dept-first search
     * @param unitRules
     */
    private static void dfs(HashSet<Node> unitRules) {
        ArrayList<Integer> df=new ArrayList<>();
        Integer dfe=new Integer(1);
        Integer dfs=new Integer(1);
        df.add(dfs);
        df.add(dfe);
        for(Node node : unitRules) {
            if(!node.isVisited()) {
                df=GrammarUtil.dfs(node,df);
            }
        }
    }

    /**
     * dept-first search on {@link Node}s
     * @author Isabel Wingen
     * @param node the current node on which we do the search
     * @param df the counter for dfe and dfs
     * @return the new dfe and dfs counter
     */
    private static ArrayList<Integer> dfs(Node node, ArrayList<Integer> df){
        node.setVisited(true);
        node.setDfs(df.get(0).intValue());
        df.set(0,new Integer(df.get(0).intValue()+1));
        for(Node child : node.getChildren()) {
            if(!child.isVisited()) {
                df=GrammarUtil.dfs(child,df);
            }
        }
        node.setDfe(df.get(1).intValue());
        df.set(1,new Integer(df.get(1).intValue()+1));
        return df;
    }



    /**
     * removes the unit rules in a Grammar, only possible if there are no circles
     * TODO What does this do? why does it return a list? is nowhere used
     * @param nodes the nonterminals as nodes. to obtain them, use
     * @param g the grammar g
     * @return a sorted List of Nodes, that has the right order for the third step of the remove Unit Rule algorithm
     */
    private static ArrayList<Node> removeUnitRules(HashSet<Node> nodes, Grammar g) {
        ArrayList<Node> sorted=GrammarUtil.bringNonterminalsInOrder(nodes,g);
        for(int i=0;i<sorted.size();i++) {
           Node current=sorted.get(i); //the current node
            for(Node child : current.getChildren()) { //add all rules from the child to the current node
                g.getRules().stream().filter(rule -> rule.getComingFrom().equals(child.getValue()))
                        .forEach(rule -> {
                            Rule tmp = rule.copy();
                            tmp.setComingFrom(current.getValue());
                            g.getRules().add(tmp);
                        });
            }
        }

        // remove the unit rules
        HashSet<Rule> withoutUnit = new HashSet<>();
        g.getRules().stream()
                .filter(rule -> rule.getRightSide().size()>1 || rule.getRightSide().get(0) instanceof Terminal)
                .forEach(withoutUnit::add);
        g.setRules(withoutUnit);
        return sorted;
    }

    /**
     * @author Isabel Wingen
     * @param nodes
     * @param g
     * @return
     */
    private static ArrayList<Node> bringNonterminalsInOrder(HashSet<Node> nodes, Grammar g) {
        //find start node
        Node start=null;
        ArrayList<Node> result=new ArrayList<>();
        for(Node node : nodes) {
            if(node.getValue().equals(g.getStartSymbol())) {
                start=node;
            }
        }

        if(start!=null) {
            HashSet<Integer> takenNumbers=new HashSet<>();
            takenNumbers.add(new Integer(0));
            // as long as some nodes have children with higher numbers, do the number-method
            while(nodes.stream().
                    anyMatch(node -> node.getChildren().stream().
                            anyMatch(child -> child.getNumber()<=node.getNumber()))) {
                GrammarUtil.number(start);
            }
        }


        for(int i=nodes.size()-1;i>=0;i--) {
            for(Node node : nodes) {
                if(node.getNumber()==i) {
                    result.add(node);
                }
            }
        }
        return result;
    }

    /**
     * numbers the parent node and all children nodes with a higher number
     * @author Isabel Wingen
     * @param node
     */
    private static void number(Node node) {
        if(node.getChildren().stream().anyMatch(child -> child.getNumber()<=node.getNumber())) {
            node.getChildren().stream().forEach(child -> child.setNumber(node.getNumber()+1));
        }
        node.getChildren().stream().forEach(child -> GrammarUtil.number(child));
    }

    /**
     * replaces a Nonterminal through another and remove it from the set
     *
     * @author Isabel Wingen
     * @param toBeReplaced the nonterminal that should be replaced
     * @param newNonterminal the new nonterminal that replaces the old
     * @param g the grammar g
     */
    private static void replaceNonterminal(Nonterminal toBeReplaced, Nonterminal newNonterminal, Grammar g) {
        g.getRules().forEach(rule -> {
            if (rule.getComingFrom().equals(toBeReplaced)) {
                rule.setComingFrom(newNonterminal);
            }
           RightSide<Symbol> newRightSide = new RightSide<Symbol>();
            rule.getRightSide().forEach(symbol -> {
                if (symbol.equals(toBeReplaced)) {
                    newRightSide.add(newNonterminal);
                } else {
                    newRightSide.add(symbol);
                }
            });
            rule.setRightSide(newRightSide);
        });

    }
    /******************************************************************************************************************
     * ---------------------------------------------------------------------------------------------------------------*
     * -                                make chomsky normal form                                                      -*
     * ---------------------------------------------------------------------------------------------------------------*
     ******************************************************************************************************************/


    public static ArrayList<Printable> chomskyNormalForm(Grammar grammar) {
        grammar.savePreviousVersion();
        ArrayList<Printable> res=new ArrayList<>(4);



        Grammar grammar1=GrammarUtil.chomskyNormalForm_StepOne(new Grammar(grammar,true));


        //step 2: remove more than two Nonterminals
        Grammar grammar2=GrammarUtil.chomskyNormalForm_StepTwo(new Grammar(grammar1,true));


        res.add(grammar);
        res.add(new Dummy());
        res.add(grammar1);
        res.add(grammar2);
        chomskyNormalForm_StepOne(grammar);
        chomskyNormalForm_StepTwo(grammar);
        return res;
    }

    private static Grammar chomskyNormalForm_StepOne(Grammar g) {
        /**
        HashSet<Nonterminal> newNonTerminals=new HashSet<>();
        for(Nonterminal nt : g.getNonterminals()) {
            for(ArrayList<Symbol> list : nt.getSymbolLists()) {
                if(list.size() > 1 && list.stream().anyMatch(symbol -> symbol instanceof Terminal)) {
                    for(Symbol sym : list) {
                        if(sym instanceof Terminal) {
                            HashSet<ArrayList<Symbol>> tmpSet=new HashSet<>();
                            ArrayList<Symbol> tmpList=new ArrayList<>();
                            tmpList.add(sym);
                            tmpSet.add(tmpList);
                            if(newNonTerminals.stream().map(x -> x.getName()).allMatch(name -> !name.equals("X_"+sym.getName()))) {
                                newNonTerminals.add(new Nonterminal("X_" + sym.getName(), tmpSet));
                            }
                        }
                    }
                }
            }
        }
        g.getNonterminals().addAll(newNonTerminals);
        for(Nonterminal nt : g.getNonterminals()) {
            for(ArrayList<Symbol> list : nt.getSymbolLists()) {
                if(list.size() > 1) {
                    for(int i=0;i<list.size();i++) {
                        if(list.get(i) instanceof Terminal) {
                            String name=list.get(i).getName();
                            for(Nonterminal z : newNonTerminals) {
                                if(z.getName().equals("X_"+name)) {
                                    list.set(i,z);
                                }
                            }
                        }
                    }
                }

            }
        }
         **/
        return g;
    }

    /**
     * step 2: modify rules with more than two nonterminals so that they only have two
     * @param g
     */
    private static Grammar chomskyNormalForm_StepTwo(Grammar g) {
        /**
        int counter=1;
        boolean changed=true;
        while(changed) {
            changed=false;
            HashSet<Nonterminal> newNonTerminals = new HashSet<>();
            for (Nonterminal nt : g.getNonterminals()) {
                for (ArrayList<Symbol> list : nt.getSymbolLists()) {
                    if (list.size() > 2) {
                        ArrayList<Symbol> listOld = new ArrayList<>();
                        ArrayList<Symbol> listNew = new ArrayList<>();
                        for (int i = 1; i < list.size(); i++) {
                            listNew.add(list.get(i));
                        }
                        HashSet<ArrayList<Symbol>> tmp = new HashSet<>();
                        tmp.add(listNew);
                        Nonterminal generatedNonTerminal = new Nonterminal("P_" + counter++, tmp);
                        list.set(1, generatedNonTerminal);
                        newNonTerminals.add(generatedNonTerminal);
                        int n = list.size();
                        for (int i = 2; i < n; i++) {
                            list.remove(2);
                        }
                        changed = true;
                    }
                }
            }
            g.getNonterminals().addAll(newNonTerminals);
        }
         **/
        return g;
    }
    public static boolean isInChomskyNormalForm(Grammar grammar) {
        return grammar.getRules().stream()
                .filter(rule -> !rule.getComingFrom().equals(grammar.getStartSymbol()))
                .map(rule -> rule.getRightSide())
                .allMatch(list -> (list.size()==1 && list.get(0) instanceof Terminal) || (list.size()==2 && list.stream().allMatch(symbol -> symbol instanceof Nonterminal)));
    }
    /******************************************************************************************************************
     * ---------------------------------------------------------------------------------------------------------------*
     * -                                           CYK                                                               -*
     * ---------------------------------------------------------------------------------------------------------------*
     ******************************************************************************************************************/

    private static Matrix createMatrix(String word) {
        Matrix m=new Matrix(word.length(),word.length()+1, word);
        return m;
    }

    public static boolean containsWord(Grammar g, String word) {
        GrammarUtil.chomskyNormalForm(g);
        Matrix matrix=GrammarUtil.cyk(g,word);
        if(matrix != null) {
            return matrix.getCell(1,word.length()-1).contains(g.getStartSymbol());
        }
        return false;
    }
    public static boolean containsWord(Grammar g, String word, Matrix matrix) {
        if(matrix != null) {
            return matrix.getCell(1,word.length()-1).contains(g.getStartSymbol());
        }
        return false;
    }

    public static Matrix cyk(Grammar g, String word) {
       if(!GrammarUtil.isInChomskyNormalForm(g)) {
           System.out.println("Is not in chomsky normal form");
           return null;
       }
        Matrix m=GrammarUtil.createMatrix(word);
        for(int i=1;i<=word.length();i++) {
            final int j=i;

            ArrayList<Nonterminal> tmp=(ArrayList<Nonterminal>)  g.getRules().stream()
                    .filter(rule -> rule.getRightSide().size()==1 && GrammarUtil.pointsOnCurrentChar(word,j,rule.getRightSide()))
                    .map(rule -> rule.getComingFrom())
                    .collect(Collectors.toList());
            for(Nonterminal nt : tmp) {
                m.addToCell(i,0,nt);
            }
        }

        for(int j=1;j<word.length();j++) {
            for(int i=1;i<=word.length()-j;i++) {
               // m.clearCell(i,j);
                for(int k=0;k<j;k++) {

                    for(Nonterminal nonterminal : g.getNonterminals()) {
                        int finalI = i;
                        int finalK = k;
                        int finalJ = j;
                        g.getRules().stream()
                                .filter(rule -> rule.getComingFrom().equals(nonterminal))
                                .map(rule -> rule.getRightSide())
                                .forEach(list -> {
                                    if(list.size()==2) {
                                        Nonterminal b= (Nonterminal) list.get(0);
                                        Nonterminal c= (Nonterminal) list.get(1);
                                        if(m.getCell(finalI, finalK).contains(b) && m.getCell(finalI + finalK +1, finalJ - finalK -1).contains(c)) {
                                            m.addToCell(finalI, finalJ,nonterminal);
                                        }
                                    }
                                });
                    }
                }
            }
        }
        return m;

    }

    public static boolean languageContainsWord(Grammar grammarOld, String word) {
        Grammar grammar = (Grammar) grammarOld.deep_copy();
        removeLambdaRules(grammar);
        eliminateUnitRules(grammar);
        chomskyNormalForm(grammar);
        return checkMatrix(cyk(grammar,word),grammar);
    }
    private static boolean checkMatrix(Matrix matrix, Grammar grammar) {
        if(matrix != null) {
           return matrix.getCell(1, matrix.getWord().length() - 1).contains(grammar.getStartSymbol());
        } else {
            return false;
        }
    }
    private static boolean pointsOnCurrentChar(String word,int i,ArrayList<Symbol> list) {
        return list.get(0).getName().equals(word.substring(i-1,i));
    }
    /******************************************************************************************************************
     * ---------------------------------------------------------------------------------------------------------------*
     * -                                           PDA                                                               -*
     * ---------------------------------------------------------------------------------------------------------------*
     ******************************************************************************************************************/
    public static PushDownAutomaton toPDA(Grammar g) {
        PushDownAutomaton pda=new PushDownAutomaton();
        State onlyState=new State(true,"z");
        pda.getStates().put("z",onlyState);
        /** fill the input- and stackalphabet **/
        for(Symbol s : g.getTerminals()) {
            PushDownAutomatonUtil.addToInputAlphabet(PushDownAutomatonUtil.asInputLetter(s),pda);
            PushDownAutomatonUtil.addToStackAlphabet(PushDownAutomatonUtil.asStackLetter(s),pda);
        }
        /** add nullsymbol **/
        PushDownAutomatonUtil.addToStackAlphabet(StackLetter.NULLSYMBOL,pda);
        PushDownAutomatonUtil.addToInputAlphabet(InputLetter.NULLSYMBOL,pda);
        /** add every nonterminal to the stack alphabet **/
        for(Symbol s : g.getNonterminals()) {
            PushDownAutomatonUtil.addToStackAlphabet(PushDownAutomatonUtil.asStackLetter(s),pda);
        }
        pda.setStartState(onlyState);
        pda.setInitalStackLetter(PushDownAutomatonUtil.getStackLetterWithName(g.getStartSymbol().getName(),pda));
        /** create the rules **/
        for(Nonterminal nonterminal : g.getNonterminals()) {
            GrammarUtil.nonterminalToRule(nonterminal,pda,g);
        }
        for(Terminal a : g.getTerminals()) {
            PushDownAutomatonSimulator.Rule tmp=new PushDownAutomatonSimulator.Rule();
            tmp.setComingFrom(pda.getStartState());
            tmp.setReadIn(pda.getInputAlphabet().get(a.getName()));
            tmp.setOldToS(pda.getStackAlphabet().get(a.getName()));
            ArrayList<StackLetter> list=new ArrayList<>();
            list.add(StackLetter.NULLSYMBOL);
            tmp.setNewToS(list);
            tmp.setGoingTo(pda.getStartState());
            pda.getStartState().getRules().add(tmp);
        }
        pda.setCurrentState(pda.getStartState());
        return pda;
    }
    private static void nonterminalToRule(Nonterminal nt, PushDownAutomaton pda, Grammar grammar) {
        grammar.getRules().stream().filter(rule -> rule.getComingFrom().equals(nt))
                .map(rule -> rule.getRightSide())
                .forEach(list -> {
                    PushDownAutomatonSimulator.Rule tmp=new PushDownAutomatonSimulator.Rule();
                    tmp.setComingFrom(pda.getStartState());
                    tmp.setReadIn(InputLetter.NULLSYMBOL);
                    System.out.println(nt.getName());
                    System.out.println(pda.getStackAlphabet().get(nt.getName()));
                    tmp.setOldToS(pda.getStackAlphabet().get(nt.getName()));
                    tmp.setGoingTo(pda.getStartState());
                    tmp.setNewToS(GrammarUtil.calculateNewTos(list,pda));
                    pda.getStartState().getRules().add(tmp);
                });
    }
    private static ArrayList<StackLetter> calculateNewTos(ArrayList<Symbol> list, PushDownAutomaton pda) {
        ArrayList<StackLetter> res=new ArrayList<>();
        for(Symbol s : list) {
            res.add(pda.getStackAlphabet().get(s.getName()));
        }
        return res;
    }

    /******************************************************************************************************************
     * ---------------------------------------------------------------------------------------------------------------*
     * -                                other things                                                                 -*
     * ---------------------------------------------------------------------------------------------------------------*
     ******************************************************************************************************************/
    static ArrayList<String>[] getHeader(Grammar grammar) {
        ArrayList<String>[] header=new ArrayList[3];
        header[0]=getTerminalsAsStrings(grammar);
        header[1]=getNonterminalsAsStrings(grammar);
        ArrayList<String> tmp=new ArrayList<>();
        tmp.add(grammar.getStartSymbol().getName());
        header[2]=tmp;
        return header;
    }

    public static ArrayList<String> getTerminalsAsStrings(Grammar grammar) {
        //Get all of the grammar's terminals in order of their appearance in the rules.
        ArrayList<Terminal> terminals = GrammarUtil.getTerminalsInOrder(grammar);
        return (ArrayList<String>) terminals.stream().map(terminal -> terminal.getName()).collect(Collectors.toList());
    }

    public static ArrayList<String> getNonterminalsAsStrings(Grammar grammar) {
        ArrayList<Nonterminal> nonterminals = GrammarUtil.getNonterminalsInOrder(grammar);
        return (ArrayList<String>) nonterminals.stream().map(nonterminal -> nonterminal.getName()).collect(Collectors.toList());
    }

    /**
     * @author Isabel Wingen
     * @param nt a nonterminal of the Grammar g
     * @param g the Grammar g
     * @return a HashSet with all right sides except the empty rule belonging to nonterminal nt
     */
    private static HashSet<RightSide<Symbol>> getSymbolListsWithoutEmptyRules(Nonterminal nt, Grammar g) {
        HashSet<RightSide<Symbol>> tmp = new HashSet<>();
        g.getRules().stream().filter(rule -> rule.getComingFrom().equals(nt)).map(Rule::getRightSide)
                .forEach(tmp::add);
        HashSet<RightSide<Symbol>> res=new HashSet<>();
        for(RightSide<Symbol> list : tmp) {
            boolean allNull;
            allNull=list.stream().allMatch(symbol -> symbol.equals(Terminal.NULLSYMBOL));
            if(!allNull) {
                res.add(list);
            }
        }
        return res;

    }

    /**
     * if the startsymbol points on lambda and if the startsymbol occurs on any side, the special rule for the empty word
     * has to be applied before the lambda-removal
     * @author Isabel Wingen
     * @param g
     * @return
     */
    public static boolean specialRuleForEmptyWord(Grammar g) {
        if(GrammarUtil.startSymbolPointsOnLambda(g) && GrammarUtil.startSymbolOnRightSide(g)) {

            Nonterminal newSymbol=new Nonterminal("S_0");
            g.getNonterminals().add(newSymbol);

            g.getRules().stream()
                    .filter(rule -> rule.getComingFrom().equals(g.getStartSymbol()))
                    .forEach(rule -> {
                        RightSide<Symbol> tmp = new RightSide<Symbol>();
                        rule.getRightSide().forEach(symbol -> {
                            if (symbol.equals(g.getStartSymbol())) {
                                tmp.add(newSymbol);
                            } else {
                                tmp.add(symbol);
                            }
                        });
                        rule.setRightSide(tmp);
                    });
            HashSet<Rule> newRules = new HashSet<>();
            g.getRules().stream().filter(rule -> rule.getRightSide().equals(g.getStartSymbol())).forEach(rule -> {
                Rule copy = rule.copy();
                copy.setComingFrom(newSymbol);
                newRules.add(copy);
            });
            g.getRules().addAll(newRules);
            return true;

        }
        return false;
    }
    public static boolean hasUnitRules(Grammar g) {
        return g.getRules().stream()
                .anyMatch(rule -> rule.getRightSide().size()==1 && rule.getRightSide().get(0) instanceof Nonterminal);
      }
    /**
     * checks, if the startsymbol occurs on a right side of any rule
     * @author Isabel Wingen
     * @param g the grammar g
     * @return true, if it is on a right side
     */
    public static boolean startSymbolOnRightSide(Grammar g) {
        return g.getRules().stream().map(rule -> rule.getRightSide())
                .anyMatch(list -> list.stream().anyMatch(symol -> symol.equals(g.getStartSymbol())));
        
    }
    /**
     * checks if a grammar is without any rules pointing on lambda
     *
     * @author Isabel Wingen
     * @param g the grammar
     * @return true, if the grammar is lambda-free
     */
    public static boolean isLambdaFree(Grammar g) {
        return g.getRules().stream().filter(rule -> !rule.getComingFrom().equals(g.getStartSymbol()))
                .allMatch(rule -> rule.getRightSide().stream().allMatch(symbol -> !symbol.equals(Terminal.NULLSYMBOL)));
    }

    /**
     * checks, if lambda is in the languague which is generated by grammar g
     * @param g
     * @return
     */
    public static boolean languageContainsLambda(Grammar g) {
       return GrammarUtil.calculateNullable(g).contains(g.getStartSymbol());
    }

    /**
     * Is there any rule in the format S --> lambda?
     * @param g
     * @return
     */
    public static boolean startSymbolPointsOnLambda(Grammar g) {
        return g.getRules().stream()
                .anyMatch(rule -> rule.getRightSide().equals(g.getStartSymbol()) && rule.getRightSide().stream().allMatch(symbol -> symbol.equals(Terminal.NULLSYMBOL)));
    }
    public static boolean isCircleFree(Grammar g) {
        //TODO
        return true;
    }
    public static <T> boolean hashSetEqual(HashSet<T> a, HashSet<T> b) {
       return a.stream().allMatch(object -> b.contains(object)) && b.stream().allMatch(object -> a.contains(object));
    }

}


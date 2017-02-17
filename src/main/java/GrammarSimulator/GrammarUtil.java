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
import java.util.*;
import java.util.stream.Collectors;



/**
 * contains the logic for {@link Grammar}s
 * @author fabian
 * @since 06.08.16
 */

@SuppressWarnings("unused")
public class GrammarUtil {

    /*
     ---------------------------------------------------------------------------------------------------------------*
     -                      First some private methods. Scroll down to see the public methods.                     -*
     ---------------------------------------------------------------------------------------------------------------*
     */

    /**
     * Returns an ArrayList that contains all terminals, that are somehow reachable from the start symbol,
     * in order of their appearance in the grammars's rules.
     *
     * @param comingFrom For the initial call, this should be the grammar's start symbol.
     * @param terminals Holds the added terminals. Should initially be empty.
     * @param visitedSymbols Keeps track of which nonterminals have already been visited. Should initially be empty.
     * @return The ArrayList.
     */
    @SuppressWarnings("unused")
    private static ArrayList<Terminal> getTerminalsInOrder(Nonterminal comingFrom, ArrayList<Terminal> terminals, HashSet<Nonterminal> visitedSymbols, Grammar grammar) {
        if (!visitedSymbols.contains(comingFrom)) {
            HashSet<Nonterminal> nextSymbols = new HashSet<>();

            //Add all terminals, that comingFrom points to to the list
            //and all nonterminals, that comingFrom points to, to nextSymbols.
            grammar.getRules().stream()
                    .filter(rule -> rule.getComingFrom().equals(comingFrom))
                    .map(Rule::getRightSide).forEach(comparableList -> comparableList.forEach(symbol -> {
                        if (symbol instanceof Terminal && !terminals.contains(symbol)) {
                            terminals.add((Terminal) symbol);
                        } else if (symbol instanceof Nonterminal) {
                            nextSymbols.add((Nonterminal) symbol);
                        }
                    }));

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
    @SuppressWarnings("unused")
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
                    .map(Rule::getRightSide)
                    .forEach(comparableList -> comparableList.forEach(symbol -> {
                        if (symbol instanceof Nonterminal && !nonterminals.contains(symbol)) {
                            nonterminals.add((Nonterminal) symbol);
                            nextSymbols.add((Nonterminal) symbol);
                        }
                    }));
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
    @SuppressWarnings("unused")
    private static void writeTerminals(boolean onGui, BufferedWriter writer, Grammar grammar) {
        //Get all of the grammar's terminals in order of their appearance in the rules.
        ArrayList<Terminal> terminals = getTerminalsInOrder(grammar);
        Iterator<Terminal> it = terminals.iterator();

        //Write the terminals.
        try {
            while(it.hasNext()) {
                Terminal currentTerminal = it.next();
                if(!currentTerminal.equals(Terminal.NULLSYMBOL)) {
                    if (onGui) {
                        writer.write("'" + currentTerminal.getDisplayName() + "'");
                    } else {
                        writer.write("'" + currentTerminal.getName() + "'");
                    }
                    if (it.hasNext()) {
                        writer.write(", ");
                    }
                }
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    private static void writeRules(BufferedWriter writer, Grammar grammar) {
        //Get all of the grammar's nonterminal symbols in order of their appearance in the rules.
        ArrayList<Nonterminal> nonterminals = getNonterminalsInOrder(grammar);

        //Iterate through all symbol lists of every nonterminal and print them.
        try {
            for (Nonterminal nonterminal : nonterminals) {
                grammar.getRules().stream()
                        .filter(rule -> rule.getComingFrom().equals(nonterminal))
                        .map(Rule::getRightSide)
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
     * @param nullable The grammar's nullable-set
     * @param firsts The grammar's first-sets.
     * @param grammar The grammar.
     * @return The first-set.
     */
    @SuppressWarnings("unused")
    private static HashSet<Terminal> calculateFirstOfList(List<Symbol> symbolList, HashSet<Nonterminal> nullable, HashMap<Nonterminal, HashSet<Terminal>> firsts, Grammar grammar) {
        HashSet<Terminal> result = new HashSet<>();

        for (Symbol aSymbolList : symbolList)
            if (aSymbolList instanceof Terminal) {
                //If the current symbol is a terminal, add it to the result-set.
                result.add((Terminal) aSymbolList);
                if (!aSymbolList.getName().endsWith("epsilon")) {
                    break;
                }
            } else {
                //If the current symbol is a nonterminal, add its first-set to the result-set.
                Nonterminal nt = (Nonterminal) aSymbolList;
                result.addAll(firsts.get(nt));
                if (!nullable.contains(nt)) {
                    break;
                } else {
                    //If the current symbol is nullable, add the empty word symbol to the result-set.
                    for (Terminal terminal : grammar.getTerminals()) {
                        if (terminal.getName().equals("epsilon")) {
                            result.add(terminal);
                            break;
                        }
                    }
                }
            }

        return result;
    }

    /**
     * Calculates the nullable-set of a given grammar.
     *
     * @param grammar The grammar.
     * @return A HashSet containing all nullable nonterminals,
     */
    @SuppressWarnings("unused")
    private static PrintableSet calculateNullableAsPrintable(Grammar grammar) {
        HashSet<Nonterminal> res = calculateNullable(grammar);
        PrintableSet result = new PrintableSet(res.size());
        res.forEach(result::add);
        return  result;
    }

    /**
     * replaces every nullsymbol through the same object
     */
    private static Grammar replaceLambda(Grammar g) {
        HashSet<Rule> freshRules = new HashSet<>();
        g.getRules().forEach(rule -> {
            List<Symbol> list = new ArrayList<>();
            rule.getRightSide().forEach(sym -> {
                if(sym.getName().equals("epsilon")||sym.getName().equals("lambda")) {
                    list.add(Terminal.NULLSYMBOL);
                } else {
                    list.add(sym);
                }
            });
            freshRules.add(new Rule(rule.getComingFrom(),list));
        });
        return new Grammar(g.getStartSymbol(),freshRules,g.getName(), (Grammar) g.getPreviousVersion());
    }

    /*
     ---------------------------------------------------------------------------------------------------------------*
     -                                The public methods follow after this comment.                                -*
     ---------------------------------------------------------------------------------------------------------------*
     */

    /**
     * Takes an input-string and parses it into a grammar. Typically the input-string comes from a file,
     * that has been loaded completely into that string.
     *
     * @param fileInput The input-string.
     * @param name the name of the grammar
     * @return The grammar.
     * @throws ParserException {@link ParserException}.
     * @throws LexerException {@link LexerException}.
     * @throws IOException {@link IOException}.
     */
    @SuppressWarnings("unused")
    public static Grammar parse(String fileInput, String name) throws ParserException, LexerException, IOException {
        StringReader reader = new StringReader(fileInput);
        PushbackReader r = new PushbackReader(reader, 100);
        Lexer l = new Lexer(r);
        Parser parser = new Parser(l);
        Start start = parser.parse();
        Visitor visitor = new Visitor();
        start.apply(visitor);
        Grammar g = visitor.getGrammar(name);
        return GrammarUtil.replaceLambda(g);
    }

    /**
     * Takes an input {@link File} and parses it into a grammar.
     *
     * @param file the input {@link File}
     * @return Grammar {@link Grammar}
     * @throws IOException {@link IOException} an IOException
     * @throws LexerException {@link LexerException} an Exception with the {@link Lexer}
     * @throws ParserException {@link ParserException} an Exception with the {@link Parser}
     */
    @SuppressWarnings("unused")
    public static Grammar parse(File file) throws IOException, LexerException, ParserException {
        String name = file.getName();
        Grammar grammar;
        BufferedReader grammarReader = new BufferedReader(new FileReader(file));
        String string = "";
        String line;
        while ((line = grammarReader.readLine()) != null) {
            string = string + line + "\n";
        }
        grammar = GrammarUtil.parse(string,name);
        grammarReader.close();
        return grammar;

    }

    /**
     * Returns an ArrayList that contains all terminals in order of their appearance in the grammars's rules.
     *
     * @param grammar The grammar.
     * @return The ArrayList.
     */
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
     * @param onGUI true, if the grammar is printed on the GUI
     */
    @SuppressWarnings("unused")
    public static void print(boolean onGUI, Grammar grammar) {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

        try {
            writer.write("{");

            writeTerminals(onGUI,writer, grammar);
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

    /**
     * saves a grammar to a file
     * @param grammar the {@link Grammar} that should be saved
     * @param file the {@link File} in which the grammar is saved
     */
    @SuppressWarnings("unused")
    public static void save(Grammar grammar, File file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            writer.write("{");

            writeTerminals(false,writer, grammar);
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
     * @throws IOException input/output Exception
     */
    @SuppressWarnings("unused")
    public static void save(Grammar grammar, String fileName) throws IOException {

            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

            writer.write("{");

            writeTerminals(false, writer, grammar);
            writer.write("; ");

            writeNonterminals(writer, grammar);
            writer.write("; ");

            writer.write(grammar.getStartSymbol().getName());

            writer.write("}\n\n");

            writeRules(writer, grammar);

    }

    /**
     * Writes either the grammar's terminals, nonterminals, or rules through a given BufferedWriter.
     * Only one of the three booleans is supposed to be set to true.
     *  @param writer The BufferedWriter.
     * @param grammar The grammar.
     * @param terminals If true, the grammar's terminals will be written.
     * @param nonterminals If true, the grammar's nonterminals will be written.
     */
    @SuppressWarnings("unused")
    public static void writePart(BufferedWriter writer, Grammar grammar, boolean terminals, boolean nonterminals) {
        //Write terminals.
        if(terminals) {
            writeTerminals(true, writer, grammar);
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
        }

        //Write rules.
    }


    /**
     * Calculates the nullable-set of a given grammar.
     *
     * @param grammar The grammar.
     * @return A HashSet containing all nullable nonterminals,
     */
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    public static HashMap<Nonterminal, HashSet<Terminal>> calculateFirst(Grammar grammar) {
        HashMap<Nonterminal, HashSet<Terminal>> result = new HashMap<>();
        HashSet<Nonterminal> nullable = calculateNullable(grammar);
        boolean changed = true;

        //Initialize the result-map.
        for(Nonterminal nonterminal : grammar.getNonterminals()) {
            result.put(nonterminal, new HashSet<>());
        }

        while(changed) {
            changed = false;
            for (Nonterminal nonterminal : grammar.getNonterminals()) {
                List<List<Symbol>> rules = grammar.getRules().stream().filter(rule -> rule.getComingFrom().equals(nonterminal)).map(Rule::getRightSide).collect(Collectors.toList());
                for (List<Symbol> symbolList : rules) {
                    for (Symbol aSymbolList : symbolList) {
                        if (aSymbolList instanceof Terminal) {
                            if (!aSymbolList.equals(Terminal.NULLSYMBOL)) {
                                if (!result.get(nonterminal).contains(aSymbolList)) {
                                    //If the current symbol is a terminal and not the empty word,
                                    //add it to the first-set of the current nonterminal.
                                    result.get(nonterminal).add((Terminal) aSymbolList);
                                    changed = true;
                                }
                                break;
                            }
                        } else if (aSymbolList instanceof Nonterminal) {
                            if (!result.get(nonterminal).containsAll(result.get(aSymbolList))) {
                                //If the current symbol is a nonterminal, add its first-set to the first-set of the current nonterminal.
                                result.get(nonterminal).addAll(result.get(aSymbolList));
                                changed = true;
                            }
                            if (!nullable.contains(aSymbolList)) {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
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
    @SuppressWarnings("unused")
    public static HashMap<Nonterminal, HashSet<Terminal>> calculateFollow(Grammar grammar) {
        HashMap<Nonterminal, HashSet<Terminal>> result = new HashMap<>();
        HashSet<Nonterminal> nullable = calculateNullable(grammar);
        HashMap<Nonterminal, HashSet<Terminal>> firsts = calculateFirst(grammar);
        ArrayList<Boolean> wrapper = new ArrayList<>();
        wrapper.add(Boolean.TRUE);

        //Initialize the result-map.
        for(Nonterminal nonterminal : grammar.getNonterminals()) {
            result.put(nonterminal, new HashSet<>());
        }
        result.get(grammar.getStartSymbol()).add(new Terminal("$"));

        while(wrapper.get(0)) {
            wrapper.set(0, Boolean.FALSE);
            for (Nonterminal nonterminal : grammar.getNonterminals()) {
                grammar.getRules().stream().filter(rule -> rule.getComingFrom().equals(nonterminal))
                        .map(Rule::getRightSide).forEach(symbolList -> {
                    //Go through each list of symbols and calculate the follow-set of each nonterminal in these lists.
                    for (int i = 0; i < symbolList.size(); i++) {
                        if (symbolList.get(i) instanceof Nonterminal) {
                            if (i == symbolList.size() - 1) {
                                if (symbolList.get(i) instanceof Nonterminal) {
                                    //If the last symbol of the current list is a nonterminal, the follow-set of this nonterminal
                                    //contains the follow-set of the current nonterminal.
                                    if (!result.get(symbolList.get(i)).containsAll(result.get(nonterminal))) {
                                        result.get(symbolList.get(i)).addAll(result.get(nonterminal));
                                        wrapper.set(0, Boolean.TRUE);
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
                                        wrapper.set(0, Boolean.TRUE);
                                    }

                                    if (nullable.contains(symbolList.get(j)) && j < symbolList.size() - 1) {
                                        //If the current symbol is nullable, we have to go on, until we find a symbol,
                                        //that isn't nullable.
                                        int k = j + 1;
                                        //noinspection SuspiciousMethodCalls
                                        do {
                                            if(symbolList.get(k) instanceof Terminal && (!symbolList.get(k).getName().equals("epsilon"))) {
                                                if(!result.get(symbolList.get(i)).contains(symbolList.get(k))) {
                                                    //If the current symbol is a terminal, add it to the follow-set of symbolList.get(i).
                                                    result.get(symbolList.get(i)).add((Terminal) symbolList.get(k));
                                                    wrapper.set(0, Boolean.TRUE);
                                                }
                                            } else if (symbolList.get(k) instanceof Nonterminal) {
                                                if(!result.get(symbolList.get(i)).containsAll(firsts.get(symbolList.get(k)))) {
                                                    //If the current symbol is a nonterminal, add its first-set to the follow-set of symbolList.get(i).
                                                    result.get(symbolList.get(i)).addAll(firsts.get(symbolList.get(k)));
                                                    wrapper.set(0, Boolean.TRUE);
                                                }
                                            }
                                            k++;
                                        } while (k < symbolList.size() && nullable.contains(symbolList.get(k)));

                                        //noinspection SuspiciousMethodCalls
                                        if (k == symbolList.size() - 1 && nullable.contains(symbolList.get(k))) {
                                            if(!result.get(symbolList.get(i)).containsAll(result.get(nonterminal))) {
                                                //If the do-while-loop has reached the last symbol of the current list
                                                //and this symbol is a nonterminal, we have to add the follow-set of this nonterminal
                                                //to the follow-set of symbol-list.get(i);
                                                result.get(symbolList.get(i)).addAll(result.get(nonterminal));
                                                wrapper.set(0, Boolean.TRUE);
                                            }
                                        }
                                    } else if (j == symbolList.size() - 1 && nullable.contains(symbolList.get(j))) {
                                        if(!result.get(symbolList.get(i)).containsAll(result.get(nonterminal))) {
                                            //If result.get(j) has reached the last symbol of the current list
                                            //and this symbol is a nonterminal, we have to add the follow-set of this nonterminal
                                            //to the follow-set of symbol-list.get(i);
                                            result.get(symbolList.get(i)).addAll(result.get(nonterminal));
                                            wrapper.set(0, Boolean.TRUE);
                                        }
                                    }

                                } else {
                                    //noinspection SuspiciousMethodCalls
                                    if(!result.get(symbolList.get(i)).contains(symbolList.get(j))) {
                                        //If symbolList.get(j) is a terminal, we can just add it to the follow-set of symbolList.get(i).
                                        result.get(symbolList.get(i)).add((Terminal) symbolList.get(j));
                                        wrapper.set(0, Boolean.TRUE);
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
    @SuppressWarnings("unused")
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
                    .map(Rule::getRightSide).forEach(symbolList -> {
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


    /**
     * removes unnecessary epsilons in a {@link Grammar}s {@link Rule}s
     * @param g the {@link Grammar}
     * @return a {@link Grammar} without unnecessary nullsymbols
     */
    public static Grammar removeUnnecessaryEpsilons(Grammar g) {
        HashSet<Rule> freshRules = new HashSet<>();
        g.getRules().forEach(rule -> {
            if (rule.getRightSide().size() != 1) {
                if(rule.getRightSide().stream().allMatch(symbol -> symbol.equals(Terminal.NULLSYMBOL))) {
                    List<Symbol> tmp = new ArrayList<>();
                    tmp.add(Terminal.NULLSYMBOL);
                    freshRules.add(new Rule(rule.getComingFrom(), tmp));
                } else {
                    List<Symbol> tmp = new ArrayList<>();
                    tmp = rule.getRightSide().stream()
                            .filter(symbol -> !symbol.equals(Terminal.NULLSYMBOL))
                            .collect(Collectors.toList());
                    freshRules.add(new Rule(rule.getComingFrom(),tmp));
                }
            } else {
                freshRules.add(rule);
            }
        });
        return new Grammar(g.getStartSymbol(),freshRules,g.getName(),g);

    }

    @SuppressWarnings("unused")

    /*
     ---------------------------------------------------------------------------------------------------------------*
     -                                Remove Lambda Rules                                                          -*
     ---------------------------------------------------------------------------------------------------------------*
     */
    /**
     *  This method removes lambda-rules in three steps and returns the result of every step
     * @param grammar the grammar, that should be modified
     * @return ArrayList
     */
    public static ArrayList<Printable> removeLambdaRulesAsPrintables(Grammar grammar) {
        ArrayList<Printable> res=new ArrayList<>(4);
        //0. before Grammar
        Grammar grammar0= (Grammar) grammar.deep_copy();

        Grammar grammar1;
        boolean specialrule;
        //1. Special Rule for Empty Word
        if(GrammarUtil.languageContainsLambda(grammar)) { //todo should be language contains lambda
            grammar1=specialRuleForEmptyWord(grammar0,grammar);
            specialrule=true;
        } else {
            specialrule = false;
            grammar1=grammar0;
        }

        //2. Nullable Set
        PrintableSet nullable_and_printable=GrammarUtil.calculateNullableAsPrintable(grammar1);

        //3. step two && unnecessary epsilons

        HashSet<Nonterminal> nullable=GrammarUtil.calculateNullable(grammar1);
        Grammar grammar2 = removeLambdaRules_StepTwo(grammar1,nullable,grammar);
        grammar2 = removeUnnecessaryEpsilons(grammar2);
        //4. step three

        Grammar grammar3= removeLambdaRules_StepThree(grammar2,true);


        res.add(grammar0);
        if(specialrule) {
            res.add(grammar1);
        }
        res.add(nullable_and_printable);
        res.add(grammar2);
        res.add(grammar3);


        return res;

    }

    /**
     * removes the lambda-rules form {@link Grammar} a grammar
     * @param g the grammar that should be modified
     * @return the modified grammar, that has no lambda-rules
     */
    @SuppressWarnings("unused")
    public static Grammar removeLambdaRules(Grammar g) {

        if(GrammarUtil.isLambdaFree(g)) {
            return g;
        } else {
            Grammar gr;
            if(GrammarUtil.languageContainsLambda(g)) {
                gr=specialRuleForEmptyWord(g,g);
            } else {
                gr=g;
            }
            /* change original grammar **/
            // Grammar grammar = specialRuleForEmptyWord(g);
            HashSet<Nonterminal> nullable = GrammarUtil.calculateNullable(gr);
            Grammar grammar1 = removeLambdaRules_StepTwo(gr, nullable, g);
            Grammar grammar2 = removeUnnecessaryEpsilons(grammar1);
            Grammar grammar3 = removeLambdaRules_StepThree(grammar2);
            return new Grammar(grammar3.getStartSymbol(),grammar3.getRules(),grammar3.getName(),g);
        }
    }

    /**
     *
     * @param g
     * @return
     */
    private static Grammar removeLambdaRules_StepThree(Grammar g) {
        Grammar res = removeLambdaRules_StepThree(g,true);
        res = removeUnreachableNonterminals(res);
        return new Grammar(res.getStartSymbol(),res.getRules(),res.getName(),g);
    }

    /**
     * the third step of the algorithm to delete lambda-rules
     * all rules which have only epsilons on the right side are removed
     * furthermore, nonterminals, which now do not appear on the right side of any rule, are removed
     * @param g the grammar g
     * @param again first time calling: true. during the algorithm new lambda-rules can emerge, so that method
     *              has to be called again, but this time with "again" set to false
     */
    @SuppressWarnings("unused")
    private static Grammar removeLambdaRules_StepThree(Grammar g, boolean again) {
        // delete lambda-rules
        HashSet<Rule> tmp2 = new HashSet<>();
        g.getRules().stream()
                .filter(rule -> !rule.getRightSide().stream().allMatch(symbol -> symbol.equals(Terminal.NULLSYMBOL)) || rule.getComingFrom().equals(g.getStartSymbol()))
                .forEach(tmp2::add);
        //  g.setRules(tmp2);
        HashSet<Nonterminal> toRemove = new HashSet<>();
        //nonterminals that point only on lambda, can be removed
        List<Nonterminal> newNT =  tmp2.stream().map(Rule::getComingFrom).collect(Collectors.toList());
        List<Nonterminal> oldNT =  g.getRules().stream().map(Rule::getComingFrom) .collect(Collectors.toList());
        oldNT.forEach(sym -> {
            if(!newNT.stream().anyMatch(e -> e.equals(sym))) {
                toRemove.add(sym);
            }
        });

        // the symbols that can be removed are replaced by the nullsymbol
        HashSet<Rule> freshRules = new HashSet<>();
        tmp2.forEach(rule -> {
            List<Symbol> tmpList = new ArrayList<>();
            List<Symbol> list = rule.getRightSide();
            for (Symbol aList : list) {
                //noinspection SuspiciousMethodCalls
                if (toRemove.contains(aList)) {
                    tmpList.add(Terminal.NULLSYMBOL);
                } else {
                    tmpList.add(aList);
                }
            }
            freshRules.add(new Rule(rule.getComingFrom(),tmpList));
        });
        Grammar res = new Grammar(g.getStartSymbol(),freshRules,g.getName(),g);

        // to find any new lambdas and lambda-rules
        if(again) {
            Grammar res1 = GrammarUtil.removeUnnecessaryEpsilons(res);

            return GrammarUtil.removeLambdaRules_StepThree(res1,false);
        } else {
            return res;
        }
    }

    @SuppressWarnings("unused")
    private static boolean containsNullable(Rule rule, HashSet<Nonterminal> nullable) {
        return rule.getRightSide().stream()
                .anyMatch(symbol -> nullable.stream().anyMatch(elem -> elem.equals(symbol)));
    }
    /**
     * the second step of the algorithm to delete lambda-rules.
     * For every rule, that contains a nullable symbol on the right side, a rule with this symbol replaced
     * by "epsilon" is added to the rule-Set
     * @param g The Grammar
     * @param nullable The set, which contains all the nullable terminals
     */
    @SuppressWarnings("unused")
    private static Grammar removeLambdaRules_StepTwo(Grammar g, HashSet<Nonterminal> nullable, Grammar original) {

        Grammar grammar = GrammarUtil.removeUnnecessaryEpsilons(g);
        Queue<Rule> queue = new LinkedList<>();
        HashSet<Rule> alreadySeen = new HashSet<>();
        Grammar res=g;
        //every rule with nullables is added to a queue.
        //every step a rule is popped and the modified and the original rule are added at the end of the queue
        //stops, if there where no changes
        grammar.getRules().stream().filter(rule -> containsNullable(rule,nullable))
                .forEach(queue::add);
        if(!queue.isEmpty()) {
            boolean changed= true;
            while(changed) {
                changed=false;
                Rule current = queue.poll();
                Rule fresh = new Rule(current.getComingFrom(),new ArrayList<>(current.getRightSide()));

                for(int i = 0; i<current.getRightSide().size(); i++) {
                    // if the i-th Symbol is a nullable symbol, remove it and replace it with lambda
                    //noinspection MismatchedQueryAndUpdateOfCollection
                    List<Symbol> tmpList = new ArrayList<>();
                    tmpList.addAll(fresh.getRightSide());
                    int finalI = i;
                    if(nullable.stream().anyMatch(elem -> elem.equals(fresh.getRightSide().get(finalI)))) {

                        List<Symbol> firstTryList = new ArrayList<>(current.getRightSide());
                        firstTryList.set(i,Terminal.NULLSYMBOL);
                        Rule firstTry = new Rule(current.getComingFrom(),firstTryList);
                        if(!queue.stream().anyMatch(elem -> elem.equals(firstTry)) || alreadySeen.contains(firstTry)) {
                            //add the new rule "first try" to the set
                            queue.add(firstTry);
                            queue.add(current);
                            changed = true; //--> change
                            break;
                        }
                    }
                }
                //if there can't be anything changed about this rule, remove it **/
                alreadySeen.add(current);
            }
            HashSet<Rule> freshRules = new HashSet<>();
            freshRules.addAll(queue);
            freshRules.addAll(alreadySeen);
            freshRules.addAll(grammar.getRules());

            res = new Grammar(grammar.getStartSymbol(),freshRules,grammar.getName(),original);
        }
        return res;
    }


    /*
     ---------------------------------------------------------------------------------------------------------------*
     -                                eliminate Unit Rules                                                         -*
     ---------------------------------------------------------------------------------------------------------------*
     */
    /**
     *  This method eliminates unit rules in three steps and returns the result of every step
     * @param grammar the grammar, that should be modified
     * @return ArrayList
     */
    @SuppressWarnings("unused")
    public static ArrayList<Printable> eliminateUnitRulesAsPrintables(Grammar grammar) {
        ArrayList<Printable> res=new ArrayList<>(3); //the results

        //Before
        Grammar grammar0= (Grammar) grammar.deep_copy();
        //Step 1
        Grammar grammar1=GrammarUtil.removeCircles(grammar0);


        //Step 2
        HashSet<Node> unitRules=findUnitRules(grammar1);

        ArrayList<Node> tmp = GrammarUtil.bringNonterminalsInOrder(unitRules);
        StringBuilder stb = new StringBuilder();
        for(int i=0;i<tmp.size();i++) {
            stb.append(tmp.get(i).getValue().getName()).append("=");
            stb.append(Integer.toString(tmp.size()-i)).append(" ");
        }
        Dummy dummy = new Dummy(stb.toString()+"\n");
        //Step 3
        Grammar grammar2=removeUnreachableNonterminals(GrammarUtil.removeUnitRules(unitRules,grammar1));

        res.add(grammar0);
        res.add(grammar1);
        res.add(dummy);
        res.add(grammar2);

        return res;

    }

    /**
     * deletes unit-rules from the grammar, without changing the grammar's language
     * @param grammar the {@link Grammar} to-be-modified grammar
     * @return {@link Grammar} a Grammar, that has the same language as before, but now without unit rules
     */
    @SuppressWarnings("unused")
    public static Grammar eliminateUnitRules(Grammar grammar) {
        if(!GrammarUtil.hasUnitRules(grammar)) {
            return grammar;
        } else {
            Grammar grammar1 = GrammarUtil.removeCircles(grammar);
            Grammar grammar2 = GrammarUtil.removeUnitRules(GrammarUtil.findUnitRules(grammar1),grammar1);
            Grammar grammar3 = GrammarUtil.removeUnreachableNonterminals(grammar2);
            return new Grammar(grammar3.getStartSymbol(),grammar3.getRules(),grammar3.getName(),grammar);
        }
    }

    @SuppressWarnings("unused")
    private static Grammar removeCircles(Grammar grammar) {
        Grammar res = grammar;
        Grammar loop = grammar;
        while(loop!=null) {
            loop = removeOneCircle(res); //removes one circle
            if(loop!=null) { //if there is no circle anymore, loop equals null and we are ready
                res=loop;
            }
        }
        HashSet<Rule> freshRules = new HashSet<>();
        res.getRules().forEach(rule -> {
            if (rule.getRightSide().size() > 1) {
                freshRules.add(rule);
            } else if(rule.getRightSide().size()==1) {
                if (!rule.getComingFrom().equals(rule.getRightSide().get(0))) {
                    freshRules.add(rule);
                }
            }
        });
        return new Grammar(res.getStartSymbol(),freshRules,res.getName(), (Grammar) res.getPreviousVersion());
    }
    /**
     * removes circles in the grammar rules
     * @param grammar the grammar that should be modified
     * @return the grammar, if there are circles. Null, if there are no circles anymore
     */
    @SuppressWarnings("unused")
    private static Grammar removeOneCircle(Grammar grammar) {
        ArrayList<Node> tmp;

        HashSet<Node> unitRules= GrammarUtil.findUnitRules(grammar); //ok no changes
        GrammarUtil.dfs(unitRules);
        tmp=GrammarUtil.findBackwardsEdge(unitRules);
        if(tmp!=null) {
            //do the replace
            Nonterminal toBeReplaced=tmp.get(0).getValue();
            Nonterminal newNonTerminal=tmp.get(1).getValue();
            HashSet<Rule> freshRules = new HashSet<>();
            grammar.getRules().forEach(rule -> {
                List<Symbol> tmpList = new ArrayList<>();
                rule.getRightSide().forEach(sym -> {
                    if(sym.equals(toBeReplaced)) {
                        tmpList.add(newNonTerminal);
                    } else {
                        tmpList.add(sym);
                    }
                });
                if(rule.getComingFrom().equals(toBeReplaced)) {
                    freshRules.add(new Rule(newNonTerminal,tmpList));
                } else {
                    freshRules.add(new Rule(rule.getComingFrom(),tmpList));
                }
            });
            if(grammar.getStartSymbol().equals(toBeReplaced)) {
                return new Grammar(newNonTerminal, freshRules, grammar.getName(), (Grammar) grammar.getPreviousVersion());
            } else {
                return new Grammar(grammar.getStartSymbol(), freshRules, grammar.getName(), (Grammar) grammar.getPreviousVersion());
            }
        } else {
            return null;
        }
    }




    /**
     * makes the nonterminals to nodes. the children are the nonterminals on which this nonterminal points
     * @param g the grammar, that is searched for unit rules
     * @return a HashSet with the Nonterminals as Nodes connected in a graph
     */
    @SuppressWarnings("unused")
    private static HashSet<Node> findUnitRules(Grammar g) {
        HashSet<Node> result=new HashSet<>();
        g.getNonterminals().stream().
                filter(nonterminal -> GrammarUtil.isLeftSideOfUnitRule(nonterminal,g) || GrammarUtil.isRightSideOfUnitRule(nonterminal,g)).
                forEach(x -> result.add(new Node(x)));


        for(Node node : result) {
            g.getRules().stream().filter(rule -> rule.getComingFrom().equals(node.getValue()))
                    .map(Rule::getRightSide).forEach(list -> {
                if(list.size()==1 && list.get(0) instanceof Nonterminal) {
                    Nonterminal nt = (Nonterminal) list.get(0);
                    result.forEach(child -> {
                        if (child.getName().equals(nt.getName())) {
                            node.getChildren().add(child);
                        }
                    });
                }
            });
        }
        return result;
    }
    @SuppressWarnings("unused")
    private static boolean isLeftSideOfUnitRule(Nonterminal nonterminal, Grammar grammar) {
        return grammar.getRules().stream()
                .anyMatch(rule -> rule.getComingFrom().equals(nonterminal) && rule.getRightSide().size()==1 && rule.getRightSide().get(0) instanceof  Nonterminal);
    }
    @SuppressWarnings("unused")
    private static boolean isRightSideOfUnitRule(Nonterminal right, Grammar g) {
        return g.getRules().stream()
                .anyMatch(rule -> rule.getRightSide().size()==1 && rule.getRightSide().get(0).equals(right));
    }

    /**
     * find backward edges after a dept-first-search
     * @param unitRules a hashSet of nodes that represent the unit rules
     * @return an ArrayList with two objects that represent backwards edge in a dept-first-search //TODO
     */
    @SuppressWarnings("unused")
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


    @SuppressWarnings("unused")
    private static void dfs(HashSet<Node> unitRules) {
        int[] df = new int[2];
        df[0]=1;
        df[1]=1;
        for(Node node : unitRules) {
            if(node.isNotVisited()) {
                df = GrammarUtil.dfs(node,df);
            }
        }

    }




    @SuppressWarnings("unused")
    private static int[] dfs(Node node, int[] df) {

        node.setVisited();
        node.setDfs(df[0]);
        df[0]++;
        for(Node child : node.getChildren()) {
            if(child.isNotVisited()) {
                df=GrammarUtil.dfs(child,df);
            }
        }
        node.setDfe(df[1]);
        df[1]++;
        return df;
    }



    /**
     * removes the unit rules in a Grammar, only possible if there are no circles
     * TODO What does this do? why does it return a list? is nowhere used
     * @param nodes the nonterminals as nodes. to obtain them, use
     * @param g the grammar g
     */
    @SuppressWarnings("unused")
    private static Grammar removeUnitRules(HashSet<Node> nodes, Grammar g) {
        ArrayList<Node> sorted=GrammarUtil.bringNonterminalsInOrder(nodes);

        //add every rule of the child as a rule of the parent

        Grammar tmp = g;
        for (Node current : sorted) {
            for (Node child : current.getChildren()) {
               tmp = removeOneUnitRule(current.getValue(),child.getValue(),tmp);
            }
        }


        //remove unit rules
        HashSet<Rule> freshRules = new HashSet<>();
        tmp.getRules().forEach(rule -> {
            if(rule.getRightSide().size()>1 || rule.getRightSide().get(0) instanceof Terminal) {
                freshRules.add(rule);
            }
        });
        return new Grammar(tmp.getStartSymbol(),freshRules,tmp.getName(), g);
    }

    @SuppressWarnings("unused")
    private static Grammar removeOneUnitRule(Nonterminal parent, Nonterminal child, Grammar grammar) {
        HashSet<Rule> rules = new HashSet<>();
        grammar.getRules().stream()
                .filter(rule -> rule.getComingFrom().equals(child))
                .forEach(rule -> {
                    Rule copy = new Rule(parent,rule.getRightSide());
                    rules.add(copy);
                });
        rules.addAll(grammar.getRules());
        return new Grammar(grammar.getStartSymbol(),rules,grammar.getName(), (Grammar) grammar.getPreviousVersion());
    }

    /**
     * @param nodes a hashset of notes that represent the nonterminals in unit rules
     * @return an arrayList of nodes in the right order
     */
    @SuppressWarnings("unused")
    private static ArrayList<Node> bringNonterminalsInOrder(HashSet<Node> nodes) {


        ArrayList<Node> result = new ArrayList<>();

        // as long as some nodes have children with higher numbers, do the number-method
        while(nodes.stream().
                anyMatch(node -> node.getChildren().stream().
                        anyMatch(child -> child.getNumber()<=node.getNumber()))) {
            int j=0;
            for(Node node : nodes) {
                   j=GrammarUtil.number(node,j);
            }
        }


       nodes.stream().sorted((x,y) -> Integer.compare(y.getNumber(),x.getNumber()))
               .forEach(result::add);



        return result;
    }

    /**
     * numbers the parent node and all children nodes with a higher number
     * @param node the parent node
     */
    @SuppressWarnings("unused")
    private static int number(Node node, int max) {
        node.setNumber(max+1);
        OptionalInt res=node.getChildren().stream().mapToInt(child -> number(child,max+2)).max();
        if(res.isPresent()) {
            return res.getAsInt();
        } else {
            return max+2;
        }
    }

    /*
    --------------------------------------------------------------------------------------------------------------------
    |                                make chomsky normal form                                                          |
    --------------------------------------------------------------------------------------------------------------------
     */

    /**
     * This method makes the grammar to cnf in steps and returns the result of every step
     * @param grammar the grammar, that should be modified
     * @return ArrayList
     */
    @SuppressWarnings("unused")
    public static ArrayList<Printable> chomskyNormalFormAsPrintables(Grammar grammar) {
        ArrayList<Printable> res=new ArrayList<>(4);

        Grammar grammar0 = (Grammar) grammar.deep_copy();

        Grammar grammar1=GrammarUtil.chomskyNormalForm_StepOne(grammar);

        //step 2: remove more than two Nonterminals
        Grammar grammar2=GrammarUtil.chomskyNormalForm_StepTwo(grammar1);


        res.add(grammar0);
        res.add(new Dummy());
        res.add(grammar1);
        res.add(grammar2);
        return res;
    }

    @SuppressWarnings("unused")
    private static String chooseName(Grammar grammar, Set<String> excluded) {
        int big = 65;
        String randomName = Character.toString((char) big);
        Set<String> names = grammar.getNonterminals().stream().map(Nonterminal::getName).collect(Collectors.toSet());
        int counter = 1;
        while(names.contains(randomName) || excluded.contains(randomName)) {
            while(big<90 && (names.contains(randomName) || excluded.contains(randomName))) {
                big++;
                randomName = "";
                for(int i=0;i<counter;i++) {
                    randomName += Character.toString((char) big);
                }
            }
            counter++;
            big=64;
        }
        return randomName;
    }

    /**
     * transforms the grammar to chomsky normal form without changing its language
     * @param grammar the to-be-modified grammar
     * @return a {@link Grammar} in CNF
     */
    @SuppressWarnings("unused")
    public static Grammar chomskyNormalForm(Grammar grammar) {
        if(!GrammarUtil.isLambdaFree(grammar)) {
            return grammar;
        } else if(GrammarUtil.hasUnitRules(grammar)) {
            return grammar;
        } else if(GrammarUtil.isInChomskyNormalForm(grammar)) {
            return grammar;
        } else {
            Grammar res1 = chomskyNormalForm_StepOne(grammar);
            Grammar res2 = chomskyNormalForm_StepTwo(res1);
            return new Grammar(res2.getStartSymbol(), res2.getRules(), res2.getName(), grammar);
        }
    }


    @SuppressWarnings("unused")
    private static Grammar chomskyNormalForm_StepOne(Grammar g) {
        String name;
        if(g.getNonterminals().stream().map(Nonterminal::getName).anyMatch(nt -> nt.startsWith("X"))) {
            name = chooseName(g, new HashSet<>()) + "_";
        } else {
            name = "X_";
        }

        HashSet<String> excluded = new HashSet<>();
        //replace every occurrences of a Terminal through a new Nonterminal
        HashSet<Rule> freshRules = new HashSet<>();
        g.getRules().forEach(rule -> {
            if(rule.getRightSide().size()>1 && rule.getRightSide().stream().anyMatch(symbol -> symbol instanceof Terminal)) {
                List<Symbol> list = new ArrayList<>();
                rule.getRightSide().forEach(sym -> {
                    if (sym instanceof Terminal) {
                        Nonterminal newNT;
                        if(Nonterminal.validName(name+sym.getName())) {
                            newNT =new Nonterminal(name+sym.getName());
                            list.add(newNT);
                        } else {
                            String randomName = chooseName(g, excluded);
                            newNT = new Nonterminal(randomName);
                            excluded.add(randomName);
                            list.add(newNT);
                        }

                        List<Symbol> tmp = new ArrayList<>();
                        tmp.add(sym);
                        freshRules.add(new Rule(newNT, tmp));
                    } else {
                        list.add(sym);
                    }
                });
                freshRules.add(new Rule(rule.getComingFrom(),list));

            } else {
                freshRules.add(rule);
            }
        });
        return new Grammar(g.getStartSymbol(),freshRules,g.getName(),g);
    }

    /**
     * step 2: modify rules with more than two nonterminals so that they only have two
     * @param g the grammar
     */
    @SuppressWarnings("unused")
    private static Grammar chomskyNormalForm_StepTwo(Grammar g) {
        String name;
        if(g.getNonterminals().stream().map(Nonterminal::getName).anyMatch(nt-> nt.startsWith("P"))) {
            name = chooseName(g, new HashSet<>()) + "_";
        } else {
            name = "P_";
        }


        final int[] counter = {0};
        Set<Rule> old = new HashSet<>(g.getRules());
        while(true) {
            HashSet<Rule> res;
            res=old.stream().reduce(new HashSet<Rule>(), (x, rule) -> {
                if(rule.getRightSide().stream().allMatch(sym -> sym.equals(Terminal.NULLSYMBOL)) || rule.getRightSide().size()<=2) {
                    x.add(rule);
                } else {
                    Nonterminal mod = new Nonterminal(name+ counter[0]++);
                    Symbol first = rule.getRightSide().get(0);
                    List<Symbol> tmp1 = new ArrayList<>();
                    for(int i = 1; i<rule.getRightSide().size(); i++) {
                        tmp1.add(rule.getRightSide().get(i));
                    }

                    List<Symbol> tmp2 = new ArrayList<>();
                    tmp2.add(first);
                    tmp2.add(mod);
                    Rule rule1 = new Rule(mod, new ArrayList<>(tmp1));
                    Rule rule2 = new Rule(rule.getComingFrom(), new ArrayList<>(tmp2));
                    x.add(rule1);
                    x.add(rule2);
                }
                return x;
            },(x, y) -> {
                x.addAll(y);
                return x;
            });

            if(old.size()==res.size()) { //nothing changed
                old.clear();
                old.addAll(res);
                break;
            } else {
                old.clear();
                old.addAll(res);
            }
        }

        return new Grammar(g.getStartSymbol(),old,g.getName(),g);
    }

    /**
     * checks if a grammar is in CNF
     * @param grammar a {@link Grammar} the to-be-checked grammar
     * @return true, if the grammar is in CNF
     */
    @SuppressWarnings("unused")
    public static boolean isInChomskyNormalForm(Grammar grammar) {
        return grammar.getRules().stream().allMatch(rule -> {
            if(rule.getComingFrom().equals(grammar.getStartSymbol())) {
                boolean a = rule.getRightSide().size()==1 && rule.getRightSide().get(0).equals(Terminal.NULLSYMBOL);
                boolean b = rule.getRightSide().size()==1 && rule.getRightSide().get(0) instanceof Terminal;
                boolean c = rule.getRightSide().size()==2 && rule.getRightSide().stream().allMatch(sym -> sym instanceof Nonterminal);
                return a || b || c;
            } else {
                boolean a = rule.getRightSide().size()==1 && rule.getRightSide().get(0) instanceof Terminal && !rule.getRightSide().get(0).equals(Terminal.NULLSYMBOL);
                boolean b = rule.getRightSide().size()==2 && rule.getRightSide().stream().allMatch(sym -> sym instanceof Nonterminal);
                return a || b;
            }
        });
    }
    /*
     * ---------------------------------------------------------------------------------------------------------------*
     * -                                           CYK                                                               -*
     * ---------------------------------------------------------------------------------------------------------------*
     */

    @SuppressWarnings("unused")
    private static Matrix createMatrix(List<String> word) {
        return new Matrix(word.size(),word.size()+1, word);
    }

    /**
     * runs the cyk-algorithm.
     * @param g the grammar
     * @param word the word to be checked for the cyk
     * @return a cyk-{@link Matrix}
     */
    @SuppressWarnings("unused")
    public static Matrix cyk(Grammar g, List<String> word) {
       if(!GrammarUtil.isInChomskyNormalForm(g)) {
           System.out.println("Is not in chomsky normal form");
           return null;
       }
        Matrix m=GrammarUtil.createMatrix(word);
        for(int i=1;i<=word.size();i++) {
            final int j=i;

            ArrayList<Nonterminal> tmp=(ArrayList<Nonterminal>)  g.getRules().stream()
                    .filter(rule -> rule.getRightSide().size()==1 && GrammarUtil.pointsOnCurrentChar(word,j,rule.getRightSide()))
                    .map(Rule::getComingFrom)
                    .collect(Collectors.toList());
            for(Nonterminal nt : tmp) {
                m.addToCell(i,0,nt);
            }
        }

        for(int j=1;j<word.size();j++) {
            for(int i=1;i<=word.size()-j;i++) {
               // m.clearCell(i,j);
                for(int k=0;k<j;k++) {

                    for(Nonterminal nonterminal : g.getNonterminals()) {
                        int finalI = i;
                        int finalK = k;
                        int finalJ = j;
                        g.getRules().stream()
                                .filter(rule -> rule.getComingFrom().equals(nonterminal))
                                .map(Rule::getRightSide)
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
    @SuppressWarnings("unused")
    private static boolean languageContainsWordAsSymbolList(Grammar grammar, List<Symbol> word) {
        List<String> w = word.stream().map(Symbol::getName).collect(Collectors.toList());
        return languageContainsWord(grammar, w);
    }

    /**
     * checks if the language of a grammar contains a certain word
     * @param grammar the {@link Grammar}
     * @param word the word, that should be checked as a List of String (a string for a terminal)
     * @return true, if the language contains the given word
     */
    @SuppressWarnings("unused")
    public static boolean languageContainsWord(Grammar grammar, List<String> word) {
        if(word.isEmpty() || (word.size()==1 && word.get(0).equals(""))) {
            return GrammarUtil.languageContainsLambda(grammar);
        }
        Grammar grammar1 = removeLambdaRules(grammar);
        Grammar grammar2 = eliminateUnitRules(grammar1);
        Grammar grammar3 = chomskyNormalForm(grammar2);
        return checkMatrix(cyk(grammar3,word),grammar3);
    }
    @SuppressWarnings("unused")
    private static boolean checkMatrix(Matrix matrix, Grammar grammar) {
        //    matrix.printConsole(new BufferedWriter(new OutputStreamWriter(System.out)));
        return matrix != null && matrix.getCell(1, matrix.getWord().size() - 1).contains(grammar.getStartSymbol());
    }
    @SuppressWarnings("unused")
    private static boolean pointsOnCurrentChar(List<String> word, int i, List<Symbol> list) {
        return list.get(0).getName().equals(word.get(i-1));
    }

    @SuppressWarnings("unused")
    private static Configuration getStartConfiguration(Grammar g) {
        List<Symbol> list = new ArrayList<>();
        list.add(g.getStartSymbol());
        return new Configuration(list,null,g);
    }

    /**
     * returns a derivation path for the given grammar and word (if there is any)
     * @param g the {@link} Grammar
     * @param word the word as a list of {@link Symbol}s
     * @param bound upper bound for the breadth-first search
     * @return a derivation path as a list of {@link Configuration}s
     */
    @SuppressWarnings("unused")
    public static List<Configuration> getPath(Grammar g, List<Symbol> word, long bound) {
        Configuration end = getEndConfig(g,word,bound);
        if(end == null) {
            return null;
        }
        Configuration current = end;
        List<Configuration> result = new ArrayList<>();
        while(current.getPrevious() != null) {
            result.add(0,current);
            current = current.getPrevious();
        }
        result.add(0,result.get(0).getPrevious());
        return result;
    }
    @SuppressWarnings("unused")
    private static Configuration getEndConfig(Grammar g, List<Symbol> word, long bound) {
        if(!languageContainsWordAsSymbolList(g,word)) {
            return null;
        }
        Configuration result=null;
        Configuration startConfig = GrammarUtil.getStartConfiguration(g);
        Queue<Configuration> queue = new LinkedList<>();
        queue.add(startConfig);
        HashSet<Configuration> alreadySeen = new HashSet<>();
        alreadySeen.add(startConfig);
        long counter=0;

        while(!queue.isEmpty() && counter < bound) {
            counter++;
            Configuration current = queue.poll();
            if(current.getConfig().equals(word)) {
                result=current;
              //  goOn = false;
                   //  goOn = false;
           break;
            }
            queue.addAll(current.getChildren().stream().filter(x -> !alreadySeen.contains(x)).collect(Collectors.toList()));
            alreadySeen.addAll(current.getChildren());


        }
        return result;
    }

    /**
     * checks if two lists are equal
     * @param list1 List 1
     * @param list2 List 2
     * @return true, if the lists are equal
     */
    @SuppressWarnings("unused")
    @Deprecated
    public static boolean listEqual(List<Symbol> list1, List<Symbol> list2) {
        if(list1.size() != list2.size()) {
            return false;
        } else {
            boolean res = true;
            for(int i=0;i<list1.size();i++) {
                res&=list1.get(i).equals(list2.get(i));
            }
            return res;
        }
    }
    /*
     ---------------------------------------------------------------------------------------------------------------*
     -                                           PDA                                                               -*
     ---------------------------------------------------------------------------------------------------------------*
     */
    /**
     * Returns an PushDownAutomaton constructed from a {@link Grammar}
     * @param g     the Grammar, from which a pda should be created
     * @return      the pda to the grammar
     */
    @SuppressWarnings("unused")
    public static PushDownAutomaton toPDA(Grammar g) {
        State start_state;
        StackLetter initialStackLetter;
        ArrayList<PDARule> rules;
        PushDownAutomaton tmp = helper(g);
        /* create the rules **/
        rules = createRules(g,tmp);

        return new PushDownAutomaton(tmp.getStartState(),tmp.getInitialStackLetter(),rules,"PDA_"+g.getName(),null);
    }
    private  static PushDownAutomaton helper(Grammar g) {
        Set<InputLetter> input = g.getTerminals().stream()
                .map(t -> new InputLetter(t.getName())).collect(Collectors.toSet());
        Set<StackLetter> stack = g.getTerminals()
                .stream()
                .map(t -> new StackLetter(t.getName())).collect(Collectors.toSet());
        stack.addAll(g.getNonterminals().stream().map(t-> new StackLetter(t.getName())).collect(Collectors.toSet()));
        return new PushDownAutomaton(input,stack,new State("z"),new StackLetter(g.getStartSymbol().getName()),new ArrayList<>(),"PDA_"+g.getName(),null);
    }

    public static ArrayList<Printable> toPDAAsPrintables(Grammar g) {
        ArrayList<Printable> res = new ArrayList<>();
        Grammar obj0 = g;
        PushDownAutomaton obj1 = helper(g);
        ArrayList<PDARule> rules1 = new ArrayList<>();
        rules1.addAll(createRules_StepOne(g,obj1));
        PushDownAutomaton obj2 = new PushDownAutomaton(obj1.getInputAlphabet(),obj1.getStackAlphabet(),obj1.getStartState(),obj1.getInitialStackLetter(),rules1,obj1.getName(),null);
        ArrayList<PDARule> rules2 = new ArrayList<>();
        rules2.addAll(createRules_StepOne(g,obj1));
        rules2.addAll(createRules_StepTwo(g,obj1));
        PushDownAutomaton obj3 = new PushDownAutomaton(obj1.getInputAlphabet(),obj1.getStackAlphabet(),obj1.getStartState(),obj1.getInitialStackLetter(),rules2,obj1.getName(),null);
        res.add(obj0);
        res.add(obj1);
        res.add(obj2);
        res.add(obj3);
        return res;
    }
    @SuppressWarnings("unused")


    private static ArrayList<PDARule> createRules(Grammar grammar, PushDownAutomaton help) {
        ArrayList<PDARule> rules = new ArrayList<>();

        rules.addAll(createRules_StepOne(grammar,help));
        rules.addAll(createRules_StepTwo(grammar,help));
        return rules;
    }
    private static ArrayList<PDARule> createRules_StepOne(Grammar grammar, PushDownAutomaton help) {
        ArrayList<PDARule> rules = new ArrayList<>();

        // for every Rule A --> q add (z0,lambda,A) -> (z0,q). The first element of q is the new ToS
        grammar.getRules().forEach(rule -> {
            List<Symbol> list = new ArrayList<>();
            list.addAll(rule.getRightSide());
            State comingFrom = help.getStartState();
            State goingTo = help.getStartState();
            InputLetter readIn = InputLetter.NULLSYMBOL;
            StackLetter oldTos = new StackLetter(rule.getComingFrom().getName());
            List<StackLetter> newTos = calculateNewTos(list);
            PDARule pdaRule = new PDARule(comingFrom, goingTo, readIn, oldTos, newTos);
            rules.add(pdaRule);


        });
        return rules;
    }
    private static ArrayList<PDARule> createRules_StepTwo(Grammar grammar, PushDownAutomaton help) {
        ArrayList<PDARule> rules = new ArrayList<>();
        //for every a add (z0, a, a) -> (z0, lambda)
        grammar.getTerminals().stream()
                .filter(t -> !t.equals(Terminal.NULLSYMBOL))
                .forEach(a -> {
                    State comingFrom = help.getStartState();
                    State goingTo = help.getStartState();
                    InputLetter readIn = new InputLetter(a.getName());
                    StackLetter oldTos = new StackLetter(a.getName());
                    ArrayList<StackLetter> newTos=new ArrayList<>();
                    newTos.add(StackLetter.NULLSYMBOL);
                    rules.add(new PDARule(comingFrom,goingTo,readIn,oldTos,newTos));

                });
        return rules;
    }
    @SuppressWarnings("unused")
    private static List<StackLetter> calculateNewTos(List<Symbol> list) {
        return list.stream().map(s -> new StackLetter(s.getName())).collect(Collectors.toCollection(ArrayList::new));
    }

    /*
     * ---------------------------------------------------------------------------------------------------------------*
     * -                                other things                                                                 -*
     * ---------------------------------------------------------------------------------------------------------------*
     */
    @SuppressWarnings("unused")
    static ArrayList<ArrayList<String>> getHeader(Grammar grammar, boolean latex) {
        ArrayList<ArrayList<String>> header= new ArrayList<>(3);
        header.add(0,getTerminalsAsStrings(grammar));
        header.add(1,getNonterminalsAsStrings(grammar,latex));
        ArrayList<String> tmp=new ArrayList<>();
        if(latex) {
            tmp.add(Printer.toLatex(grammar.getStartSymbol().getName()));
        } else {
            tmp.add(grammar.getStartSymbol().getName());
        }
        header.add(2,tmp);
        return header;
    }

    @SuppressWarnings("unused")
    private static ArrayList<String> getTerminalsAsStrings(Grammar grammar) {
        //Get all of the grammar's terminals in order of their appearance in the rules.
        ArrayList<Terminal> terminals = GrammarUtil.getTerminalsInOrder(grammar);
        return (ArrayList<String>) terminals.stream().filter(terminal -> !terminal.equals(Terminal.NULLSYMBOL)).map(Terminal::getName).collect(Collectors.toList());
    }

    @SuppressWarnings("unused")
    private static ArrayList<String> getNonterminalsAsStrings(Grammar grammar, boolean latex) {
        ArrayList<Nonterminal> nonterminals = GrammarUtil.getNonterminalsInOrder(grammar);
        if (latex) {
            return (ArrayList<String>) nonterminals.stream().map(Nonterminal::nameToLatex).collect(Collectors.toList());
        } else {
            return (ArrayList<String>) nonterminals.stream().map(Nonterminal::getName).collect(Collectors.toList());
        }

    }

    /**
     * the special rule for empty word guarantees, that if the start symbol points on lambda,
     * it does not occurs on any right side.
     * @param g the grammar, that should be modified
     * @param original the original grammar, which functions as the previous version of the newly created grammar
     * @return a grammar, where the special rule vor the empty word has been applied
     */
    @SuppressWarnings("unused")
    private static Grammar specialRuleForEmptyWord(Grammar g, Grammar original) {
    //    if(GrammarUtil.startSymbolPointsOnLambda(original) && GrammarUtil.startSymbolOnRightSide(original)) {

            HashSet<Rule> freshRules = new HashSet<>();
            Nonterminal newNonterminal = new Nonterminal("S_0");
            g.getRules().stream()
                    .filter(rule -> rule.getComingFrom().equals(g.getStartSymbol()))
                    .forEach(rule -> {
                        ArrayList<Symbol> tmp = new ArrayList<>();
                        rule.getRightSide().forEach(sym -> {
                            if(sym.equals(g.getStartSymbol())) {
                                tmp.add(newNonterminal);
                            } else {
                                tmp.add(sym);
                            }
                        });
                        freshRules.add(new Rule(g.getStartSymbol(),tmp));
                    });
            g.getRules().forEach(rule -> {
                Nonterminal start;
                if(rule.getComingFrom().equals(g.getStartSymbol())) {
                    start = newNonterminal;
                } else {
                    start = rule.getComingFrom();
                }
                ArrayList<Symbol> tmp = new ArrayList<>();
                rule.getRightSide().forEach(sym -> {
                    if(sym.equals(g.getStartSymbol())) {
                        tmp.add(newNonterminal);
                    } else {
                        tmp.add(sym);
                    }
                });
                freshRules.add(new Rule(start,tmp));
            });
            if(GrammarUtil.startSymbolPointsOnLambda(original)) {
                ArrayList<Symbol> t = new ArrayList<>();
                t.add(Terminal.NULLSYMBOL);
                freshRules.add(new Rule(g.getStartSymbol(),t));
            }
            return new Grammar(g.getStartSymbol(),freshRules,g.getName(),original);
   //     }
     //   return g;
    }

    public static ArrayList<Printable> renameNonterminalsAsPrintables(Grammar g) {
        ArrayList<Printable> res = new ArrayList<>();
        res.add(0,g);
        res.add(1,renameNonterminals(g));
        return res;
    }
    @SuppressWarnings("unused")
    public static Grammar renameNonterminals(Grammar g) {
        Set<Nonterminal> nts = g.getNonterminals();
        Grammar res = g;
        for(Nonterminal nt : nts) {
            res=replaceNonterminal(res,nt,new Nonterminal(nt.getName()+"_xxxxx"));
        }

        nts = res.getNonterminals();
        HashSet<String> excluded = new HashSet<>();
        for(Nonterminal nt: nts) {
            String name = GrammarUtil.chooseName(res,excluded);
            excluded.add(name);
            res=replaceNonterminal(res,nt,new Nonterminal(name));
        }
        return new Grammar(res.getStartSymbol(),res.getRules(),res.getName(),g);
    }

    /*
    Some boolean method that check a grammar for certain traits
     */
    /**
     * checks if the given grammar has unit rules
     * @param g the {@link Grammar}
     * @return true, if the grammar has unit rules
     */
    @SuppressWarnings("unused")
    public static boolean hasUnitRules(Grammar g) {
        return g.getRules().stream()
                .anyMatch(rule -> rule.getRightSide().size()==1 && rule.getRightSide().get(0) instanceof Nonterminal);
      }
    /**
     * checks, if the start symbol occurs on a right side of any rule
     * @param g the grammar g
     * @return true, if it is on a right side
     */
    @SuppressWarnings("unused")
    private static boolean startSymbolOnRightSide(Grammar g) {
        return g.getRules().stream().map(Rule::getRightSide)
                .anyMatch(list -> list.stream().anyMatch(symbol -> symbol.equals(g.getStartSymbol())));
        
    }
    /**
     * checks if a grammar is without any rules pointing on lambda
     *
     * @param g the grammar
     * @return true, if the grammar is lambda-free
     */
    @SuppressWarnings("unused")
    public static boolean isLambdaFree(Grammar g) {
        //true, if there is a rule A -> lambda with A != S
        boolean check1 = g.getRules().stream().filter(rule -> !rule.getComingFrom().equals(g.getStartSymbol()))
                .allMatch(rule -> rule.getRightSide().stream().allMatch(symbol -> !symbol.equals(Terminal.NULLSYMBOL)));
        //true, if the language contains Lambda (start symbol is nullable) and the start symbol points on lambda and the start symbol is not on any right Side
        boolean check2 = GrammarUtil.languageContainsLambda(g) && GrammarUtil.startSymbolPointsOnLambda(g) && !GrammarUtil.startSymbolOnRightSide(g);
        //true, if the language does not contain lambda
        boolean check3 = !GrammarUtil.languageContainsLambda(g);
        return check1 && (check2 || check3);
    }

    /**
     * checks, if lambda is in the language of {@link Grammar} g
     * @param g the grammar that is checked
     * @return true, if the language contains lambda
     */
    @SuppressWarnings("unused")
    private static boolean languageContainsLambda(Grammar g) {
       return GrammarUtil.calculateNullable(g).contains(g.getStartSymbol());
    }

    /**
     * Is there any rule in the format S to lambda?
     * @param g the grammar that is checked
     * @return true, if the start symbol points on lambda
     */
    @SuppressWarnings("unused")
    private static boolean startSymbolPointsOnLambda(Grammar g) {
        return g.getRules().stream()
                .anyMatch(rule -> rule.getComingFrom().equals(g.getStartSymbol()) && rule.getRightSide().stream().allMatch(symbol -> symbol.equals(Terminal.NULLSYMBOL)));
    }


    private static boolean hasUnreachableNonterminals(Grammar g) {
        return g.getNonterminals().stream().anyMatch(nt -> g.getRules().stream().noneMatch(rule -> rule.getRightSide().contains(nt)));
    }


    private static boolean isUnreachable(Grammar g, Nonterminal nt) {
        return g.getRules().stream().noneMatch(rule -> rule.getRightSide().contains(nt)) && !nt.equals(g.getStartSymbol());
    }

    /**
     * checks if a Grammar has Nonterminals which don't point anywhere
     * @param g a {@link Grammar}
     * @return true, if the grammar has nonterminals, which do not appear on any left side
     */

    private static boolean hasDeadEnds(Grammar g) {
        return g.getRules().stream()
                .anyMatch(rule -> rule.getRightSide().stream()
                        .filter(sym -> sym instanceof Nonterminal)
                        .anyMatch(sym -> isDead(g, (Nonterminal) sym)));
    }

    private static boolean isDead(Grammar g, Nonterminal nt) {
        return g.getRules().stream().filter(rule -> rule.getComingFrom().equals(nt)).count() == 0;
    }



    private static boolean hasRedundantNonterminals(Grammar g) {
        for(Nonterminal nt1 : g.getNonterminals()) {
            for(Nonterminal nt2 : g.getNonterminals()) {
                if(!nt1.equals(nt2)) {
                    boolean check = compareRules(g.getRules(),nt1,nt2);
                    if(check) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*
        Simplification of grammar
     */


    public static ArrayList<Printable> simplifyAsPrintable(Grammar g) {
        ArrayList<Printable> res = new ArrayList<>();
        res.add(0,g);
        res.add(1,simplify(g));
        return res;
    }
    public static Grammar simplify(Grammar g) {
        Grammar res = removeRedundantNonterminals(removeDeadEnds(removeUnreachableNonterminals(g)));
        return new Grammar(res.getStartSymbol(),res.getRules(),res.getName(),g);
    }
    /**
     * removes redundant nonterminal that can occur when using {@link #chomskyNormalForm(Grammar)}
     * @param g the {@link Grammar}
     * @return a {@link Grammar} without redundant rules
     */
    @SuppressWarnings("unused")
    public static Grammar removeRedundantNonterminals(Grammar g) {
        Grammar res = redundantSet(g);
        return new Grammar(res.getStartSymbol(),res.getRules(),res.getName(),g);

    }

    @SuppressWarnings("unused")
    private static Grammar redundantSet(Grammar g) {
        for(Nonterminal nt1 : g.getNonterminals()) {
            for(Nonterminal nt2 : g.getNonterminals()) {
                if(!nt1.equals(nt2)) {
                    boolean check = compareRules(g.getRules(),nt1,nt2);
                    if(check) {
                        return redundantSet(replaceNonterminal(g,nt1,nt2));
                    }
                }
            }
        }
        return g;
    }



    @SuppressWarnings("unused")
    private static boolean compareRules(Set<Rule> rules, Nonterminal one, Nonterminal two) {
        Set<Rule> rules1 = rules.stream().filter(rule -> rule.getComingFrom().equals(one)).collect(Collectors.toSet());
        Set<Rule> rules2 = rules.stream().filter(rule -> rule.getComingFrom().equals(two))
                .map(rule -> new Rule(one,rule.getRightSide()))
                .collect(Collectors.toSet());

        boolean check = rules1.size() == rules2.size();
        if(check) {
            for(Rule r1 : rules1) {
                check &= rules2.contains(r1);
            }
        }
        return check;
    }


    /**
     * replaces a Nonterminal through another
     * @param g a {@link Grammar}
     * @param old the {@link Nonterminal} that should be replaced
     * @param replacedBy the {@link Nonterminal} that replaces the old Nonterminal
     * @return a {@link Grammar} which is equivalent to to the old grammar
     */
    @SuppressWarnings("unused")
    private static Grammar replaceNonterminal(Grammar g, Nonterminal old, Nonterminal replacedBy) {
        Set<Rule> rules1=g.getRules().stream()
                .map(rule -> {
                    if(rule.getComingFrom().equals(old)) {
                        return new Rule(replacedBy,rule.getRightSide());
                    } else {
                        return rule;
                    }
                })
                .map(rule -> {
                    if(rule.getRightSide().contains(old)) {
                        List<Symbol> list = rule.getRightSide().stream().map(symbol -> {
                            if(symbol.equals(old)) {
                                return replacedBy;
                            } else {
                                return symbol;
                            }
                        }).collect(Collectors.toList());
                        return new Rule(rule.getComingFrom(),list);
                    } else {
                        return rule;
                    }
                }).collect(Collectors.toSet());
        if(g.getStartSymbol().equals(old)) {
            return new Grammar(replacedBy, rules1, g.getName(), g);
        } else {
            return new Grammar(g.getStartSymbol(), rules1, g.getName(), g);
        }
    }

    /**
     * removes nonterminals, that can be reached
     * @param g the Grammar g
     * @return a Grammar without unreachable Nonterminals
     */
    public static Grammar removeUnreachableNonterminals(Grammar g) {
        if(hasUnreachableNonterminals(g)) {
            HashSet<Nonterminal> unreachable = new HashSet<>();
            g.getNonterminals().stream().filter(nt -> isUnreachable(g,nt)).forEach(unreachable::add);
            Set<Rule> rules;
            rules = g.getRules().stream().filter(rule -> !unreachable.contains(rule.getComingFrom())).collect(Collectors.toSet());
            return new Grammar(g.getStartSymbol(),rules,g.getName(),g);
        } else {
            return g;
        }
    }



    /**
     * removes Nonterminals that don't point anywhere. Attention: Every Rule,
     * that contains such Nonterminals is deleted
     * @param g a {@link Grammar}
     * @return a {@link Grammar} that does not have Nonterminal that point nowhere
     */
    @SuppressWarnings("unused")
    public static Grammar removeDeadEnds(Grammar g) {
        Grammar res = g;

       while(hasDeadEnds(res)) {
           res=removeDeadEnds_helper(res);
       }
        return res;
    }

    /**
     * Helper-method for {@link #removeDeadEnds(Grammar)}. removes rules, which contain nonterminal that don't point anywhere
     * @param g {@link Grammar}
     * @return a {@link Grammar} with one some dead ends removed
     */
    @SuppressWarnings("unused")
    private static Grammar removeDeadEnds_helper(Grammar g) {
        HashSet<Rule> rules = new HashSet<>();
        g.getRules().forEach(rule -> {
            if(rule.getRightSide().stream()
                    .filter(sym -> sym instanceof Nonterminal)
                    .allMatch(symbol -> g.getRules().stream().filter(r -> r.getComingFrom().equals(symbol)).count()>0)) {
                rules.add(rule);
            }
        });
        return new Grammar(g.getStartSymbol(),rules,g.getName(),g);
    }

    public Grammar addRule(Grammar g, Rule rule) {
        HashSet<Rule> rules = new HashSet<>();
        rules.addAll(g.getRules());
        rules.add(rule);
        return new Grammar(g.getStartSymbol(),rules,g.getName(),g);
    }

}


package GrammarSimulator;

import GrammarParser.Visitor;
import GrammarParser.lexer.Lexer;
import GrammarParser.lexer.LexerException;
import GrammarParser.node.Start;
import GrammarParser.parser.Parser;
import GrammarParser.parser.ParserException;
import com.sun.java.swing.plaf.windows.WindowsTreeUI;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

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
    private static ArrayList<Terminal> getTerminalsInOrder(Nonterminal comingFrom, ArrayList<Terminal> terminals, HashSet<Nonterminal> visitedSymbols) {
        if (!visitedSymbols.contains(comingFrom)) {
            HashSet<Nonterminal> nextSymbols = new HashSet<>();

            //Add all terminals, that comingFrom points to to the list
            //and all nonterminals, that comingFrom points to, to nextSymbols.
            for (ArrayList<Symbol> symbols : comingFrom.getSymbolLists()) {
                for(Symbol currentSymbol : symbols) {
                    if (currentSymbol instanceof Terminal && !terminals.contains(currentSymbol)) {
                        terminals.add((Terminal) currentSymbol);
                    } else if(currentSymbol instanceof Nonterminal){
                        nextSymbols.add((Nonterminal) currentSymbol);
                    }
                }
            }
            visitedSymbols.add(comingFrom);

            //Call this method recursively with all symbols in nextSymbols as comingFrom.
            for (Nonterminal goingTo : nextSymbols) {
                getTerminalsInOrder(goingTo, terminals, visitedSymbols);
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
    private static ArrayList<Nonterminal> getNonterminalsInOrder(Nonterminal comingFrom, ArrayList<Nonterminal> nonterminals) {
        HashSet<Nonterminal> nextSymbols = new HashSet<>();

        try {
            //Determine, whether comingFrom's name should be add, or not.
            //If yes, add it to the list.
            if (!nonterminals.contains(comingFrom)) {
                nonterminals.add(comingFrom);
            }

            //Do the same for all symbols, that comingFrom points to.
            for(ArrayList<Symbol> symbols : comingFrom.getSymbolLists()) {
                for(Symbol currentSymbol : symbols) {
                    if (currentSymbol instanceof Nonterminal && !nonterminals.contains(currentSymbol)) {
                        nonterminals.add((Nonterminal) currentSymbol);
                        nextSymbols.add((Nonterminal) currentSymbol);
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        //Call this method recursively with all symbols in nextSymbols as comingFrom.
        for(Nonterminal goingTo : nextSymbols) {
            getNonterminalsInOrder(goingTo, nonterminals);
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
                for (ArrayList<Symbol> symbolList : nonterminal.getSymbolLists()) {
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
                }
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
        return visitor.getGrammar();
    }

    /**
     * Returns an ArrayList that contains all terminals in order of their appearance in the grammars's rules.
     *
     * @param grammar The grammar.
     * @return The ArrayList.
     */
    public static ArrayList<Terminal> getTerminalsInOrder(Grammar grammar) {
        ArrayList<Terminal> terminals = getTerminalsInOrder(grammar.getStartSymbol(), new ArrayList<>(), new HashSet<>());
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
        ArrayList<Nonterminal> nonterminals = getNonterminalsInOrder(grammar.getStartSymbol(), new ArrayList<>());
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

            writeRules(writer, grammar);
        } catch (IOException e) {
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
    public static HashSet<Nonterminal> calculateNullable(Grammar grammar) {
        HashSet<Nonterminal> result = new HashSet<>();
        boolean changed = true;

        while(changed) {
            changed = false;
            for (Nonterminal nonterminal : grammar.getNonterminals()) {
                for (ArrayList<Symbol> symbolList : nonterminal.getSymbolLists()) {
                    if(symbolList.stream().allMatch(symbol -> (symbol.getName().equals("epsilon") || result.contains(symbol))) && !result.contains(nonterminal)) {
                        result.add(nonterminal);

                        changed=true;
                    }
                }
            }
        }

        return  result;
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
        boolean changed = true;

        //Initialize the result-map.
        for(Nonterminal nonterminal : grammar.getNonterminals()) {
            result.put(nonterminal, new HashSet<>());
        }

        while(changed) {
            changed = false;
            for (Nonterminal nonterminal : grammar.getNonterminals()) {
                for (ArrayList<Symbol> symbolList : nonterminal.getSymbolLists()) {
                    for (int i = 0; i < symbolList.size(); i++) {
                        if (symbolList.get(i) instanceof Terminal) {
                            if (!symbolList.get(i).getName().equals("epsilon") ) {
                                if(!result.get(nonterminal).contains(symbolList.get(i))) {
                                    //If the current symbol is a terminal and not the empty word,
                                    //add it to the first-set of the current nonterminal.
                                    result.get(nonterminal).add((Terminal) symbolList.get(i));
                                    changed = true;
                                }
                                break;
                            }
                        } else if (symbolList.get(i) instanceof Nonterminal) {
                            if(!result.get(nonterminal).containsAll(result.get(symbolList.get(i)))) {
                                //If the current symbol is a nonterminal, add its first-set to the first-set of the current nonterminal.
                                result.get(nonterminal).addAll(result.get(symbolList.get(i)));
                                changed = true;
                            }
                            if (!nullable.contains(symbolList.get(i))) {
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
    public static HashMap<Nonterminal, HashSet<Terminal>> calculateFollow(Grammar grammar) {
        HashMap<Nonterminal, HashSet<Terminal>> result = new HashMap<>();
        HashSet<Nonterminal> nullable = calculateNullable(grammar);
        HashMap<Nonterminal, HashSet<Terminal>> firsts = calculateFirst(grammar);
        boolean changed = true;

        //Initialize the result-map.
        for(Nonterminal nonterminal : grammar.getNonterminals()) {
            result.put(nonterminal, new HashSet<>());
        }
        result.get(grammar.getStartSymbol()).add(new Terminal("$"));

        while(changed) {
            changed = false;
            for (Nonterminal nonterminal : grammar.getNonterminals()) {
                for (ArrayList<Symbol> symbolList : nonterminal.getSymbolLists()) {
                    //Go through each list of symbols and calculate the follow-set of each nonterminal in these lists.
                    for (int i = 0; i < symbolList.size(); i++) {
                        if (symbolList.get(i) instanceof Nonterminal) {
                            if (i == symbolList.size() - 1) {
                                if (symbolList.get(i) instanceof Nonterminal) {
                                    //If the last symbol of the current list is a nonterminal, the follow-set of this nonterminal
                                    //contains the follow-set of the current nonterminal.
                                    if (!result.get(symbolList.get(i)).containsAll(result.get(nonterminal))) {
                                        result.get(symbolList.get(i)).addAll(result.get(nonterminal));
                                        changed = true;
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
                                        changed = true;
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
                                                    changed = true;
                                                }
                                            } else if (symbolList.get(k) instanceof Nonterminal) {
                                                if(!result.get(symbolList.get(i)).containsAll(firsts.get(result.get(k)))) {
                                                    //If the current symbol is a nonterminal, add its first-set to the follow-set of symbolList.get(i).
                                                    result.get(symbolList.get(i)).addAll(firsts.get(result.get(k)));
                                                    changed = true;
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
                                                changed = true;
                                            }
                                        }
                                    } else if (j == symbolList.size() - 1 && nullable.contains(symbolList.get(j))) {
                                        if(!result.get(symbolList.get(i)).containsAll(result.get(nonterminal))) {
                                            //If result.get(j) has reached the last symbol of the current list
                                            //and this symbol is a nonterminal, we have to add the follow-set of this nonterminal
                                            //to the follow-set of symbol-list.get(i);
                                            result.get(symbolList.get(i)).addAll(result.get(nonterminal));
                                            changed = true;
                                        }
                                    }

                                } else {
                                    if(!result.get(symbolList.get(i)).contains(symbolList.get(j))) {
                                        //If symbolList.get(j) is a terminal, we can just add it to the follow-set of symbolList.get(i).
                                        result.get(symbolList.get(i)).add((Terminal) symbolList.get(j));
                                        changed = true;
                                    }
                                }
                            }
                        }
                    }
                }
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
            for(ArrayList<Symbol> symbolList : nonterminal.getSymbolLists()) {
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
            }
        }

        return result;
    }

    public static void removeUnneccesaryEpsilons(Grammar g) {
        for(Nonterminal nt : g.getNonterminals()) {
            HashSet<ArrayList<Symbol>> res=new HashSet<>();
            for(ArrayList<Symbol> list : nt.getSymbolLists()) {
                ArrayList<Symbol> temp=(ArrayList<Symbol>) list.stream().filter(x -> !x.equals(Terminal.NULLSYMBOL)).collect(Collectors.toList());
                if(temp.size()!=0) {
                    res.add(temp);
                } else {
                    temp=new ArrayList<>();
                    temp.add(Terminal.NULLSYMBOL);
                    res.add(temp);
                }
            }
            nt.getSymbolLists().clear();
            nt.getSymbolLists().addAll(res);
        }
    }
    public static void replaceLambda(Grammar g) {
        Iterator<Terminal> it=g.getTerminals().iterator();
        boolean hasNull=false;
        Terminal toRemove=null;
        while(it.hasNext()) {
            Terminal t=it.next();
            if(t.getName().equals("epsilon")) {
                toRemove=t;
                hasNull=true;
            }
        }
        if(hasNull) {
            g.getTerminals().remove(toRemove);
            g.getTerminals().add(Terminal.NULLSYMBOL);
        }
    }

    /**
     * the third step of the algorithm to delete lambda-rules
     * all rules which have only epsilons on the right side are removed
     * furthermore, nonterminals, which now do not appear on the left side of any rule, are removed
     * @param g the grammar g
     * @param again first time calling: true. during the algorithm new lambda-rules can emerge, so that method has to be called again, but this time with again set to false
     */
    public static void removeLambdaRules_StepThree(Grammar g, boolean again) {

        // delete lambda-rules
        for(Nonterminal nt : g.getNonterminals()) {
            HashSet<ArrayList<Symbol>> tmp = new HashSet<>();

            tmp.addAll(nt.getSymbolLists().stream()
                    .filter(list -> !list.stream().allMatch(symbol -> symbol.equals(Terminal.NULLSYMBOL)))
                    .collect(Collectors.toList()));
            nt.getSymbolLists().clear();
            nt.getSymbolLists().addAll(tmp);
        }
        //these nonterminals can be removed. Store them in toRemove
        List<Symbol> toRemove = new ArrayList<>();
        for(Nonterminal nt : g.getNonterminals()) {
            if(nt.getSymbolLists().isEmpty()) {
                toRemove.add(nt);
            }
        }
        // the symbols that can be removed are replaced by the nullsymbol
        for(Nonterminal nt : g.getNonterminals()) {
            HashSet<ArrayList<Symbol>> tmp = new HashSet<>();
            for(ArrayList<Symbol> list : nt.getSymbolLists()) {
                ArrayList<Symbol> tmpList=new ArrayList<>();
                for(int i=0;i<list.size();i++) {
                    if(toRemove.contains(list.get(i))) {
                        tmpList.add(Terminal.NULLSYMBOL);
                    } else {
                        tmpList.add(list.get(i));
                    }
                }
                tmp.add(tmpList);
            }
            nt.getSymbolLists().clear();
            nt.getSymbolLists().addAll(tmp);
        }
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

    }

    /**
     * the second step of the algorithm to delete lambda-rules.
     * For every rule, that contains a nullable symbol on the right side, a rule with this symbol replaced by "epsilon" is added to the ruleset
     * @param grammar The Grammar
     * @param nullable The set, which contains all the nullable terminals
     */
    public static void removeLambdaRules_StepTwo(Grammar grammar, HashSet<Nonterminal> nullable) {
        GrammarUtil.removeUnneccesaryEpsilons(grammar);
        for(Nonterminal nonterminal : grammar.getNonterminals()) {
            //contains all rules for this nonterminal which need to be edited
            Queue<ArrayList<Symbol>> queue = new LinkedList<>();

            queue.addAll(GrammarUtil.getSymbolListsWithoutEmptyRules(nonterminal, grammar));
            if (!queue.isEmpty()) {
                boolean changed = true;
                //contains every rule that is already edited
                HashSet<ArrayList<Symbol>> alreadySeen=new HashSet<>();
                while (changed && !queue.isEmpty()) { //stop, if there is no change anymore
                    changed = false;
                    // gets the current head of the queue and removes it
                    ArrayList<Symbol> current = queue.poll();

                    ArrayList<Symbol> newRightSide = new ArrayList<>();
                    newRightSide.addAll(current);
                    HashSet<ArrayList<Symbol>> toAdd = new HashSet<>();
                    for (int i = 0; i < current.size(); i++) {
                        // if the i-th Symbol is a nullable symbol, remove it and replace it with lambda
                        if (nullable.contains(current.get(i))) {
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
                        if(alreadySeen.contains(current)) {
                            queue.add(current);
                        } else {
                            alreadySeen.add(current);
                            changed=true;
                        }

                    }
                }
                nonterminal.getSymbolLists().clear();
                nonterminal.getSymbolLists().addAll(queue);
                nonterminal.getSymbolLists().addAll(alreadySeen);
            }
        }
    }

    /**
     * @author Isabel Wingen
     * @param nt a nonterminal of the Grammar g
     * @param g the Grammar g
     * @return a HashSet with all right sides except the empty rule belonging to nonterminal nt
     */
    public static HashSet<ArrayList<Symbol>> getSymbolListsWithoutEmptyRules(Nonterminal nt, Grammar g) {
        HashSet<ArrayList<Symbol>> tmp=nt.getSymbolLists();
        HashSet<ArrayList<Symbol>> res=new HashSet<>();
        for(ArrayList<Symbol> list : tmp) {
            boolean allNull=true;
            for(Symbol sym : list) {
                if(sym.equals(Terminal.NULLSYMBOL)) {
                    allNull=allNull & true;
                } else {
                    allNull=false;
                }
            }
            if(allNull==false) {
                res.add(list);
            }
        }
        return res;

    }
    public static boolean specialRuleForEmptyWord(Grammar g) {
        if(GrammarUtil.startSymbolPointsOnLambda(g) && GrammarUtil.startSymbolOnRightSide(g)) {

            Nonterminal newSymbol=new Nonterminal("S#",new HashSet<>());
            for(Nonterminal nt : g.getNonterminals()) {
                HashSet<ArrayList<Symbol>> newRightSide=new HashSet<>();
                for(ArrayList<Symbol> list : nt.getSymbolLists()) {
                    ArrayList<Symbol> tmp=new ArrayList<>();
                    for(Symbol sym : list) {
                        if(sym.equals(g.getStartSymbol())) {
                            tmp.add(newSymbol);
                        } else {
                            tmp.add(sym);
                        }
                    }
                    newRightSide.add(tmp);
                }
                nt.getSymbolLists().clear();
                nt.getSymbolLists().addAll(newRightSide);
            }
            newSymbol.getSymbolLists().addAll(g.getStartSymbol().getSymbolLists());
            return true;

        }
        return false;
    }
    public static HashSet<Nonterminal> unitRules(Grammar g) {
        HashSet<Nonterminal> unitRules=new HashSet<>();
        g.getNonterminals().stream().filter(nonterminal -> nonterminal.
                getSymbolLists().
                stream().
                anyMatch(list -> list.size()==1 && list.get(0) instanceof Nonterminal)).
                forEach(nt -> unitRules.add(new Nonterminal(nt.getName(),nt.getSymbolLists())));
        for(Nonterminal t : unitRules) {
            HashSet<ArrayList<Symbol>> tmp=new HashSet<>();
            tmp.addAll(t.getSymbolLists().stream().filter(list -> list.size()==1 && list.get(0) instanceof Nonterminal).collect(Collectors.toList()));
            t.setSymbolLists(tmp);
        }
       
        return unitRules;
    }

    public static boolean startSymbolOnRightSide(Grammar g) {

        return g.getNonterminals().stream().anyMatch(nt -> nt.getSymbolLists().stream().
        anyMatch(list -> list.stream().anyMatch(symbol -> symbol.equals(g.getStartSymbol()))));
    }
    /**
     * @author Isabel Wingen
     * @param g the grammar
     * @return true, if the grammar is lambda-free
     */
    public static boolean isLambdaFree(Grammar g) {
     return   !g.getNonterminals().stream().anyMatch(nonterminal ->
             nonterminal.getSymbolLists().stream().anyMatch(list ->
                     list.stream().anyMatch(symbol -> symbol.equals(Terminal.NULLSYMBOL))));
    }
    public static boolean languageContainsLambda(Grammar g) {
       return GrammarUtil.calculateNullable(g).contains(g.getStartSymbol());
    }
    public static boolean startSymbolPointsOnLambda(Grammar g) {
        return g.getStartSymbol().getSymbolLists().stream().anyMatch(list -> list.stream().
                allMatch(symbol -> symbol.equals(Terminal.NULLSYMBOL)));
    }

}


package PushDownAutomatonSimulator;

import AutomatonSimulator.Automaton;
import GrammarSimulator.Symbol;
import GrammarSimulator.Terminal;
import PushDownAutomatonParser.Visitor;
import PushDownAutomatonParser.lexer.Lexer;
import PushDownAutomatonParser.lexer.LexerException;
import PushDownAutomatonParser.node.Start;
import PushDownAutomatonParser.parser.Parser;
import PushDownAutomatonParser.parser.ParserException;

import java.io.*;
import java.util.ArrayList;
import java.util.Stack;
import java.util.stream.Collectors;

import static java.lang.System.out;
import static java.util.stream.Collectors.*;

/**
 * Created by Isabel on 29.10.2016.
 */
public class PushDownAutomatonUtil {


    public static PushDownAutomaton parse(String fileInput) throws ParserException, IOException, LexerException {
        StringReader reader = new StringReader(fileInput);
        PushbackReader r = new PushbackReader(reader, 100);
        Lexer l = new Lexer(r);
        Parser parser = new Parser(l);
        Start start = parser.parse();
        Visitor visitor = new Visitor();
        start.apply(visitor);
        PushDownAutomaton pda = visitor.getPushDownAutomaton();
        return pda;
    }

    public static PushDownAutomaton parse(File file) throws IOException, LexerException, ParserException {
        String name = file.getName();
        PushDownAutomaton pda;
        BufferedReader grammarReader = new BufferedReader(new FileReader(file));
        String string = "";
        String line;
        while ((line = grammarReader.readLine()) != null) {
            string = string + line + "\n";
        }
        pda = PushDownAutomatonUtil.parse(string);
        pda.setName(name);
        grammarReader.close();
        return pda;

    }
    public static void save(PushDownAutomaton pda, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

            writer.write("{'");
            writer.write(pda.getInputAlphabet().keySet().stream().collect(joining("', '")));
            writer.write("';'");
            writer.write(pda.getStackAlphabet().keySet().stream().collect(joining("', '")));
            writer.write("';");
            writer.write(pda.getStates().keySet().stream().collect(joining(", ")));
            writer.write("; ");
            writer.write(pda.getStartState().getName());
            writer.write("; '");
            writer.write(pda.getInitalStackLetter().getName());
            writer.write("'}\n\n");

            pda.getStates().values().stream().filter(state -> state.getRules()!=null && !state.getRules().isEmpty())
                    .forEach(state -> {
                        state.getRules().stream().forEach(rule -> {
                            try {
                                writer.write("'"+rule.getComingFrom().getName()+"', '");
                                writer.write(rule.getReadIn().getName()+"', '");
                                writer.write(rule.getOldToS().getName()+"' --> '");
                                writer.write(rule.getGoingTo().getName()+"', '");
                                writer.write(rule.getNewToS().stream().map(x -> x.getName()).collect(joining("', '"))+"'\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    });
            writer.flush();
            writer.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean doRule(Rule rule, PushDownAutomaton pda) {
        if(!equals(rule.getComingFrom(),pda.getCurrentState())) { // the rule can not be applied. WRONG STATE
            return false;
        } else if(!equals(rule.getOldToS(),pda.getStack().peek())) { //the rule can not be applied. WRONG TOS
            return false;
        } else {
            if(equals(rule.getReadIn(),asInputLetter(Terminal.NULLSYMBOL))) {
                pda.getStack().pop();
                if(!(rule.getNewToS().size()==1 && equals(rule.getNewToS().get(0),asStackLetter(Terminal.NULLSYMBOL)))) {
                    for(int i=rule.getNewToS().size()-1;i>-1;i--) {
                        pda.getStack().push(rule.getNewToS().get(i));
                    }
                }
                pda.setCurrentState(rule.getGoingTo());
                return true;
            } else if(equals(rule.getReadIn(),pda.getCurrentInput().get(0))) {
                pda.getStack().pop();
                if(!(rule.getNewToS().size()==1 && equals(rule.getNewToS().get(0),asStackLetter(Terminal.NULLSYMBOL)))) {
                    for (int i = rule.getNewToS().size() - 1; i > -1; i--) {
                        pda.getStack().push(rule.getNewToS().get(i));
                    }
                }
                pda.setCurrentState(rule.getGoingTo());
                pda.getCurrentInput().remove(0);
                return true;
            } else {
                return false;
            }

        }
    }
    private static boolean equals(InputLetter a, InputLetter b) {
        return a.getName().equals(b.getName());
    }
    private static boolean equals(State a, State b) {
        return a.getName().equals(b.getName());
    }
    private static boolean equals(StackLetter a, StackLetter b) {
        return a.getName().equals(b.getName());
    }


    public static InputLetter asInputLetter(Symbol s) {
        if(s.equals(Terminal.NULLSYMBOL)) {
            return InputLetter.NULLSYMBOL;
        }
        return new InputLetter(s.getName());
    }
    public static StackLetter asStackLetter(Symbol s) {
        if(s.equals(Terminal.NULLSYMBOL)) {
            return StackLetter.NULLSYMBOL;
        }
        return new StackLetter(s.getName());
    }
    public static InputLetter asInputLetter(String s) {
        return new InputLetter(s);
    }
    public static StackLetter asStackLetter(String s) {
        return new StackLetter(s);
    }
    public static boolean addToStackAlphabet(StackLetter st, PushDownAutomaton pda) {
        if(pda.getStackAlphabet().get(st.getName())==null) {
            pda.getStackAlphabet().put(st.getName(), st);
            return true;
        } else {
            return false;
        }

    }
    public static boolean addToInputAlphabet(InputLetter ip, PushDownAutomaton pda) {
        if(pda.getInputAlphabet().get(ip.getName())==null) {
            pda.getInputAlphabet().put(ip.getName(),ip);
            return true;
        } else {
            return false;
        }
    }
    public static StackLetter getStackLetterWithName(String s, PushDownAutomaton pda) {
        return pda.getStackAlphabet().get(s);
    }
    public static InputLetter getInputLetterWithName(String s, PushDownAutomaton pda) {
       return pda.getInputAlphabet().get(s);
    }
    public static State getStateWithName(String name, PushDownAutomaton pda) {
        return pda.getStates().get(name);
    }
}

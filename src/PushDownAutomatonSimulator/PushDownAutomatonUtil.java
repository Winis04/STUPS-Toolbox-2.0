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
        grammarReader.close();
        return pda;

    }



    public static void print(PushDownAutomaton pda) {
        out.println("States: ");
        out.println(pda.getStates().stream().map(state -> state.getName()).collect(joining(", ")));
        out.println("Start State");
        out.println(pda.getStartState().getName());
        out.println("Input Alphabet: ");
        out.println(pda.getInputAlphabet().stream().map(letter -> letter.getName()).collect(joining(", ")));
        out.println("Stack Alphabet: ");
        out.println(pda.getStackAlphabet().stream().map(letter -> letter.getName()).collect(joining(", ")));
        out.println("initial stack symbol:");
        out.println(pda.getInitalStackLetter().getName());
        out.println("rules:");
        pda.getStates().stream().forEach(state -> {
            state.getRules().stream().forEach(rule -> {
                System.out.printf("%s, %s, %s --> %s, %s\n",state.getName(),rule.getReadIn().getName(),rule.getOldToS().getName(),rule.getGoingTo().getName(), rule.getNewToS().stream().map(symbol -> symbol.getName()).collect(joining(", ")));

            });
        });
        if(!pda.getStack().isEmpty()) {
            out.println("current Stack:");
            out.print(pda.getStack().stream().map(el -> el.getName()).collect(joining("\n")));
            System.out.println(" \t\t<- ToS");
        } else {
            System.out.println("Stack is empty, input is accepted!");
        }
        if(!pda.getCurrentInput().isEmpty()) {
            out.println("current Input:");
            out.println(pda.getCurrentInput().stream().map(x -> x.getName()).collect(joining(" ")));
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
        return new InputLetter(s.getName());
    }
    public static StackLetter asStackLetter(Symbol s) {
        return new StackLetter(s.getName());
    }
    public static InputLetter asInputLetter(String s) {
        return new InputLetter(s);
    }
    public static StackLetter asStackLetter(String s) {
        return new StackLetter(s);
    }
    public static boolean addToStackAlphabet(StackLetter st, PushDownAutomaton pda) {
        if(!pda.getStackAlphabet().stream().anyMatch(letter -> letter.getName().equals(st.getName()))) {
            pda.getStackAlphabet().add(st);
            return true;
        } else {
            return false;
        }

    }
    public static boolean addToInputAlphabet(InputLetter ip, PushDownAutomaton pda) {
        if(!pda.getInputAlphabet().stream().anyMatch(letter -> letter.getName().equals(ip.getName()))) {
            pda.getInputAlphabet().add(ip);
            return true;
        } else {
            return false;
        }
    }
    public static StackLetter getStackLetterWithName(String s, PushDownAutomaton pda) {
        for(StackLetter st : pda.getStackAlphabet()) {
            if(st.getName().equals(s)) {
                return st;
            }
        }
        return null;
    }
    public static InputLetter getInputLetterWithName(String s, PushDownAutomaton pda) {
        for(InputLetter ip : pda.getInputAlphabet()) {
            if(ip.getName().equals(s)) {
                return ip;
            }
        }
        return null;
    }
    public static State getStateWithName(String name, PushDownAutomaton pda) {
        for(State s : pda.getStates()) {
            if(s.getName().equals(name)) {
                return s;
            }
        }
        return null;
    }
}

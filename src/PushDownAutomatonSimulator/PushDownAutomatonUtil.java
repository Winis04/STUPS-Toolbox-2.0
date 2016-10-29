package PushDownAutomatonSimulator;

import java.util.stream.Collectors;

import static java.lang.System.out;
import static java.util.stream.Collectors.*;

/**
 * Created by Isabel on 29.10.2016.
 */
public class PushDownAutomatonUtil {
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
        for(Rule rule : pda.getRules()) {
            out.printf("%s, %s, %s --> ",rule.getComingFrom().getName(),rule.getReadIn().getName(),rule.getOldToS().getName());
            out.printf("%s, %s\n",rule.getGoingTo().getName(),rule.getNewToS().stream().map(letter -> letter.getName()).collect(joining(", ")));

        }
    }
}

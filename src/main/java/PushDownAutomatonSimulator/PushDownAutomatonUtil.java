package PushDownAutomatonSimulator;



import PushDownAutomatonParser.lexer.Lexer;
import PushDownAutomatonParser.lexer.LexerException;
import PushDownAutomatonParser.node.Start;
import PushDownAutomatonParser.parser.Parser;
import PushDownAutomatonParser.parser.ParserException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;


public class PushDownAutomatonUtil {


    public static PushDownAutomaton parse(String fileInput, String name) throws ParserException, IOException, LexerException {
        StringReader reader = new StringReader(fileInput);
        PushbackReader r = new PushbackReader(reader, 100);
        Lexer l = new Lexer(r);
        Parser parser = new Parser(l);
        Start start = parser.parse();
        Visitor visitor = new Visitor();
        start.apply(visitor);
        return visitor.getPushDownAutomaton(name);
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
        pda = PushDownAutomatonUtil.parse(string,name);
        grammarReader.close();
        return pda;

    }
    public static void save(PushDownAutomaton pda, File file) {
        save(pda,file.getAbsolutePath());
    }
    public static void save(PushDownAutomaton pda, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

            writer.write("{'");
            writer.write(pda.getInputAlphabet().stream().map(InputLetter::getName).collect(joining("', '")));
            writer.write("';'");
            writer.write(pda.getStackAlphabet().stream().map(StackLetter::getName).collect(joining("', '")));
            writer.write("';");
            writer.write(pda.getStates().stream().map(State::getName).collect(joining(", ")));
            writer.write("; ");
            writer.write(pda.getStartState().getName());
            writer.write("; '");
            writer.write(pda.getInitialStackLetter().getName());
            writer.write("'}\n\n");

            pda.getRules().forEach(rule -> {
                try {
                    writer.write("'" + rule.getComingFrom().getName() + "', '");
                    writer.write(rule.getReadIn().getName() + "', '");
                    writer.write(rule.getOldToS().getName() + "' --> '");
                    writer.write(rule.getGoingTo().getName() + "', '");
                    writer.write(rule.getNewToS().stream().map(StackLetter::getName).collect(joining("', '")) + "'\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.flush();
            writer.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static RunThroughInfo doRule(PDARule rule, RunThroughInfo run) {
        PushDownAutomaton pda = run.getMyPDA();
        if(run.getStack().isEmpty()) {
            return run;
        }
        if(!run.getCurrentState().equals(rule.getComingFrom())) {//rule cannot be applied, because the states don't match
            return run;
        }
        if(!run.getStack().peek().equals(rule.getOldToS())) { //rule cannot be applied because the ToS doesn't match
            return run;
        }
        if(run.getInput().isEmpty()) {
            if (!rule.getReadIn().equals(InputLetter.NULLSYMBOL)) {
                return run;
            } else {
                State currentState = rule.getGoingTo();
                Stack<StackLetter> stack = new Stack<>();
                stack.addAll(run.getStack());
                stack.pop();
                ArrayList<StackLetter> list = new ArrayList<>();
                list.addAll(rule.getNewToS());

                for(int i=list.size()-1;i>=0;i--) {
                    StackLetter stackLetter = list.get(i);
                    if(stackLetter != StackLetter.NULLSYMBOL) {
                        stack.push(stackLetter);
                    }
                }

                return new RunThroughInfo(stack,run.getInput(),currentState,run,pda);
            }
        }
        if(!run.getInput().get(0).equals(rule.getReadIn())) { // the first letter and the letter of the rule are not the same
            if(!rule.getReadIn().equals(InputLetter.NULLSYMBOL)) { // the rule cannot be applied, because the input is not right
                return run;
            } else {
                State currentState = rule.getGoingTo();
                Stack<StackLetter> stack = new Stack<>();
                stack.addAll(run.getStack());
                stack.pop();
                ArrayList<StackLetter> list = new ArrayList<>();
                list.addAll(rule.getNewToS());

                for(int i=list.size()-1;i>=0;i--) {
                    StackLetter stackLetter = list.get(i);
                    if(stackLetter != StackLetter.NULLSYMBOL) {

                        stack.push(stackLetter);
                    }
                }
                return new RunThroughInfo(stack,run.getInput(),currentState,run,pda);
            }
        }

        //the rule is not a lambda-rule and can be applied

        State currentState = rule.getGoingTo();
        Stack<StackLetter> stack;
        stack = run.getStack(); //TODO: check Change
        stack.pop();
        ArrayList<StackLetter> list = new ArrayList<>();
        list.addAll(rule.getNewToS());

        for(int i=list.size()-1;i>=0;i--) {
            StackLetter stackLetter = list.get(i);
            if(stackLetter != StackLetter.NULLSYMBOL) {

                stack.push(stackLetter);
            }
        }
        ArrayList<InputLetter> input = new ArrayList<>();

        input.addAll(run.getInput().stream().skip(1).collect(Collectors.toList()));
        return new RunThroughInfo(stack,input,currentState,run,pda);
    }

    public static RunThroughInfo startRunThrough(PushDownAutomaton pda, List<String> strings) {
        State currentState = pda.getStartState();
        Stack<StackLetter> stack = new Stack<>();
        stack.add(pda.getInitialStackLetter());
        ArrayList<InputLetter> input = (ArrayList<InputLetter>) strings.stream().map(InputLetter::new).collect(toList());
        return new RunThroughInfo(stack,input,currentState,null,pda);
    }

}

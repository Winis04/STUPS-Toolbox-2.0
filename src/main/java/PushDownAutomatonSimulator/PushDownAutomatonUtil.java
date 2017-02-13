package PushDownAutomatonSimulator;



import GrammarSimulator.*;
import PushDownAutomatonParser.lexer.Lexer;
import PushDownAutomatonParser.lexer.LexerException;
import PushDownAutomatonParser.node.Start;
import PushDownAutomatonParser.parser.Parser;
import PushDownAutomatonParser.parser.ParserException;

import java.io.*;
import java.util.*;
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
     public static void save(PushDownAutomaton pda, File file) throws IOException {
        save(pda,file.getAbsolutePath());
    }
    public static void save(PushDownAutomaton pda, String fileName) throws IOException {

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
                    if(!stackLetter.equals(StackLetter.NULLSYMBOL)) {
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
                    if(!stackLetter.equals(StackLetter.NULLSYMBOL)) {

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
            if(!stackLetter.equals(StackLetter.NULLSYMBOL)) {

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

    public static boolean checkIfLengthLesserThenTwo(PushDownAutomaton pda) {
        return pda.getRules().stream().allMatch(rule -> rule.getNewToS().size() <= 2);
    }

    /**
     * transforms a PDA to a Grammar
     * @param pda the {@link PushDownAutomaton}
     * @return a {@link Grammar}
     */
    public static Grammar toGrammar(PushDownAutomaton pda) {
       if(checkIfLengthLesserThenTwo(pda)) {



           HashSet<Rule> rules = new HashSet<>();
           Nonterminal startSymbol = new Nonterminal("S");
           //Step 1
           for (State state : pda.getStates()) {
               String name = toNameOfNonterminal(pda.getStartState(), pda.getInitialStackLetter(), state);
               ArrayList<Symbol> rightSide = new ArrayList<>();
               rightSide.add(new Nonterminal(name));
               rules.add(new Rule(startSymbol, rightSide));
           }
           //Step 2
           List<PDARule> step2 = pda.getRules()
                   .stream()
                   .filter(rule -> rule.getNewToS().size() == 1 && rule.getNewToS().get(0).equals(StackLetter.NULLSYMBOL))
                   .collect(toList());
           for (PDARule pdaRule : step2) {
               String name = toNameOfNonterminal(pdaRule.getComingFrom(), pdaRule.getOldToS(), pdaRule.getGoingTo());
               Nonterminal comingFrom = new Nonterminal(name);
               ArrayList<Symbol> rightSide = new ArrayList<>();
               rightSide.add(new Terminal(pdaRule.getReadIn().getName()));
               rules.add(new Rule(comingFrom, rightSide));
           }

           //Step 3
           List<PDARule> step3 = pda.getRules()
                   .stream()
                   .filter(rule -> rule.getNewToS().size()==1 && !rule.getNewToS().get(0).equals(StackLetter.NULLSYMBOL))
                   .collect(toList());
           for(PDARule pdaRule : step3) {
               for(State z : pda.getStates()) {
                   String nameLeft = toNameOfNonterminal(pdaRule.getComingFrom(), pdaRule.getOldToS(), z);
                   String nameRight = toNameOfNonterminal(pdaRule.getGoingTo(), pdaRule.getNewToS().get(0), z);
                   ArrayList<Symbol> rightSide = new ArrayList<>();
                   rightSide.add(0, new Terminal(pdaRule.getReadIn().getName()));
                   rightSide.add(1, new Nonterminal(nameRight));
                   rules.add(new Rule(new Nonterminal(nameLeft), rightSide));
               }
           }

           //Step 4
           List<PDARule> step4 = pda.getRules()
                   .stream()
                   .filter(rule -> rule.getNewToS().size()==2)
                   .collect(toList());
           for(PDARule pdaRule : step4) {
               for(State z1 : pda.getStates()) {
                   for(State z2 : pda.getStates()) {
                       String nameLeft = toNameOfNonterminal(pdaRule.getComingFrom(), pdaRule.getOldToS(), z1);
                       String nameFirst = toNameOfNonterminal(pdaRule.getGoingTo(), pdaRule.getNewToS().get(0), z2);
                        String nameSecond = toNameOfNonterminal(z2,pdaRule.getNewToS().get(1),z1);
                       ArrayList<Symbol> rightSide = new ArrayList<>();
                       rightSide.add(0,new Terminal(pdaRule.getReadIn().getName()));
                       rightSide.add(1,new Nonterminal(nameFirst));
                       rightSide.add(2,new Nonterminal(nameSecond));
                       rules.add(new Rule(new Nonterminal(nameLeft),rightSide));
                   }
               }
           }
           Grammar grammar1 = GrammarUtil.removeDeadEnds(new Grammar(startSymbol, rules, pda.getName() + "_Grammar", null));
           Grammar grammar2 = GrammarUtil.removeUnnecessaryEpsilons(grammar1);
           Grammar grammar3= GrammarUtil.removeUnreachableNonterminals(grammar2);
           return new Grammar(grammar3.getStartSymbol(),grammar3.getRules(),grammar3.getName(),null);
       } else {
           return null;
       }
    }

    private static String toNameOfNonterminal(State z, StackLetter A, State x) {
        String a = z.getName();
        String b = A.getName();
        if(b.equals(StackLetter.NULLSYMBOL.getName())) {
            b = "epsilon";
        }
        if(!Nonterminal.validName(b)) {
            b=Nonterminal.makeValid(b);
        }
        String c = x.getName();
        return a+"_"+b+"_"+c;
    }

   public static PushDownAutomaton splitRules(PushDownAutomaton pda) {
        List<PDARule> res = new ArrayList<>();
        List<PDARule> old = new ArrayList<>(pda.getRules());
        int j=0;
        for(PDARule rule : old) {
            Set<PDARule> tmpSet = splitRule(rule,j);
            j+=tmpSet.size()-1;
            res.addAll(tmpSet);
        }
        return new PushDownAutomaton(pda.getStartState(),pda.getInitialStackLetter(),res,pda.getName(),pda);

    }

    private static Set<PDARule> splitRule(PDARule rule, int offset) {
        int k=rule.getNewToS().size();
        HashSet<PDARule> res = new HashSet<>();
        if(k <= 2) {
            res.add(rule);
            return res;
        } else {
            res.add(new PDARule(rule.getComingFrom(),new State("zz"+(1+offset)),rule.getReadIn(),rule.getOldToS(),rule.getNewToS().subList(k-2,k)));
            for(int i=1;i<k-2;i++) {
                State comingFrom = new State("zz"+(i+offset));
                State goingTo = new State("zz"+(i+1+offset));
                StackLetter oldTos = rule.getNewToS().get(k-1-i);
                List<StackLetter> newTos = rule.getNewToS().subList(k-2-i,k-i);
                res.add(new PDARule(comingFrom,goingTo,InputLetter.NULLSYMBOL,oldTos,newTos));
            }
            res.add(new PDARule(new State("zz"+(k-2+offset)),rule.getGoingTo(),InputLetter.NULLSYMBOL,rule.getNewToS().get(1),rule.getNewToS().subList(0,2)));
            return res;
        }

    }

    public static PushDownAutomaton renameStates(PushDownAutomaton pda) {
        Set<State> states = new HashSet<>(pda.getStates());
        PushDownAutomaton res=pda;
        for(State state : states) {
            res=renameState(res,state,new State(state.getName()+"_old"));
        }
        List<State> sorted = res.getStates().stream()
                .sorted((x,y) -> x.getName().compareTo(y.getName()))
                .collect(toList());


        int i=0;
        for(State state : sorted) {
            res = renameState(res,state,new State("z"+i));
            i++;
        }
        return new PushDownAutomaton(res.getStartState(),res.getInitialStackLetter(),res.getRules(),res.getName(),pda);
    }

    private static PushDownAutomaton renameState(PushDownAutomaton pda, State old, State replacedBy) {
       List<PDARule> freshRules = pda.getRules().stream()
                .map(rule -> {
                    if(rule.getComingFrom().equals(old)) {
                        if(rule.getGoingTo().equals(old)) {
                            return new PDARule(replacedBy,replacedBy,rule.getReadIn(),rule.getOldToS(),rule.getNewToS());
                        } else {
                            return new PDARule(replacedBy,rule.getGoingTo(),rule.getReadIn(),rule.getOldToS(),rule.getNewToS());
                        }
                    } else {
                        if(rule.getGoingTo().equals(old)) {
                            return new PDARule(rule.getComingFrom(),replacedBy,rule.getReadIn(),rule.getOldToS(),rule.getNewToS());
                        } else {
                            return rule;
                        }
                    }
                }).collect(Collectors.toList());
        if(old.equals(pda.getStartState())) {
            return new PushDownAutomaton(replacedBy,pda.getInitialStackLetter(),freshRules,pda.getName(),pda);
        } else {
            return new PushDownAutomaton(pda.getStartState(),pda.getInitialStackLetter(),freshRules,pda.getName(),pda);
        }

    }

}

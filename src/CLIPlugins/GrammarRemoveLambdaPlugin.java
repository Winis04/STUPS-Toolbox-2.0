package CLIPlugins;

import GrammarSimulator.*;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by isabel on 20.10.16.
 */
public class GrammarRemoveLambdaPlugin implements CLIPlugin {

    private boolean errorFlag = false;

    @Override
    public String[] getNames() {
        return new String[]{"rlr", "remove-lambda-rules"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        return true;
    }

    @Override
    public String getHelpText() {
        return "removes lambda-rules. Does not take any parameters";
    }

    @Override
    public Object execute(Object object, String[] parameters) {
        errorFlag = false;
        if(object == null) {
            System.out.println("Please load a grammar before using this command!");
            errorFlag = true;
            return null;
        }
        //first step: calculate the Nullable set
        Grammar grammar = (Grammar) object;
        HashSet<Nonterminal> nullable= GrammarUtil.calculateNullable(grammar);
        System.out.printf("First Step:\nnullable = {%s}\n",nullable.stream().map(nt -> nt.getName()).collect(Collectors.joining(", ")));
        //second step: for every rule with a nullable nonterminal, add that rule without this nonterminal

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
                            newRightSide.set(i, grammar.getNullSymbol());
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
        GrammarUtil.removeUnneccesaryEpsilons(grammar);
        System.out.println("Step 2:");
        GrammarUtil.print(grammar);
        GrammarUtil.removeLambdaRules(grammar,true);

        System.out.println("Step 3:");


        GrammarUtil.print(grammar);
        return null;
    }

    @Override
    public Class inputType() {
        return Grammar.class;
    }

    @Override
    public Class outputType() {
        return null;
    }

    @Override
    public boolean errorFlag() {
        return errorFlag;
    }
}

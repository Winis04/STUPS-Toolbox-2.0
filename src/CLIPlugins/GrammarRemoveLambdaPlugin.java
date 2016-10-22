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


        Grammar grammar = (Grammar) object;
        GrammarUtil.replaceLambda(grammar);
        //first step: calculate the Nullable set
        HashSet<Nonterminal> nullable= GrammarUtil.calculateNullable(grammar);
        System.out.printf("Step 1:\nnullable = {%s}\n",nullable.stream().map(nt -> nt.getName()).collect(Collectors.joining(", ")));
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
        GrammarUtil.removeUnneccesaryEpsilons(grammar);
        System.out.println("Step 2:");
        GrammarUtil.print(grammar);
        GrammarUtil.removeLambdaRules(grammar,true);

        System.out.println("Step 3:");


        GrammarUtil.print(grammar);
        return null;
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
    public static void removeLambdaRules(Grammar g, boolean again) {
        for(Nonterminal nt : g.getNonterminals()) {
            HashSet<ArrayList<Symbol>> tmp = new HashSet<>();

            tmp.addAll(nt.getSymbolLists().stream()
                    .filter(list -> !(list.size() == 1 && list.get(0).equals(Terminal.NULLSYMBOL)))
                    .collect(Collectors.toList()));
            nt.getSymbolLists().clear();
            nt.getSymbolLists().addAll(tmp);
        }
        //these nonterminals can be removed
        List<Symbol> toRemove = new ArrayList<>();
        for(Nonterminal nt : g.getNonterminals()) {
            if(nt.getSymbolLists().isEmpty()) {
                toRemove.add(nt);
            }
        }

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
        ArrayList<Symbol> bla=new ArrayList<>();
        bla.addAll(g.getNonterminals());
        g.getNonterminals().clear();
        for(Symbol nonterminal : bla) {
            if(!toRemove.contains((Nonterminal) nonterminal)) {
                g.getNonterminals().add((Nonterminal) nonterminal);
            }
        }
        if(again) {
            GrammarUtil.removeUnneccesaryEpsilons(g);
            GrammarUtil.removeLambdaRules(g,false);
            g.getTerminals().remove(Terminal.NULLSYMBOL);
        }

    }

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

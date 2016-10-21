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
                Queue<ArrayList<Symbol>> queue=new LinkedList<>();
                queue.addAll(nonterminal.getSymbolLists());
                boolean changed=true;
                while(changed) {
                    changed=false;
                    ArrayList<Symbol> current=queue.poll();
                    ArrayList<Symbol> newRightSide=new ArrayList<>();
                    newRightSide.addAll(current);
                    HashSet<ArrayList<Symbol>> toAdd=new HashSet<>();
                    for(int i=0;i<current.size();i++) {
                        if(nullable.contains(current.get(i))) {
                            newRightSide.set(i,grammar.getNullSymbol());
                            if(queue.contains(newRightSide)) {
                                newRightSide.set(i,current.get(i));
                            } else {
                                queue.add(newRightSide);
                                queue.add(current);
                                changed=true;
                                break;
                            }
                        }
                    }
                    if(!changed) {
                        queue.add(current);
                    }
                }
                nonterminal.getSymbolLists().clear();
                nonterminal.getSymbolLists().addAll(queue);
            }

        return null;
    }
    private boolean addNewRules(ArrayList<Integer> positions, ArrayList<Symbol> rightSide, HashSet<ArrayList<Symbol>> res) {
        boolean changed=false;
        for(Integer i : positions) {
            ArrayList<Symbol> newRightSide=new ArrayList<>();
            for(int j=0;j<rightSide.size();j++) {
                if(i.intValue()==j) {
                    newRightSide.add(new Terminal("epsilon"));
                } else {
                    newRightSide.add(rightSide.get(j));
                }
            }
            if(!res.contains(newRightSide)) {
                res.add(newRightSide);
                changed=true;
            }
        }
        return changed;
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

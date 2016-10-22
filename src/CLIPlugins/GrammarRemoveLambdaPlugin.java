package CLIPlugins;

import GrammarSimulator.*;

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
        //
       if(GrammarUtil.specialRuleForEmptyWord(grammar)) {
           System.out.println("added new symbol S#:");
           GrammarUtil.print(grammar);
       }

        //first step: calculate the Nullable set
        HashSet<Nonterminal> nullable= GrammarUtil.calculateNullable(grammar);
        System.out.printf("Step 1:\nnullable = {%s}\n",nullable.stream().map(nt -> nt.getName()).collect(Collectors.joining(", ")));



        //second step: for every rule with a nullable nonterminal, add that rule without this nonterminal

        System.out.println("Step 2:");
        GrammarUtil.removeLambdaRules_StepTwo(grammar,nullable);
        GrammarUtil.print(grammar);

        System.out.println("Step 3:");
        GrammarUtil.removeLambdaRules_StepThree(grammar,true);
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
            GrammarUtil.removeLambdaRules_StepThree(g,false);
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

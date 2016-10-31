package CLIPlugins;

import GrammarSimulator.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * Created by Isabel on 26.10.2016.
 */
public class GrammarCYK implements CLIPlugin {
    private boolean errorFlag;
    private Explanation type;
    @Override
    public String[] getNames() {
        return new String[]{"cyk"};
    }

    @Override
    public boolean checkParameters(String[] parameters) {
        if(parameters.length==2) {
            if(parameters[0].equals("no")) {
                type=Explanation.NO;
                return true;
            } else if(parameters[0].equals("short")) {
                type=Explanation.SHORT;
                return true;
            } else if(parameters[0].equals("long")) {
                type=Explanation.LONG;
                return true;
            } else {
                System.out.println("This Input is not valid");
                return false;
            }
        } else {
            System.out.println("the number of parameters isn't right");
            return false;
        }
    }

    @Override
    public String getHelpText() {
        return "does the cyk-algorithm. Takes a word of the language as a parameter";
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
        Matrix matrix;
        switch (type) {
            case SHORT:
                matrix=GrammarUtil.cykWithShortOutput(grammar,parameters[1]);
                break;
            case LONG:
                matrix=GrammarUtil.cykWithLongOutput(grammar,parameters[1]);
                break;
            case NO:
                matrix=GrammarUtil.cykWithNoOutput(grammar,parameters[1]);
                break;
            default:
                matrix=new Matrix(1,1,"default");
        }
        if(matrix != null) {
            if (matrix.getCell(1,parameters[1].length()-1).contains(grammar.getStartSymbol())) {
                System.out.println("L(G) contains " + parameters[1] + ".");
            } else {
                System.out.println("L(G) does not contain " + parameters[1] + ".");
            }
        }
        LinkedHashSet<Node> trees= GrammarUtil.makeSyntaxTree(matrix,grammar);
        Node test=new Node();
        for(Node node : trees) {
            test=node;
        }
        ArrayList<Node> list=new ArrayList<>();
        list.addAll(test.getChildren());
        System.out.printf("%s |- ",test.getName());
        boolean changed=true;
        while(changed) {
            changed = false;
            System.out.printf("%s |- ", list.stream().map(child -> child.getName()).collect(joining(", ")));
            int i = 0;
            boolean stop=false;
            while (!stop && i < list.size()) {
                Node toRemove = list.get(i);
                if(!toRemove.getChildren().isEmpty()) {
                    list.remove(i);
                    list.addAll(i,toRemove.getChildren());
                    stop=true;
                    changed=true;
                }
                i++;
            }
        }
        System.out.println("");
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

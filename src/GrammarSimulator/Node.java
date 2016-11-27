package GrammarSimulator;

import Print.Printable;
import Print.Printer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

/**
 * Created by Isabel on 24.10.2016.
 */
public class Node {
    /**
     * for the deep-search
     * is true, if the node was already visited
     */
    private boolean visited;
    /**
     * when was this node finished
     */
    private int dfe;
    /**
     * when was this node visited
     */
    private int dfs;
    /**
     * the following nodes
     */
    private LinkedHashSet<Node> children;
    /**
     *
     */
    private int number;

    /**
     * the value of this node
     */
    private Nonterminal value;
    /**
     *
     */
    private String name;
    public Node(Nonterminal value) {
        this.visited=false;
        this.number=0;
        this.dfe=0;
        this.dfe=0;
        this.children=new LinkedHashSet<>();
        this.value=value;
        this.name=value.getName();
    }
    public Node(String name) {
        this.visited=false;
        this.number=0;
        this.dfe=0;
        this.dfe=0;
        this.children=new LinkedHashSet<>();
        this.name=name;
        this.value=new Nonterminal(name,null);
    }
    public Node(Nonterminal value, LinkedHashSet<Node> children) {
        this.visited=false;
        this.number=0;
        this.dfe=0;
        this.dfe=0;
        this.children=children;
        this.value=value;
        this.name=value.getName();
    }
    public Node() {
        this.children=new LinkedHashSet<>();
        this.value=null;
        this.visited=false;
        this.number=0;
        this.dfe=0;
        this.dfe=0;
    }


    public boolean isVisited() {
        return visited;
    }



    public int getDfe() {
        return dfe;
    }

    public int getDfs() {
        return dfs;
    }

    public HashSet<Node> getChildren() {
        return children;
    }

    public Nonterminal getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }


    public void setDfe(int dfe) {
        this.dfe = dfe;
    }

    public void setDfs(int dfs) {
        this.dfs = dfs;
    }

    public void setChildren(LinkedHashSet<Node> children) {
        this.children = children;
    }


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public static final Node EMPTY_NODE=new Node();


}

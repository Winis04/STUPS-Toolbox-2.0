package GrammarSimulator;

import java.util.HashSet;
import java.util.LinkedHashSet;



@SuppressWarnings("unused")
class Node {
    /**
     * for the deep-search
     * is true, if the node was already visited
     */
    @SuppressWarnings("unused")
    private boolean visited;
    /**
     * when was this node finished
     */
    @SuppressWarnings("unused")
    private int dfe;
    /**
     * when was this node visited
     */
    @SuppressWarnings("unused")
    private int dfs;
    /**
     * the following nodes
     */
    @SuppressWarnings("unused")
    private final LinkedHashSet<Node> children;
    /**
     *
     */
    @SuppressWarnings("unused")
    private int number;

    /**
     * the value of this node
     */
    @SuppressWarnings("unused")
    private final Nonterminal value;
    /**
     *
     */
    @SuppressWarnings("unused")
    private String name;
    @SuppressWarnings("unused")
    public Node(Nonterminal value) {
        this.visited=false;
        this.number=0;
        this.dfe=0;
        this.dfe=0;
        this.children=new LinkedHashSet<>();
        this.value=value;
        this.name=value.getName();
    }
    @SuppressWarnings("unused")
    public Node(String name) {
        this.visited=false;
        this.number=0;
        this.dfe=0;
        this.dfe=0;
        this.children=new LinkedHashSet<>();
        this.name=name;
        this.value=new Nonterminal(name);
    }
    @SuppressWarnings("unused")
    public Node(Nonterminal value, LinkedHashSet<Node> children) {
        this.visited=false;
        this.number=0;
        this.dfe=0;
        this.dfe=0;
        this.children=children;
        this.value=value;
        this.name=value.getName();
    }
    @SuppressWarnings("unused")
    public Node() {
        this.children=new LinkedHashSet<>();
        this.value=null;
        this.visited=false;
        this.number=0;
        this.dfe=0;
        this.dfe=0;
    }


    @SuppressWarnings("unused")
    boolean isNotVisited() {
        return !visited;
    }



    @SuppressWarnings("unused")
    public int getDfe() {
        return dfe;
    }

    @SuppressWarnings("unused")
    public int getDfs() {
        return dfs;
    }

    @SuppressWarnings("unused")
    public HashSet<Node> getChildren() {
        return children;
    }

    @SuppressWarnings("unused")
    public Nonterminal getValue() {
        return value;
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public void setVisited() {
        this.visited = true;
    }


    @SuppressWarnings("unused")
    public void setDfe(int dfe) {
        this.dfe = dfe;
    }

    @SuppressWarnings("unused")
    public void setDfs(int dfs) {
        this.dfs = dfs;
    }


    @SuppressWarnings("unused")
    public int getNumber() {
        return number;
    }

    @SuppressWarnings("unused")
    public void setNumber(int number) {
        this.number = number;
    }


}

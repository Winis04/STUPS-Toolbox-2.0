package GrammarSimulator;

import java.util.HashSet;

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
     * for the deep-search
     * true, if the node is finished (all child-nodes are finished)
     */
    private boolean finished;
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
    private HashSet<Node> children;
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
        this.finished=false;
        this.dfe=0;
        this.dfe=0;
        this.children=new HashSet<>();
        this.value=value;
        this.name=value.getName();
    }
    public Node(Nonterminal value, HashSet<Node> children) {
        this.visited=false;
        this.finished=false;
        this.dfe=0;
        this.dfe=0;
        this.children=children;
        this.value=value;
        this.name=value.getName();
    }

    public boolean isVisited() {
        return visited;
    }

    public boolean isFinished() {
        return finished;
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

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setDfe(int dfe) {
        this.dfe = dfe;
    }

    public void setDfs(int dfs) {
        this.dfs = dfs;
    }

    public void setChildren(HashSet<Node> children) {
        this.children = children;
    }
}

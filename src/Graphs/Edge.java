package Graphs;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author siggiaze
 */
public class Edge {

    private float Weight;
    private Node Source, Target;
    private int label;
    private boolean visited = false;

    public Edge(Node from, Node to, float weight, int label) {
        this.Source = from;
        this.Target = to;
        this.Weight = weight;
        this.label = label;
    }

    public Node getSourceNode() {
        return this.Source;
    }

    public Node getTargetNode() {
        return this.Target;
    }

    public float getWeight() {
        return this.Weight;
    }

    public void setSourceNode(Node s) {
        this.Source = s;
    }

    public void setTargetNode(Node t) {
        this.Target = t;
    }

    public void setWeight(float w) {
        this.Weight = w;
    }

    public int getLabel() {
        return this.label;
    }

    public void setLabel(int l) {
        this.label = l;
    }

    public boolean isVisited() {
        return this.visited;
    }

    public void setVisited(boolean b) {
        this.visited = b;
    }
}

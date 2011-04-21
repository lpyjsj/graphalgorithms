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
    private Vertex Source, Target;
    private String label;

    public Edge(Vertex from, Vertex to, float weight, String label) throws NoSuchFieldException {
        this.Source = from;
        this.Target = to;
        this.Weight = weight;
        this.label = label;
    }

    public Vertex getSourceVertex() {
        return this.Source;
    }

    public Vertex getTargetVertex() {
        return this.Target;
    }

    public float getWeight() {
        return this.Weight;
    }

    public void setSourceVertex(Vertex s) {
        this.Source = s;
    }

    public void setTargetVertex(Vertex t) {
        this.Target = t;
    }

    public void setWeight(float w) {
        this.Weight = w;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String l) {
        this.label = l;
    }
}

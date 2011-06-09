package Graphs;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Christian Hildebrandt (210238835), Matthias Bady (210235131)
 */
public class Edge {

    private int label;
    private float weight;
    private int source, target;

    public Edge(int from, int to)
    {
        this(from, to, 1.f, -1);
    }
    
    public Edge(int from, int to, float weight, int label)
    {
        this.source = from;
        this.target = to;
        this.weight = weight;
        this.label = label;
    }

    public int getSourceNode()
    {
        return this.source;
    }

    public int getTargetNode()
    {
        return this.target;
    }

    public float getWeight()
    {
        return this.weight;
    }

    public void setSourceNode(int s)
    {
        this.source = s;
    }

    public void setTargetNode(int t)
    {
        this.target = t;
    }

    public void setWeight(float w)
    {
        this.weight = w;
    }

    public int getLabel()
    {
        return this.label;
    }

    public void setLabel(int l)
    {
        this.label = l;
    }

    @Override
    public String toString()
    {
        return "e " + source + ' ' + target;
    }
}

package Graphs;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Christian Hildebrandt (210238835), Matthias Bady (210235131)
 */
public class SimpleGraph extends Graph {

    public SimpleGraph() {
        super();
    }

    @Override
    void addNode(Node v) {
        this.Nodes.add(v);
    }

    @Override
    void addEdge(Edge e) {
        this.Nodes.get(this.Nodes.indexOf(e.getSourceNode())).addAdjecentNodeIn(e.getTargetNode());
        this.Nodes.get(this.Nodes.indexOf(e.getTargetNode())).addAdjecentNodeIn(e.getSourceNode());

    }

    @Override
    void removeNode(Node v) {
        this.Nodes.remove(v);
        //Remove this Node too in all adjacentlists
        for (Node vertex : this.Nodes) {
            vertex.removeAdjecentNodeIn(v);
        }
    }

    @Override
    void removeEdge(Edge e) {
        this.Nodes.get(this.Nodes.indexOf(e.getSourceNode())).removeAdjecentNodeIn(e.getTargetNode());
        this.Nodes.get(this.Nodes.indexOf(e.getTargetNode())).removeAdjecentNodeIn(e.getSourceNode());
    }
}

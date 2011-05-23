package Graphs;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author siggiaze
 */
public class DirectedGraph extends Graph {

    public DirectedGraph() {
        super();
    }
    
    @Override
    void addNode(Node v) {
        this.Nodes.add(v);
    }

    @Override
    void addEdge(Edge e) {
        this.Edges.add(e);
        this.Nodes.get(this.Nodes.indexOf(e.getSourceNode())).addAdjecentNodeIn(e.getTargetNode());
    }

    @Override
    void removeNode(Node v) {
        this.Nodes.remove(v);
        for(Node vertex : this.Nodes){
            vertex.removeAdjecentNodeIn(v);
        }
    }

    @Override
    void removeEdge(Edge e) {
        this.Nodes.get(this.Nodes.indexOf(e.getSourceNode())).removeAdjecentNodeIn(e.getTargetNode());
    }
}

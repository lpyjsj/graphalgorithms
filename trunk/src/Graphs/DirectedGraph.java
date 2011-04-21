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

    public DirectedGraph(int n, int m) {
        super(n, m);
    }

    @Override
    void addVertex(Vertex v) {
        this.Vertices.add(v);
    }

    @Override
    void addEdge(Edge e) {
        this.Edges.add(e);
        this.Vertices.get(this.Vertices.indexOf(e.getSourceVertex())).addAdjecentVertex(e.getTargetVertex());
    }

    @Override
    void removeVertex(Vertex v) {
        this.Vertices.remove(v);
        for(Vertex vertex : this.Vertices){
            vertex.removeAdjecentVertex(v);
        }
    }

    @Override
    void removeEdge(Edge e) {
        this.Vertices.get(this.Vertices.indexOf(e.getSourceVertex())).removeAdjecentVertex(e.getTargetVertex());
    }

    @Override
    void generateAdjacencyListFromEdges() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

package Graphs;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author siggiaze
 */
public class SimpleGraph extends Graph {

    public SimpleGraph(int n, int m) {
        super(n, m);
    }

    @Override
    void addVertex(Vertex v) {
        this.Vertices.add(v);
    }

    @Override
    void addEdge(Edge e) {
        this.Vertices.get(this.Vertices.indexOf(e.getSourceVertex())).addAdjecentVertex(e.getTargetVertex());
        this.Vertices.get(this.Vertices.indexOf(e.getTargetVertex())).addAdjecentVertex(e.getSourceVertex());

    }

    @Override
    void removeVertex(Vertex v) {
        this.Vertices.remove(v);
        //Remove this Vertex too in all adjacentlists
        for(Vertex vertex : this.Vertices){
            vertex.removeAdjecentVertex(v);
        }
    }

    @Override
    void removeEdge(Edge e) {
        this.Vertices.get(this.Vertices.indexOf(e.getSourceVertex())).removeAdjecentVertex(e.getTargetVertex());
        this.Vertices.get(this.Vertices.indexOf(e.getTargetVertex())).removeAdjecentVertex(e.getSourceVertex());
    }

    @Override
    void generateAdjacencyListFromEdges() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

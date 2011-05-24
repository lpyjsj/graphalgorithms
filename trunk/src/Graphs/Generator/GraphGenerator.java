/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphs.Generator;

import Graphs.DirectedGraph;
import Graphs.Edge;
import Graphs.Graph;
import Graphs.Node;
import java.util.Random;

/**
 *
 * @author Christian Hildebrandt (210238835), Matthias Bady (210235131)
 */
public class GraphGenerator {

    private int nodes, edges;

    public GraphGenerator() {
    }

    public Graph generateNewRandomGraph(int nodes, int edges) {
        Graph G = new DirectedGraph();
        for (int i = 0; i < nodes; i++) {
            G.getNodes().add(new Node(i));
        }
        //generating random edges
        Random generator = new Random();
        for (int j = 0; j < edges; j++) {
            Node From = G.getNodes().elementAt(generator.nextInt(nodes - 1));
            Node To = G.getNodes().elementAt(generator.nextInt(nodes - 1));
            G.getEdges().add(new Edge(From, To, 0.f, j));
        }
        G.generateAdjacencyListFromEdges();
        return G;
    }

    /**Generates a new Graph with backwards edges to make sure
     * this Graph contains a euler circle
     *
     * @param nodes - amount of nodes which the graph contains
     * @return - returns the graph
     */
    public Graph generateNewEulerGraph(int nodes, int edges) {
        if (edges < 2 * (nodes - 1) || edges % 2 != 0) {
            System.err.println("Edges must be > or = 2*(Nodes-1) and odd!");
        }
        Graph G = new DirectedGraph();
        Node current = null;
        Node last = null;
        for (int i = 0; i < nodes; i++) {
            if (i > 0) {
                //generating nodes
                last = current;
                current = new Node(i);
                //generating edges
                G.getEdges().add(new Edge(last, current, 0.f, i));
                G.getEdges().add(new Edge(current, last, 0.f, 2 * i));
            } else {
                //generating first node
                current = new Node(i);
            }
            G.getNodes().add(current);
        }
        //generating some extra edges
        Random generator = new Random();
        for (int i = 0; i < (edges - 2 * (nodes - 1)) / 2; i++) {
            Node From = G.getNodes().elementAt(generator.nextInt(nodes - 1));
            Node To = G.getNodes().elementAt(generator.nextInt(nodes - 1));
            G.getEdges().add(new Edge(From, To, 0.f, i));
            G.getEdges().add(new Edge(To, From, 0.f, 2 * i));
        }
        //now generating adjaceny lists of each node from all edges
        G.generateAdjacencyListFromEdges();
        return G;
    }
}

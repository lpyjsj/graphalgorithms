/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphs.Generator;

import Graphs.Edge;
import Graphs.Graph;
import Graphs.Node;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author Christian Hildebrandt (210238835), Matthias Bady (210235131)
 */
public class GraphGenerator {

    public static Graph generateNewRandomGraph(int numNodes, int numEdges, int mode) {
        Graph graph = new Graph(numNodes);
        for (int i = 0; i < numNodes; i++) {
            graph.nodes.add(new Node(i));
        }
        //generating random edges
        Random generator = new Random();
        Collection<Edge> edges = new LinkedList<Edge>();
        for (int j = 0; j < numEdges; j++) {
            edges.add(new Edge(generator.nextInt(numNodes - 1), generator.nextInt(numNodes - 1), 0.f, j));
        }
        graph.setEdges(edges, mode);
        return graph;
    }

    /**Generates a new Graph with backwards edges to make sure
     * this Graph contains a euler circle
     *
     * @param numNodes - amount of nodes which the graph contains
     * @return - returns the graph
     */
    public static Graph generateNewEulerGraph(int numNodes, int mode) {
        Collection<Edge> edges = new LinkedList<Edge>();
        Graph graph = new Graph(numNodes);
        int current = 0;
        int last = 0;
        for (int i = 0; i < numNodes; i++) {
            if (i > 0) {
                //generating nodes
                last = current;
                current = i;
                //generating edges
                edges.add(new Edge(last, current, 0.f, i));
                edges.add(new Edge(current, last, 0.f, 2 * i));
            } else {
                //generating first node
                current = i;
            }
            graph.nodes.add(new Node(current));
        }
        
        graph.setEdges(edges, mode);
        return graph;
    }
}

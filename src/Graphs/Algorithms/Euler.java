/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphs.Algorithms;

import Graphs.DirectedGraph;
import Graphs.Graph;
import Graphs.Node;
import java.io.File;
import java.util.Vector;

/**
 *
 * @author siggiaze
 */
public class Euler {

    public static void main(String[] args) {

        if (args.length < 2) {
            System.err.println("Too few arguments!");
            System.err.println("Usage: java -jar Graph.jar <infile> <outfile>");
            System.err.println("Closing...");
            System.exit(1);
        }
        File infile = new File(args[0]);
        File outfile = new File(args[1]);
        //GraphGenerator GG = new GraphGenerator();
        //Graph G = GG.generateNewEulerGraph(200, 800);
        Graph G = new DirectedGraph();
        G.readFromFile(infile);
        System.out.println("EulerKreis in Graph enthalten: " + isEulerCircle(G));
        G.writeToFile(outfile);
    }

    private static boolean isEulerCircle(Graph G) {

        Vector<Node> toVisit = new Vector<Node>();
        Vector<Node> Visited = new Vector<Node>();
        boolean thereIsAEulerCircle = true;
        //maybe random!!!
        toVisit.add(G.getNodes().firstElement());
        while (!toVisit.isEmpty() & thereIsAEulerCircle) {
            Node nextNode = toVisit.firstElement();
            while (toVisit.size() > 1 & nextNode.isVisited()) {
                toVisit.remove(toVisit.firstElement());
                nextNode = toVisit.firstElement();
            }
            Node current = toVisit.firstElement();
            if (current.getDegree() % 2 != 0) {
                thereIsAEulerCircle = false;
            } else {
                //add all not visited nodes to toVisit
                for (Node n : current.getAdjacencyListOut()) {
                    if (!n.isVisited()) {
                        toVisit.add(n);
                    }
                }
                toVisit.remove(current);
                current.setVisited(true);
                Visited.add(current);
            }
        }
        //Remove duplicates
        for (Node n : Visited) {
        }

        if (Visited.size() != G.getNodes().size() + 1) {
            thereIsAEulerCircle = false;
        }
        return thereIsAEulerCircle;
    }
}

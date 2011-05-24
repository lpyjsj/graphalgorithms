/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphs.Algorithms;

import Graphs.DirectedGraph;
import Graphs.Graph;
import Graphs.Node;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

/**
 *
 * @author Christian Hildebrandt (210238835), Matthias Bady (210235131)
 */
public class Euler {

    private static File infile, outfile;

    public static void main(String[] args) {

        if (args.length < 2) {
            System.err.println("Too few arguments!");
            System.err.println("Usage: java -jar Graph.jar <infile> <outfile>");
            System.err.println("Closing...");
            System.exit(1);
        }
        infile = new File(args[0]);
        outfile = new File(args[1]);
        //GraphGenerator GG = new GraphGenerator();
        //Graph G = GG.generateNewEulerGraph(200, 800);
        Graph G = new DirectedGraph();
        G.readFromFile(infile);
        System.out.println("Euler-Kreis in Graph enthalten: " + isEulerCircle(G));
        System.out.println("Weg in Datei " + outfile.getName() + " enthalten.");
    }

    private static boolean isEulerCircle(Graph G) {

        Vector<Node> toVisit = new Vector<Node>();
        Vector<Node> Visited = new Vector<Node>();
        boolean thereIsAEulerCircle = false, firstStartRun = true;
        // initial node add in to visit list
        toVisit.add(G.getNodes().firstElement());
        int nodeIndex = 0;
        // initial start node
        Node start = toVisit.firstElement(), current = null;
        while (!toVisit.isEmpty()) {

            // If the start node has no in edges there is no way
            // to come back to this node so choose another node of G
            if (start.getAdjacencyListIn().isEmpty()) {
                // replace old start with new start node & clear to visit & visited list
                start = G.getNodes().get(nodeIndex);
                toVisit.clear();
                Visited.clear();
                Visited.add(start);
                firstStartRun = true;
                // check whether all nodes hav been visited
                if (nodeIndex < G.getNodes().size()) {
                    nodeIndex += 1;
                } else {
                    thereIsAEulerCircle = false;
                    break;
                }
            }

            if (firstStartRun) {
                // add all out adjecent nodes of start to visit list
                for (Node n : start.getAdjacencyListOut()) {
                    toVisit.add(n);
                }
                toVisit.remove(start);
                Visited.add(start);
                firstStartRun = false;
                // else add out nodes of current node to visit list
            } else {
                current = toVisit.firstElement();
                toVisit.remove(current);
                Visited.add(current);
                for (Node n : current.getAdjacencyListOut()) {
                    toVisit.add(n);
                }
                // check whether we run around up to start node
                if (current.equals(start)) {
                    thereIsAEulerCircle = true;
                    break;
                }
            }
        }
        WritePathToFile(outfile, Visited);
        return thereIsAEulerCircle;
    }

    private static void WritePathToFile(File f, Vector<Node> path) {
        // delete old paths
        if (f.exists()) {
            f.delete();
        }
        // write the found path to specified file
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            for (Node n : path) {
                bw.write("Node: " + n.getLabel() + System.getProperty("line.separator"));
            }
            bw.close();
        } catch (IOException ioe) {
            System.err.println("Cannot write to file: " + f.getName());

        }

    }
}

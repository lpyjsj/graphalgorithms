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
        if (isEulerCircle(G)) {
            System.out.println("Euler-Kreis in Graph enthalten.");
            System.out.println("Weg in Datei " + outfile.getName() + " geschrieben.");
        } else {
            System.out.println("Kein Euler-Kreis in Graph enthalten.");
        }
    }

    private static boolean isEulerCircle(Graph G) {

        Vector<Node> toVisit = new Vector<Node>();
        Vector<Node> Visited = new Vector<Node>();
        boolean thereIsAEulerCircle = false, firstStartRun = true;
        // initial node add in to visit list
        toVisit.add(G.getNodes().firstElement());
        int nodeIndex = 0;
        // initial start node
        Node start = null, current = null;
        boolean run = true;
        while (run) {

            // If the start node has no in edges there is no way
            // to come back to this node so choose another node of G
            if (start == null || start.getAdjacencyListIn().isEmpty()) {
                // replace old start with new start node & clear to visit & visited list
                start = G.getNodes().get(nodeIndex);
                toVisit.clear();
                toVisit.add(start);
                Visited.clear();
                firstStartRun = true;
                // check whether all nodes hav been visited
                if (nodeIndex < G.getNodes().size()) {
                    nodeIndex += 1;
                } else {
                    thereIsAEulerCircle = false;
                    break;
                }
            }

            current = toVisit.firstElement();

            // add all out adjecent nodes of start to visit list
            for (Node n : current.getAdjacencyListOut()) {
                toVisit.add(n);
            }
            // write lists
            toVisit.remove(current);
            Visited.add(current);



            // check whether we run around up to start node
            if (!firstStartRun & current.equals(start)) {
                thereIsAEulerCircle = true;
                break;
            } else {
                firstStartRun = false;
            }

            // check whether we visited one node twice -
            // so we have entered an euler circle with another node than start
            int twice = 0;
            for (Node n : Visited) {
                if (current.equals(n)) {
                    twice += 1;
                }
                if (twice > 1) {
                    start = null;
                }
            }

            // check whether we have still unvisited nodes in G
            // but no more nodes in to visit list
            if (toVisit.isEmpty()) {
                if (nodeIndex < (G.getNodes().size() - 1)) {
                    start = null;
                } else {
                    run = false;
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
        } // write the found path to specified file
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

package Graphs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Christian Hildebrandt (210238835), Matthias Bady (210235131)
 */
public abstract class Graph {

    protected Vector<Node> Nodes;
    protected Vector<Edge> Edges;

    public Graph() {
        this.Nodes = new Vector<Node>();
        this.Edges = new Vector<Edge>();
    }

    //abstrakte Methoden werden für unterschiedliche Graphen unterschiedlich impementiert!!!
    abstract void addNode(Node v);

    abstract void addEdge(Edge e);

    abstract void removeNode(Node v);

    abstract void removeEdge(Edge e);

    public void generateAdjacencyListFromEdges() {
        for (Edge e : this.Edges) {
            e.getSourceNode().addAdjecentNodeOut(e.getTargetNode());
            e.getTargetNode().addAdjecentNodeIn(e.getSourceNode());

        }
    }

    public void writeToFile(File f) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            bw.write("n " + this.Nodes.size() + " m " + this.Edges.size());
            bw.newLine();
            for (Edge e : this.Edges) {
                bw.write("e " + e.getSourceNode().getLabel() + " " + e.getTargetNode().getLabel());
                bw.newLine();
            }
            bw.close();
        } catch (IOException ioe) {
            System.err.println("Could not write ro File: " + f.getName());
            System.out.println(ioe.getLocalizedMessage());
        }

    }

    /* wird möglicherweise benötigt??!!??
    abstract int getGraphType();
     */
    public Vector<Node> getNodes() {
        return this.Nodes;
    }

    public Vector<Edge> getEdges() {
        return this.Edges;
    }

    public void setNodes(Vector<Node> v) {
        this.Nodes = v;
    }

    public void setEdges(Vector<Edge> e) {
        this.Edges = e;
    }

    public void readFromFile(File graphFile) {

        try {

            BufferedReader br = new BufferedReader(new FileReader(graphFile));
            String line = null;
            StringTokenizer st = null;

            //reading first line
            line = br.readLine();
            st = new StringTokenizer(line);

            //throwing away char n
            st.nextToken();

            //getting amount of vertices
            Nodes.ensureCapacity(Integer.parseInt(st.nextToken()));

            //throwing away char m
            st.nextToken();

            //getting amount of edges
            Edges.ensureCapacity(Integer.parseInt(st.nextToken()));

            int edgecounter = 0;
            //reading rest (assuming nodes are seperated by space delimter)
            while ((line = br.readLine()) != null) {
                st = new StringTokenizer(line);
                //throwing away char e
                st.nextToken();

                //getting node A
                Node A = new Node(Integer.parseInt(st.nextToken()));
                // prevent inserting different node objects with same label
                if (!this.Nodes.isEmpty()) {
                    for (Node n : this.Nodes) {
                        if (n.equals(A)) {
                            A = n;
                            break;
                        } else if (n.equals(this.Nodes.lastElement())) {
                            this.Nodes.add(A);
                            break;
                        }
                    }
                } else {
                    this.Nodes.add(A);
                }

                //getting node B
                Node B = new Node(Integer.parseInt(st.nextToken()));
                // prevent inserting different node objects with same label
                if (!this.Nodes.isEmpty()) {
                    for (Node n : this.Nodes) {
                        if (n.equals(B)) {
                            B = n;
                            break;
                        } else if (n.equals(this.Nodes.lastElement())) {
                            this.Nodes.add(B);
                            break;
                        }
                    }
                } else {
                    this.Nodes.add(B);
                }


                //generating edges
                Edge E;
                this.Edges.add((E = new Edge(A, B, 0, edgecounter)));
                edgecounter += 1;
            }
            br.close();
        } catch (IOException ioe) {
            System.err.println("ERROR: Cannot read File (" + ioe.getLocalizedMessage() + ")");
            System.exit(1);
        }
        //now generate adj.-lists for each node from readed edges
        generateAdjacencyListFromEdges();

    }

    public void printGraph() {
        //TODO print Graph as PNG or somewhat
    }
}

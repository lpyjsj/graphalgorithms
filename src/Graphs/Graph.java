package Graphs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.StringTokenizer;
import java.util.Vector;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Christian Hildebrandt
 */
public abstract class Graph {

    protected Vector<Vertex> Vertices;
    protected Vector<Edge> Edges;
    protected int verticesAmount, edgesAmount;

    public Graph(int vertices, int edges) {

        this.verticesAmount = vertices;
        this.edgesAmount = edges;
        this.Vertices = new Vector<Vertex>();
        this.Edges = new Vector<Edge>();
    }

    //abstrakte Methoden werden für unterschiedliche Graphen unterschiedlich impementiert!!!
    abstract void addVertex(Vertex v);

    abstract void addEdge(Edge e);

    abstract void removeVertex(Vertex v);

    abstract void removeEdge(Edge e);

    abstract void generateAdjacencyListFromEdges();

    /* wird möglicherweise benötigt??!!??
    abstract int getGraphType();
     */
    public Vector<Vertex> getVertices() {
        return this.Vertices;
    }

    public Vector<Edge> getEdges() {
        return this.Edges;
    }

    public void setVertices(Vector<Vertex> v) {
        this.Vertices = v;
    }

    public void setEdges(Vector<Edge> e) {
        this.Edges = e;
    }

    protected void generateFromFile(String PathToFile) {
        File graphFile = new File(PathToFile);
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
            this.verticesAmount = Integer.parseInt(st.nextToken());

            //throwing away char m
            st.nextToken();

            //getting amount of edges
            this.edgesAmount = Integer.parseInt(st.nextToken());

            //reading rest (assuming nodes are seperated by space delimter)
            while ((line = br.readLine()) != null) {
                st = new StringTokenizer(line);
                //throwing away char e
                st.nextToken();

                //getting node A
                Vertex A;
                this.Vertices.add((A = new Vertex(st.nextToken())));

                //getting node B
                Vertex B;
                this.Vertices.add((B = new Vertex(st.nextToken())));

                //generating edges
                Edge E;
                this.Edges.add((E = new Edge(A, B, 0, "An Edge")));
            }
            br.close();
        } catch (Exception e) {
            System.err.println("ERROR: Cannot read File (" + e.getLocalizedMessage() + ")");
        }
        //now generate adj.-lists for each node from readed edges
        generateAdjacencyListFromEdges();

    }

    public void printGraph() {
        //TODO print Graph as PNG or somewhat
    }
}

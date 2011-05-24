package Graphs;

import java.util.Vector;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Christian Hildebrandt (210238835), Matthias Bady (210235131)
 */
public class Node {

    private Vector<Node> adjacencyListIn, adjacencyListOut;
    private int label, inDegree = 0, outDegree = 0, Degree = 0;
    private boolean visited = false;

    public Node(int label) {
        this.adjacencyListIn = new Vector<Node>();
        this.adjacencyListOut = new Vector<Node>();
        this.label = label;
    }

    public void addAdjecentNodeIn(Node v) {
        this.adjacencyListIn.add(v);
        this.inDegree = this.adjacencyListIn.size();
        this.Degree = this.adjacencyListIn.size() + this.adjacencyListOut.size();
    }

    public void addAdjecentNodeOut(Node v) {
        this.adjacencyListOut.add(v);
        this.outDegree = this.adjacencyListIn.size();
        this.Degree = this.adjacencyListIn.size() + this.adjacencyListOut.size();
    }

    public void removeAdjecentNodeIn(Node v) {
        this.adjacencyListIn.remove(v);
        this.inDegree = this.adjacencyListIn.size();
        this.Degree = this.adjacencyListIn.size() + this.adjacencyListOut.size();
    }

    public void removeAdjecentNodeOut(Node v) {
        this.adjacencyListOut.remove(v);
        this.outDegree = this.adjacencyListIn.size();
        this.Degree = this.adjacencyListIn.size() + this.adjacencyListOut.size();
    }

    public Vector<Node> getAdjacencyListIn() {
        return this.adjacencyListIn;
    }

    public Vector<Node> getAdjacencyListOut() {
        return this.adjacencyListOut;
    }

    public void setAdjacencyListIn(Vector<Node> v) {
        this.adjacencyListIn = v;
    }

    public void setAdjacencyListIOut(Vector<Node> v) {
        this.adjacencyListOut = v;
    }

    public int getLabel() {
        return this.label;
    }

    public void setLabel(int l) {
        this.label = l;
    }

    public boolean isVisited() {
        return this.visited;
    }

    public void setVisited(boolean b) {
        this.visited = b;
    }

    public int getInDegree() {
        return this.inDegree;
    }

    public int getOutDegree() {
        return this.outDegree;
    }

    public int getDegree() {
        return this.Degree;
    }

    public boolean equals(Node n) {
        if (this.label == n.getLabel()) {
            return true;
        } else {
            return false;
        }
    }
}

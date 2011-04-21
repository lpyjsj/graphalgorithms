package Graphs;


import java.util.Vector;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author siggiaze
 */
public class Vertex {

    private Vector<Vertex> adjacencyList;
    private String label;

    public Vertex(String label){
        this.adjacencyList = new Vector<Vertex>();
    }

    public void addAdjecentVertex(Vertex v){
        this.adjacencyList.add(v);
    }

    public void removeAdjecentVertex(Vertex v){
        this.adjacencyList.remove(v);
    }

    public Vector<Vertex> getAdjacencyList(){
        return this.adjacencyList;
    }

    public void setAdjacencyList(Vector<Vertex> v){
        this.adjacencyList = v;
    }

    public String getLabel(){
        return this.label;
    }

    public void setLabel(String l){
        this.label =l;
    }

}

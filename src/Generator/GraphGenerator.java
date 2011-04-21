/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Generator;

import Graphs.DirectedGraph;
import Graphs.Graph;

/**
 *
 * @author siggiaze
 */
public class GraphGenerator {

    public GraphGenerator() {
        //I've no idea how to build a graphgenerator
    }

    public Graph generateNewDirectedGraph(int nodes, int edges) {
        Graph G = new DirectedGraph(nodes, edges);
        return G;
    }
}

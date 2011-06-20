package Graphs.Algorithms;

import Graphs.Graph;
import Graphs.MyPriorityQueue;
import Graphs.Node;
import java.util.Comparator;

/**
 *
 * @author ad-530
 */
public class DefaultDijkstra extends AbstractDijkstra {


    public DefaultDijkstra(Graph g, Integer s, Integer t)
    {
        this(g, s, t, ComputeMode.UNIDIRECTIONAL);
    }

    public DefaultDijkstra(Graph g, Integer s, Integer t, ComputeMode m)
    {
        super(g, s, t, m);
    }

    @Override
    float nodeCost(Node n1, Node n2)
    {
        return Math.abs(n2.getX() - n1.getX()) + Math.abs(n2.getY() - n1.getY());
    }

    @Override
    MyPriorityQueue<Node> getNewQueue()
    {
        return new MyPriorityQueue<Node>(graph.nodes.size(), new Comparator() {

            @Override
            public int compare(Object o1, Object o2)
            {
                Node n1 = (Node) o1;
                Node n2 = (Node) o2;
                return Float.compare(n1.getCost(), n2.getCost());
            }
        }) {

            @Override
            public void decreaseKey(Node elm)
            {
                remove(elm);
                add(elm);
            }
        };
    }
}

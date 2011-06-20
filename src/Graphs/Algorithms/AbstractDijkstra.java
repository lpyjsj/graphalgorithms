package Graphs.Algorithms;

import Graphs.Graph;
import Graphs.MyPriorityQueue;
import Graphs.Node;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public abstract class AbstractDijkstra {

    public enum ComputeMode {

        UNIDIRECTIONAL,
        BIDIRECTIONAL
    };
    public LinkedList<Integer> path = new LinkedList<Integer>();
    /***/
    protected Graph graph;
    protected Integer start, target;
    protected ComputeMode mode;
    /***/
    private HashMap<Integer, Integer> frontPredecessor, backPredecessor;
    private boolean[] frontVisited, backVisited;
    private MyPriorityQueue<Node> frontQueue, backQueue;

    protected AbstractDijkstra()
    {
    }

    public AbstractDijkstra(Graph g, Integer s, Integer t)
    {
        this(g, s, t, ComputeMode.UNIDIRECTIONAL);
    }

    public AbstractDijkstra(Graph g, Integer s, Integer t, ComputeMode m)
    {
        graph = g;
        start = s;
        target = t;
        mode = m;
    }

    abstract MyPriorityQueue<Node> getNewQueue();

    abstract float nodeCost(Node n1, Node n2);

    protected void init()
    {
        path.clear();

        switch (mode) {
            case UNIDIRECTIONAL: {
                frontQueue = getNewQueue();
                backQueue = null;
                frontPredecessor = new HashMap<Integer, Integer>();
                backPredecessor = null;
                frontVisited = new boolean[graph.nodes.size()];
                backVisited = null;
                Arrays.fill(frontVisited, false);
            }
            break;

            case BIDIRECTIONAL: {
                frontQueue = getNewQueue();
                backQueue = getNewQueue();
                frontPredecessor = new HashMap<Integer, Integer>();
                backPredecessor = new HashMap<Integer, Integer>();
                frontVisited = new boolean[graph.nodes.size()];
                backVisited = new boolean[graph.nodes.size()];
                Arrays.fill(frontVisited, false);
                Arrays.fill(backVisited, false);
            }
            break;
        }

        for (Node n : graph.nodes.values()) {
            n.setCost(Float.POSITIVE_INFINITY);
        }
        System.out.println("Dijkstra initialized");
    }

    public void compute()
    {
        init();
        dijkstra();
    }

    protected void dijkstra()
    {

        graph.nodes.get(start).setCost(0.f);
        frontQueue.add(graph.nodes.get(start));
        if (mode == ComputeMode.BIDIRECTIONAL) {
            graph.nodes.get(target).setCost(0.f);
            backQueue.add(graph.nodes.get(target));
        }

        float my = Float.MAX_VALUE;
        LinkedList<Integer> shortestPath = new LinkedList<Integer>();

        int directionIdx = -1;
        int[] adjArray, adjIndices;
        MyPriorityQueue<Node> queue = null;

        while (!frontQueue.isEmpty() || (backQueue != null && !backQueue.isEmpty())) {

            if (frontQueue.isEmpty() || (backQueue != null && !backQueue.isEmpty() && (backQueue.peek().getCost() < frontQueue.peek().getCost()))) {
                queue = backQueue;
                directionIdx = 1;
                adjArray = graph.inAdjArray;
                adjIndices = graph.inAdjIndices;
            } else {
                queue = frontQueue;
                directionIdx = 0;
                adjArray = graph.outAdjArray;
                adjIndices = graph.outAdjIndices;
            }

            Node n = queue.poll();
            if (directionIdx == 0) {
                frontVisited[n.getLabel()] = true;
            } else if (directionIdx == 1) {
                backVisited[n.getLabel()] = true;
            }

            if(backVisited != null && frontVisited[n.getLabel()] && backVisited[n.getLabel()]) {
                path = shortestPath;
                System.out.println("Path cost: " + my);
                return;
            }

            if (mode == ComputeMode.UNIDIRECTIONAL) {
                if (target.equals(n.getLabel())) {
                    break;
                }
            }

            int idx = adjIndices[n.getLabel()];
            if (idx == -1) {
                continue;
            }
            int next = -1;
            if (n.getLabel() < adjIndices.length - 1) {
                next = adjIndices[n.getLabel() + 1];
            } else {
                next = adjArray.length;
            }

            for (int j = idx; j < next; j++) {

                Node other = graph.nodes.get(adjArray[j]);

                if (backVisited != null) {
                    if (n.getCost() + other.getCost() + nodeCost(n, other) < my) {
                        if (directionIdx == 0 && backVisited[other.getLabel()]) {
                            my = n.getCost() + other.getCost() + nodeCost(n, other);
                            shortestPath = concatPath(n.getLabel(), other.getLabel());
                            continue;
                        } else if (directionIdx == 1 && frontVisited[other.getLabel()]) {
                            my = n.getCost() + other.getCost() + nodeCost(n, other);
                            shortestPath = concatPath(other.getLabel(), n.getLabel());
                            continue;
                        }
                    }
                }
                float tmp = n.getCost() + nodeCost(n, other);
                if (tmp < other.getCost()) {
                    other.setCost(tmp);
                    if (Float.isInfinite(other.getCost())) {
                        queue.add(other);
                    } else {
                        queue.decreaseKey(other);
                    }
                    if (directionIdx == 0) {
                        frontPredecessor.put(other.getLabel(), n.getLabel());
                    } else if (directionIdx == 1) {
                        backPredecessor.put(other.getLabel(), n.getLabel());
                    }
                }
            }
        }
        createPath(target);
        System.out.println("Path cost: " + graph.nodes.get(target).getCost());
    }

    protected LinkedList<Integer> concatPath(Integer forward, Integer backward)
    {
        LinkedList<Integer> path = new LinkedList<Integer>();

        int i = forward;
        path.add(forward);
        while (i != start) {
            i = frontPredecessor.get(i);
            path.addFirst(i);
        }
        i = backward;
        path.add(backward);
        while (i != target) {
            i = backPredecessor.get(i);
            path.add(i);
        }
        return path;
    }

    protected void createPath(Integer node)
    {
        int i = node;
        path.add(node);
        while (i != start) {
            i = frontPredecessor.get(i);
            path.addFirst(i);
        }
    }
}

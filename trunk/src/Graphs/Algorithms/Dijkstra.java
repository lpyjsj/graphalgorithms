package Graphs.Algorithms;

import Graphs.Graph;
import Graphs.Node;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Dijkstra {

    public enum ComputeMode {

        UNIDIRECTIONAL,
        BIDIRECTIONAL
    };

    public enum Implementation {
        NONE,
        DEFAULT,
        DIAL,
        GOAL_DIRECTED
    };
    public LinkedList<Integer> path = new LinkedList<Integer>();
    /***/
    protected Graph graph;
    protected Integer start, target;
    protected ComputeMode mode;
    protected Implementation implementation;
    /***/
    private HashMap<Integer, Integer> frontPredecessor, backPredecessor;
    private boolean[] frontVisited, backVisited;
    private MyPriorityQueue<Node> frontQueue, backQueue;
    /***/
    private final static int FWD = 0;
    private final static int BWD = 1;

    protected Dijkstra()
    {
    }

    public Dijkstra(Graph g, Integer s, Integer t, Implementation i, ComputeMode m)
    {
        graph = g;
        start = s;
        target = t;
        implementation = i;
        mode = m;
    }

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

        int direction = -1;
        int[] adjArray, adjIndices;
        MyPriorityQueue<Node> queue = null;

        while (!frontQueue.isEmpty() || (backQueue != null && !backQueue.isEmpty())) {

            if (frontQueue.isEmpty() || (backQueue != null && !backQueue.isEmpty() && (backQueue.peek().getCost() < frontQueue.peek().getCost()))) {
                queue = backQueue;
                direction = BWD;
                adjArray = graph.inAdjArray;
                adjIndices = graph.inAdjIndices;
            } else {
                queue = frontQueue;
                direction = FWD;
                adjArray = graph.outAdjArray;
                adjIndices = graph.outAdjIndices;
            }

            Node n = queue.poll();
            if (direction == FWD) {
                frontVisited[n.getLabel()] = true;
            } else if (direction == BWD) {
                backVisited[n.getLabel()] = true;
            }

            if (backVisited != null && frontVisited[n.getLabel()] && backVisited[n.getLabel()]) {
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

                float tmp = 0.f;
                if (backVisited != null) {
                    tmp = n.getCost() + nodeCost(n, other) + other.getCost();
                    if (tmp < my) {
                        if (direction == FWD && backVisited[other.getLabel()]) {
                            my = tmp;
                            shortestPath = concatPath(n.getLabel(), other.getLabel());
                            continue;
                        } else if (direction == BWD && frontVisited[other.getLabel()]) {
                            my = tmp;
                            shortestPath = concatPath(other.getLabel(), n.getLabel());
                            continue;
                        }
                    }
                }
                if (implementation == Implementation.GOAL_DIRECTED) {
                    if (direction == FWD) {
                        tmp = nodeCost(other, graph.nodes.get(target));
                    } else if (direction == BWD) {
                        tmp = nodeCost(other, graph.nodes.get(start));
                    }
                } else {
                    tmp = n.getCost() + nodeCost(n, other);
                }
                if (tmp < other.getCost()) {
                    if (Float.isInfinite(other.getCost())) {
                        other.setCost(tmp);
                        queue.add(other);
                    } else {
                        other.setCost(tmp);
                        queue.decreaseKey(other);
                    }
                    if (direction == FWD) {
                        frontPredecessor.put(other.getLabel(), n.getLabel());
                    } else if (direction == BWD) {
                        backPredecessor.put(other.getLabel(), n.getLabel());
                    }
                }
            }
        }
        createPath(target);
        System.out.println("Path cost: " + graph.nodes.get(target).getCost());
    }

    MyPriorityQueue<Node> getNewQueue()
    {
        if (implementation == Implementation.DIAL) {
        } else {
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
        return null;
    }

    float nodeCost(Node n1, Node n2)
    {
        return Math.abs(n2.getX() - n1.getX()) + Math.abs(n2.getY() - n1.getY());
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

    abstract class MyPriorityQueue<T> extends PriorityQueue<T> {

        public MyPriorityQueue(int initialCapacity, Comparator<? super T> comparator)
        {
            super(initialCapacity, comparator);
        }

        public abstract void decreaseKey(T elm);
    }
}

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
    /** Speichert die Vorgänger der gescannten Knoten. Eine Map pro Suchrichtung (Vorwärts, Rückwärts). */
    private HashMap<Integer, Integer> frontPredecessor, backPredecessor;
    /** Die in der jeweiligen Suchrichtung (Vorwärts, Rückwärts) endgültig gelabelten Knoten. */
    private boolean[] frontVisited, backVisited;
    /** Die Queues für Vorwärts, Rückwärts Suche */
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

    /** Berechnet den kürzesten Pfad mit dem Dijkstra Algorithmus */
    public void compute()
    {
        init();
        dijkstra();
    }

    /** Implementierung des Dijkstra Algorithmus. Deckt Heap, Dial, Bidirirektional und zielgerichtet ab. */
    protected void dijkstra()
    {

        Node startNode = graph.nodes.get(start), targetNode = graph.nodes.get(target);
        startNode.setCost(0.f);
        frontQueue.add(startNode);
        if (mode == ComputeMode.BIDIRECTIONAL) {
            targetNode.setCost(0.f);
            backQueue.add(targetNode);
        }

        /** Länge des kürzesten gefunden Pfades (wird für bidirektional benötigt) */
        float my = Float.MAX_VALUE;
        /** Kürzester gefundener Pfad (wird für bidirektional benötigt) */
        LinkedList<Integer> shortestPath = new LinkedList<Integer>();

        int direction = -1;
        int[] adjArray, adjIndices;
        MyPriorityQueue<Node> queue = null;

        /** Richtungswahl für gleiche Expansionsgeschwindigkeit in beiden Richtungen */
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

            if (mode == ComputeMode.BIDIRECTIONAL) {
                if (implementation != Implementation.GOAL_DIRECTED) {
                    if (frontVisited[n.getLabel()] && backVisited[n.getLabel()]) {
                        /** Bidirektional wird abgebrochen sobald der erste Knoten aus beiden Queues entfernt wird. */
                        path = shortestPath;
                        return;
                    }
                } else {
                    /** Bei bidirektionaler und zielgerichteter Suche muss länger gesucht werden.
                    Abbruch kann erst erfolgen, wenn der erste Knoten gescannt wird, dessen Kosten größer, gleich sind als die Länge
                    des aktuell kürzesten Pfades*/
                    if (n.getCost() >= my) {
                        path = shortestPath;
                        return;
                    }
                }
            } else {
                /** Early out */
                if (targetNode == n) {
                    break;
                }
            }

            /**Endpunkt in der Adjazenzliste bestimmen. (Startpunkt ist durch adjIndices gegeben) */
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

            //Iterieren über die Nachfolger
            for (int j = idx; j < next; j++) {

                Node other = graph.nodes.get(adjArray[j]);

                float tmp = 0.f;
                if (mode == ComputeMode.BIDIRECTIONAL) {
                    if (implementation == Implementation.GOAL_DIRECTED) {
                        /** Bestimme aktuelle Pfadkosten */
                        if (direction == FWD) {
                            tmp = computePathCost(n.getLabel(), frontPredecessor) + computePathCost(other.getLabel(), backPredecessor);
                        } else if (direction == BWD) {
                            tmp = computePathCost(n.getLabel(), backPredecessor) + computePathCost(other.getLabel(), frontPredecessor);
                        }
                        tmp += nodeCost(n, other);
                    } else {
                        /** Klassische Kantengewichte */
                        tmp = n.getCost() + nodeCost(n, other) + other.getCost();
                    }
                    if (tmp < my) {
                        /** Falls ein neuer Pfad gefunden wurde: bestimme Länge und speichere falls kürzer als aktuell kürzester */
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

                /** Bestimmung neuer Kosten */
                if (implementation == Implementation.GOAL_DIRECTED) {
                    /** Modifizierte Kantengewichte für zielgerichtete Suche */
                    if (direction == FWD) {
                        tmp = n.getCost() + (nodeCost(n, other) + nodeCost(n, targetNode) - nodeCost(other, targetNode));
                    } else if (direction == BWD) {
                        tmp = n.getCost() + (nodeCost(n, other) + nodeCost(n, startNode) - nodeCost(other, startNode));
                    }
                } else {
                    /** Klassische Gewichte */
                    tmp = n.getCost() + nodeCost(n, other);
                }
                float oldCost = other.getCost();
                if (tmp < oldCost) {
                    /** Knoten neu labeln und Vorgänger setzen */
                    if (Float.isInfinite(oldCost)) {
                        other.setCost(tmp);
                        queue.add(other);
                    } else {
                        queue.decreaseKey(other, tmp);
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
    }

    interface MyPriorityQueue<T> {

        public void add(T elm);

        public T peek();

        public T poll();

        public boolean isEmpty();

        public void decreaseKey(T elm, float cost);
    }


    /** Gibt eine Implementierung einer PQ entsprechend der gewählten Implementierung zurück. */
    MyPriorityQueue<Node> getNewQueue()
    {
        /** Dials Implementierung der Queue */
        if (implementation == Implementation.DIAL) {

            return new MyPriorityQueue<Node>() {

                HashMap<Integer, LinkedList<Node>> pq = new HashMap<Integer, LinkedList<Node>>();

                @Override
                public void add(Node elm)
                {
                    Integer label = (int) elm.getCost();
                    LinkedList<Node> list = pq.get(label);
                    if (list == null) {
                        list = new LinkedList<Node>();
                        pq.put(label, list);
                    }
                    list.add(elm);
                }

                @Override
                public Node peek()
                {
                    if (isEmpty()) {
                        return null;
                    }
                    int sz = graph.nodes.size();
                    for (int i = 0; i < sz; i++) {
                        LinkedList<Node> list = pq.get(i);
                        if (list != null && !list.isEmpty()) {
                            return list.peekFirst();
                        }
                    }
                    return null;
                }

                @Override
                public Node poll()
                {
                    if (isEmpty()) {
                        return null;
                    }
                    int sz = graph.nodes.size();
                    for (int i = 0; i < sz; i++) {
                        LinkedList<Node> list = pq.get(i);
                        if (list != null && !list.isEmpty()) {
                            return list.pollFirst();
                        }
                    }
                    return null;
                }

                @Override
                public boolean isEmpty()
                {
                    return pq.isEmpty();
                }

                @Override
                public void decreaseKey(Node elm, float cost)
                {
                    LinkedList<Node> list = pq.get((int) elm.getCost());
                    try {
                        list.remove(elm);
                    } catch (NullPointerException npe) {
                    }
                    elm.setCost(cost);
                    add(elm);
                }
            };
        } else {
            /** Klassicher Heap. DecreaseKey: entfernen+hinzufügen */
            return new MyPriorityQueue<Node>() {

                PriorityQueue<Node> pq = new PriorityQueue<Node>(graph.nodes.size() / 2, new Comparator() {

                    @Override
                    public int compare(Object o1, Object o2)
                    {
                        Node n1 = (Node) o1;
                        Node n2 = (Node) o2;
                        return Float.compare(n1.getCost(), n2.getCost());
                    }
                });

                @Override
                public void decreaseKey(Node elm, float cost)
                {
                    pq.remove(elm);
                    elm.setCost(cost);
                    pq.add(elm);
                }

                @Override
                public void add(Node elm)
                {
                    pq.add(elm);
                }

                @Override
                public Node poll()
                {
                    return pq.poll();
                }

                @Override
                public boolean isEmpty()
                {
                    return pq.isEmpty();
                }

                @Override
                public Node peek()
                {
                    return pq.peek();
                }
            };
        }
    }

    /** Manhattan-Metrik */
    float nodeCost(Node n1, Node n2)
    {
        return Math.abs(n2.getX() - n1.getX()) + Math.abs(n2.getY() - n1.getY());
    }

    /** Konkateniere Teilpfade der beiden Suchrichtungen bei bidirektionaler Suche. */
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

    /** Erstelle Pfad anhand der Vorgänger bei unidirektionaler Suche */
    protected void createPath(Integer node)
    {
        int i = node;
        path.add(node);
        while (i != start) {
            i = frontPredecessor.get(i);
            path.addFirst(i);
        }
    }

    /** Summiere die einzelnen Knotenkosten eines Pfades */
    protected float computePathCost(Integer node, HashMap<Integer, Integer> predecessor)
    {
        float cost = 0.f;

        int i = node;
        while (predecessor.containsKey(i)) {
            int pre = predecessor.get(i);
            cost += nodeCost(graph.nodes.get(i), graph.nodes.get(pre));
            i = pre;
        }

        return cost;
    }
}

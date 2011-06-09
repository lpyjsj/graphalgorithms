package Graphs.Algorithms;

import Graphs.Graph;
import Graphs.Node;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class Dijkstra {

    private final static int MODE_H = 0x1;
    private final static int MODE_D = 0x2;
    private final static int MODE_B = 0x4;
    private final static int MODE_Z = 0x8;

    public static void main(String[] args)
    {
        if (args.length < 6) {
            System.err.println("Falsche Anzahl von Argumenten!");
            System.exit(-1);
        }

        String filename = args[0];
        int mode = 0, s = -1, t = -1;

        for (int i = 1; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-s")) {
                try {
                    s = Integer.parseInt(args[i + 1]);
                } catch (Exception e) {
                    System.err.println("Kein oder kein gültiges Argument für Option -s gefunden!");
                    System.exit(-1);
                }
            } else if (args[i].equalsIgnoreCase("-t")) {
                try {
                    t = Integer.parseInt(args[i + 1]);
                } catch (Exception e) {
                    System.err.println("Kein oder kein gültiges Argument für Option -t gefunden!");
                    System.exit(-1);
                }
            } else if (args[i].equalsIgnoreCase("-h")) {
                mode |= MODE_H;
            } else if (args[i].equalsIgnoreCase("-d")) {
                mode |= MODE_D;
            } else if (args[i].equalsIgnoreCase("-b")) {
                mode |= MODE_B;
            } else if (args[i].equalsIgnoreCase("-z")) {
                mode |= MODE_Z;
            }
        }
        if (mode == 0) {
            System.err.println("Kein Modus ausgewählt!");
            System.exit(-1);
        }
//        else if ((mode & (MODE_H | MODE_D)) == 1) {
//            System.err.println("Optionen -h und -d können nicht gemeinsam verwendet werden!");
//            System.exit(-1);
//        }

        long t1 = System.currentTimeMillis();
        Graph graph = Graph.readFromFile(new File(filename), Graph.MODE_ADJ);
        System.out.println("Graph loaded in: " + (System.currentTimeMillis() - t1) / 1000.0 + "s");
        System.out.println(graph.nodes.size() + " nodes");

        t1 = System.currentTimeMillis();
        List<Integer> path = dial(graph, s, t);
        System.out.println("Path computed in: " + (System.currentTimeMillis() - t1) / 1000.0 + "s");
        for (Integer i : path) {
            System.out.print(i + " <- ");
        }
    }

    protected static float manhattan(Node n1, Node n2)
    {
        return Math.abs(n2.getX() - n1.getX()) + Math.abs(n2.getY() - n1.getY());
    }

    protected static List<Integer> compute(Graph graph, Integer s, Integer t)
    {
        final int[] pre = new int[graph.nodes.size()];
        final boolean[] visited = new boolean[graph.nodes.size()];
        Arrays.fill(visited, false);

        for (Node n : graph.nodes.values()) {
            n.setCost(Float.POSITIVE_INFINITY);
        }
        graph.nodes.get(s).setCost(0.f);

        PriorityQueue<Node> queue = new PriorityQueue<Node>(graph.nodes.size(), new Comparator() {

            @Override
            public int compare(Object o1, Object o2)
            {
                Node n1 = (Node) o1;
                Node n2 = (Node) o2;
                return Float.compare(n1.getCost(), n2.getCost());
            }
        });
        queue.add(graph.nodes.get(s));

        System.out.println("compute initialized");

        while (!queue.isEmpty()) {
            Node n = queue.poll();
            visited[n.getLabel()] = true;
            if (t.equals(n.getLabel())) {
                break;
            }

            int idx = graph.adjIndices[n.getLabel()];
            if (idx == -1) {
                continue;
            }
            int next = -1;
            if (n.getLabel() < graph.adjIndices.length - 1) {
                next = graph.adjIndices[n.getLabel() + 1];
            } else {
                next = graph.adjArray.length;
            }

            for (int j = idx; j < next; j++) {
                Node other = graph.nodes.get(graph.adjArray[j]);
                //TODO visited setzen!!!
                if (!visited[other.getLabel()]) {
                    float tmp = n.getCost() + manhattan(n, other);
                    if (tmp < other.getCost()) {
                        other.setCost(tmp);
                        if (Float.isInfinite(other.getCost())) {
                            queue.add(other);
                        } else {
                            queue.remove(other);
                            queue.add(other);
                        }
                        pre[other.getLabel()] = n.getLabel();
                    }
                }
            }
        }

        List<Integer> path = new LinkedList<Integer>();
        path.add(t);
        int i = t;
        while (i != s) {
            i = pre[i];
            path.add(i);
        }

        return path;
    }

    //a good description: http://publish.uwo.ca/~jmalczew/gida_1/Zhan/Zhan.htm#5.%20The%20Dijkstra%27s%20Algorithm%20Implemented%20With
    //or this: http://books.google.de/books?id=DnP9vmaPwwEC&pg=PA120&lpg=PA120&dq=dial+algorithmus&source=bl&ots=e6wdHo-2b-&sig=31Mr5h-XGCpdDSh8YFgBILMLU30&hl=de&ei=tyLxTYWjH8Tu-gbA74iiAw&sa=X&oi=book_result&ct=result&resnum=6&ved=0CEEQ6AEwBQ#v=onepage&q=dial%20algorithmus&f=false
    //TODO testing!!!
    protected static List<Integer> dial(Graph graph, Integer s, Integer t)
    {
        final int[] pre = new int[graph.nodes.size()];

        for (Node n : graph.nodes.values()) {
            n.setCost(Float.POSITIVE_INFINITY);
        }

        HashMap<Integer, LinkedList<Node>> queue = new HashMap<Integer, LinkedList<Node>>();

        //initial putting s into queue
        Node start = graph.nodes.get(s);
        start.setCost(0.f);

        LinkedList<Node> first = new LinkedList<Node>();
        first.add(start);

        queue.put(0, first);

        boolean run = true;

        while (run) {
            for (int i : queue.keySet()) {
                while (!queue.get(i).isEmpty()) {
                    Node n = queue.get(i).poll();

                    if (t.equals(n.getLabel())) {
                        run = false;
                        break;
                    }

                    n.setCost(i);

                    int idx = graph.adjIndices[n.getLabel()];
                    if (idx == -1) {
                        continue;
                    }
                    int next = -1;
                    if (n.getLabel() < graph.adjIndices.length - 1) {
                        next = graph.adjIndices[n.getLabel() + 1];
                    } else {
                        next = graph.adjArray.length;
                    }

                    //check neighbors of n
                    for (int j = idx; j < next; j++) {
                        Node other = graph.nodes.get(graph.adjArray[j]);
                        other.setPred(n);

                        //if cost is infinite this node was not yet visited
                        if (Float.isInfinite(other.getCost())) {
                            int newBucketNumber = (int) (n.getCost() + manhattan(n, other));

                            LinkedList<Node> bucket;

                            //check whether this newBucketNumber exists in queue
                            if (queue.containsKey(newBucketNumber)) {
                                bucket = queue.get(newBucketNumber);
                            } else {
                                bucket = new LinkedList<Node>();
                            }

                            //check whether node just had a bucket
                            if (other.getBucketNumber() == -1) {

                                bucket.add(other);
                                queue.put(newBucketNumber, bucket);
                            } //check whether old bucket = new bucket ( if old = new - nothing to do)
                            else if (other.getBucketNumber() != newBucketNumber) {
                                queue.get(other.getBucketNumber()).remove(other);
                                bucket.add(other);
                                queue.put(newBucketNumber, bucket);
                            }
                        }
                    }
                }
                break;
            }
        }
        List<Integer> path = new LinkedList<Integer>();
        Node n = graph.nodes.get(t);
        while(!n.getPred().equals(graph.nodes.get(s))){
            path.add(n.getLabel());
        }
        path.add(s);
        return path;
    }
}

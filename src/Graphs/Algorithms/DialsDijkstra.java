package Graphs.Algorithms;

/**
 * a good description: http://publish.uwo.ca/~jmalczew/gida_1/Zhan/Zhan.htm#5.%20The%20Dijkstra%27s%20Algorithm%20Implemented%20With
 * or this: http://books.google.de/books?id=DnP9vmaPwwEC&pg=PA120&lpg=PA120&dq=dial+algorithmus&source=bl&ots=e6wdHo-2b-&sig=31Mr5h-XGCpdDSh8YFgBILMLU30&hl=de&ei=tyLxTYWjH8Tu-gbA74iiAw&sa=X&oi=book_result&ct=result&resnum=6&ved=0CEEQ6AEwBQ#v=onepage&q=dial%20algorithmus&f=false
 *
 * @author Christian Hildebrandt
 */
public class DialsDijkstra {

//
//
//    protected DialsDijkstra()
//    {
//        super();
//    }
//
//    public DialsDijkstra(Graph g, Integer s, Integer t)
//    {
//        super(g, s, t);
//    }
//
//    @Override
//    public void run()
//    {
//        init();
//
//        for (Node n : graph.nodes.values()) {
//            n.setCost(Float.POSITIVE_INFINITY);
//        }
//
//        HashMap<Integer, LinkedList<Node>> queue = new HashMap<Integer, LinkedList<Node>>();
//        LinkedList<Node> first = new LinkedList<Node>();
//
//        //initial putting start into queue
//        graph.nodes.get(start).setCost(0.f);
//        first.add(graph.nodes.get(start));
//
//        queue.put(0, first);
//
//        boolean run = true;
//
//        while (run) {
//            for (int i : queue.keySet()) {
//                while (!queue.get(i).isEmpty()) {
//                    Node n = queue.get(i).poll();
//
//                    if (target.equals(n.getLabel())) {
//                        run = false;
//                        break;
//                    }
//
//                    n.setCost(i);
//
//                    int idx = graph.adjIndices[n.getLabel()];
//                    if (idx == -1) {
//                        continue;
//                    }
//                    int next = -1;
//                    if (n.getLabel() < graph.adjIndices.length - 1) {
//                        next = graph.adjIndices[n.getLabel() + 1];
//                    } else {
//                        next = graph.adjArray.length;
//                    }
//
//                    //check neighbors of n
//                    for (int j = idx; j < next; j++) {
//                        Node other = graph.nodes.get(graph.adjArray[j]);
//                        other.setPred(n);
//
//                        //if cost is infinite this node was not yet visited
//                        if (Float.isInfinite(other.getCost())) {
//                            int newBucketNumber = (int) (n.getCost() + distance(n, other));
//
//                            LinkedList<Node> bucket;
//
//                            //check whether this newBucketNumber exists in queue
//                            if (queue.containsKey(newBucketNumber)) {
//                                bucket = queue.get(newBucketNumber);
//                            } else {
//                                bucket = new LinkedList<Node>();
//                            }
//
//                            //check whether node just had a bucket
//                            if (other.getBucketNumber() == -1) {
//
//                                bucket.add(other);
//                                queue.put(newBucketNumber, bucket);
//                            } //check whether old bucket = new bucket ( if old = new - nothing to do)
//                            else if (other.getBucketNumber() != newBucketNumber) {
//                                queue.get(other.getBucketNumber()).remove(other);
//                                bucket.add(other);
//                                queue.put(newBucketNumber, bucket);
//                            }
//                        }
//                    }
//                }
//                break;
//            }
//        }
//        Node n = graph.nodes.get(target);
//        while (!n.getPred().equals(graph.nodes.get(start))) {
//            path.add(n.getLabel());
//        }
//        path.add(start);
//    }
}

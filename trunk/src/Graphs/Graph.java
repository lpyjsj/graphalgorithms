package Graphs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Christian Hildebrandt (210238835), Matthias Bady (210235131)
 */
public class Graph {

    public static final int MODE_ADJ_OUT = 0x1;
    public static final int MODE_ADJ_IN = 0x2;
    public static final int MODE_EDG = 0x4;
    /***/
    public int mode;
    public int[] inAdjArray, outAdjArray;
    public int[] inAdjIndices, outAdjIndices;
    public Edge[] edgeList;
    public HashMap<Integer, Node> nodes;

    public Graph(int numNodes)
    {
        nodes = new HashMap<Integer, Node>(numNodes);
    }

    /** Legt die Kanten im Graph ab.
     *  Dabei sind verschiedene (miteinander kombinierbare) Speicherarten möglich:
     *  MODE_ADJ_OUT: Die Kanten werden in Form einer Adjazenzliste ausgehender Kanten gespeichert.
     *                Die Speicherung erfolgt wie im Artikel "Parallel graph component labelling with GPUs and CUDA" - Abbildung 5d beschrieben.
     *  MODE_ADJ_IN: Die Kanten werden in Form einer Adjazenzliste eingehender Kanten gespeichert.
     *               Die Speicherung erfolgt analog zu MODE_ADJ_OUT
     *  MODE_EDG: die Kanten werden in einem einfachen Array (edgeList) abgelegt
     *
     * @param edges
     * @param _mode
     */
    public void setEdges(Collection<Edge> edges, int _mode)
    {
        mode = _mode;

        if ((mode & MODE_ADJ_OUT) == MODE_ADJ_OUT) {
            outAdjIndices = new int[nodes.size()];
            outAdjArray = new int[edges.size()];

            HashMap<Integer, List<Integer>> tmpAdjOut = new HashMap<Integer, List<Integer>>(nodes.size());
            for (Edge e : edges) {
                List<Integer> list = tmpAdjOut.get(e.getSourceNode());
                if (list == null) {
                    list = new LinkedList<Integer>();
                    tmpAdjOut.put(e.getSourceNode(), list);
                }
                list.add(e.getTargetNode());
            }

            int idx = 0;
            for (int i = 0; i < nodes.size(); i++) {
                List<Integer> list = tmpAdjOut.get(new Integer(i));
                if (list == null) {
                    outAdjIndices[i] = -1;
                } else {
                    outAdjIndices[i] = idx;
                    for (Integer n2 : list) {
                        outAdjArray[idx++] = n2;
                    }
                }
            }
        }
        if ((mode & MODE_ADJ_IN) == MODE_ADJ_IN) {
            inAdjIndices = new int[nodes.size()];
            inAdjArray = new int[edges.size()];

            HashMap<Integer, List<Integer>> tmpAdjIn = new HashMap<Integer, List<Integer>>(nodes.size());
            for (Edge e : edges) {
                List<Integer> list = tmpAdjIn.get(e.getTargetNode());
                if (list == null) {
                    list = new LinkedList<Integer>();
                    tmpAdjIn.put(e.getTargetNode(), list);
                }
                list.add(e.getSourceNode());
            }

            int idx = 0;
            for (int i = 0; i < nodes.size(); i++) {
                List<Integer> list = tmpAdjIn.get(new Integer(i));
                if (list == null) {
                    inAdjIndices[i] = -1;
                } else {
                    inAdjIndices[i] = idx;
                    for (Integer n2 : list) {
                        inAdjArray[idx++] = n2;
                    }
                }
            }
        }

        if ((mode
                & MODE_EDG) == MODE_EDG) {
            edgeList = new Edge[edges.size()];
            edges.toArray(edgeList);
        }
    }

    /** Schreibt den Graph in ein dot-file zur Visualisierung mit Graphviz. */
    public void writeToDotFile(File f) throws FileNotFoundException {
        PrintStream out = new PrintStream(f);

        out.println("digraph g {");

        for(Node n : nodes.values()) {
            out.println(n.getLabel()+";");
        }

        if ((mode & MODE_EDG) == MODE_EDG) {
            for (Edge e : edgeList) {
                out.println(e.getSourceNode() + " -> " + e.getTargetNode());
            }
        } else if ((mode & MODE_ADJ_OUT) == MODE_ADJ_OUT) {
            for (int i = 0; i < inAdjIndices.length; i++) {
                int idx = inAdjIndices[i];
                if (idx == -1) {
                    continue;
                }
                int next = -1;
                if (i < inAdjIndices.length - 1) {
                    next = inAdjIndices[i + 1];
                } else {
                    next = inAdjArray.length;
                }

                for (int j = idx; j < next; j++) {
                    out.println(i + " -> "  + inAdjArray[j]);
                }
            }
        }

        out.println("}");
        out.close();
    }

    public static Graph readFromFile(File graphFile, int mode)
    {
        int lineCount = 1;
        Graph graph = null;

        Pattern flPattern = Pattern.compile("n ([0-9]+) m ([0-9]+)", Pattern.CASE_INSENSITIVE);
        Pattern pattern = Pattern.compile("([ve]) ([0-9]+) (-??[0-9]+) ?(-??[0-9]+)?", Pattern.CASE_INSENSITIVE);
        try {

            int numNodes = -1;
            Matcher m = null;
            String line = null;
            BufferedReader br = new BufferedReader(new FileReader(graphFile));

            //read first line
            line = br.readLine();
            m = flPattern.matcher(line);
            m.find();
            numNodes = Integer.parseInt(m.group(1));

            //die kanten werden zunächst in eine einfache liste geladen und
            //anschließend - dem gewünschten modus entsprechend - im graph abgelegt.
            graph = new Graph(numNodes);
            LinkedList<Edge> edges = new LinkedList<Edge>();

            while ((line = br.readLine()) != null) {
                lineCount++;
                m = pattern.matcher(line);
                m.find();
                if (m.group(1).equalsIgnoreCase("v")) {
                    //Node

                    int label = Integer.parseInt(m.group(2));
                    int x = -1, y = -1;
                    try {
                        x = Integer.parseInt(m.group(3));
                    } catch (Exception e) {
                        x = -1;
                    }
                    try {
                        y = Integer.parseInt(m.group(4));
                    } catch (Exception e) {
                        y = -1;
                    }
                    graph.nodes.put(new Integer(label), new Node(label, x, y));
                } else {
                    //Edge
                    Integer n1 = Integer.parseInt(m.group(2));
                    Integer n2 = Integer.parseInt(m.group(3));

                    edges.add(new Edge(n1, n2));
                }
            }
            br.close();
            graph.setEdges(edges, mode);
        } catch (IOException ioe) {
            System.err.println("ERROR: Cannot read File (" + ioe.getLocalizedMessage() + ")");
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Fehler beim Laden der Datei: " + graphFile.getName() + " in Zeile: " + lineCount);
            e.printStackTrace();
            System.exit(1);
        }

        return graph;
    }
}

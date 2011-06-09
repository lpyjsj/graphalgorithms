package Graphs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Christian Hildebrandt (210238835), Matthias Bady (210235131)
 */
public class Graph {

    public static final int MODE_ADJ = 0x1;
    public static final int MODE_EDG = 0x2;
    /***/
    public int mode;
    public int[] adjArray;
    public int[] adjIndices;
    public Edge[] edgeList;
    public HashMap<Integer, Node> nodes;

    public Graph(int numNodes)
    {
        nodes = new HashMap<Integer, Node>(numNodes);
    }

    public void setEdges(Collection<Edge> edges, int _mode)
    {
        mode = _mode;

        if ((mode & MODE_ADJ) == 1) {
            adjIndices = new int[nodes.size()];
            adjArray = new int[edges.size()];
            HashMap<Integer, List<Integer>> tmpAdj = new HashMap<Integer, List<Integer>>(nodes.size());

            for (Edge e : edges) {
                List<Integer> list = tmpAdj.get(e.getSourceNode());
                if (list == null) {
                    list = new LinkedList<Integer>();
                    tmpAdj.put(e.getSourceNode(), list);
                }
                list.add(e.getTargetNode());
            }

            int idx = 0;
            for (int i = 0; i < nodes.size(); i++) {
                List<Integer> list = tmpAdj.get(new Integer(i));
                if (list == null) {
                    adjIndices[i] = -1;
                } else {
                    adjIndices[i] = idx;
                    for (Integer n2 : list) {
                        adjArray[idx++] = n2.intValue();
                    }
                }
            }
        }
        if ((mode & MODE_EDG) == 1) {
            edgeList = new Edge[edges.size()];
            edges.toArray(edgeList);
        }
    }

    public void writeToFile(File f)
    {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write("n " + nodes.size() + " m ");

            if ((mode & MODE_EDG) == 1) {
                bw.write(new Integer(edgeList.length).toString());
            } else if ((mode & MODE_ADJ) == 1) {
                bw.append(new Integer(adjArray.length).toString());
            }
            bw.newLine();
            for (Node n : nodes.values()) {
                bw.write(n.toString());
                bw.newLine();
            }
            if ((mode & MODE_EDG) == 1) {
                for (Edge e : edgeList) {
                    bw.write(e.toString());
                    bw.newLine();
                }
            } else if ((mode & MODE_ADJ) == 1) {
                for (int i = 0; i < adjIndices.length; i++) {
                    int idx = adjIndices[i];
                    if (idx == -1) {
                        continue;
                    }
                    int next = -1;
                    if(i<adjIndices.length-1) {
                        next = adjIndices[i + 1];
                    } else {
                        next = adjArray.length;
                    }

                    for (int j = idx; j < next; j++) {
                        bw.write("e " + i + " " + adjArray[j]);
                        bw.newLine();
                    }
                }
            }
            bw.close();
        } catch (IOException ioe) {
            System.err.println("Could not write ro File: " + f.getName());
            System.out.println(ioe.getLocalizedMessage());
        }
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
            
            graph = new Graph(numNodes);
            Collection<Edge> edges = new LinkedList<Edge>();

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

    public void printGraph()
    {
        //TODO print Graph as PNG or somewhat
    }
}

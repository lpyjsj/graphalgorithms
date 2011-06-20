/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphs;

import Graphs.Algorithms.Dijkstra;
import java.io.File;

/**
 *
 * @author ad-530
 */
public class DijkstraTest {

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
        if ((mode & (MODE_H | MODE_D)) == (MODE_H | MODE_D)) {
            System.err.println("Optionen -h und -d können nicht gemeinsam verwendet werden!");
            System.exit(-1);
        }

        Graph graph = null;
        Dijkstra.ComputeMode cMode;

        long t1 = System.currentTimeMillis();
        if ((mode & MODE_B) == MODE_B) {
            cMode = Dijkstra.ComputeMode.BIDIRECTIONAL;
            graph = Graph.readFromFile(new File(filename), Graph.MODE_ADJ_IN | Graph.MODE_ADJ_OUT);
        } else {
            cMode = Dijkstra.ComputeMode.UNIDIRECTIONAL;
            graph = Graph.readFromFile(new File(filename), Graph.MODE_ADJ_OUT);
        }
        System.out.println("Graph loaded in: " + (System.currentTimeMillis() - t1) / 1000.0 + "s");
        System.out.println(graph.nodes.size() + " nodes");

        t1 = System.currentTimeMillis();
        System.out.println(cMode.toString());

        Dijkstra.Implementation impl = Dijkstra.Implementation.NONE;
        if ((mode & MODE_H) == MODE_H) {
            impl = Dijkstra.Implementation.DEFAULT;
        } else if ((mode & MODE_D) == MODE_D) {
            impl = Dijkstra.Implementation.DIAL;
        } else if ((mode & MODE_Z) == MODE_Z) {
            impl = Dijkstra.Implementation.GOAL_DIRECTED;
        }
        if (impl == Dijkstra.Implementation.NONE) {
            System.err.println("Eine der Operationen -h, -d oder -z benötigt!");
            System.exit(-1);
        }

        Dijkstra d = new Dijkstra(graph, s, t, impl, cMode);
        d.compute();

        System.out.println("Path computed in: " + (System.currentTimeMillis() - t1) / 1000.0 + "s");
        System.out.println(d.path.toString());
    }
}

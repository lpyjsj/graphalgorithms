package Graphs.Algorithms;

import Graphs.Generator.GraphGenerator;
import Graphs.Graph;
import java.io.File;

public class Dijkstra {

    private final static int MODE_H = 0x1;
    private final static int MODE_D = 0x2;
    private final static int MODE_B = 0x4;
    private final static int MODE_Z = 0x8;
    
    public static void main(String[] args) {

        if (args.length < 7) {
            System.err.println("Falsche Anzahl von Argumenten!");
            System.exit(-1);
        }
        
        String filename = args[1];
        int mode = 0, s = -1, t = -1;
        
        for (int i = 2; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-s")) {
                try {
                    s = Integer.parseInt(args[i+1]);
                } catch(Exception e) {
                    System.err.println("Kein oder kein gültiges Argument für Option -s gefunden!");
                    System.exit(-1);
                }
            } else if (args[i].equalsIgnoreCase("-t")) {
                try {
                    s = Integer.parseInt(args[i+1]);
                } catch(Exception e) {
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
        } else if((mode & (MODE_H|MODE_D)) == 1) {
            System.err.println("Optionen -h und -d können nicht gemeinsam verwendet werden!");
            System.exit(-1);
        }
    }
}

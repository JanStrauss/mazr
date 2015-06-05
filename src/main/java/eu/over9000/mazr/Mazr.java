package eu.over9000.mazr;

import eu.over9000.mazr.model.Edge;
import eu.over9000.mazr.model.Maze;
import eu.over9000.mazr.prim.Prim;

import java.util.Set;

/**
 * Created by jan on 05.06.15.
 */
public class Mazr {

    public static void main(String[] args) {
        Maze m = new Maze(128,16, 1337);
        //m.printMaze();
        //m.printAdjacency();
        //m.printNodes();
        //m.printEdges();
        Set<Edge> mst = Prim.calculateMinimumSpanningTree(m);

       // System.out.println(mst);

        m.printMST(mst);
    }


}

package eu.over9000.mazr.prim;

import eu.over9000.mazr.model.Edge;
import eu.over9000.mazr.model.Maze;
import eu.over9000.mazr.model.Node;

import java.util.*;

/**
 * Created by jan on 05.06.15.
 */
public class Prim {
    private Prim(){}

    public static Set<Edge> calculateMinimumSpanningTree(Maze maze){
        Set<Edge> result= new HashSet<>();

        Set<Node> unspanned = new HashSet<>(maze.getNodes());

            Node root = maze.getStartNode();
            unspanned.remove(root);

            PriorityQueue<Edge> dangling = new PriorityQueue<>();

            dangling.addAll(maze.getEdgesOfNode().get(root));

            for (Edge minEdge; (minEdge = dangling.poll()) != null; ) {
                Node source = maze.getEdgeSource(minEdge);
                Node target = unspanned.contains(source) ? source : maze.getEdgeTarget(minEdge);

                if (!unspanned.contains(target)) {
                    continue;
                }

                result.add(minEdge);

                unspanned.remove(target);

                for (Edge edge : maze.getEdgesOfNode().get(target)) {
                    if (unspanned.contains(maze.getEdgeSource(edge).equals(target) ? maze.getEdgeTarget(edge) : maze.getEdgeSource(edge))) {
                        dangling.add(edge);
                    }
                }
            }


        return result;
    }
}

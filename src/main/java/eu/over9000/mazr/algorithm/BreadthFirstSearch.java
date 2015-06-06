package eu.over9000.mazr.algorithm;

import java.util.*;
import java.util.stream.Collectors;

import javafx.util.Pair;

import eu.over9000.mazr.model.Edge;
import eu.over9000.mazr.model.Node;

/**
 * Created by Jan on 06.06.2015.
 */
public class BreadthFirstSearch {

	private BreadthFirstSearch() {
	}

	public static Map<Node, Integer> calculateDistance(Node start, final Map<Node, Set<Edge>> edgesOfNode, final Map<Edge, Pair<Node, Node>> nodesOfEdge, final Set<Edge> mst) {
		final Map<Node, Integer> result = new HashMap<>();

		Queue<Node> queueNodes = new LinkedList<>();
		Queue<Integer> queueDist = new LinkedList<>();

		queueNodes.add(start);
		queueDist.add(0);

		while (!queueNodes.isEmpty()) {
			Node currentNode = queueNodes.remove();
			Integer currentDistance = queueDist.remove();

			result.put(currentNode, currentDistance);

			getValidEdges(currentNode, edgesOfNode, mst).forEach(edge -> {
				Node target = getTargetNode(currentNode, edge, nodesOfEdge);
				if (!result.containsKey(target)) {
					queueNodes.add(target);
					queueDist.add(currentDistance + 1);
				}
			});

		}


		return result;
	}

	private static Set<Edge> getValidEdges(Node node, final Map<Node, Set<Edge>> edgesOfNode, final Set<Edge> mst) {
		return edgesOfNode.get(node).stream().filter(mst::contains).collect(Collectors.toSet());
	}

	private static Node getTargetNode(Node source, Edge edge, Map<Edge, Pair<Node, Node>> nodesOfEdge) {
		Pair<Node, Node> nodePair = nodesOfEdge.get(edge);
		return source.equals(nodePair.getKey()) ? nodePair.getValue() : nodePair.getKey();
	}

	/*
	1  procedure BFS(G,v) is
2      let Q be a queue
3      Q.enqueue(v)
4      label v as discovered
5      while Q is not empty
6         v ← Q.dequeue()
7         process(v)
8         for all edges from v to w in G.adjacentEdges(v) do
9             if w is not labeled as discovered
10                 Q.enqueue(w)
11                label w as discovered
	 */
}

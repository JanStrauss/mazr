package eu.over9000.mazr.model;

import java.util.*;

import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.util.Pair;

import eu.over9000.mazr.algorithm.BreadthFirstSearch;
import eu.over9000.mazr.algorithm.Prim;
import eu.over9000.mazr.util.ConsoleOutUtil;

/**
 * Created by jan on 05.06.15.
 */
public class Maze {

	private final Node[][] nodes;
	private final Edge[][] edges_ver;
	private final Edge[][] edges_hor;
	private final int width;
	private final int height;
	private final Set<Node> nodesSet;
	private final Map<Node, Set<Edge>> edgesOfNode;
	private final Map<Edge, Pair<Node, Node>> nodesOfEdge;
	private final Random random;
	private final Node startNode;

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public Map<Node, Set<Edge>> getEdgesOfNode() {
		return edgesOfNode;
	}

	public Maze(int width, int height, long seed) {
		nodesSet = new HashSet<>();
		edgesOfNode = new HashMap<>();
		nodesOfEdge = new HashMap<>();

		this.height = height;
		this.width = width;

		random = new Random(seed);
		edges_hor = new Edge[height][width - 1];
		edges_ver = new Edge[height - 1][width];
		nodes = new Node[height][width];


		// init edges hor
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width - 1; j++) {
				edges_hor[i][j] = new Edge(j, i, random.nextFloat(), Orientation.HORIZONTAL);
			}
		}
		// init edges ver
		for (int i = 0; i < height - 1; i++) {
			for (int j = 0; j < width; j++) {
				edges_ver[i][j] = new Edge(j, i, random.nextFloat(), Orientation.VERTICAL);
			}
		}

		// init nodes
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				nodes[i][j] = new Node(j, i);
			}
		}

		// populate maps
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				buildMapContents(nodes[i][j]);
			}
		}
		buildEdgesMap();


		startNode = nodes[random.nextInt(height)][random.nextInt(width)];

	}

	private void buildEdgesMap() {
		edgesOfNode.forEach((node, edges) -> edges.forEach(edge -> {
			if (!nodesOfEdge.containsKey(edge)) {
				nodesOfEdge.put(edge, new Pair<>(node, null));
			} else {
				Pair<Node, Node> nodes = nodesOfEdge.get(edge);
				if (!nodes.getKey().equals(node)) {
					nodesOfEdge.put(edge, new Pair<>(nodes.getKey(), node));
				}
			}
		}));
	}

	public void printMaze() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				System.out.print(" [] ");
				if ((j < width - 1)) {
					System.out.print(String.format("%.2f", edges_hor[i][j].getWeight()) + " ");
				}
			}
			System.out.println();
			if ((i < height - 1)) {
				for (int j = 0; j < width; j++) {
					System.out.print(String.format("%.2f", edges_ver[i][j].getWeight()) + "     ");
				}
				System.out.println();
			}

		}
		System.out.println();
	}

	public void printMST(Set<Edge> mst) {
		Map<Node, EnumSet<Side>> sides = ConsoleOutUtil.buildSidesOfNode(edgesOfNode, mst);

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				System.out.print(ConsoleOutUtil.resolveChar(sides.get(nodes[i][j])));
			}
			System.out.println();
		}
		System.out.println();
	}

	public int[][] buildDistMap() {
		int[][] result = new int[height][width];

		final Map<Node, Integer> distances = BreadthFirstSearch.calculateDistance(startNode, edgesOfNode, nodesOfEdge, Prim.calculateMinimumSpanningTree(this));

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				result[i][j] = distances.get(nodes[i][j]);
			}
		}

		return result;
	}

	private void buildMapContents(Node node) {
		Set<Edge> edges = new HashSet<>();

		int above = node.getY() - 1;
		int below = node.getY() + 1;
		int left = node.getX() - 1;
		int right = node.getX() + 1;

		if (above >= 0) {
			edges.add(this.edges_ver[above][node.getX()]);
		}
		if (below < height) {
			edges.add(this.edges_ver[below - 1][node.getX()]);
		}
		if (left >= 0) {
			edges.add(this.edges_hor[node.getY()][left]);
		}
		if (right < width) {
			edges.add(this.edges_hor[node.getY()][right - 1]);
		}

		nodesSet.add(node);
		edgesOfNode.put(node, edges);
	}

	public void printNodes() {
		edgesOfNode.entrySet().forEach(e -> System.out.println(e.getKey() + "=" + e.getValue()));
		System.out.println();
	}

	public Node getStartNode() {
		return startNode;
	}

	public Node getEdgeSource(Edge edge) {
		return nodesOfEdge.get(edge).getKey();
	}

	public Node getEdgeTarget(Edge edge) {
		return nodesOfEdge.get(edge).getValue();
	}

	public void printEdges() {
		nodesOfEdge.entrySet().forEach(e -> System.out.println(e.getKey() + "=" + e.getValue()));
		System.out.println();
	}

	public Set<Node> getNodes() {
		return nodesSet;
	}
}

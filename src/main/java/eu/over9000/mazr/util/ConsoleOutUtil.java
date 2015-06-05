package eu.over9000.mazr.util;

import eu.over9000.mazr.model.Edge;
import eu.over9000.mazr.model.Node;
import javafx.geometry.Orientation;
import javafx.geometry.Side;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by jan on 05.06.15.
 */
public class ConsoleOutUtil {


    private static final char[] CHARS = {'═' ,'║' ,'╔' ,'╗' ,'╚' ,'╝' ,'╠' ,'╣' ,'╦' ,'╩' ,'╬','╥','╞','╡', '╨'};

    private static final Map<EnumSet<Side>, Character> charMap = new HashMap<>();

    static{
        charMap.put(EnumSet.of(Side.TOP), '╨');
        charMap.put(EnumSet.of(Side.LEFT), '╡');
        charMap.put(EnumSet.of(Side.BOTTOM), '╥');
        charMap.put(EnumSet.of(Side.RIGHT), '╞');
        charMap.put(EnumSet.of(Side.TOP, Side.LEFT), '╝');
        charMap.put(EnumSet.of(Side.TOP, Side.BOTTOM), '║');
        charMap.put(EnumSet.of(Side.TOP, Side.RIGHT), '╚');
        charMap.put(EnumSet.of(Side.LEFT, Side.BOTTOM), '╗');
        charMap.put(EnumSet.of(Side.LEFT, Side.RIGHT), '═');
        charMap.put(EnumSet.of(Side.BOTTOM, Side.RIGHT), '╔');
        charMap.put(EnumSet.of(Side.TOP, Side.LEFT, Side.BOTTOM), '╣');
        charMap.put(EnumSet.of(Side.TOP, Side.LEFT, Side.RIGHT), '╩');
        charMap.put(EnumSet.of(Side.TOP, Side.BOTTOM, Side.RIGHT), '╠');
        charMap.put(EnumSet.of(Side.LEFT, Side.BOTTOM, Side.RIGHT), '╦');
        charMap.put(EnumSet.of(Side.TOP, Side.LEFT, Side.BOTTOM, Side.RIGHT), '╬');
    }


    public static char resolveChar(EnumSet<Side> sides){
        Character result = charMap.get(sides);
        return result == null ? '?' : result;
    }

    public static Map<Node, EnumSet<Side>> buildSidesOfNode(Map<Node, Set<Edge>> edgesOfNode, Set<Edge> mst){
        Map<Node, EnumSet<Side>> sidesOfNode = new HashMap<>();

        edgesOfNode.entrySet().forEach(entry -> {
            Node node = entry.getKey();
            EnumSet<Side> sides = EnumSet.noneOf(Side.class);
            entry.getValue().forEach(edge -> {
                if (!mst.contains(edge)) {
                    return;
                }

                if (edge.getOrientation() == Orientation.HORIZONTAL) {
                    if (edge.getX() < node.getX()) {
                        sides.add(Side.LEFT);
                    } else {
                        sides.add(Side.RIGHT);
                    }
                } else {
                    if (edge.getY() < node.getY()) {
                        sides.add(Side.TOP);
                    } else {
                        sides.add(Side.BOTTOM);
                    }
                }
            });

            sidesOfNode.put(node, sides);
        });
        return sidesOfNode;
    }
}

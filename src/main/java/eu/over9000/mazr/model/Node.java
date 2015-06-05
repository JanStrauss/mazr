package eu.over9000.mazr.model;

import java.util.Objects;

/**
 * Created by jan on 05.06.15.
 */
public class Node {
    private final int x;
    private final int y;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(x, node.x) && Objects.equals(y, node.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Node{" + x + "|" + y + "}";
    }
}

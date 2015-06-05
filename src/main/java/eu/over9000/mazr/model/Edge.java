package eu.over9000.mazr.model;

import javafx.geometry.Orientation;

import java.util.Objects;

/**
 * Created by jan on 05.06.15.
 */
public class Edge implements Comparable<Edge> {
    public int getX() {
        return x;
    }

    private final int x;
    private final int y;

    private final float weight;
    private final Orientation orientation;

    public float getWeight() {
        return weight;
    }

    public Edge(int x, int y, float weight, Orientation orientation) {
        this.x = x;
        this.y = y;
        this.weight = weight;
        this.orientation = orientation;
    }

    @Override
    public int compareTo(Edge other) {
        return Float.compare(weight, other.getWeight());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(x, edge.x) &&
                Objects.equals(y, edge.y) &&
                Objects.equals(orientation, edge.orientation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, orientation);
    }

    @Override
    public String toString() {
        return "Edge{" +
                "x=" + x +
                ", y=" + y +
                ", weight=" + weight +
                ", orientation=" + orientation +
                '}';
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public int getY() {
        return y;
    }
}

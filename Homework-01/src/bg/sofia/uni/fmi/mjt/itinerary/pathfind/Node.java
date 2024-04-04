package bg.sofia.uni.fmi.mjt.itinerary.pathfind;

import bg.sofia.uni.fmi.mjt.itinerary.City;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {
    public static final int DEFAULT_PRICE = 20;
    public static final int METERS_IN_KILOMETER = 1000;
    private final City city;
    private Node parent = null;
    private List<Edge> neighbors;
    public BigDecimal f = new BigDecimal(0);
    public BigDecimal g = new BigDecimal(0);
    public BigDecimal h;

    public static class Edge {
        public BigDecimal cost;
        public Node node;
        public Edge(BigDecimal cost, Node node) {
            this.cost = cost;
            this.node = node;
        }
    }

    public Node(City city) {
        this.city = city;
        this.neighbors = new ArrayList<>();
    }

    public Node(City city, BigDecimal h) {
        this.city = city;
        this.h = h;
        this.neighbors = new ArrayList<>();
    }

    public City getCity() {
        return city;
    }

    public Node getParent() {
        return parent;
    }

    public List<Edge> getNeighbors() {
        return neighbors;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    @Override
    public int compareTo(Node other) {
        if (this.f.compareTo(other.f) == 0) {
            return this.parent.city.name().compareTo(other.parent.city.name());
        }

        return this.f.compareTo(other.f);
    }

    public BigDecimal calculateHeuristic(Node target) {
        return BigDecimal.valueOf((long) getDistanceTo(target) * DEFAULT_PRICE / METERS_IN_KILOMETER);
    }

    private int getDistanceTo(Node target) {
        return Math.abs(target.city.location().x() - city.location().x()) +
            Math.abs(target.city.location().y() - city.location().y());
    }
}
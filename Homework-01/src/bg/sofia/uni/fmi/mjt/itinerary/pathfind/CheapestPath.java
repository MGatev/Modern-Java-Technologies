package bg.sofia.uni.fmi.mjt.itinerary.pathfind;

import bg.sofia.uni.fmi.mjt.itinerary.City;
import bg.sofia.uni.fmi.mjt.itinerary.Journey;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.SequencedCollection;

public class CheapestPath {
    private static void addNeighbours(Node node, List<Journey> schedule,
                                      PriorityQueue<Node> openList, List<Node> closedList) {
        for (Journey journey : schedule) {
            if (journey.from().equals(node.getCity())) {
                boolean isDestinationInClosedList = false;
                for (Node m : closedList) {
                    if (m.getCity().equals(journey.to())) {
                        isDestinationInClosedList = true;
                        break;
                    }
                }

                if (!isDestinationInClosedList) {
                    boolean isDestinationInOpenList = false;
                    for (Node n : openList) {
                        if (n.getCity().equals(journey.to())) {
                            isDestinationInOpenList = true;
                            node.getNeighbors().add(new Node.Edge(journey.getPriceWithTax(), n));
                            break;
                        }
                    }
                    if (!isDestinationInOpenList) {
                        node.getNeighbors().add(new Node.Edge(journey.getPriceWithTax(), new Node(journey.to())));
                    }
                }
            }
        }
    }

    private static void examineAllNeighbours(Node node, Node target, PriorityQueue<Node> openList,
                                             List<Node> closedList) {
        for (Node.Edge edge : node.getNeighbors()) {
            Node neighbourNode = edge.node;
            BigDecimal totalCost = node.g.add(edge.cost);

            if (!openList.contains(neighbourNode) && !closedList.contains(neighbourNode)) {
                neighbourNode.setParent(node);
                neighbourNode.g = totalCost;
                neighbourNode.f = neighbourNode.g.add(neighbourNode.calculateHeuristic(target));
                openList.add(neighbourNode);
            } else {
                if (neighbourNode.g.compareTo(totalCost) > 0) {
                    neighbourNode.setParent(node);
                    neighbourNode.g = totalCost;
                    neighbourNode.f = neighbourNode.g.add(neighbourNode.calculateHeuristic(target));

                    if (closedList.contains(neighbourNode)) {
                        closedList.remove(neighbourNode);
                        openList.add(neighbourNode);
                    }
                }
            }
        }
    }

    public static Node findPathBetween(Node start, Node target, List<Journey> schedule) {
        List<Node> closedList = new ArrayList<>();
        PriorityQueue<Node> openList = new PriorityQueue<>();

        start.f = start.g.add(start.calculateHeuristic(target));
        openList.add(start);

        while (!openList.isEmpty()) {
            Node topNode = openList.peek();
            if (topNode.getCity().name().equals(target.getCity().name())) {
                return topNode;
            }

            if (topNode.getNeighbors().isEmpty()) {
                addNeighbours(topNode, schedule, openList, closedList);
            }

            examineAllNeighbours(topNode, target, openList, closedList);

            openList.remove(topNode);
            closedList.add(topNode);
        }
        return null;
    }

    public static SequencedCollection<City> reconstructPath(Node target) {
        Node tempNode = target;

        if (tempNode == null)
            return List.of();

        List<City> path = new ArrayList<>();

        while (tempNode.getParent() != null) {
            path.add(tempNode.getCity());
            tempNode = tempNode.getParent();
        }

        path.add(tempNode.getCity());
        Collections.reverse(path);

        return path;
    }
}
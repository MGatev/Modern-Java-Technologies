package bg.sofia.uni.fmi.mjt.order.server.repository;

import bg.sofia.uni.fmi.mjt.order.server.Response;
import bg.sofia.uni.fmi.mjt.order.server.destination.Destination;
import bg.sofia.uni.fmi.mjt.order.server.order.Order;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Color;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Size;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.TShirt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MJTOrderRepository implements OrderRepository {

    Collection<Order> orders = new ArrayList<>();
    private int id = 1;

    private Size validateSize(String size) {
        for (Size currentSize : Size.values()) {
            if (currentSize.getName().equals(size)) {
                return Size.valueOf(size);
            }
        }

        return Size.UNKNOWN;
    }

    private Color validateColor(String color) {
        for (Color currentColor : Color.values()) {
            if (currentColor.getName().equals(color)) {
                return Color.valueOf(color);
            }
        }

        return Color.UNKNOWN;
    }

    private Destination validateDestination(String destination) {
        for (Destination currentDestination : Destination.values()) {
            if (currentDestination.getName().equals(destination)) {
                return Destination.valueOf(destination);
            }
        }

        return Destination.UNKNOWN;
    }

    private void addSizeToAdditionalInfo(Size requestSize, StringBuilder additionalInfoBuilder) {
        if (requestSize.equals(Size.UNKNOWN)) {
            additionalInfoBuilder.append("size");
        }
    }

    private void addColorToAdditionalInfo(Color requestColor, StringBuilder additionalInfoBuilder) {
        if (requestColor.equals(Color.UNKNOWN)) {
            if (additionalInfoBuilder.toString().equals("invalid=")) {
                additionalInfoBuilder.append("color");
            } else {
                additionalInfoBuilder.append(",color");
            }
        }
    }

    private void addDestinationToAdditionalInfo(Destination requestDestination, StringBuilder additionalInfoBuilder) {
        if (requestDestination.equals(Destination.UNKNOWN)) {
            if (additionalInfoBuilder.toString().equals("invalid=")) {
                additionalInfoBuilder.append("destination");
            } else {
                additionalInfoBuilder.append(",destination");
            }
        }
    }

    @Override
    public Response request(String size, String color, String destination) {
        if (size == null || color == null || destination == null) {
            throw new IllegalArgumentException("Invalid arguments!");
        }
        Size requestSize = validateSize(size);

        Color requestColor = validateColor(color);

        Destination requestDestination = validateDestination(destination);

        StringBuilder additionalInfoBuilder = new StringBuilder("invalid=");
        addSizeToAdditionalInfo(requestSize, additionalInfoBuilder);
        addColorToAdditionalInfo(requestColor, additionalInfoBuilder);
        addDestinationToAdditionalInfo(requestDestination, additionalInfoBuilder);

        String additionalInfo = additionalInfoBuilder.toString();

        if (!additionalInfo.equals("invalid=")) {
            Order requestedOrder = new Order(-1, new TShirt(requestSize, requestColor), requestDestination);
            orders.add(requestedOrder);
            return Response.decline(additionalInfo);
        }

        orders.add(new Order(id, new TShirt(requestSize, requestColor), requestDestination));
        id++;
        return Response.create(id - 1);
    }

    @Override
    public Response getOrderById(int id) {
        int foundId = orders.stream()
            .map(Order::id)
            .filter(curr -> curr == id)
            .findFirst().orElse(-1);

        if (foundId == id) {
            Order orderToReturn = orders.stream()
                .filter(order -> order.id() == id)
                .toList()
                .getFirst();
            return Response.ok(List.of(orderToReturn));
        }
        return Response.notFound(id);
    }

    @Override
    public Response getAllOrders() {
        return Response.ok(orders);
    }

    @Override
    public Response getAllSuccessfulOrders() {
        return Response.ok(orders.stream()
            .filter(order -> order.id() != -1)
            .toList());
    }
}

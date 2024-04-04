package bg.sofia.uni.fmi.mjt.order.server;

import bg.sofia.uni.fmi.mjt.order.server.order.Order;

import java.util.Collection;

public record Response(Status status, String additionalInfo, Collection<Order> orders) {
    private enum Status {
        OK, CREATED, DECLINED, NOT_FOUND
    }

    /**
     * Creates a response
     *
     * @param id order id
     * @return response with    status Status.CREATED and with proper message for additional info
     */
    public static Response create(int id) {
        return new Response(Status.CREATED, "ORDER_ID=" + id, null);
    }

    /**
     * Creates a response
     *
     * @param orders the orders which will be returned to the client
     * @return response with status Status.OK and Collection of orders
     */
    public static Response ok(Collection<Order> orders) {
        return new Response(Status.OK, "", orders);
    }

    /**
     * Creates a response
     *
     * @param errorMessage the message which will be sent as additionalInfo
     * @return response with status Status.DECLINED and errorMessage as additionalInfo
     */
    public static Response decline(String errorMessage) {
        return new Response(Status.DECLINED, errorMessage, null);
    }

    /**
     * Creates a response
     *
     * @param id order id
     * @return response with status Status.NOT_FOUND and with proper message for additional info
     */
    public static Response notFound(int id) {
        return new Response(Status.NOT_FOUND, "Order with id = " + id + " does not exist.", null);
    }

    @Override
    public String toString() {
        StringBuilder stringRepresent = new StringBuilder("{");
        switch (status) {
            case CREATED -> stringRepresent.append("\"status\":\"CREATED\", \"additionalInfo\":\"")
                .append(additionalInfo).append("\"}");
            case DECLINED ->
                stringRepresent.append("\"status\":\"DECLINED\", \"additionalInfo\":\"").append(additionalInfo)
                    .append("\"}");
            case OK -> {
                stringRepresent.append("\"status\":\"OK\", \"orders\":[");

                int index = 0;
                for (Order current : orders) {
                    stringRepresent.append("{\"id\":").append(current.id()).append(", \"tShirt\":{\"size\":\"")
                        .append(current.tShirt().size()).append("\", \"color\":\"").append(current.tShirt().color())
                        .append("\"}, \"destination\":\"").append(current.destination()).append("\"}");

                    if ((index++) != orders().size() - 1) {
                        stringRepresent.append(",").append(System.lineSeparator());
                    }
                }
                stringRepresent.append("]}").append(System.lineSeparator());
            }
            case NOT_FOUND -> stringRepresent.append("\"status\":\"NOT_FOUND\", \"additionalInfo\":\"")
                .append(additionalInfo).append("\"}");
        }
        return stringRepresent.toString();
    }
}
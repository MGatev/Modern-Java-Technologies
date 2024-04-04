package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType;

import java.math.BigDecimal;

public record Journey(VehicleType vehicleType, City from, City to, BigDecimal price) {
    public BigDecimal getPriceWithTax() {
        return price.add(price.multiply(vehicleType.getGreenTax()));
    }

    public void printJourney() {
        System.out.println("Journey{" + vehicleType + ", from = " + from.name() +
            " , to = " + to.name() + ", price = " + price + "}");
    }
}

package bg.sofia.uni.fmi.mjt.simcity.utility;

import bg.sofia.uni.fmi.mjt.simcity.property.billable.Billable;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class UtilityService implements UtilityServiceAPI {

    static final double HUNDRED = 100.0;

    Map<UtilityType, Double> taxRates = new EnumMap<>(UtilityType.class);

    public UtilityService(Map<UtilityType, Double> taxRates) {
        this.taxRates.putAll(taxRates);
    }

    @Override
    public <T extends Billable> double getUtilityCosts(UtilityType utilityType, T billable) {
        if (utilityType == null || billable == null) {
            throw new IllegalArgumentException("Invalid parameters!");
        }

        double tax = 0.0;

        if (utilityType.equals(UtilityType.ELECTRICITY)) {
            tax += taxRates.get(utilityType) * billable.getElectricityConsumption();
        } else if (utilityType.equals(UtilityType.NATURAL_GAS)) {
            tax += taxRates.get(utilityType) * billable.getNaturalGasConsumption();
        } else if (utilityType.equals(UtilityType.WATER)) {
            tax += taxRates.get(utilityType) * billable.getWaterConsumption();
        }

        return tax;

    }

    @Override
    public <T extends Billable> double getTotalUtilityCosts(T billable) {
        if (billable == null) {
            throw new IllegalArgumentException("Invalid parameter!");
        }

        double tax = 0.0;

        for (UtilityType u : UtilityType.values()) {
            tax += getUtilityCosts(u, billable);
        }

        return tax;
    }

    @Override
    public <T extends Billable> Map<UtilityType, Double> computeCostsDifference(T firstBillable, T secondBillable) {
        if (firstBillable == null || secondBillable == null) {
            throw new IllegalArgumentException("Invalid parameters!");
        }

        Map<UtilityType, Double> costDifference = new EnumMap<>(UtilityType.class);

        for (UtilityType u : UtilityType.values()) {
            double cost = Math.round(Math.abs(getUtilityCosts(u, firstBillable) -
                getUtilityCosts(u, secondBillable)) * HUNDRED) / HUNDRED;
            costDifference.put(u, cost);
        }

        Map<UtilityType, Double> mapToReturn = Collections.unmodifiableMap(new EnumMap<>(costDifference));

        return mapToReturn;
    }
}

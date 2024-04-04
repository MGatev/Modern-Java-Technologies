package bg.sofia.uni.fmi.mjt.cooking.dto;

import java.util.Collection;

public record Recipe(String label, Collection<String> dietLabels,
                     Collection<String> healthLabels, double calories,
                     double totalCO2Emissions, double totalWeight, double totalTime,
                     Collection<String> cuisineType, Collection<String> mealType,
                     Collection<String> dishType, Collection<String> ingredientLines) {
}

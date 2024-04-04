package bg.sofia.uni.fmi.mjt.cooking.request;

public enum MealType {
    BREAKFAST("Breakfast"),
    DINNER("Dinner"),
    LUNCH("Lunch"),
    SNACK("Snack"),
    TEATIME("Teatime");

    final String name;

    MealType(String name) {
        this.name = name;
    }

    public String getString() {
        return name;
    }
}

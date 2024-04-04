package bg.sofia.uni.fmi.mjt.cooking.request;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecipeRequestTest {
    private final List<String> keywords = List.of("Chicken", "Egg", "Fish");
    private final Set<MealType> mealTypes = EnumSet.of(MealType.DINNER, MealType.LUNCH);
    private final Set<HealthType> healthTypes = EnumSet.of(HealthType.EGG_FREE, HealthType.FISH_FREE);

    @Test
    void testGetUriWithoutCategories() throws URISyntaxException {
        RecipeRequest request = new RecipeRequest(null, null, null);
        String expected =
            "https://api.edamam.com/api/recipes/v2?type=public&app_id=465fcbe2&app_key=cdbc82c0a149a2c2f8d58a7dcd6828e7";
        String actual = request.getUri().toString();
        assertEquals(expected, actual,
            "Expected URI: " + expected + ", but was: " + actual);
    }

    @Test
    void testGetUriWithKeywordsOnly() throws URISyntaxException {
        RecipeRequest request = new RecipeRequest(keywords, null, null);
        String expected =
            "https://api.edamam.com/api/recipes/v2?type=public&q=Chicken%2C%20Egg%2C%20Fish&app_id=465fcbe2&app_key=cdbc82c0a149a2c2f8d58a7dcd6828e7";
        String actual = request.getUri().toString();
        assertEquals(expected, actual, "Expected URI: " + expected + ", but was: " + actual);
    }

    @Test
    void testGetUriWithMealTypesOnly() throws URISyntaxException {
        RecipeRequest request = new RecipeRequest(null, mealTypes, null);
        String expected =
            "https://api.edamam.com/api/recipes/v2?type=public&app_id=465fcbe2&app_key=cdbc82c0a149a2c2f8d58a7dcd6828e7&mealType=Dinner&mealType=Lunch";
        String actual = request.getUri().toString();
        assertEquals(expected, actual, "Expected URI: " + expected + ", but was: " + actual);
    }

    @Test
    void testGetUriWithHealthTypesOnly() throws URISyntaxException {
        RecipeRequest request = new RecipeRequest(null, null, healthTypes);
        String expected =
            "https://api.edamam.com/api/recipes/v2?type=public&app_id=465fcbe2&app_key=cdbc82c0a149a2c2f8d58a7dcd6828e7&health=egg-free&health=fish-free";
        String actual = request.getUri().toString();
        assertEquals(expected, actual, "Expected URI: " + expected + ", but was: " + actual);
    }

    @Test
    void testGetBuiltUriWithKeywordsAndMealTypes() throws URISyntaxException {
        RecipeRequest request = new RecipeRequest(keywords, mealTypes, null);
        String expected = "https://api.edamam.com/api/recipes/v2?type=public&q=Chicken%2C%20Egg%2C%20Fish&app_id=465fcbe2&app_key=cdbc82c0a149a2c2f8d58a7dcd6828e7&mealType=Dinner&mealType=Lunch";
        String actual = request.getUri().toString();
        assertEquals(expected, actual, "Expected URI: " + expected + ", but was: " + actual);
    }

    @Test
    void testGetUriWithKeywordsAndHealthTypes() throws URISyntaxException {
        RecipeRequest request = new RecipeRequest(keywords, null, healthTypes);
        String expected = "https://api.edamam.com/api/recipes/v2?type=public&q=Chicken%2C%20Egg%2C%20Fish&app_id=465fcbe2&app_key=cdbc82c0a149a2c2f8d58a7dcd6828e7&health=egg-free&health=fish-free";
        String actual = request.getUri().toString();
        assertEquals(expected, actual, "Expected URI: " + expected + ", but was: " + actual);
    }

    @Test
    void testGetUriWithMealTypesAndHealthTypes() throws URISyntaxException {
        RecipeRequest request = new RecipeRequest(null, mealTypes, healthTypes);
        String expected = "https://api.edamam.com/api/recipes/v2?type=public&app_id=465fcbe2&app_key=cdbc82c0a149a2c2f8d58a7dcd6828e7&health=egg-free&health=fish-free&mealType=Dinner&mealType=Lunch";
        String actual = request.getUri().toString();
        assertEquals(expected, actual, "Expected URI: " + expected + ", but was: " + actual);
    }

    @Test
    void testGetUriWithAllCategories() throws URISyntaxException {
        RecipeRequest request = new RecipeRequest(keywords, mealTypes, healthTypes);
        String expected = "https://api.edamam.com/api/recipes/v2?type=public&q=Chicken%2C%20Egg%2C%20Fish&app_id=465fcbe2&app_key=cdbc82c0a149a2c2f8d58a7dcd6828e7&health=egg-free&health=fish-free&mealType=Dinner&mealType=Lunch";
        String actual = request.getUri().toString();
        assertEquals(expected, actual, "Expected URI: " + expected + ", but was: " + actual);
    }

    @Test
    void testGetUriWithNotNullUri() throws URISyntaxException {
        RecipeRequest request = new RecipeRequest(URI.create("https://abc.bg"));
        String expected = "https://abc.bg";
        String actual = request.getUri().toString();
        assertEquals(expected, actual, "Expected URI: " + expected + ", but was: " + actual);
    }
}

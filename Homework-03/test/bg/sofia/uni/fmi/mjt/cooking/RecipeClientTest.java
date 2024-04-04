package bg.sofia.uni.fmi.mjt.cooking;

import bg.sofia.uni.fmi.mjt.cooking.dto.Recipe;
import bg.sofia.uni.fmi.mjt.cooking.dto.RecipeContainer;
import bg.sofia.uni.fmi.mjt.cooking.exception.InvalidRequestException;
import bg.sofia.uni.fmi.mjt.cooking.request.HealthType;
import bg.sofia.uni.fmi.mjt.cooking.request.MealType;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class RecipeClientTest {

    private final RecipeContainer r1 = new RecipeContainer(new Recipe("Salted Cashew Coconut Truffles",
        List.of("Low-Sodium"),
        List.of("Vegan", "Vegetarian", "Pescatarian", "Paleo", "Mediterranean", "Dairy-Free", "Gluten-Free",
            "Wheat-Free", "Egg-Free", "Peanut-Free", "Soy-Free", "Fish-Free", "Shellfish-Free", "Pork-Free",
            "Red-Meat-Free", "Crustacean-Free", "Celery-Free", "Mustard-Free", "Sesame-Free", "Lupine-Free",
            "Mollusk-Free", "Alcohol-Free", "No oil added", "Sulfite-Free", "Kosher"), 1714.264000012703,
        866.466507321036, 359.26354166889234, 0, List.of("caribbean"),
        List.of("snack"), List.of("desserts"),
        List.of("* 6 medjool date, pitted", "* 1/2 cup creamy cashew butter (trader joe's brand i my fave)",
            "* 1/4 teaspoon vanilla extract", "* 1/4 teaspoon sea salt",
            "* 1 cup shredded unsweetened coconut, divided")));

    private final RecipeContainer r2 = new RecipeContainer(new Recipe("RAW Vanilla Cashew Truffles",
        List.of("Low-Sodium"),
        List.of("Vegan", "Vegetarian", "Pescatarian", "Mediterranean", "Dairy-Free", "Gluten-Free", "Wheat-Free",
            "Egg-Free", "Peanut-Free", "Soy-Free", "Fish-Free", "Shellfish-Free", "Pork-Free", "Red-Meat-Free",
            "Crustacean-Free", "Celery-Free", "Mustard-Free", "Lupine-Free", "Mollusk-Free", "Alcohol-Free",
            "No oil added", "Sulfite-Free", "Kosher"), 2245.9899999784943, 977.4773406229549, 558.7870712246705, 180,
        List.of("american"),
        List.of("snack"), List.of("desserts"),
        List.of("1-½ cup Cashews (soaked In Water For At Least 1 Hour, And Drained Fully)",
            "2 Tablespoons Reserved Cashew Water", "1 cup Dates", "1 Tablespoon Pure Vanilla Extract",
            "¼ teaspoons Sea Salt", "⅓ cups Hemp Hearts, Or Sesame Seeds")));

    private final RecipeContainer r3 = new RecipeContainer(new Recipe("Summer Sunshine Snack Truffles recipes",
        List.of("Low-Sodium"),
        List.of("Low Potassium", "Kidney-Friendly", "Vegetarian", "Pescatarian", "Mediterranean", "Gluten-Free",
            "Wheat-Free", "Egg-Free", "Peanut-Free", "Soy-Free", "Fish-Free", "Shellfish-Free", "Pork-Free",
            "Red-Meat-Free", "Crustacean-Free", "Celery-Free", "Mustard-Free", "Sesame-Free", "Lupine-Free",
            "Mollusk-Free", "Alcohol-Free", "No oil added", "Kosher"), 1323.9550000000002, 648.3858333512501, 404, 20,
        List.of("american"),
        List.of("brunch", "snack"), List.of("starter"),
        List.of("¼ cup So Delicious Cultured Vanilla Coconut Milk (aka dairy-free yogurt!)", "¼ cup chopped carrot",
            "¼ cup toasted almonds", "¼ cup sunflower seeds", "¼ cup chopped dried apricots",
            "¼ cup coconut flakes (preferably unsweetened)", "4 chopped Medjool dates, pitted",
            "¼ cup chopped candied or crystallized ginger", "¼ cup dried cranberries")));

    private final RecipeContainer r4 = new RecipeContainer(new Recipe("Cold Brigadeiros",
        List.of(),
        List.of("Vegetarian", "Pescatarian", "Gluten-Free", "Wheat-Free", "Egg-Free", "Peanut-Free", "Tree-Nut-Free",
            "Soy-Free", "Fish-Free", "Shellfish-Free", "Pork-Free", "Red-Meat-Free", "Crustacean-Free", "Celery-Free",
            "Mustard-Free", "Sesame-Free", "Lupine-Free", "Mollusk-Free", "Alcohol-Free", "No oil added",
            "Sulfite-Free", "Kosher"), 3902.06989774375, 12104.316023612224, 1642.922818125, 30,
        List.of("south american"), List.of("snack"),
        List.of("desserts"),
        List.of("28 ounces, fluid Sweetened, Condensed Milk", "19 ounces, fluid Milk", "5 Tablespoons Cocoa Powder",
            "10 ounces, fluid Heavy Cream")));

    private final RecipeContainer r5 = new RecipeContainer(new Recipe("Cocoa-Date Truffles",
        List.of("Low-Sodium"),
        List.of("Kidney-Friendly", "Vegan", "Vegetarian", "Pescatarian", "Dairy-Free", "Gluten-Free", "Wheat-Free",
            "Egg-Free", "Peanut-Free", "Soy-Free", "Fish-Free", "Shellfish-Free", "Pork-Free", "Red-Meat-Free",
            "Crustacean-Free", "Celery-Free", "Mustard-Free", "Lupine-Free", "Mollusk-Free", "Alcohol-Free",
            "No oil added", "Sulfite-Free", "Kosher"), 2369.5249999976513, 1651.2742151760121, 589.6867187502756, 0,
        List.of("american"),
        List.of("snack"), List.of("desserts"),
        List.of("3 tablespoons raw cacao powder", "1 1/2 cups Medjool dates, pitted",
            "3 tablespoons (or more) unsweetened shredded coconut or quick-cooking oats", "Pinch of sea salt",
            "1 tablespoon unsweetened shredded coconut", "1 teaspoon finely grated orange zest",
            "1 teaspoon instant espresso powder", "1/2 cup unsweetened shredded coconut",
            "1/4 cup lightly toasted sesame seeds", "1/2 cup crushed lightly toasted pistachios",
            "1/2 cup crushed lightly toasted hazelnuts",
            "Ingredient info: Raw cacao powder and unsweetened shredded coconut can be found at natural foods stores, specialty foods stores, and some supermarkets.")));

    @Test
    void testGetRecipesWith() throws InvalidRequestException, URISyntaxException {
        Collection<Recipe> expected = List.of(r1.recipe(), r2.recipe(), r3.recipe(), r4.recipe(), r5.recipe());
        Collection<Recipe> actual = RecipeClient.builder().setKeywords("truffle").setMealTypes(MealType.SNACK)
            .setHealthTypes(HealthType.EGG_FREE, HealthType.FISH_FREE, HealthType.MUSTARD_FREE,
                HealthType.NO_OIL_ADDED,
                HealthType.RED_MEAT_FREE, HealthType.SHELLFISH_FREE, HealthType.VEGETARIAN).build().getRecipes();
        assertIterableEquals(expected, actual,
            "Expected size of the collection is: " + expected.size() + ", but was: " + actual.size());
    }
}

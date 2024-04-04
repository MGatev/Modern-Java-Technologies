package bg.sofia.uni.fmi.mjt.cooking;

import bg.sofia.uni.fmi.mjt.cooking.api.RecipeHttpClient;
import bg.sofia.uni.fmi.mjt.cooking.dto.Recipe;
import bg.sofia.uni.fmi.mjt.cooking.dto.RecipeContainer;
import bg.sofia.uni.fmi.mjt.cooking.exception.InvalidRequestException;
import bg.sofia.uni.fmi.mjt.cooking.request.HealthType;
import bg.sofia.uni.fmi.mjt.cooking.request.MealType;
import bg.sofia.uni.fmi.mjt.cooking.request.RecipeRequest;
import bg.sofia.uni.fmi.mjt.cooking.request.Request;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class RecipeClient implements RecipeClientAPI {
    private final Request request;

    public Collection<Recipe> getRecipes() throws URISyntaxException, InvalidRequestException {
        return RecipeHttpClient.getRecipesFromResponse(request).hits()
            .stream()
            .map(RecipeContainer::recipe)
            .toList();
    }

    public static RecipeRequestBuilder builder() {
        return new RecipeRequestBuilder();
    }

    private RecipeClient(RecipeRequestBuilder builder) {
        request = new RecipeRequest(builder.keywords, builder.mealTypes, builder.healthTypes);
    }

    public static class RecipeRequestBuilder {
        private List<String> keywords;
        private Set<MealType> mealTypes;
        private Set<HealthType> healthTypes;

        public RecipeRequestBuilder setKeywords(String... keywords) {
            this.keywords = new ArrayList<>();
            this.keywords.addAll(Arrays.asList(keywords));
            return this;
        }

        public RecipeRequestBuilder setMealTypes(MealType... mealTypes) {
            MealType[] rest = Arrays.copyOfRange(mealTypes, 1, mealTypes.length);
            this.mealTypes = EnumSet.of(mealTypes[0], rest);
            return this;
        }

        public RecipeRequestBuilder setHealthTypes(HealthType... healthTypes) {
            HealthType[] rest = Arrays.copyOfRange(healthTypes, 1, healthTypes.length);
            this.healthTypes = EnumSet.of(healthTypes[0], rest);
            return this;
        }

        public RecipeClient build() {
            return new RecipeClient(this);
        }
    }
}

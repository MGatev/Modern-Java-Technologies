package bg.sofia.uni.fmi.mjt.cooking;

import bg.sofia.uni.fmi.mjt.cooking.dto.Recipe;
import bg.sofia.uni.fmi.mjt.cooking.exception.InvalidRequestException;

import java.net.URISyntaxException;
import java.util.Collection;

public interface RecipeClientAPI {

    /**
     * Returns a collection of the desired recipes
     *
     * @throws InvalidRequestException if the request for the recipes is invalid
     */
    Collection<Recipe> getRecipes() throws URISyntaxException, InvalidRequestException;
}

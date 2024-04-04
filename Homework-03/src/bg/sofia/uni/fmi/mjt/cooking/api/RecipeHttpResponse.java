package bg.sofia.uni.fmi.mjt.cooking.api;

import bg.sofia.uni.fmi.mjt.cooking.api.util.Iterator;
import bg.sofia.uni.fmi.mjt.cooking.error.RecipeError;

import bg.sofia.uni.fmi.mjt.cooking.dto.RecipeCollection;

import java.net.URI;

public class RecipeHttpResponse {
    private final RecipeError recipeError;
    private final RecipeCollection recipes;

    public RecipeError getRecipeError() {
        return recipeError;
    }

    public RecipeCollection getRecipes() {
        return recipes;
    }

    public static RecipeHttpResponseBuilder builder() {
        return new RecipeHttpResponseBuilder();
    }

    private RecipeHttpResponse(RecipeHttpResponseBuilder builder) {
        recipeError = builder.recipeError;
        recipes = builder.recipes;
    }

    public Iterator<URI> getIterator() {
        if (recipes._links().next() != null) {
            return new ResponseIterator(URI.create(recipes._links().next().href()));
        }

        return null;
    }

    public static class RecipeHttpResponseBuilder {
        private RecipeError recipeError;
        private RecipeCollection recipes;

        public RecipeHttpResponseBuilder setRecipeError(RecipeError recipeError) {
            this.recipeError = recipeError;
            return this;
        }

        public RecipeHttpResponseBuilder setRecipes(RecipeCollection recipes) {
            this.recipes = recipes;
            return this;
        }

        public RecipeHttpResponse build() {
            return new RecipeHttpResponse(this);
        }
    }

    private static class ResponseIterator implements Iterator<URI> {
        private static final int INCLUDING_PAGES = 4;
        private URI nextPageUri;
        private int pagesCount = 2;

        private ResponseIterator(URI nextPageUri) {
            this.nextPageUri = nextPageUri;
        }

        @Override
        public boolean hasNext() {
            return nextPageUri != null && pagesCount <= INCLUDING_PAGES;
        }

        @Override
        public URI next() {
            return nextPageUri;
        }

        @Override
        public void increment(URI newValue) {
            pagesCount++;
            nextPageUri = newValue;
        }
    }
}

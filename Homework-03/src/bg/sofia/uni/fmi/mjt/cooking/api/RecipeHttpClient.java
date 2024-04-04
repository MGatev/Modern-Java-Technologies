package bg.sofia.uni.fmi.mjt.cooking.api;

import bg.sofia.uni.fmi.mjt.cooking.dto.RecipeCollection;
import bg.sofia.uni.fmi.mjt.cooking.error.RecipeError;
import bg.sofia.uni.fmi.mjt.cooking.exception.InvalidRequestException;
import bg.sofia.uni.fmi.mjt.cooking.request.RecipeRequest;
import bg.sofia.uni.fmi.mjt.cooking.request.Request;
import com.google.gson.Gson;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.net.http.HttpResponse.BodyHandlers;

public class RecipeHttpClient {
    private static final String APP_ID = "465fcbe2";
    private static final String APP_KEY = "cdbc82c0a149a2c2f8d58a7dcd6828e7";
    private static final int OK_STATUS_CODE = 200;

    private static RecipeHttpResponse getResponseFromAPI(Request request) throws URISyntaxException {
        try (ExecutorService executor = Executors.newCachedThreadPool()) {
            HttpClient client =
                HttpClient.newBuilder().executor(executor).build();

            HttpRequest httpRequest = HttpRequest.newBuilder().uri(request.getUri())
                .setHeader(APP_ID, APP_KEY).build();

            Gson gson = new Gson();

            CompletableFuture<RecipeHttpResponse> future = client.sendAsync(httpRequest, BodyHandlers.ofString())
                .thenApply(response -> {
                    RecipeHttpResponse recipeResponse;
                    if (response.statusCode() != OK_STATUS_CODE) {
                        RecipeError recipeError = gson.fromJson(response.body(), RecipeError.class);
                        recipeResponse = RecipeHttpResponse.builder().setRecipeError(recipeError).build();
                    } else {
                        recipeResponse = RecipeHttpResponse.builder()
                            .setRecipes(gson.fromJson(response.body(), RecipeCollection.class)).build();
                    }
                    return recipeResponse;
                });

            return future.join();
        }
    }

    public static RecipeCollection getRecipesFromResponse(Request recipeRequest)
        throws URISyntaxException, InvalidRequestException {

        RecipeHttpResponse response = getResponseFromAPI(recipeRequest);

        if (response.getRecipeError() != null) {
            throw new InvalidRequestException(response.getRecipeError().message());
        }

        var iterator = response.getIterator();

        if (iterator != null) {
            while (iterator.hasNext()) {
                RecipeRequest nextRequest = new RecipeRequest(iterator.next());
                RecipeHttpResponse nextResponse = getResponseFromAPI(nextRequest);
                response.getRecipes().hits().addAll(nextResponse.getRecipes().hits());
                iterator.increment(URI.create(nextResponse.getRecipes()._links().next().href()));
            }
        }

        return response.getRecipes();
    }

    private RecipeHttpClient() {
    }
}

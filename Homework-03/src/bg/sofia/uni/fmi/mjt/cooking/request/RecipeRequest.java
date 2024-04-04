package bg.sofia.uni.fmi.mjt.cooking.request;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

public class RecipeRequest implements Request {

    private static final String URI_SCHEME = "https://";
    private static final String URI_HOST = "api.edamam.com/";
    private static final String URI_PATH = "api/recipes/v2";
    private static final String SYMBOL_BEFORE_PARAMETERS = "?";
    private static final String PARAMETERS_SEPARATOR = "&";
    private static final String TYPE = "type=public";
    private static final String APP_ID = "app_id=465fcbe2";
    private static final String APP_KEY = "app_key=cdbc82c0a149a2c2f8d58a7dcd6828e7";
    private static final String KEY_WORDS_SYMBOL = "q=";
    private static final String KEY_WORDS_SEPARATOR = "%2C%20";
    private static final String HEALTH_PARAMETER = "health=";
    private static final String MEAL_TYPE_PARAMETER = "mealType=";
    private final List<String> keywords;
    private final Set<MealType> mealTypes;
    private final Set<HealthType> healthTypes;
    private URI uri;

    public RecipeRequest(List<String> keywords, Set<MealType> mealTypes, Set<HealthType> healthTypes) {
        this.keywords = keywords;
        this.mealTypes = mealTypes;
        this.healthTypes = healthTypes;
    }

    public RecipeRequest(URI uri) {
        this.uri = uri;
        keywords = null;
        mealTypes = null;
        healthTypes = null;
    }

    public URI getUri() throws URISyntaxException {

        if (uri == null) {
            buildURI();
        }
        return uri;
    }

    private void buildURI() throws URISyntaxException {
        StringBuilder uri = new StringBuilder(URI_SCHEME);

        uri.append(URI_HOST).append(URI_PATH).append(SYMBOL_BEFORE_PARAMETERS).append(TYPE)
            .append(PARAMETERS_SEPARATOR);

        if (keywords != null) {
            uri.append(KEY_WORDS_SYMBOL).append(keywords.getFirst());
            for (int i = 1; i < keywords.size(); i++) {
                uri.append(KEY_WORDS_SEPARATOR);
                uri.append(keywords.get(i));
            }
            uri.append(PARAMETERS_SEPARATOR);
        }

        uri.append(APP_ID).append(PARAMETERS_SEPARATOR).append(APP_KEY);

        if (healthTypes != null) {
            for (HealthType health : healthTypes) {
                uri.append(PARAMETERS_SEPARATOR).append(HEALTH_PARAMETER).append(health.getString());
            }
        }
        if (mealTypes != null) {
            for (MealType mealType : mealTypes) {
                uri.append(PARAMETERS_SEPARATOR).append(MEAL_TYPE_PARAMETER).append(mealType.getString());
            }
        }

        this.uri = new URI(uri.toString());
    }
}

package bg.sofia.uni.fmi.mjt.space.rocket;

import java.util.Optional;

public class RocketParser {

    private static final int TOKEN_WIKI = 2;
    private static final int TOKEN_HEIGHT = 3;

    //All methods are package-private on purpose, because they are
    //specific and are used only in this package
    static String parseHeightString(String height) {
        return (height.substring(0, height.length() - 2));
    }

    static Optional<Double> parseHeightToOptional(String[] lineTokens) {
        if (TOKEN_HEIGHT >= lineTokens.length) {
            return Optional.empty();
        }

        return Optional.of(Double.parseDouble(parseHeightString(lineTokens[TOKEN_HEIGHT])));
    }

    static Optional<String> parseWikiToOptional(String[] lineTokens) {
        if (TOKEN_WIKI == lineTokens.length) {
            return Optional.empty();
        }

        return Optional.of(lineTokens[TOKEN_WIKI]);
    }

    static String parseName(String name) {
        if (name.toCharArray()[0] == '"') {
            return name.substring(1, name.length() - 1);
        }
        return name;
    }
}

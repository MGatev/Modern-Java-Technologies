package bg.sofia.uni.fmi.mjt.space.rocket;

import java.util.Optional;

import static bg.sofia.uni.fmi.mjt.space.rocket.RocketParser.parseHeightToOptional;
import static bg.sofia.uni.fmi.mjt.space.rocket.RocketParser.parseName;
import static bg.sofia.uni.fmi.mjt.space.rocket.RocketParser.parseWikiToOptional;

public record Rocket(String id, String name, Optional<String> wiki, Optional<Double> height) {

    private static final String REGEX = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
    private static final int TOKEN_ID = 0;
    private static final int TOKEN_NAME = 1;

    public static Rocket of(String line) {
        String[] tokens = line.split(REGEX);

        String id = tokens[TOKEN_ID];
        String name = parseName(tokens[TOKEN_NAME]);
        Optional<String> wiki = parseWikiToOptional(tokens);
        Optional<Double> height = parseHeightToOptional(tokens);

        return new Rocket(id, name, wiki, height);
    }
}

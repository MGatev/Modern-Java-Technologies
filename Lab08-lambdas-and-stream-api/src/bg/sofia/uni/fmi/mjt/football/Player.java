package bg.sofia.uni.fmi.mjt.football;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record Player(String name, String fullName, LocalDate birthDate, int age, double heightCm,
                     double weightKg, List<Position> positions, String nationality, int overallRating,
                     int potential, long valueEuro, long wageEuro, Foot preferredFoot) {
    private static final int TOKEN_NAME = 0;
    private static final int TOKEN_FULL_NAME = 1;
    private static final int TOKEN_BDAY = 2;
    private static final int TOKEN_AGE = 3;
    private static final int TOKEN_HEIGHT = 4;
    private static final int TOKEN_WEIGHT = 5;
    private static final int TOKEN_POSITIONS = 6;
    private static final int TOKEN_NATIONALITY = 7;
    private static final int TOKEN_RATING = 8;
    private static final int TOKEN_POTENTIAL = 9;
    private static final int TOKEN_VALUE = 10;
    private static final int TOKEN_WAGE = 11;
    private static final int TOKEN_FOOT = 12;

    public static Player of(String line) {
        String[] tokens = line.split(";");
        String[] date = tokens[TOKEN_BDAY].split("/");
        String[] positionsString = tokens[TOKEN_POSITIONS].split(",");
        List<Position> positions = new ArrayList<>(Arrays.stream(positionsString)
            .map(Position::valueOf)
            .toList());

        return new Player(tokens[TOKEN_NAME], tokens[TOKEN_FULL_NAME],
            LocalDate.of(Integer.parseInt(date[2]), Integer.parseInt(date[0]), Integer.parseInt(date[1])),
            Integer.parseInt(tokens[TOKEN_AGE]), Double.parseDouble(tokens[TOKEN_HEIGHT]),
            Double.parseDouble(tokens[TOKEN_WEIGHT]), positions, tokens[TOKEN_NATIONALITY],
            Integer.parseInt(tokens[TOKEN_RATING]), Integer.parseInt(tokens[TOKEN_POTENTIAL]),
            Long.parseLong(tokens[TOKEN_VALUE]), Long.parseLong(tokens[TOKEN_WAGE]),
            Foot.valueOf(tokens[TOKEN_FOOT].toUpperCase()));
    }
}

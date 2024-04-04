package bg.sofia.uni.fmi.mjt.space.mission;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public class MissionParser {

    private static final String FORMAT_PATTERN = "EEE MMM dd, yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
        .ofPattern(FORMAT_PATTERN).withLocale(Locale.getDefault());

    //All methods except one are package-private on purpose, because they are
    //specific and are used only in this package
    static LocalDate parseDate(String date) {
        String strippedDate = date.substring(1, date.length() - 1);
        return LocalDate.parse(strippedDate, DATE_FORMATTER);
    }

    static Detail parseDetail(String detail) {
        if (detail.toCharArray()[0] == '"') {
            detail = detail.substring(1, detail.length() - 1);
        }

        String[] tokens = detail.split("\\|");
        return new Detail(tokens[0].substring(0, tokens[0].length() - 1), tokens[1].substring(1));
    }

    static String parseLocation(String location) {
        return location.substring(1, location.length() - 1);
    }

    static String parseRocketStatus(String rocketStatus) {
        String[] rocketStatusTokens = rocketStatus.split("(?=[A-Z])");
        return (rocketStatusTokens[0] + "_" + rocketStatusTokens[1]).toUpperCase();
    }

    private static String parseCostString(String cost) {
        return (cost.substring(1, cost.length() - 2)).replace(",", "");
    }

    static String parseMissionStatus(String missionStatus) {
        String[] missionStatusTokens = missionStatus.split(" ");
        if (missionStatusTokens.length == 1) {
            return missionStatusTokens[0].toUpperCase();
        } else {
            return (missionStatusTokens[0] + "_" + missionStatusTokens[1]).toUpperCase();
        }
    }

    static Optional<Double> parseCostToOptional(String cost) {
        if (cost.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(Double.parseDouble(parseCostString(cost)));
        }
    }
}

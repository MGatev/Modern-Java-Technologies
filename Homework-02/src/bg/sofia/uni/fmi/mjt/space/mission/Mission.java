package bg.sofia.uni.fmi.mjt.space.mission;

import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;

import java.time.LocalDate;
import java.util.Optional;

import static bg.sofia.uni.fmi.mjt.space.mission.MissionParser.parseLocation;
import static bg.sofia.uni.fmi.mjt.space.mission.MissionParser.parseCostToOptional;
import static bg.sofia.uni.fmi.mjt.space.mission.MissionParser.parseDate;
import static bg.sofia.uni.fmi.mjt.space.mission.MissionParser.parseDetail;
import static bg.sofia.uni.fmi.mjt.space.mission.MissionParser.parseMissionStatus;
import static bg.sofia.uni.fmi.mjt.space.mission.MissionParser.parseRocketStatus;

public record Mission(String id, String company, String location, LocalDate date, Detail detail,
                      RocketStatus rocketStatus, Optional<Double> cost, MissionStatus missionStatus) {

    private static final String REGEX = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
    private static final int TOKEN_ID = 0;
    private static final int TOKEN_COMPANY = 1;
    private static final int TOKEN_LOCATION = 2;
    private static final int TOKEN_DATE = 3;
    private static final int TOKEN_DETAIL = 4;
    private static final int TOKEN_ROCKET_STATUS = 5;
    private static final int TOKEN_COST = 6;
    private static final int TOKEN_MISSION_STATUS = 7;

    public static Mission of(String line) {
        String[] tokens = line.split(REGEX);

        String id = tokens[TOKEN_ID];
        String company = tokens[TOKEN_COMPANY];
        String location = parseLocation(tokens[TOKEN_LOCATION]);
        LocalDate date = parseDate(tokens[TOKEN_DATE]);
        Detail detail = parseDetail(tokens[TOKEN_DETAIL]);
        RocketStatus rocketStatus = RocketStatus.valueOf(parseRocketStatus(tokens[TOKEN_ROCKET_STATUS]));
        Optional<Double> cost = parseCostToOptional(tokens[TOKEN_COST]);
        MissionStatus missionStatus = MissionStatus.valueOf(parseMissionStatus(tokens[TOKEN_MISSION_STATUS]));

        return new Mission(id, company, location, date, detail, rocketStatus, cost, missionStatus);
    }
}

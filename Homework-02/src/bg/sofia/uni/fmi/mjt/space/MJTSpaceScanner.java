package bg.sofia.uni.fmi.mjt.space;

import bg.sofia.uni.fmi.mjt.space.algorithm.Rijndael;
import bg.sofia.uni.fmi.mjt.space.algorithm.SymmetricBlockCipher;
import bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import bg.sofia.uni.fmi.mjt.space.exception.TimeFrameMismatchException;
import bg.sofia.uni.fmi.mjt.space.mission.Detail;
import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import bg.sofia.uni.fmi.mjt.space.rocket.Rocket;
import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MJTSpaceScanner implements SpaceScannerAPI {

    private final Collection<Mission> missions;
    private final Collection<Rocket> rockets;
    private final SecretKey secretKey;

    public MJTSpaceScanner(Reader missionsReader, Reader rocketsReader, SecretKey secretKey) {
        try (var missionsInput = new BufferedReader(missionsReader);
             var rocketsInput = new BufferedReader(rocketsReader)) {

            missions = missionsInput.lines()//
                .skip(1)//
                .map(Mission::of)//
                .toList();

            rockets = rocketsInput.lines()
                .skip(1)
                .map(Rocket::of)
                .toList();

        } catch (IOException ex) {
            throw new IllegalArgumentException("Could not load dataset", ex);
        }

        this.secretKey = secretKey;
    }

    @Override
    public Collection<Mission> getAllMissions() {
        return missions;
    }

    @Override
    public Collection<Mission> getAllMissions(MissionStatus missionStatus) {
        if (missionStatus == null) {
            throw new IllegalArgumentException("Mission status cannot be null!");
        }

        return missions.stream()
            .filter(m -> m.missionStatus().equals(missionStatus))
            .collect(Collectors.toList());
    }

    @Override
    public String getCompanyWithMostSuccessfulMissions(LocalDate from, LocalDate to) {
        if (from == null) {
            throw new IllegalArgumentException("The start time cannot be null!");
        }
        if (to == null) {
            throw new IllegalArgumentException("The end time cannot be null!");
        }

        if (to.isBefore(from)) {
            throw new TimeFrameMismatchException("The end time cannot be before the start time!");
        }

        return missions.stream()
            .filter(mission -> (mission.date().isBefore(to) && mission.date().isAfter(from)) ||
                mission.date().isEqual(to) || mission.date().isEqual(from))
            .filter(mission -> mission.missionStatus().equals(MissionStatus.SUCCESS))
            .collect(Collectors.groupingBy(Mission::company, Collectors.counting()))
            .entrySet()
            .stream()
            .max(Map.Entry.comparingByValue())
            .orElse(Map.entry("", 0L))
            .getKey();
    }

    @Override
    public Map<String, Collection<Mission>> getMissionsPerCountry() {
        return missions.stream()
            .flatMap(this::mapCountryToMissionEntry)
            .collect(Collectors.toUnmodifiableMap(SimpleEntry::getKey, this::newCollectionFromMissionEntry,
                this::mergeCollections));
    }

    private String getCountryOfAMission(Mission mission) {
        String[] locationTokens = mission.location().split(",");
        return locationTokens[locationTokens.length - 1].substring(1);
    }

    private Stream<SimpleEntry<String, Mission>> mapCountryToMissionEntry(Mission mission) {
        return missions.stream()
            .map(this::getCountryOfAMission)
            .collect(Collectors.toSet())
            .stream()
            .filter(country -> mission.location().contains(country))
            .map(country -> new SimpleEntry<>(country, mission));
    }

    private Collection<Mission> newCollectionFromMissionEntry(SimpleEntry<String, Mission> missionEntry) {
        Set<Mission> missionSet = new HashSet<>();
        missionSet.add(missionEntry.getValue());

        return missionSet;
    }

    private Collection<Mission> mergeCollections(Collection<Mission> col1, Collection<Mission> col2) {
        col1.addAll(col2);

        return col1;
    }

    @Override
    public List<Mission> getTopNLeastExpensiveMissions(int n, MissionStatus missionStatus, RocketStatus rocketStatus) {
        if (n <= 0) {
            throw new IllegalArgumentException("N cannot be 0 or negative number!");
        }
        if (missionStatus == null) {
            throw new IllegalArgumentException("Mission Status is invalid!");
        }
        if (rocketStatus == null) {
            throw new IllegalArgumentException("Rocket Status is invalid!");
        }

        return missions.stream()
            .filter(mission -> mission.missionStatus().equals(missionStatus) &&
                mission.rocketStatus().equals(rocketStatus) &&
                mission.cost().isPresent())
            .sorted(Comparator.comparing(mission -> mission.cost().get()))
            .limit(n)
            .toList();
    }

    @Override
    public Map<String, String> getMostDesiredLocationForMissionsPerCompany() {
        Collection<String> companies = getAllCompanies();
        Map<String, String> result = new HashMap<>();
        for (String company : companies) {
            Map<String, Long> companyMostDesiredLocation = missions.stream()
                .filter(mission -> mission.company().equals(company))
                .collect(Collectors.groupingBy(Mission::location, Collectors.counting()));

            result.put(company, Collections.max(companyMostDesiredLocation.entrySet(),
                Map.Entry.comparingByValue()).getKey());
        }
        return result;
    }

    private Collection<String> getAllCompanies() {
        return missions.stream()
            .map(Mission::company)
            .collect(Collectors.toSet());
    }

    @Override
    public Map<String, String> getLocationWithMostSuccessfulMissionsPerCompany(LocalDate from, LocalDate to) {
        if (from == null) {
            throw new IllegalArgumentException("The start time cannot be null!");
        }
        if (to == null) {
            throw new IllegalArgumentException("The end time cannot be null!");
        }

        if (to.isBefore(from)) {
            throw new TimeFrameMismatchException("The end time cannot be before the start time!");
        }

        Collection<String> companies = getAllCompanies();
        Map<String, String> result = new HashMap<>();
        for (String company : companies) {
            Map<String, Long> companyMostDesiredLocation = missions.stream()
                .filter(mission -> mission.company().equals(company))
                .filter(mission -> mission.missionStatus().equals(MissionStatus.SUCCESS))
                .filter(mission -> (mission.date().isBefore(to) && mission.date().isAfter(from)) ||
                    mission.date().isEqual(to) || mission.date().isEqual(from))
                .collect(Collectors.groupingBy(Mission::location, Collectors.counting()));

            if (!companyMostDesiredLocation.isEmpty()) {
                result.put(company, Collections.max(companyMostDesiredLocation.entrySet(),
                    Map.Entry.comparingByValue()).getKey());
            }
        }
        return result;
    }

    @Override
    public Collection<Rocket> getAllRockets() {
        return rockets;
    }

    @Override
    public List<Rocket> getTopNTallestRockets(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N cannot be 0 or negative number!");
        }

        return rockets.stream()
            .filter(rocket -> rocket.height().isPresent())
            .sorted(Comparator.comparing(rocket -> rocket.height().get(), Comparator.reverseOrder()))
            .limit(n)
            .toList();
    }

    @Override
    public Map<String, Optional<String>> getWikiPageForRocket() {
        return rockets.stream()
            .collect(Collectors.toUnmodifiableMap(Rocket::name, Rocket::wiki));
    }

    @Override
    public List<String> getWikiPagesForRocketsUsedInMostExpensiveMissions(int n, MissionStatus missionStatus,
                                                                          RocketStatus rocketStatus) {
        if (n <= 0) {
            throw new IllegalArgumentException("N cannot be 0 or negative number!");
        }
        if (missionStatus == null) {
            throw new IllegalArgumentException("Mission Status is invalid!");
        }
        if (rocketStatus == null) {
            throw new IllegalArgumentException("Rocket Status is invalid!");
        }

        List<String> rocketNames = missions.stream()
            .filter(mission -> mission.missionStatus().equals(missionStatus))
            .filter(mission -> mission.rocketStatus().equals(rocketStatus))
            .filter(mission -> mission.cost().isPresent())
            .sorted(Comparator.comparing(mission -> mission.cost().get(), Comparator.reverseOrder()))
            .limit(n)
            .map(Mission::detail)
            .map(Detail::rocketName)
            .toList();

        Map<String, Optional<String>> wikiPagesForAllRockets = getWikiPageForRocket();

        return rocketNames.stream()
            .map(wikiPagesForAllRockets::get)
            .map(rocketName -> rocketName.orElse(""))
            .filter(rocketName -> !rocketName.isEmpty())
            .toList();
    }

    @Override
    public void saveMostReliableRocket(OutputStream outputStream, LocalDate from, LocalDate to) throws CipherException {
        if (from == null) {
            throw new IllegalArgumentException("The start time cannot be null!");
        }
        if (to == null) {
            throw new IllegalArgumentException("The end time cannot be null!");
        }
        if (to.isBefore(from)) {
            throw new TimeFrameMismatchException("The end time cannot be before the start time!");
        }
        if (outputStream == null) {
            throw new IllegalArgumentException("The output cannot be null!");
        }

        SymmetricBlockCipher cipher = new Rijndael(secretKey);

        Collection<String> rocketNames = getRocketNamesInTimeInterval(from, to);

        String mostReliableRocketName = rocketNames.stream()
            .max((rocket1, rocket2) -> Double.compare(calculateReliability(rocket1), calculateReliability(rocket2)))
            .orElse("");

        try (var inputRocketNameStream = new ByteArrayInputStream(mostReliableRocketName.getBytes())) {
            cipher.encrypt(inputRocketNameStream, outputStream);
        } catch (IOException e) {
            throw new UncheckedIOException("A problem occurred with the input", e);
        }
    }

    private Collection<String> getRocketNamesInTimeInterval(LocalDate from, LocalDate to) {
        return missions.stream()
            .filter(mission -> (mission.date().isBefore(to) && mission.date().isAfter(from)) ||
                mission.date().isEqual(to) || mission.date().isEqual(from))
            .map(Mission::detail)
            .map(Detail::rocketName)
            .toList();
    }

    private double calculateReliability(String rocket) {
        double rocketSuccessfulMissions = (double) getRocketSuccessfulMissionsCount(rocket);
        double rocketNotSuccessfulMissions = (double) getRocketNotSuccessfulMissionsCount(rocket);
        double allRocketMissions = rocketSuccessfulMissions + rocketNotSuccessfulMissions;
        return (((2 * rocketSuccessfulMissions) + rocketNotSuccessfulMissions) / (2 * allRocketMissions));
    }

    private long getRocketSuccessfulMissionsCount(String rocket) {
        return missions.stream()
            .filter(mission -> mission.detail().rocketName().equals(rocket))
            .filter(mission -> mission.missionStatus().equals(MissionStatus.SUCCESS))
            .count();
    }

    private long getRocketNotSuccessfulMissionsCount(String rocket) {
        return missions.stream()
            .filter(mission -> mission.detail().rocketName().equals(rocket))
            .filter(mission -> !mission.missionStatus().equals(MissionStatus.SUCCESS))
            .count();
    }
}

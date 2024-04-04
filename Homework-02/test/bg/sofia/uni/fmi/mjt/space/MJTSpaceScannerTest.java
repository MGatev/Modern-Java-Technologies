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
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MJTSpaceScannerTest {
    private final Reader missionReader = new StringReader("""
        Empty Line
        241,SpaceX,"SLC-4E, Vandenberg AFB, California, USA","Tue May 22, 2018",Falcon 9 Block 4 | Iridium-6 & GRACE-FO,StatusRetired,"62.0 ",Success
        242,Northrop,"LP-0A, Wallops Flight Facility, Virginia, USA","Mon May 21, 2018",Antares 230 | CRS OA-9E,StatusRetired,"85.0 ",Success
        243,CASC,"LC-3, Xichang Satellite Launch Center, China","Sun May 20, 2018","Long March 4C | Queqiao, Longjiang 1 & 2",StatusActive,"64.68 ",Success
        244,SpaceX,"LC-39A, Kennedy Space Center, Florida, USA","Fri May 11, 2018",Falcon 9 Block 5 | Bangabandhu-1,StatusActive,"50.0 ",Success
        202,Landspace,"Site 95, Jiuquan Satellite Launch Center, China","Sat Oct 27, 2018",ZhuQue-1 | CCTV Future-1,StatusRetired,,Failure
        246,ULA,"SLC-3E, Vandenberg AFB, California, USA","Sat May 05, 2018",Atlas V 401 | InSight,StatusActive,"109.0 ",Success
        263,Roscosmos,"Site 1/5, Baikonur Cosmodrome, Kazakhstan","Wed Mar 21, 2018",Soyuz FG | Soyuz MS-08 (54S),StatusRetired,,Success
        """);

    private final Reader rocketsReader = new StringReader("""
        Empty Line
        147,Delta IV Medium,https://en.wikipedia.org/wiki/Delta_(rocket_family)#Delta_M,62.5 m
        148,"Delta IV Medium+ (4,2)",https://en.wikipedia.org/wiki/Delta_IV,62.5 m
        149,"Delta IV Medium+ (5,2)",https://en.wikipedia.org/wiki/Delta_IV,66.4 m
        150,"Delta IV Medium+ (5,4)",https://en.wikipedia.org/wiki/Delta_IV,66.4 m
        153,Diamant BP4,https://en.wikipedia.org/wiki/Diamant,21.64 m
        194,Jielong-1,,
        154,Dnepr,https://en.wikipedia.org/wiki/Dnepr_(rocket),34.3 m
        156,Electron/Curie,https://en.wikipedia.org/wiki/Electron_(rocket),17.0 m
        157,Electron/Photon,https://en.wikipedia.org/wiki/Electron_(rocket),17.0 m
        162,Epsilon PBS,https://en.wikipedia.org/wiki/Epsilon_(rocket),26.0 m
        163,Epsilon S,https://en.wikipedia.org/wiki/Epsilon_(rocket),27.0 m
        164,Europa 1,https://en.wikipedia.org/wiki/Europa_(rocket),31.68 m
        165,Europa 2,https://en.wikipedia.org/wiki/Europa_(rocket),31.68 m
        166,Falcon 1,https://en.wikipedia.org/wiki/Falcon_1,22.25 m
        168,Falcon 9 Block 4,https://en.wikipedia.org/wiki/Falcon_9,70.0 m
        """);

    private final Mission m1 =
        new Mission("241", "SpaceX", "SLC-4E, Vandenberg AFB, California, USA", LocalDate.of(2018, Month.MAY, 22),
            new Detail("Falcon 9 Block 4", "Iridium-6 & GRACE-FO"), RocketStatus.STATUS_RETIRED,
            Optional.of(62.0), MissionStatus.SUCCESS);
    private final Mission m2 = new Mission("242", "Northrop", "LP-0A, Wallops Flight Facility, Virginia, USA",
        LocalDate.of(2018, Month.MAY, 21), new Detail("Antares 230", "CRS OA-9E"), RocketStatus.STATUS_RETIRED,
        Optional.of(85.0), MissionStatus.SUCCESS);
    private final Mission m3 = new Mission("243", "CASC", "LC-3, Xichang Satellite Launch Center, China",
        LocalDate.of(2018, Month.MAY, 20), new Detail("Long March 4C", "Queqiao, Longjiang 1 & 2"),
        RocketStatus.STATUS_ACTIVE,
        Optional.of(64.68), MissionStatus.SUCCESS);
    private final Mission m4 = new Mission("244", "SpaceX", "LC-39A, Kennedy Space Center, Florida, USA",
        LocalDate.of(2018, Month.MAY, 11), new Detail("Falcon 9 Block 5", "Bangabandhu-1"),
        RocketStatus.STATUS_ACTIVE,
        Optional.of(50.0), MissionStatus.SUCCESS);
    private final Mission m5 = new Mission("202", "Landspace", "Site 95, Jiuquan Satellite Launch Center, China",
        LocalDate.of(2018, Month.OCTOBER, 27), new Detail("ZhuQue-1", "CCTV Future-1"), RocketStatus.STATUS_RETIRED,
        Optional.empty(), MissionStatus.FAILURE);
    private final Mission m6 =
        new Mission("246", "ULA", "SLC-3E, Vandenberg AFB, California, USA", LocalDate.of(2018, Month.MAY, 5),
            new Detail("Atlas V 401", "InSight"), RocketStatus.STATUS_ACTIVE,
            Optional.of(109.0), MissionStatus.SUCCESS);
    private final Mission m7 = new Mission("263", "Roscosmos", "Site 1/5, Baikonur Cosmodrome, Kazakhstan",
        LocalDate.of(2018, Month.MARCH, 21), new Detail("Soyuz FG", "Soyuz MS-08 (54S)"),
        RocketStatus.STATUS_RETIRED,
        Optional.empty(), MissionStatus.SUCCESS);

    private final Rocket r1 =
        new Rocket("147", "Delta IV Medium", Optional.of("https://en.wikipedia.org/wiki/Delta_(rocket_family)#Delta_M"),
            Optional.of(62.5));
    private final Rocket r2 =
        new Rocket("148", "Delta IV Medium+ (4,2)", Optional.of("https://en.wikipedia.org/wiki/Delta_IV"),
            Optional.of(62.5));
    private final Rocket r3 =
        new Rocket("149", "Delta IV Medium+ (5,2)", Optional.of("https://en.wikipedia.org/wiki/Delta_IV"),
            Optional.of(66.4));
    private final Rocket r4 =
        new Rocket("150", "Delta IV Medium+ (5,4)", Optional.of("https://en.wikipedia.org/wiki/Delta_IV"),
            Optional.of(66.4));
    private final Rocket r5 =
        new Rocket("153", "Diamant BP4", Optional.of("https://en.wikipedia.org/wiki/Diamant"),
            Optional.of(21.64));
    private final Rocket r6 =
        new Rocket("194", "Jielong-1", Optional.empty(),
            Optional.empty());
    private final Rocket r7 =
        new Rocket("154", "Dnepr", Optional.of("https://en.wikipedia.org/wiki/Dnepr_(rocket)"),
            Optional.of(34.3));
    private final Rocket r8 =
        new Rocket("156", "Electron/Curie", Optional.of("https://en.wikipedia.org/wiki/Electron_(rocket)"),
            Optional.of(17.0));
    private final Rocket r9 =
        new Rocket("157", "Electron/Photon", Optional.of("https://en.wikipedia.org/wiki/Electron_(rocket)"),
            Optional.of(17.0));
    private final Rocket r10 =
        new Rocket("162", "Epsilon PBS", Optional.of("https://en.wikipedia.org/wiki/Epsilon_(rocket)"),
            Optional.of(26.0));
    private final Rocket r11 =
        new Rocket("163", "Epsilon S", Optional.of("https://en.wikipedia.org/wiki/Epsilon_(rocket)"),
            Optional.of(27.0));
    private final Rocket r12 =
        new Rocket("164", "Europa 1", Optional.of("https://en.wikipedia.org/wiki/Europa_(rocket)"),
            Optional.of(31.68));
    private final Rocket r13 =
        new Rocket("165", "Europa 2", Optional.of("https://en.wikipedia.org/wiki/Europa_(rocket)"),
            Optional.of(31.68));
    private final Rocket r14 =
        new Rocket("166", "Falcon 1", Optional.of("https://en.wikipedia.org/wiki/Falcon_1"),
            Optional.of(22.25));
    private final Rocket r15 =
        new Rocket("168", "Falcon 9 Block 4", Optional.of("https://en.wikipedia.org/wiki/Falcon_9"),
            Optional.of(70.0));

    private final SecretKey secretKey;

    {
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyGenerator.init(128);
        secretKey = keyGenerator.generateKey();
    }

    private final SpaceScannerAPI scanner = new MJTSpaceScanner(missionReader, rocketsReader, secretKey);

    private <K, V> boolean areMapsEqual(Map<K, V> map1, Map<K, V> map2) {
        if (map1.size() != map2.size()) {
            return false;
        }

        for (Map.Entry<K, V> entry : map1.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();

            if (!map2.containsKey(key) || !value.equals(map2.get(key))) {
                return false;
            }
        }

        return true;
    }

    private <T> boolean areCollectionsEqual(Collection<T> lhs, Collection<T> rhs) {
        if (lhs == null && rhs == null) {
            return true;
        }

        return lhs != null && rhs != null && lhs.size() == rhs.size() && lhs.containsAll(rhs) && rhs.containsAll(lhs);
    }

    private boolean areMapsWithCollectionValueEqual(Map<String, Collection<Mission>> map1,
                                                    Map<String, Collection<Mission>> map2) {
        if (map1.size() != map2.size()) {
            return false;
        }

        for (Map.Entry<String, Collection<Mission>> entry : map1.entrySet()) {
            String key = entry.getKey();
            Collection<Mission> value = entry.getValue();

            if (!map2.containsKey(key) || !areCollectionsEqual(value, map2.get(key))) {
                return false;
            }
        }

        return true;
    }

    @Test
    void testGetAllMissionsWithEmptyCollection() {
        SpaceScannerAPI tempScanner = new MJTSpaceScanner(new StringReader(""), new StringReader(""), secretKey);
        assertIterableEquals(Collections.EMPTY_LIST, tempScanner.getAllMissions(),
            "An empty list is expected, when there are no missions");
    }

    @Test
    void testGetAllMissionsWithValidCollection() {
        Collection<Mission> expectedList = List.of(m1, m2, m3, m4, m5, m6, m7);
        Collection<Mission> actualList = scanner.getAllMissions();
        assertTrue(areCollectionsEqual(expectedList, actualList),
            "Expected size was: " + expectedList.size() + ", but was: " + actualList.size());
    }

    @Test
    void testGetAllMissionsWithStatusThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> scanner.getAllMissions(null),
            "Expected IllegalArgumentException to be thrown when missionStatus is null");
    }

    @Test
    void testGetAllMissionsWithStatusSuccess() {
        Collection<Mission> expectedList = List.of(m1, m2, m3, m4, m6, m7);
        Collection<Mission> actualList = scanner.getAllMissions(MissionStatus.SUCCESS);
        assertTrue(areCollectionsEqual(expectedList, actualList),
            "Expected size was: " + expectedList.size() + ", but was: " + actualList.size());
    }

    @Test
    void testGetAllMissionsWithStatusFailure() {
        Collection<Mission> expectedList = List.of(m5);
        Collection<Mission> actualList = scanner.getAllMissions(MissionStatus.FAILURE);
        assertTrue(areCollectionsEqual(expectedList, actualList),
            "Expected size was: " + expectedList.size() + ", but was: " + actualList.size());
    }

    @Test
    void testGetAllMissionsWithStatusPreLaunchFailure() {
        Collection<Mission> actualList = scanner.getAllMissions(MissionStatus.PRELAUNCH_FAILURE);
        assertIterableEquals(Collections.EMPTY_LIST, actualList,
            "Expected size was: 0, but was: " + actualList.size());
    }

    @Test
    void testGetAllMissionsWithStatusPartialFailure() {
        assertIterableEquals(Collections.EMPTY_LIST, scanner.getAllMissions(MissionStatus.PARTIAL_FAILURE),
            "When there are not missions with the desired status, an empty list is expected");
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsWithNullFrom() {
        assertThrows(IllegalArgumentException.class,
            () -> scanner.getCompanyWithMostSuccessfulMissions(null, LocalDate.MAX),
            "Expected IllegalArgumentException to be thrown when from is null");
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsWithNullTo() {
        assertThrows(IllegalArgumentException.class,
            () -> scanner.getCompanyWithMostSuccessfulMissions(LocalDate.MIN, null),
            "Expected IllegalArgumentException to be thrown when to is null");
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsWithSwitchedFromAndTo() {
        assertThrows(TimeFrameMismatchException.class,
            () -> scanner.getCompanyWithMostSuccessfulMissions(LocalDate.MAX, LocalDate.MIN),
            "Expected TimeFrameMismatchException to be thrown when to is before from");
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissions() {
        String actual = scanner.getCompanyWithMostSuccessfulMissions(LocalDate.of(2018, Month.MAY, 11),
            LocalDate.of(2018, Month.MAY, 22));
        assertEquals("SpaceX", actual, "Expected: SpaceX, but was:" + actual);
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsWithNoMissionsInPeriod() {
        assertEquals("", scanner.getCompanyWithMostSuccessfulMissions(LocalDate.of(1950, Month.MAY, 11),
            LocalDate.of(1951, Month.MAY, 22)), "If there are no missions, an empty string must be returned!");
    }

    @Test
    void testGetMissionsPerCountry() {
        Map<String, Collection<Mission>> expected = Map.of("USA", List.of(m1, m2, m4, m6),
            "China", List.of(m3, m5), "Kazakhstan", List.of(m7));

        assertTrue(areMapsWithCollectionValueEqual(expected, scanner.getMissionsPerCountry()),
            "Maps should have the same content!");
    }

    @Test
    void testGetMissionsPerCountryWithEmptyCollection() {
        SpaceScannerAPI tempScanner = new MJTSpaceScanner(new StringReader(""), new StringReader(""), secretKey);
        Map<String, Collection<Mission>> expectedEmptyMap = new HashMap<>();
        assertTrue(areMapsWithCollectionValueEqual(expectedEmptyMap, tempScanner.getMissionsPerCountry()),
            "When there are not missions, an empty map is expected");
    }

    @Test
    void testGetTopNLeastExpensiveMissionsWithInvalidN() {
        assertThrows(IllegalArgumentException.class,
            () -> scanner.getTopNLeastExpensiveMissions(-5, MissionStatus.SUCCESS, RocketStatus.STATUS_ACTIVE),
            "An IllegalArgumentException is expected when n is invalid");
    }

    @Test
    void testGetTopNLeastExpensiveMissionsWithInvalidMissionStatus() {
        assertThrows(IllegalArgumentException.class,
            () -> scanner.getTopNLeastExpensiveMissions(10, null, RocketStatus.STATUS_ACTIVE),
            "An IllegalArgumentException is expected when missionStatus is invalid");
    }

    @Test
    void testGetTopNLeastExpensiveMissionsWithInvalidRocketStatus() {
        assertThrows(IllegalArgumentException.class,
            () -> scanner.getTopNLeastExpensiveMissions(10, MissionStatus.SUCCESS, null),
            "An IllegalArgumentException is expected when rocketStatus is invalid");
    }

    @Test
    void testGetTopNLeastExpensiveMissionsWithValidMission() {
        Collection<Mission> expected = List.of(m4, m3);
        Collection<Mission> actual =
            scanner.getTopNLeastExpensiveMissions(2, MissionStatus.SUCCESS, RocketStatus.STATUS_ACTIVE);
        assertTrue(areCollectionsEqual(expected, actual),
            "Expected size: " + expected.size() + ", but was: " + actual.size());
    }

    @Test
    void testGetTopNLeastExpensiveMissionsWithEmptyMissions() {
        assertIterableEquals(Collections.EMPTY_LIST,
            scanner.getTopNLeastExpensiveMissions(2, MissionStatus.PARTIAL_FAILURE, RocketStatus.STATUS_ACTIVE),
            "Empty list is expected when there are not valid missions");
    }

    @Test
    void testGetMostDesiredLocationForMissionsPerCompany() {
        Map<String, String> expected = Map.of("SpaceX", "SLC-4E, Vandenberg AFB, California, USA",
            "Northrop", "LP-0A, Wallops Flight Facility, Virginia, USA", "CASC",
            "LC-3, Xichang Satellite Launch Center, China", "Landspace",
            "Site 95, Jiuquan Satellite Launch Center, China", "ULA", "SLC-3E, Vandenberg AFB, California, USA",
            "Roscosmos", "Site 1/5, Baikonur Cosmodrome, Kazakhstan");

        assertEquals(expected, scanner.getMostDesiredLocationForMissionsPerCompany(),
            "Maps should have the same content");
    }

    @Test
    void testGetMostDesiredLocationForMissionPerCompanyWithEmptyMissions() {
        SpaceScannerAPI tempScanner = new MJTSpaceScanner(new StringReader(""), new StringReader(""), secretKey);
        Map<String, String> expectedEmptyMap = new HashMap<>();
        assertEquals(expectedEmptyMap, tempScanner.getMostDesiredLocationForMissionsPerCompany(),
            "When there are not missions, an empty map is expected");
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanyWithNullFrom() {
        assertThrows(IllegalArgumentException.class,
            () -> scanner.getLocationWithMostSuccessfulMissionsPerCompany(null, LocalDate.MIN),
            "Expected IllegalArgumentException to be thrown when from is null");
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanyWithNullTo() {
        assertThrows(IllegalArgumentException.class,
            () -> scanner.getLocationWithMostSuccessfulMissionsPerCompany(LocalDate.MIN, null),
            "Expected IllegalArgumentException to be thrown when to is null");
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanyWithSwappedFromAndTo() {
        assertThrows(TimeFrameMismatchException.class,
            () -> scanner.getLocationWithMostSuccessfulMissionsPerCompany(LocalDate.MAX, LocalDate.MIN),
            "Expected TimeFrameMismatchException to be thrown when to is before from");
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompany() {
        Map<String, String> expected = Map.of("SpaceX", "SLC-4E, Vandenberg AFB, California, USA",
            "Northrop", "LP-0A, Wallops Flight Facility, Virginia, USA", "CASC",
            "LC-3, Xichang Satellite Launch Center, China", "ULA", "SLC-3E, Vandenberg AFB, California, USA",
            "Roscosmos", "Site 1/5, Baikonur Cosmodrome, Kazakhstan");

        assertEquals(expected, scanner.getLocationWithMostSuccessfulMissionsPerCompany(LocalDate.MIN, LocalDate.MAX),
            "Maps should have the same content");
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanyWithEmptyMissions() {
        SpaceScannerAPI tempScanner = new MJTSpaceScanner(new StringReader(""), new StringReader(""), secretKey);
        Map<String, String> expectedEmptyMap = new HashMap<>();
        assertEquals(expectedEmptyMap,
            tempScanner.getLocationWithMostSuccessfulMissionsPerCompany(LocalDate.MIN, LocalDate.MAX),
            "When there are not missions, an empty map is expected");
    }

    @Test
    void testGetAllRocketsWithEmptyRockets() {
        SpaceScannerAPI tempScanner = new MJTSpaceScanner(new StringReader(""), new StringReader(""), secretKey);
        assertIterableEquals(Collections.EMPTY_LIST, tempScanner.getAllRockets(),
            "An empty list is expected, when there are no missions");
    }

    @Test
    void testGetAllRockets() {
        Collection<Rocket> expected = List.of(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15);
        assertTrue(areCollectionsEqual(expected, scanner.getAllRockets()),
            "Expected size: " + expected.size() + ", but was: " + scanner.getAllRockets().size());
    }

    @Test
    void testGetTopNTallestRocketsWithInvalidN() {
        assertThrows(IllegalArgumentException.class, () -> scanner.getTopNTallestRockets(-1),
            "IllegalArgumentException is expected when n is equal or smaller than zero");
    }

    @Test
    void testGetTopNTallestRockets() {
        Collection<Rocket> expected = List.of(r15, r3, r4);
        Collection<Rocket> actual = scanner.getTopNTallestRockets(3);
        assertTrue(areCollectionsEqual(expected, actual),
            "Expected size: " + expected.size() + ", but was: " + actual.size());
    }

    @Test
    void testGetTopNTallestRocketsWithEmptyCollection() {
        SpaceScannerAPI tempScanner = new MJTSpaceScanner(new StringReader(""), new StringReader(""), secretKey);
        assertIterableEquals(Collections.EMPTY_LIST, tempScanner.getTopNTallestRockets(3),
            "An empty list is expected, when there are not missions");
    }

    @Test
    void testGetWikiPageForRocket() {
        Map<String, Optional<String>> expected =
            new HashMap<>(
                Map.of("Delta IV Medium", Optional.of("https://en.wikipedia.org/wiki/Delta_(rocket_family)#Delta_M"),
                    "Delta IV Medium+ (4,2)", Optional.of("https://en.wikipedia.org/wiki/Delta_IV"),
                    "Delta IV Medium+ (5,2)", Optional.of("https://en.wikipedia.org/wiki/Delta_IV"),
                    "Delta IV Medium+ (5,4)", Optional.of("https://en.wikipedia.org/wiki/Delta_IV"),
                    "Diamant BP4", Optional.of("https://en.wikipedia.org/wiki/Diamant"),
                    "Dnepr", Optional.of("https://en.wikipedia.org/wiki/Dnepr_(rocket)"),
                    "Electron/Curie", Optional.of("https://en.wikipedia.org/wiki/Electron_(rocket)"),
                    "Electron/Photon", Optional.of("https://en.wikipedia.org/wiki/Electron_(rocket)"),
                    "Epsilon PBS", Optional.of("https://en.wikipedia.org/wiki/Epsilon_(rocket)"),
                    "Epsilon S", Optional.of("https://en.wikipedia.org/wiki/Epsilon_(rocket)")));
        expected.put("Jielong-1", Optional.empty());
        expected.put("Europa 1", Optional.of("https://en.wikipedia.org/wiki/Europa_(rocket)"));
        expected.put("Europa 2", Optional.of("https://en.wikipedia.org/wiki/Europa_(rocket)"));
        expected.put("Falcon 1", Optional.of("https://en.wikipedia.org/wiki/Falcon_1"));
        expected.put("Falcon 9 Block 4", Optional.of("https://en.wikipedia.org/wiki/Falcon_9"));

        assertTrue(areMapsEqual(expected, scanner.getWikiPageForRocket()), "Maps should have the same content!");
    }

    @Test
    void testGetWikiPageForRocketWithEmptyMissions() {
        SpaceScannerAPI tempScanner = new MJTSpaceScanner(new StringReader(""), new StringReader(""), secretKey);
        Map<String, Optional<String>> expected = new HashMap<>();
        assertTrue(areMapsEqual(expected, tempScanner.getWikiPageForRocket()), "Maps should have the same content!");
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissionsWithInvalidN() {
        assertThrows(IllegalArgumentException.class,
            () -> scanner.getWikiPagesForRocketsUsedInMostExpensiveMissions(-5, MissionStatus.SUCCESS,
                RocketStatus.STATUS_ACTIVE),
            "IllegalArgumentException is expected when n is equal or smaller than zero");
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissionsWithNullMissionStatus() {
        assertThrows(IllegalArgumentException.class,
            () -> scanner.getWikiPagesForRocketsUsedInMostExpensiveMissions(10, null,
                RocketStatus.STATUS_ACTIVE), "IllegalArgumentException is expected when missionStatus is null");
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissionsWithNullRocketStatus() {
        assertThrows(IllegalArgumentException.class,
            () -> scanner.getWikiPagesForRocketsUsedInMostExpensiveMissions(10, MissionStatus.SUCCESS,
                null), "IllegalArgumentException is expected when rocketStatus is null");
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissions() {
        Reader specificMissionsReader = new StringReader("""
            EmptyLine
            241,SpaceX,"SLC-4E, Vandenberg AFB, California, USA","Tue May 22, 2018",Falcon 9 Block 4 | Iridium-6 & GRACE-FO,StatusRetired,"62.0 ",Success
            263,Roscosmos,"Site 1/5, Baikonur Cosmodrome, Kazakhstan","Wed Mar 21, 2018",Soyuz FG | Soyuz MS-08 (54S),StatusRetired,,Success
            """);

        Reader specificRocketsReader = new StringReader("""
            Empty Line
            147,Delta IV Medium,https://en.wikipedia.org/wiki/Delta_(rocket_family)#Delta_M,62.5 m
            148,"Delta IV Medium+ (4,2)",https://en.wikipedia.org/wiki/Delta_IV,62.5 m
            149,"Delta IV Medium+ (5,2)",https://en.wikipedia.org/wiki/Delta_IV,66.4 m
            150,"Delta IV Medium+ (5,4)",https://en.wikipedia.org/wiki/Delta_IV,66.4 m
            153,Diamant BP4,https://en.wikipedia.org/wiki/Diamant,21.64 m
            194,Jielong-1,,
            154,Dnepr,https://en.wikipedia.org/wiki/Dnepr_(rocket),34.3 m
            156,Electron/Curie,https://en.wikipedia.org/wiki/Electron_(rocket),17.0 m
            157,Electron/Photon,https://en.wikipedia.org/wiki/Electron_(rocket),17.0 m
            162,Epsilon PBS,https://en.wikipedia.org/wiki/Epsilon_(rocket),26.0 m
            163,Epsilon S,https://en.wikipedia.org/wiki/Epsilon_(rocket),27.0 m
            164,Europa 1,https://en.wikipedia.org/wiki/Europa_(rocket),31.68 m
            165,Europa 2,https://en.wikipedia.org/wiki/Europa_(rocket),31.68 m
            166,Falcon 1,https://en.wikipedia.org/wiki/Falcon_1,22.25 m
            168,Falcon 9 Block 4,https://en.wikipedia.org/wiki/Falcon_9,70.0 m
            """);

        SpaceScannerAPI specificScanner = new MJTSpaceScanner(specificMissionsReader, specificRocketsReader, secretKey);
        Collection<String> expected = List.of("https://en.wikipedia.org/wiki/Falcon_9");
        Collection<String> actual =
            specificScanner.getWikiPagesForRocketsUsedInMostExpensiveMissions(1, MissionStatus.SUCCESS,
                RocketStatus.STATUS_RETIRED);

        assertTrue(areCollectionsEqual(expected, actual),
            "Expected size: " + expected.size() + ", but was: " + actual.size());
    }

    @Test
    void testSaveMostReliableRocketWithNullOutputStream() {
        assertThrows(IllegalArgumentException.class,
            () -> scanner.saveMostReliableRocket(null, LocalDate.MIN, LocalDate.MAX),
            "IllegalArgumentException is expected when output is null");
    }

    @Test
    void testSaveMostReliableRocketWithNullFrom() {
        assertThrows(IllegalArgumentException.class,
            () -> scanner.saveMostReliableRocket(null, null, LocalDate.MAX),
            "IllegalArgumentException is expected when from is null");
    }

    @Test
    void testSaveMostReliableRocketWithNullTo() {
        assertThrows(IllegalArgumentException.class,
            () -> scanner.saveMostReliableRocket(null, LocalDate.MIN, null),
            "IllegalArgumentException is expected when to is null");
    }

    @Test
    void testSaveMostReliableRocketWithSwappedFromAndTo() {
        assertThrows(TimeFrameMismatchException.class,
            () -> scanner.saveMostReliableRocket(null, LocalDate.MAX, LocalDate.MIN),
            "TimeFrameMismatchException is expected when to is before from is null");
    }

    @Test
    void testSaveMostReliableRocket() throws CipherException {
        OutputStream expectedOutput = new ByteArrayOutputStream();
        OutputStream actualOutput = new ByteArrayOutputStream();
        SymmetricBlockCipher cipher = new Rijndael(secretKey);
        InputStream expectedWordToEncrypt = new ByteArrayInputStream("Falcon 9 Block 4".getBytes());

        cipher.encrypt(expectedWordToEncrypt, expectedOutput);
        scanner.saveMostReliableRocket(actualOutput, LocalDate.MIN, LocalDate.MAX);
        String expectedOutputString = expectedOutput.toString();
        String actualOutputString = actualOutput.toString();

        assertEquals(expectedOutputString, actualOutputString, "Incorrect rocketName saved!");

    }

    @Test
    void testSaveMostReliableRocketWithoutRocketsInTimeInterval() throws CipherException {
        OutputStream expectedOutput = new ByteArrayOutputStream();
        OutputStream actualOutput = new ByteArrayOutputStream();
        SymmetricBlockCipher cipher = new Rijndael(secretKey);
        InputStream expectedWordToEncrypt = new ByteArrayInputStream("".getBytes());

        cipher.encrypt(expectedWordToEncrypt, expectedOutput);
        scanner.saveMostReliableRocket(actualOutput, LocalDate.of(1920, Month.MARCH, 19),
            LocalDate.of(1921, Month.AUGUST, 10));
        String expectedOutputString = expectedOutput.toString();
        String actualOutputString = actualOutput.toString();

        assertEquals(expectedOutputString, actualOutputString, "Incorrect rocketName saved!");
    }
}

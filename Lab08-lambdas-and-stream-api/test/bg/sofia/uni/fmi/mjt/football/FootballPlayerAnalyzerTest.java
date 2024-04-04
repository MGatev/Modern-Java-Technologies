package bg.sofia.uni.fmi.mjt.football;

import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FootballPlayerAnalyzerTest {
    Reader reader = new StringReader("dboibdoibdoi\n" +
        "L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;LEFT\n" +
        "C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;RIGHT\n" +
        "P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;RIGHT\n");

    FootballPlayerAnalyzer test = new FootballPlayerAnalyzer(reader);

    @Test
    void testGetAllPlayers(){
        assertDoesNotThrow(() -> test.getAllPlayers());
        Player p1 = Player.of("L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;LEFT");
        Player p2 = Player.of("C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;RIGHT");
        Player p3 = Player.of("P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;RIGHT");
        List<Player> list = new ArrayList<>();
        list.add(p1);
        list.add(p2);
        list.add(p3);

        assertIterableEquals(list, test.getAllPlayers());
    }

    @Test
    void testGetAllNationalities() {
        Set<String> expected = Set.of("Argentina", "Denmark", "France");
        assertEquals(expected, test.getAllNationalities());
    }

    @Test
    void testGetHighestPaidPlayerByNationality() {
        Player p1 = Player.of("P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;RIGHT");

        assertEquals(p1, test.getHighestPaidPlayerByNationality("France"));
    }

    @Test
    void testGetHighestPaidPlayerByNationalityThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> test.getHighestPaidPlayerByNationality(null));
    }

    @Test
    void testGroupByPosition() {
        Map<Position, Set<Player>> groups = new HashMap<>();
        Player p1 = Player.of("L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;LEFT");
        Player p2 = Player.of("C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;RIGHT");
        Player p3 = Player.of("P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;RIGHT");

        groups.put(Position.CM, Set.of(p2, p3));
        groups.put(Position.CAM, Set.of(p2, p3));
        groups.put(Position.RM, Set.of(p2));
        groups.put(Position.CF, Set.of(p1));
        groups.put(Position.RW, Set.of(p1));
        groups.put(Position.ST, Set.of(p1));

        for(Position pos : Position.values()) {
            assertEquals(groups.get(pos), test.groupByPosition().get(pos));
        }
    }

    @Test
    void testGetTopProspectPlayerForPositionInBudgetThrowsIllegalArgumentWithNull() {
        assertThrows(IllegalArgumentException.class,
            () -> test.getTopProspectPlayerForPositionInBudget(null, 50));
    }

    @Test
    void testGetTopProspectPlayerForPositionInBudgetThrowsIllegalArgumentWithNegative() {
        assertThrows(IllegalArgumentException.class,
            () -> test.getTopProspectPlayerForPositionInBudget(Position.CF, -200L));
    }

    @Test
    void testGetTopProspectPlayerForPositionInBudget() {
        Player expected =  Player.of("P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;RIGHT");
        assertEquals(expected, test.getTopProspectPlayerForPositionInBudget(Position.CM, 10000000000000000L).orElse(null));
    }

    @Test
    void testGetPlayersByFullNameKeyword() {
        Player expectedPl =  Player.of("P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;RIGHT");
        Set<Player> expected = Set.of(expectedPl);
        assertEquals(expected, test.getPlayersByFullNameKeyword("Pog"));
    }

    @Test
    void testGetPlayersByFullNameKeywordThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> test.getPlayersByFullNameKeyword(null));
    }

    @Test
    void testGetSimilarPlayersThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> test.getSimilarPlayers(null));
    }

    @Test
    void testGetSimilarPlayers() {
        Player p2 = Player.of("C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;RIGHT");
        Player p3 = Player.of("P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;RIGHT");
        Set<Player> expected = Set.of(p3, p2);

        assertEquals(expected, test.getSimilarPlayers(p2));
    }

}

package com.coderatchet.tss;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by jared on 15/03/17.
 */
public class MatchTest {

    private static final String P1 = "p1";
    private static final String P2 = "p2";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private Match match = null;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        match = new Match(P1, P2);
    }

    @Test
    public void testCanCreateAndInitMatch() {
        Match match = new Match("bob", "judy");
        assertEquals("bob", match.getP1Name());
        assertEquals("judy", match.getP2Name());
        assertArrayEquals(new int[]{0, 0, 0, 0}, new int[]{
                match.getP1GamesWon(),
                match.getP2GamesWon(),
                match.getP1GamePoints(),
                match.getP2GamePoints()});
    }

    @Test
    public void testNewGameScoreIsCorrect() {
        assertEquals("", match.getGameScore());
    }

    @Test
    public void testNewGameSetScoreIsCorrect() {
        assertEquals("0-0", match.getSetScore());
    }

    private class ScoreSet implements Comparable<ScoreSet> {
        private Integer p1;
        private Integer p2;

        ScoreSet(int p1, int p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        @Override
        public int compareTo(ScoreSet o) {
            int p1Compare = this.p1.compareTo(o.p1);
            int p2Compare = this.p2.compareTo(o.p2);
            return p1Compare == 0 ? p2Compare : p1Compare;
        }
    }

    /**
     * Exhaustively test early game point combinations. There aren't many.
     */

    @Test
    public void testExpectedScores() {
        Map<ScoreSet, String> expectedScore = new TreeMap<>();

        // add map of points to expected output
        expectedScore.put(new ScoreSet(0, 0), "");
        expectedScore.put(new ScoreSet(0, 1), "0-15");
        expectedScore.put(new ScoreSet(0, 2), "0-30");
        expectedScore.put(new ScoreSet(0, 3), "0-40");
        expectedScore.put(new ScoreSet(1, 0), "15-0");
        expectedScore.put(new ScoreSet(1, 1), "15-15");
        expectedScore.put(new ScoreSet(1, 3), "15-30");
        expectedScore.put(new ScoreSet(1, 3), "15-40");
        expectedScore.put(new ScoreSet(2, 0), "30-0");
        expectedScore.put(new ScoreSet(2, 1), "30-15");
        expectedScore.put(new ScoreSet(2, 2), "30-30");
        expectedScore.put(new ScoreSet(2, 3), "30-40");
        expectedScore.put(new ScoreSet(3, 0), "40-0");
        expectedScore.put(new ScoreSet(3, 1), "40-15");
        expectedScore.put(new ScoreSet(3, 2), "40-30");

        for (Map.Entry<ScoreSet, String> entry : expectedScore.entrySet()) {
            testExpectedOutput(entry);
        }
    }

    private void testExpectedOutput(Map.Entry<ScoreSet, String> entry) {
        Match m = new Match("jack", "jill");
        for (int i = 0; i < entry.getKey().p1; i++) {
            m.pointWonBy("jack");
        }
        for (int i = 0; i < entry.getKey().p2; i++) {
            m.pointWonBy("jill");
        }
        assertEquals(entry.getValue(), m.getGameScore());
    }


    @Test
    public void testWinningConditions1() {
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        assertEquals(1, match.getP1GamesWon());
        assertEquals(0, match.getP2GamesWon());
        assertEquals("", match.getGameScore());
    }

    @Test
    public void testWinningConditions2() {
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        assertEquals(1, match.getP2GamesWon());
        assertEquals(0, match.getP1GamesWon());
        assertEquals("", match.getGameScore());
    }

    @Test
    public void testP1DidNotWinWhenDifferenceLessThan2() {
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        assertEquals(0, match.getP1GamesWon());
        assertEquals(0, match.getP2GamesWon());
    }

    @Test
    public void testP2DidNotWinWhenDifferenceLessThan2() {
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        assertEquals(0, match.getP1GamesWon());
        assertEquals(0, match.getP2GamesWon());
    }

    @Test
    public void testDeuceScoreShowsCorrectly() {
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        assertEquals("Deuce", match.getGameScore());
    }


    @Test
    public void testAdvantageP1ScoreShowsCorrectly() {
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        assertEquals("Advantage " + P1, match.getGameScore());
    }


    @Test
    public void testAdvantageP2ScoreShowsCorrectly() {
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        assertEquals("Advantage " + P2, match.getGameScore());
    }

    private void simulateTieBreak(Match m) {
        // 5 Wins P1
        simulateGameWin(m, P1, 5);

        // 6 Wins P2
        simulateGameWin(m, P2, 6);

        // Tie Break win P1
        simulateGameWin(m, P1, 1);


    }

    private void simulateGameWin(Match m, String player, int num) {
        for (int i = 0; i < num; i++) {
            int pointsToWin;
            if (m.getP1GamesWon() == 6 && m.getP2GamesWon() == 6) {
                pointsToWin = 7;
            } else {
                pointsToWin = 4;
            }
            for (int j = 0; j < pointsToWin; j++) {
                m.pointWonBy(player);
            }
        }
    }

    /**
     * Exhaustively test the first few wins of games score. starting from non zero score.
     */

    @Test
    public void testSetScore() {
        for (int i = 0; i < 6; i++) {
            Match m = new Match(P1, P2);
            simulateGameWin(m, P1, i);
            for (int j = 0; j < 6; j++) {
                simulateGameWin(m, P2, 1);
                assertEquals(String.format("%s-%s", i, j + 1), m.getSetScore());
            }
        }
    }

    @Test
    public void testP1WinsIfPoints6WithGT1Diff() {
        simulateGameWin(match, P1, 5);
        simulateGameWin(match, P2, 4);
        simulateGameWin(match, P1, 1);
        assertEquals(P1, match.getWinner());
        assertEquals("Congratulations to " + P1+ " for winning! Final score: 6-4\n", outContent.toString());
    }

    @Test
    public void testP2WinsIfPoints6WithGT1Diff() {
        simulateGameWin(match, P2, 5);
        simulateGameWin(match, P1, 4);
        simulateGameWin(match, P2, 1);
        assertEquals(P2, match.getWinner());
        assertEquals("Congratulations to " + P2 + " for winning! Final score: 4-6\n", outContent.toString());
    }


    @Test
    public void testP1DoesNotWinWhenPointDifferenceLessThan2() {

        // 5 wins P2
        simulateGameWin(match, P2, 5);

        // 6 wins P1
        simulateGameWin(match, P1, 6);
        assertNull(match.getWinner());

        // Set Point P1
        simulateGameWin(match, P1, 1);
        assertEquals(P1, match.getWinner());
    }


    @Test
    public void testP2DoesNotWinWhenPointDifferenceLessThan2() {
        // 5 wins
        simulateGameWin(match, P1, 5);

        // 6 wins
        simulateGameWin(match, P2, 6);
        assertNull(match.getWinner());

        simulateGameWin(match, P2, 1);
        assertEquals(P2, match.getWinner());
    }

    @Test
    public void testTieRulesEntered() {
        for (int i = 0; i < 6; i++) {
            Match m = new Match(P1, P2);

            simulateTieBreak(m);
            assertNull(m.getWinner());
            for (int j = 0; j < i; j++) {
                m.pointWonBy(P1);
            }
            for (int j = 0; j < 6; j++) {
                m.pointWonBy(P2);
                assertEquals(String.format("%s-%s", i, j + 1), m.getGameScore());
            }
        }
    }

    @Test
    public void testTieBreakNotWonWithDifferenceLessThan2AndPointsMoreThan6P1() {
        simulateTieBreak(match);
        assertNull(match.getWinner());

        // 6 points P2
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);

        // 7 points P1
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        assertEquals("7-6", match.getGameScore());
        assertNull(match.getWinner());
    }

    @Test
    public void testTieBreakNotWonWithDifferenceLessThan2AndPointsMoreThan6P2() {
        simulateTieBreak(match);
        assertNull(match.getWinner());

        // 6 points P1
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);

        // 7 points P2
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);

        assertEquals("6-7", match.getGameScore());
        assertNull(match.getWinner());
    }

    @Test
    public void testTieBreakerWonWhenMoreThan6PointsAndAtLeast2MoreThanOtherP1() {
        simulateTieBreak(match);
        assertNull(match.getWinner());

        // 5 wins P2
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);

        // 7 wins P1
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);

        assertEquals("7-6", match.getSetScore());
        assertEquals(P1, match.getWinner());
        assertEquals("Congratulations to " + P1 + " for winning! Final score: 7-6\n", outContent.toString());
    }

    @Test
    public void testTieBreakerWonWhenMoreThan6PointsAndAtLeast2MoreThanOtherP2() {

        simulateTieBreak(match);
        assertNull(match.getWinner());


        // 5 points P1
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);

        // 7 points P2
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);

        assertEquals("6-7", match.getSetScore());
        assertEquals(P2, match.getWinner());
        assertEquals("Congratulations to " + P2 + " for winning! Final score: 6-7\n", outContent.toString());
    }

    @Test
    public void testCanPrintOverallScoreOnNewGame() {
        match.score();
        assertEquals("0-0\n", outContent.toString());
    }

    @Test
    public void testCanPrintOverallScoreOnNewGameWithPoints() {
        match.pointWonBy(P1);
        match.score();
        assertEquals("0-0, 15-0\n", outContent.toString());

        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        outContent.reset();
        match.score();
        assertEquals("1-0\n", outContent.toString());

        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P1);
        outContent.reset();
        match.score();
        assertEquals("1-0, 15-30\n", outContent.toString());
    }
}
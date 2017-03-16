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

    private void simulateGameWinP1(Match m) {
        m.pointWonBy(P1);
        m.pointWonBy(P1);
        m.pointWonBy(P1);
        m.pointWonBy(P1);
    }

    private void simulateGameWinP2(Match m) {
        m.pointWonBy(P2);
        m.pointWonBy(P2);
        m.pointWonBy(P2);
        m.pointWonBy(P2);
    }

    /**
     * Exhaustively test the first few wins of games score. starting from non zero score.
     */

    @Test
    public void testSetScore() {
        for (int i = 0; i < 6; i++) {
            Match m = new Match(P1, P2);
            for (int k = 0; k < i; k++) {
                simulateGameWinP1(m);
            }
            for (int j = 0; j < 6; j++) {
                simulateGameWinP2(m);
                assertEquals(String.format("%s-%s", i, j + 1), m.getSetScore());
            }
        }
    }

    @Test
    public void testP1WinsIfPoints6WithGT1Diff() {
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        assertEquals(P1, match.getWinner());
    }

    @Test
    public void testP2WinsIfPoints6WithGT1Diff() {
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        assertEquals(P2, match.getWinner());
    }


    @Test
    public void testP1DoesNotWinWhenPointDifferenceLessThan2() {

        // 5 wins
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);

        // 6 wins
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        assertNull(match.getWinner());
        simulateGameWinP1(match);
        assertEquals(P1, match.getWinner());
    }


    @Test
    public void testP2DoesNotWinWhenPointDifferenceLessThan2() {
        // 5 wins
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP1(match);

        // 6 wins
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        assertNull(match.getWinner());
        simulateGameWinP2(match);
        assertEquals(P2, match.getWinner());
    }

    @Test
    public void testTieRulesEntered() {
        for (int i = 0; i < 6; i++) {
            Match m = new Match(P1, P2);
            simulateGameWinP1(m);
            simulateGameWinP1(m);
            simulateGameWinP1(m);
            simulateGameWinP1(m);
            simulateGameWinP1(m);
            simulateGameWinP2(m);
            simulateGameWinP2(m);
            simulateGameWinP2(m);
            simulateGameWinP2(m);
            simulateGameWinP2(m);
            simulateGameWinP2(m);
            simulateGameWinP1(m);
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
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP1(match);
        assertNull(match.getWinner());
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P1);
        assertEquals("7-6", match.getGameScore());
        assertNull(match.getWinner());
    }

    @Test
    public void testTieBreakNotWonWithDifferenceLessThan2AndPointsMoreThan6P2() {
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP1(match);
        assertNull(match.getWinner());
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P2);
        assertEquals("6-7", match.getGameScore());
        assertNull(match.getWinner());
    }

    @Test
    public void testTieBreakerWonWhenMoreThan6PointsAndAtLeast2MoreThanOtherP1(){

        // 5 wins P1
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP1(match);

        // 6 wins P2
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);

        // 1 win P1 (tie-breaker)
        simulateGameWinP1(match);
        assertNull(match.getWinner());
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P2);
        assertEquals("6-7", match.getSetScore());
        assertEquals(P2, match.getWinner());
    }

    @Test
    public void testTieBreakerWonWhenMoreThan6PointsAndAtLeast2MoreThanOtherP2(){

        // 5 wins P1
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);
        simulateGameWinP2(match);

        // 6 wins P2
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP1(match);
        simulateGameWinP1(match);

        // 1 win P2 (tie-breaker)
        simulateGameWinP2(match);
        assertNull(match.getWinner());

        // 6 points P1
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);

        // 5 points p2
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);

        // winning point P1
        match.pointWonBy(P1);
        assertEquals("7-6", match.getSetScore());
        assertEquals(P1, match.getWinner());
    }

    @Test
    public void testCanPrintOverallScoreOnNewGame() {
        match.score();
        assertEquals("0-0\n", outContent.toString());
    }

//    @Test
//    public void testCanPrintOverallScoreOnNewGameWithPoints() {
//        match.pointWonBy(P1);
//        match.score();
//        assertEquals("0-0, 0-1", outContent.toString());
//    }
}
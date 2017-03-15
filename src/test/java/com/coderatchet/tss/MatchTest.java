package com.coderatchet.tss;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

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
        assertArrayEquals(new int[]{0, 0, 0, 0, 1}, new int[]{
                match.getP1GamesWon(),
                match.getP2GamesWon(),
                match.getP1GamePoints(),
                match.getP2GamePoints(),
                match.getCurrentGame()});
    }

    @Test
    public void testNewGameScoreIsCorrect() {
        assertEquals("0-0", match.getGameScore());
    }

    @Test
    public void testNewGameSetScoreIsCorrect() {
        assertEquals("0-0", match.getSetScore());
    }

    private class ScoreSet implements Comparable<ScoreSet>{
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
        expectedScore.put(new ScoreSet(0, 0), "0-0");
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
//        expectedScore.put(new ScoreSet(3, 3), "Deuce");

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
    public void testWinningConditions1(){
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        match.pointWonBy(P1);
        assertEquals(1, match.getP1GamesWon());
        assertEquals("0-0", match.getGameScore());
    }
}
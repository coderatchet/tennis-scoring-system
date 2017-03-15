package com.coderatchet.tss;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by jared on 15/03/17.
 */
public class MatchTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private Match match = null;

    @Before
    public void setUpStreams(){
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        match = new Match("p1", "p2");
    }

    @Test
    public void testCanCreateAndInitMatch(){
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

    @Test
    public void testNewGamePlayer1Scores1Point(){
        match.pointWonBy("p1");
        assertEquals("15-0", match.getGameScore());
    }
}
package com.coderatchet.tss;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by jared on 15/03/17.
 */
public class MatchTest {

//    TODO test output of score system with method found from http://stackoverflow.com/questions/1119385/junit-test-for-system-out-println
//    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
//
//    @Before
//    public void setUpStreams(){
//        System.setOut(new PrintStream(outContent));
//        System.setErr(new PrintStream(errContent));
//    }

    @Test
    public void testCanCreateAndInitMatch(){
        Match match = new Match("bob", "judy");
        assertEquals("bob", match.getP1Name());
        assertEquals("judy", match.getP2Name());
        assertArrayEquals(new int[]{0, 0, 0, 0, 1}, new int[]{
            match.getP1GamesWon(),
            match.getP2GamesWon(),
            match.getP1GameScore(),
            match.getP2GameScore(),
            match.getCurrentGame()});
    }
}
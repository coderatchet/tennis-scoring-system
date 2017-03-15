package com.coderatchet.tss;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by jared on 14/03/17.
 */
public class Match {

    private String p1Name;
    private String p2Name;
    private int p1GamesWon = 0;
    private int p2GamesWon = 0;
    private int p1GameScore = 0;
    private int p2GameScore = 0;
    private int currentGame = 1;

    public Match(String player1Name, String player2Name) {
        p1Name = player1Name;
        p2Name = player2Name;
    }

    public void pointWonBy(String playerName) {
        throw new NotImplementedException();
    }

    public void score() {
        throw new NotImplementedException();
    }

    public String getP1Name() {
        return p1Name;
    }

    public String getP2Name() {
        return p2Name;
    }

    public int getP1GamesWon() {
        return p1GamesWon;
    }

    public int getP2GamesWon() {
        return p2GamesWon;
    }

    public int getP1GameScore() {
        return p1GameScore;
    }

    public int getP2GameScore() {
        return p2GameScore;
    }

    public int getCurrentGame() {
        return currentGame;
    }
}

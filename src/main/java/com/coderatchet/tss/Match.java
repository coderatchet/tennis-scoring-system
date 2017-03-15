package com.coderatchet.tss;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jared on 14/03/17.
 */
public class Match {

    private String p1Name;
    private String p2Name;
    private int p1GamesWon = 0;
    private int p2GamesWon = 0;
    private int p1GamePoints = 0;
    private int p2GamePoints = 0;
    private int currentGame = 1;

    private Map<Integer, String> pointConversionMap = new TreeMap<>();

    public Match(String player1Name, String player2Name) {
        p1Name = player1Name;
        p2Name = player2Name;
        pointConversionMap.put(0, "0");
        pointConversionMap.put(1, "15");
        pointConversionMap.put(2, "30");
        pointConversionMap.put(3, "40");
    }

    void pointWonBy(String playerName) {
        // no null tests, correct content assumed
        if (playerName.equals(this.getP1Name())) {
            this.p1GamePoints++;
        } else if (playerName.equals(this.getP2Name())) {
            throw new NotImplementedException();
        } else {
            throw new RuntimeException(String.format("Player '%s' does not exist.", playerName));
        }
    }

    public void score() {
        throw new NotImplementedException();
    }

    String getP1Name() {
        return p1Name;
    }

    String getP2Name() {
        return p2Name;
    }

    int getP1GamesWon() {
        return p1GamesWon;
    }

    int getP2GamesWon() {
        return p2GamesWon;
    }

    int getP1GamePoints() {
        return p1GamePoints;
    }

    int getP2GamePoints() {
        return p2GamePoints;
    }

    int getCurrentGame() {
        return currentGame;
    }

    String getGameScore() {
        if (this.p1GamePoints >= 4 || this.p2GamePoints >= 4) {
            throw new NotImplementedException();
        } else {
            return String.format("%s-%s",
                    this.pointConversionMap.get(this.p1GamePoints),
                    this.pointConversionMap.get(this.p2GamePoints));
        }
    }

    String getSetScore() {
        return "0-0";
    }
}

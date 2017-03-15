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

    /**
     * For this demo, We are assuming the players will obey game restarting rules when a game is won and the next point
     * applies to the next game.
     *
     * @param playerName player who scored
     */
    void pointWonBy(String playerName) {
        // no null tests, correct content assumed
        if (playerName.equals(getP1Name())) {
            p1GamePoints++;

            // winning means at least 4 points and difference of at least 2
            if (p1GamePoints >= 4 && p1GamePoints - p2GamePoints >= 2) {
                p1GamesWon++;
                resetScore();
            }
        } else if (playerName.equals(getP2Name())) {
            p2GamePoints++;
            if (p2GamePoints >= 4 && p2GamePoints - p1GamePoints >= 2) {
                p2GamesWon++;
                resetScore();
            }
        } else {
            throw new RuntimeException(String.format("Player '%s' does not exist.", playerName));
        }
    }

    private void resetScore() {
        p1GamePoints = 0;
        p2GamePoints = 0;
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
        if (p1GamePoints >= 3 && p2GamePoints >= 3) {
            if (p1GamePoints > p2GamePoints) {
                return "Advantage " + p1Name;
            }
            return "Deuce";
        } else {
            return String.format("%s-%s",
                    pointConversionMap.get(p1GamePoints),
                    pointConversionMap.get(p2GamePoints));
        }
    }

    String getSetScore() {
        return "0-0";
    }
}

package com.coderatchet.tss;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.lang.model.type.UnionType;
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

    private GameRules currentGameRules = new NormalGameRules();

    public Match(String player1Name, String player2Name) {
        p1Name = player1Name;
        p2Name = player2Name;
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
        } else if (playerName.equals(getP2Name())) {
            p2GamePoints++;
        } else {
            throw new RuntimeException(String.format("Player '%s' does not exist.", playerName));
        }

        // winning means at least 4 points and difference of at least 2
        String gameWinner = currentGameRules.getGameWinner();
        if (null != gameWinner) {
            if (p1Name.equals(gameWinner)) {
                p1GamesWon++;
            } else {
                p2GamesWon++;
            }
            resetScore();
        }

        // enter a tie breaker if the match is 12 games in with no winner.
        if (p1GamesWon == 6 && p2GamesWon == 6) {
            // enter tiebreaker match
            currentGameRules = new TieBreakerGameRules();
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

    String getGameScore() {
        return currentGameRules.getScore();
    }

    String getWinner() {
        return currentGameRules.getSetWinner();
    }

    String getSetScore() {
        return String.format("%s-%s", getP1GamesWon(), getP2GamesWon());
    }

    private interface GameRules {

        /**
         * returns the winners name of the current game or null for no winner yet.
         *
         * @return null|String
         */
        String getGameWinner();

        /**
         * returns the printable score for the current game according to the scoring rules. empty for no points scored
         * yet.
         *
         * @return String
         */
        String getScore();

        /**
         * return the ultimate set winner or null if none decided yet
         *
         * @return null|String
         */
        String getSetWinner();
    }

    /**
     * not a tie breaker
     */
    private class NormalGameRules implements GameRules {

        private Map<Integer, String> pointConversionMap = new TreeMap<>();

        @Override
        public String getGameWinner() {
            if (p1GamePoints >= 4 && p1GamePoints - p2GamePoints >= 2) {
                return p1Name;
            } else if (p2GamePoints >= 4 && p2GamePoints - p1GamePoints >= 2) {
                return p2Name;
            } else return null;
        }

        @Override
        public String getScore() {
            pointConversionMap.put(0, "0");
            pointConversionMap.put(1, "15");
            pointConversionMap.put(2, "30");
            pointConversionMap.put(3, "40");
            if (p1GamePoints >= 3 && p2GamePoints >= 3) {
                if (p1GamePoints > p2GamePoints) {
                    return "Advantage " + p1Name;
                } else if (p2GamePoints > p1GamePoints) {
                    return "Advantage " + p2Name;
                }
                return "Deuce";
            } else {
                return String.format("%s-%s",
                        pointConversionMap.get(p1GamePoints),
                        pointConversionMap.get(p2GamePoints));
            }
        }

        @Override
        public String getSetWinner() {
            if ((p1GamesWon >= 6 && p1GamesWon - p2GamesWon >= 2) ||
                    (p2GamesWon >= 6 && p2GamesWon - p1GamesWon >= 2)) {
                return p1GamesWon > p2GamesWon ? p1Name : p2Name;
            } else return null;
        }
    }

    /**
     * not a tie breaker
     */
    private class TieBreakerGameRules implements GameRules {

        @Override
        public String getGameWinner() {
            if (p1GamePoints >= 7 && p1GamePoints - p2GamePoints >= 2) {
                return p1Name;
            } else if (p2GamePoints >= 7 && p2GamePoints - p1GamePoints >= 2) {
                return p2Name;
            } else return null;
        }

        @Override
        public String getScore() {
            return String.format("%s-%s", p1GamePoints, p2GamePoints);
        }

        @Override
        public String getSetWinner() {
            // since this is the last game, then we can do this simple check.
            if (p1GamesWon == 7 || p2GamesWon == 7) {
                return p1GamesWon == 7 ? p1Name : p2Name;
            } else {
                return null;
            }
        }
    }
}

package com.coderatchet.tss;

/**
 * Test scenario as presented in github challenge.
 *
 * run mvn test for passing tests and investigate the MatchTest.java file for how the game works.
 *
 * Created by jared on 16/03/17.
 */
public class Main {

    static final String P1 = "Roger Federer";
    static final String P2 = "Rafael Nadal";

    public static void main(String[] args){
        Match match = new Match(P1, P2);

        match.pointWonBy(P1);
        match.pointWonBy(P2);
        // this will return "0-0, 15-15"
        match.score();

        match.pointWonBy(P1);
        match.pointWonBy(P1);
        // this will return "0-0, 40-15"
        match.score();

        match.pointWonBy(P2);
        match.pointWonBy(P2);
        // this will return "0-0, Deuce"
        match.score();

        match.pointWonBy(P1);
        // this will return "0-0, Advantage player 1"
        match.score();

        match.pointWonBy(P1);
        // this will return "1-0"
        match.score();

        // Rafael flogs Roger for 5 games
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                match.pointWonBy(P2);
            }
        }

        match.score();

        // Roger makes a comeback and comes close to set win
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                match.pointWonBy(P1);
            }
        }

        match.score();

        // Rafael brings us to tie breaker
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);
        match.pointWonBy(P2);

        match.score();

        match.pointWonBy(P1);
        match.pointWonBy(P2);
        match.pointWonBy(P1);
        match.pointWonBy(P2);
        match.pointWonBy(P1);
        match.pointWonBy(P2);
        match.pointWonBy(P1);
        match.pointWonBy(P2);
        match.pointWonBy(P1);
        match.pointWonBy(P2);
        match.pointWonBy(P1);
        match.pointWonBy(P2);
        match.pointWonBy(P1);

        // close game!!
        match.score();

        // Roger WINS!
        match.pointWonBy(P1);
    }
}

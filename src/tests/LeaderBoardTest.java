package tests;

import Game.LeaderBoard;
import Game.Player;
import Game.RobotPlayer;
import Game.UserPlayer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class LeaderBoardTest {

    Player user;
    Player r1;
    Player r2;
    Player r3;
    ArrayList<Player> PlayerList;
    LeaderBoard LB;

    @Before
    public void setUp() {
        user = new UserPlayer("Player");
        r1 = new RobotPlayer("Robot 1");
        r2 = new RobotPlayer("Robot 2");
        r3 = new RobotPlayer("Robot 3");
        PlayerList = new ArrayList<>();
        PlayerList.add(user);
        PlayerList.add(r1);
        PlayerList.add(r2);
        PlayerList.add(r3);

        LB = new LeaderBoard(PlayerList);
    }

    @Test
    public void testGetWinner() {
        // Normal Cases to test method logic
        /* Test ID - 011 */
        r1.addScore(2);
        assertEquals("Winner not correctly chosen", r1, LB.getWinner());

        /* Test ID - 012 */
        r2.addScore(2);
        r3.addScore(4);
        assertEquals("Winner not correctly chosen", r3, LB.getWinner());

        /* Test ID - 013 */
        user.addScore(10);
        assertEquals("Winner not correctly chosen", user, LB.getWinner());

        //  When the cord values are equal first person who was ahead is the winner
        //  Unanimous Decisions.
        /* Test ID - 014 */
        r1.setScore(0);
        r2.setScore(0);
        r3.setScore(0);
        user.setScore(0);

        r1.addScore(2);
        r2.addScore(2);
        assertEquals("Winner not correctly chosen", r1, LB.getWinner());

        /* Test ID - 015 */
        user.addScore(4);
        r2.addScore(2);
        r1.addScore(2);
        r3.addScore(4);
        assertEquals("Winner not correctly chosen", r1, LB.getWinner());
    }

    @Test
    public void testUpdateScore() {
        /* Test ID - 016 */
        LB.updateScore(user,2);
        assertEquals("Score not correctly updated", 2, user.getScore());

        /* Test ID - 017 */
        LB.updateScore(r1,4);
        assertEquals("Score not correctly updated", 4, r1.getScore());
    }

    @Test
    public void testGetRank() {
        // Normal test cases for Relative ranks
        /* Test ID - 018 */
        r2.addScore(2);
        assertEquals("Rank is not correct", 1, LB.getRank(r2));
        assertEquals("Rank is not correct", 2, LB.getRank(r1));
        assertEquals("Rank is not correct", 2, LB.getRank(user));
        assertEquals("Rank is not correct", 2, LB.getRank(r3));

        /* Test ID - 019 */
        user.addScore(4);
        r2.addScore(2);
        assertEquals("Rank is not correct", 1, LB.getRank(r2));
        assertEquals("Rank is not correct", 1,  LB.getRank(user));
        assertEquals("Rank is not correct", 3,  LB.getRank(r1));
        assertEquals("Rank is not correct", 3,  LB.getRank(r3));

        /* Test ID - 020 */
        user.addScore(2);
        r1.addScore(4);
        assertEquals("Rank is not correct", 2, LB.getRank(r2));
        assertEquals("Rank is not correct", 1, LB.getRank(user));
        assertEquals("Rank is not correct", 2, LB.getRank(r1));
        assertEquals("Rank is not correct", 4, LB.getRank(r3));

        //  At the start all should be one
        /* Test ID - 021 */
        r1.setScore(0);
        r2.setScore(0);
        r3.setScore(0);
        user.setScore(0);

        assertEquals("Rank is not correct", 1, LB.getRank(user));
        assertEquals("Rank is not correct", 1, LB.getRank(r1));
        assertEquals("Rank is not correct", 1, LB.getRank(r2));
        assertEquals("Rank is not correct", 1, LB.getRank(r3));

    }

    @Test
    public void testGetPlayerList() {
        //  Check if same object in LeaderBoard
        /* Test ID - 022 */
        assertEquals("Does not refer the same object",this.PlayerList, LB.getPlayerList());
    }
}
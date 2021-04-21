package tests;

import Game.PlayCard;
import Game.RobotPlayer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RobotPlayerTest {

    RobotPlayer R1;
    RobotPlayer R2;
    RobotPlayer R3;
    RobotPlayer R4;
    RobotPlayer R5;

    @Before
    public void setUp() {
        R1 = new RobotPlayer("r1");
        R2 = new RobotPlayer("r2");
        R3 = new RobotPlayer("r3");
        R4 = new RobotPlayer("r4");
        R5 = new RobotPlayer("r5");

        PlayCard p1 = new PlayCard("Q","C");
        PlayCard p2 = new PlayCard("A","H");
        PlayCard p3 = new PlayCard("10","S");
        PlayCard p4 = new PlayCard("K","D");

        PlayCard p5 = new PlayCard("K","D");
        PlayCard p6 = new PlayCard("A","D");
        PlayCard p7 = new PlayCard("7","C");
        PlayCard p8 = new PlayCard("K","S");

        PlayCard p9 = new PlayCard("8","C");
        PlayCard p10 = new PlayCard("J","C");
        PlayCard p11 = new PlayCard("Q","D");
        PlayCard p12 = new PlayCard("8","S");

        PlayCard p13 = new PlayCard("7","S");
        PlayCard p14 = new PlayCard("8","S");
        PlayCard p15 = new PlayCard("9","S");
        PlayCard p16 = new PlayCard("K","S");

        PlayCard p17 = new PlayCard("A","C");
        PlayCard p18 = new PlayCard("9","C");
        PlayCard p19 = new PlayCard("Q","S");
        PlayCard p20 = new PlayCard("8","C");


        R1.addCards(p1);
        R1.addCards(p2);
        R1.addCards(p3);
        R1.addCards(p4);

        R2.addCards(p5);
        R2.addCards(p6);
        R2.addCards(p7);
        R2.addCards(p8);

        R3.addCards(p9);
        R3.addCards(p10);
        R3.addCards(p11);
        R3.addCards(p12);

        R4.addCards(p13);
        R4.addCards(p14);
        R4.addCards(p15);
        R4.addCards(p16);

        R5.addCards(p17);
        R5.addCards(p18);
        R5.addCards(p19);
        R5.addCards(p20);
    }

    @Test
    public void testGetTrump()
    {
        // Normal Case 1 suit : that suit is trump
        /* Test ID - 038 */
        assertEquals("Trump not Selected Properly","S", R4.getTrump(""));

        // Normal Case 2 different suits : suit with most cards is trump
        /* Test ID - 039 */
        assertEquals("Trump not Selected Properly","C", R5.getTrump(""));

        //   Normal Case 3 different suit : If suit with most occurring does not have 'A' and has small Range
        //   then select that is not included in cards
        /* Test ID - 040 */
        assertEquals("Trump not Selected Properly","D", R2.getTrump(""));

        //   Normal Case 3 different suit : If suit with most occurring does not have 'A' and has small Range
        //   then select that is not included in cards
        /* Test ID - 041 */
        assertEquals("Trump not Selected Properly","H", R3.getTrump(""));

        //   Normal Case 4 different suit : highest is trump
        /* Test ID - 042 */
        assertEquals("Trump not Selected Properly","H", R1.getTrump(""));
    }


    @Test
    public void testPlayCInTurn() {
        // Normal Case Check if card is played from robots hand, this is random.
        //  This is when the Robot leads the trick.

        ArrayList<PlayCard> R1HandCopy = new ArrayList<>(R1.getPlayerHand());
        ArrayList<PlayCard> R2HandCopy = new ArrayList<>(R2.getPlayerHand());
        ArrayList<PlayCard> R3HandCopy = new ArrayList<>(R3.getPlayerHand());
        ArrayList<PlayCard> R4HandCopy = new ArrayList<>(R4.getPlayerHand());
        ArrayList<PlayCard> R5HandCopy = new ArrayList<>(R5.getPlayerHand());

        /* Test ID - 043 */
        PlayCard R1card = R1.playCInTurn("");
        assertTrue("Card not selected from hand", R1HandCopy.contains(R1card));

        /* Test ID - 044 */
        PlayCard R2card = R2.playCInTurn("");
        assertTrue("Card not selected from hand", R2HandCopy.contains(R2card));

        /* Test ID - 045 */
        PlayCard R3card = R3.playCInTurn("");
        assertTrue("Card not selected from hand", R3HandCopy.contains(R3card));

        /* Test ID - 046 */
        PlayCard R4card = R4.playCInTurn("");
        assertTrue("Card not selected from hand", R4HandCopy.contains(R4card));

        /* Test ID - 047 */
        PlayCard R5card = R5.playCInTurn("");
        assertTrue("Card not selected from hand", R5HandCopy.contains(R5card));
    }

    @Test
    public void testPlayCInTurnWithTrumps() {

        //  Normal Case 1 where the highest card from same suit is played
        /* Test ID - 048 */
        PlayCard PlayerCard1 = new PlayCard("7", "D");
        PlayCard R1card = R1.playCInTurn(PlayerCard1, "C");
        assertEquals("K of ♦",R1card.toString());

        // Normal Case 2 where lowest trump card is played when same suit is not there
        /* Test ID - 049 */
        PlayCard PlayerCard2 = new PlayCard("10", "H");
        PlayCard R2card = R2.playCInTurn(PlayerCard2, "C");
        assertEquals("7 of ♣",R2card.toString());

        // Normal Case 3 where lowest card of the trump cards is selected
        // Due no same suit cards
        /* Test ID - 050 */
        PlayCard PlayerCard3 = new PlayCard("A", "H");
        PlayCard R3card = R3.playCInTurn(PlayerCard3, "C");
        assertEquals("8 of ♣",R3card.toString());

        // Normal Case 4 where lowest card of the hand is selected
        // Due no same suit cards or trump cards.
        /* Test ID - 051 */
        PlayCard PlayerCard4 = new PlayCard("K", "D");
        PlayCard R4card = R4.playCInTurn(PlayerCard4, "H");
        assertEquals("7 of ♠",R4card.toString());
    }

}
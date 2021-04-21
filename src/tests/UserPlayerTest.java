package tests;

import Game.PlayCard;
import Game.UserPlayer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserPlayerTest {

    UserPlayer user1;
    UserPlayer user2;

    PlayCard p1;
    PlayCard p2;
    PlayCard p3;
    PlayCard p4;
    PlayCard p5;
    PlayCard p6;
    PlayCard p7;
    PlayCard p8;
    PlayCard p9;
    PlayCard p10;

    @Before
    public void setUp()
    {
        user1 = new UserPlayer("user1");
        user2 = new UserPlayer("user2");

        p1 = new PlayCard("9","C");
        p2 = new PlayCard("A","H");
        p3 = new PlayCard("10","D");
        p4 = new PlayCard("K","S");
        p5 = new PlayCard("2","D");
        p6 = new PlayCard("2","C");
        p7 = new PlayCard("2","S");
        p8 = new PlayCard("2","H");
        p9 = new PlayCard("4","C");
        p10 = new PlayCard("J","H");

        user1.addCards(p1);
        user1.addCards(p2);
        user1.addCards(p3);
        user1.addCards(p4);

        user2.addCards(p5);
        user2.addCards(p6);
        user2.addCards(p7);
        user2.addCards(p8);
        user2.addCards(p9);
        user2.addCards(p10);


    }

    @Test
    public void testGetTrump() {
        // Normal Cases for testing get trumps
        /* Test ID - 052 */
        assertEquals("Trump not properly selected", "D", user1.getTrump("D"));
        /* Test ID - 053 */
        assertEquals("Trump not properly selected", "S", user1.getTrump("S"));
        /* Test ID - 054 */
        assertEquals("Trump not properly selected", "C", user1.getTrump("C"));
        /* Test ID - 055 */
        assertEquals("Trump not properly selected", "H", user1.getTrump("H"));

        // Normal Cases for testing get trumps with symbols
        /* Test ID - 056 */
        assertEquals("Trump not properly selected", "D", user1.getTrump("♦"));
        /* Test ID - 057 */
        assertEquals("Trump not properly selected", "S", user1.getTrump("♠"));
        /* Test ID - 058 */
        assertEquals("Trump not properly selected", "C", user1.getTrump("♣"));
        /* Test ID - 059 */
        assertEquals("Trump not properly selected", "H", user1.getTrump("♥"));
    }

    @Test
    public void testGetTrumpMessy() {

        // Corner Test Cases to test with messy user input: no capitalization
        /* Test ID - 060 */
        assertEquals("Trump not properly selected", "D", user1.getTrump("d"));
        /* Test ID - 061 */
        assertEquals("Trump not properly selected", "S", user1.getTrump("s"));
        /* Test ID - 062 */
        assertEquals("Trump not properly selected", "C", user1.getTrump("c"));
        /* Test ID - 063 */
        assertEquals("Trump not properly selected", "H", user1.getTrump("h"));

        // Corner Test Cases to test with messy user input: unwanted spaces
        /* Test ID - 064 */
        assertEquals("Trump not properly selected",  "D", user1.getTrump("  ♦"));
        /* Test ID - 065 */
        assertEquals("Trump not properly selected", "S", user1.getTrump("S  "));
        /* Test ID - 066 */
        assertEquals("Trump not properly selected", "C", user1.getTrump(" C"));
        /* Test ID - 067 */
        assertEquals("Trump not properly selected", "H", user1.getTrump("H "));
        /* Test ID - 068 */
        assertEquals("Trump not properly selected", "D", user1.getTrump("      D  "));
        /* Test ID - 069 */
        assertEquals("Trump not properly selected",  "S", user1.getTrump("  s"));
        /* Test ID - 070 */
        assertEquals("Trump not properly selected",  "C", user1.getTrump("♣ "));
        /* Test ID - 071 */
        assertEquals("Trump not properly selected", "H", user1.getTrump("  h"));
        /* Test ID - 072 */
        assertEquals("Trump not properly selected", "D", user1.getTrump("    d"));
        /* Test ID - 073 */
        assertEquals("Trump not properly selected", "S", user1.getTrump("  s "));
        /* Test ID - 074 */
        assertEquals("Trump not properly selected", "C", user1.getTrump("   c"));
        /* Test ID - 075 */
        assertEquals("Trump not properly selected", "H", user1.getTrump("  ♥"));

        // Corner Test Cases to test with messy user input: excess letters provided
        /* Test ID - 076 */
        assertEquals("Trump not properly selected", "D", user1.getTrump("♦qq"));
        /* Test ID - 077 */
        assertEquals("Trump not properly selected", "S", user1.getTrump("S s"));
        /* Test ID - 078 */
        assertEquals("Trump not properly selected", "C", user1.getTrump(" Cz"));
        /* Test ID - 079 */
        assertEquals("Trump not properly selected", "H", user1.getTrump("♥ 1 "));
        /* Test ID - 080 */
        assertEquals("Trump not properly selected", "D", user1.getTrump("      D1  "));
        /* Test ID - 081 */
        assertEquals("Trump not properly selected", "S", user1.getTrump("  S2"));
        /* Test ID - 082 */
        assertEquals("Trump not properly selected", "C", user1.getTrump("♣D "));
        /* Test ID - 083 */
        assertEquals("Trump not properly selected", "H", user1.getTrump("  h2"));
        /* Test ID - 084 */
        assertEquals("Trump not properly selected", "D", user1.getTrump("    do 0s"));
    }

    @Test
    public void testPlayCInTurn() {
        //  Normal testing to check if method gives cards in users hand

        /* Test ID - 085 */
        PlayCard testCard1 = user1.playCInTurn("9C");
        assertEquals("Card not properly selected", "9 of ♣", testCard1.toString());
        /* Test ID - 086 */
        PlayCard testCard2 = user1.playCInTurn("KS");
        assertEquals("Card not properly selected", "K of ♠", testCard2.toString());
        /* Test ID - 087 */
        PlayCard testCard3 = user1.playCInTurn("AH");
        assertEquals("Card not properly selected", "A of ♥", testCard3.toString());
        /* Test ID - 088 */
        PlayCard testCard4 = user1.playCInTurn("10D");
        assertEquals("Card not properly selected", "10 of ♦", testCard4.toString());


        // Normal testing to check if method returns null for cards not in hand.

        /* Test ID - 089 */
        testCard1 = user1.playCInTurn("9D");
        assertNull("No Card should be selected", testCard1);
        /* Test ID - 090 */
        testCard2 = user1.playCInTurn("AS");
        assertNull("No Card should be selected", testCard2);
        /* Test ID - 091 */
        testCard3 = user1.playCInTurn("2H");
        assertNull("No Card should be selected", testCard3);
        /* Test ID - 092 */
        testCard4 = user1.playCInTurn("JD");
        assertNull("No Card should be selected", testCard4);

        // Normal Case check if methods handles symbol suits

        /* Test ID - 093 */
        testCard1 = user2.playCInTurn("2♦");
        assertEquals("Card not properly selected", "2 of ♦", testCard1.toString());
        /* Test ID - 094 */
        testCard2 = user2.playCInTurn("2♠");
        assertEquals("Card not properly selected", "2 of ♠", testCard2.toString());
        /* Test ID - 095 */
        testCard3 = user2.playCInTurn("2♥");
        assertEquals("Card not properly selected", "2 of ♥", testCard3.toString());
        /* Test ID - 096 */
        testCard4 = user2.playCInTurn("J♥");
        assertEquals("Card not properly selected", "J of ♥", testCard4.toString());
        /* Test ID - 097 */
        PlayCard testCard5 = user2.playCInTurn("4♣");
        assertEquals("Card not properly selected", "4 of ♣", testCard5.toString());
        /* Test ID - 098 */
        PlayCard testCard6 = user2.playCInTurn("2♣");
        assertEquals("Card not properly selected", "2 of ♣", testCard6.toString());
    }

    @Test
    public void testPlayCInTurnMessy()
    {
        // Corner Test Cases to test with messy user input: unwanted spaces

        /* Test ID - 099 */
        PlayCard testCard1 = user1.playCInTurn("9 ♣");
        assertEquals("Card not properly selected", "9 of ♣", testCard1.toString());
        /* Test ID - 100 */
        PlayCard testCard2 = user1.playCInTurn("  KS");
        assertEquals("Card not properly selected", "K of ♠", testCard2.toString());
        /* Test ID - 101 */
        PlayCard testCard3 = user1.playCInTurn("  A♥  ");
        assertEquals("Card not properly selected", "A of ♥", testCard3.toString());
        /* Test ID - 102 */
        PlayCard testCard4 = user1.playCInTurn(" 10  D  ");
        assertEquals("Card not properly selected", "10 of ♦", testCard4.toString());


        // Corner Test Cases to test with messy user input: no capitalization and excess letter

        /* Test ID - 103 */
        testCard1 = user2.playCInTurn("2 ♦hj");
        assertEquals("Card not properly selected", "2 of ♦", testCard1.toString());
        /* Test ID - 104 */
        testCard2 = user2.playCInTurn("2  ♠  S");
        assertEquals("Card not properly selected", "2 of ♠", testCard2.toString());
        /* Test ID - 105 */
        testCard3 = user2.playCInTurn("2♥h  ");
        assertEquals("Card not properly selected", "2 of ♥", testCard3.toString());
        /* Test ID - 106 */
        testCard4 = user2.playCInTurn("j♥  2");
        assertEquals("Card not properly selected", "J of ♥", testCard4.toString());
        /* Test ID - 107 */
        PlayCard testCard5 = user2.playCInTurn("4♣113");
        assertEquals("Card not properly selected", "4 of ♣", testCard5.toString());
        /* Test ID - 108 */
        PlayCard testCard6 = user2.playCInTurn("  2   ♣1  ");
        assertEquals("Card not properly selected", "2 of ♣", testCard6.toString());
    }

    @Test
    public void testPlayCInTurnExtreme()
    {
        // The most extreme cases that the method can handle: no capitalization, excess letter and 'of' in the middle

        /* Test ID - 109 */
        PlayCard testCard1 = user2.playCInTurn("2 of♦hj");
        assertEquals("Card not properly selected", "2 of ♦", testCard1.toString());
        /* Test ID - 110 */
        PlayCard testCard2 = user2.playCInTurn("2  of  ♠  S");
        assertEquals("Card not properly selected", "2 of ♠", testCard2.toString());
        /* Test ID - 111 */
        PlayCard testCard3 = user2.playCInTurn("2of♥");
        assertEquals("Card not properly selected", "2 of ♥", testCard3.toString());
        /* Test ID - 112 */
        PlayCard testCard4 = user2.playCInTurn("j  of ♥");
        assertEquals("Card not properly selected", "J of ♥", testCard4.toString());
        /* Test ID - 113 */
        PlayCard testCard5 = user2.playCInTurn("4of♣1131");
        assertEquals("Card not properly selected", "4 of ♣", testCard5.toString());
        /* Test ID - 114 */
        PlayCard testCard6 = user2.playCInTurn("  2   of     ♣1  ");
        assertEquals("Card not properly selected", "2 of ♣", testCard6.toString());
    }

}
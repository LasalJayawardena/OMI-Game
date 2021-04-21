package tests;

import Game.PlayCard;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayCardTest {

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

    }

    @Test
    public void compareTo() {

        // Check if cards with less value are correctly identified
        /* Test ID - 023 */
        assertEquals( "Cards should be identified as Smaller", -1, p1.compareTo(p3));
        //  Check if it is the Same with non integer Cards : (A, K, Q, J)
        /* Test ID - 024 */
        assertEquals( "Cards should be identified as Smaller", -1, p10.compareTo(p2));
        /* Test ID - 025 */
        assertEquals( "Cards should be identified as Smaller", -1, p9.compareTo(p10));


        // Check if cards with Higher value are correctly identified
        /* Test ID - 026 */
        assertEquals( "Cards should be identified as Higher", 1, p4.compareTo(p8));
        //  Check if it is the Same with non integer Cards : (A, K, Q, J)
        /* Test ID - 027 */
        assertEquals( "Cards should be identified as Higher", 1, p2.compareTo(p4));
        /* Test ID - 028 */
        assertEquals( "Cards should be identified as Higher", 1, p10.compareTo(p9));


        // Check if cards are equal in value wih no regard of suit
        /* Test ID - 029 */
        assertEquals( "Different Suits influence Value", 0, p5.compareTo(p6));
        /* Test ID - 030 */
        assertEquals( "Different Suits influence Value", 0, p5.compareTo(p7));
        /* Test ID - 031 */
        assertEquals( "Different Suits influence Value", 0, p5.compareTo(p8));
        /* Test ID - 032 */
        assertEquals( "Different Suits influence Value", 0, p6.compareTo(p7));
        /* Test ID - 033 */
        assertEquals( "Different Suits influence Value", 0, p6.compareTo(p8));
        /* Test ID - 034 */
        assertEquals( "Different Suits influence Value", 0, p7.compareTo(p8));

    }

    @Test
    public void testToString()
    {
        //  Test for normal cards
        /* Test ID - 035 */
        assertEquals( "Cards is improperly printed", "2 of ♣", p6.toString());

        //  Test for picture cards
        /* Test ID - 036 */
        assertEquals( "Cards is improperly printed", "K of ♠", p4.toString());

        //  Test for cards with value 10
        /* Test ID - 037 */
        assertEquals( "Cards is improperly printed", "10 of ♦", p3.toString());
    }
}
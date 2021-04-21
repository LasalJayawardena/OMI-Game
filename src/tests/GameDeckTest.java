package tests;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import  Game.*;

public class GameDeckTest {

    GameDeck GD;
    GameDeck GD2;

    @Before
    public void setUp() {
        GD = new GameDeck();
        GD2 = new GameDeck();
    }

    @Test
    public void testDealCards() throws Exception
    {
        int size = GD.GetSize();
        //   Check if the deck deals right amount of cards

        /* Test ID - 001 */
        GD.DealCards(4);
        assertEquals("Cards Dealt improperly", size-4, GD.GetSize());
        size -= 4;

        /* Test ID - 002 */
        GD.DealCards(2);
        assertEquals("Cards Dealt improperly", size-2, GD.GetSize());
        size -= 2;

        // corner case to check whether deck can deal all cards
         /* Test ID - 003 */
        GD2.DealCards(GD2.GetSize());
        assertEquals("All Cards are not dealt properly", 0, GD2.GetSize());

        //  Check if deck doesnt deals cards for negative amounts or 0
         /* Test ID - 004 */
        GD.DealCards(-4);
        assertEquals("Cards Dealt improperly", size, GD.GetSize());

         /* Test ID - 005 */
        GD.DealCards(0);
        assertEquals("Cards Dealt improperly",  size, GD.GetSize());

        //  Check if Deck deals more cards than it holds
        /* Test ID - 006 */
        try
        {
            GD.DealCards(40);
            fail("Dealt more cards than max amount");
        }catch(Exception ignored){}

        /* Test ID - 007 */
        try
        {
            GD.DealCards(33);
            fail("Dealt more cards than max amount");
        }catch(Exception ignored){}


    }

    @Test
    public void testReset_deck() throws Exception
    {
        // Check if any error occurs when resetting a full deck
        /* Test ID - 008 */
        GD.reset_deck();
        assertEquals("Deck not reset properly", 32, GD.GetSize());

        //  Check for normal cases
        /* Test ID - 009 */
        GD2.DealCards(4);
        GD2.reset_deck();
        assertEquals("Deck not reset properly", 32, GD2.GetSize());

        // Check whether it resets an empty deck properly
        /* Test ID - 010 */
        GD.DealCards(32);
        GD.reset_deck();
        assertEquals("Deck not reset properly", 32, GD.GetSize());

    }
}

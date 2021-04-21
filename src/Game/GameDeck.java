package Game;

import java.util.ArrayList;
import java.util.Random;

public class GameDeck {

    final String[] suit  = {"H", "S", "D", "C"};
    final String[] values = {"7", "8", "9","10", "J", "Q", "K", "A"};

    private final ArrayList<PlayCard> deck;

    public GameDeck()
    {
        this.deck = new ArrayList<>();
        this.populateDeck();
        this.ShuffleDeck();
    }

    /*
    * Method to add all cards to deck.
    * Combines all possible suits with each value.
    */
    private void populateDeck()
    {
        for(String c_S : this.suit)
        {
            for(String c_V : this.values)
            {
                PlayCard newCard = new PlayCard(c_V, c_S);
                this.deck.add(newCard);
            }
        }
    }

    /*
    * Method to Shuffle deck effectively.
    * Implemented Fisher–Yates algorithm to shuffle the cards.
    * Because it has a Time complexity of O(n) and the after the shuffle the randomness is also good.
    */
    private void ShuffleDeck()
    {
        Random deckR = new Random();

        for (int index = this.deck.size()-1; index > 0; index--)
        {
            int randI = deckR.nextInt(index+1);

            PlayCard Temp_Card = this.deck.get(index);
            PlayCard Random_Card = this.deck.get(randI);

            this.deck.set(index, Random_Card);
            this.deck.set(randI, Temp_Card);
        }
    }

    /*
    * Method use to deal cards of a certain number from the deck created.
    * Method simulates the card being removed from top of the deck.
    * And returns the removed cards as an ArrayList
    */
    public ArrayList<PlayCard> DealCards(int nCards) throws InvaildNum
    {
        //   Validation to check if requested number is less than the number of cards available.
        //   Throw a custom error if validation fails.
        if (nCards > this.deck.size())
        {
            String msg = "You cannot remove more than "+this.deck.size()+" from current deck";
            throw new InvaildNum(msg);
        }

        ArrayList<PlayCard> dealtCards = new ArrayList<>();
        for(int n=1; n<= nCards; n++)
        {
            PlayCard removedCard = this.deck.remove(0);
            dealtCards.add(removedCard);
        }
        return dealtCards;
    }

    /*
    *  Method clears all the cards in current deck.
    *  Then populates it and shuffles the cards.
    */
    public void reset_deck()
    {
        this.deck.clear();
        this.populateDeck();
        this.ShuffleDeck();
    }

    /*
    * Returns Number of Cards in the deck.
    */
    public int GetSize()
    {
        return this.deck.size();
    }

    @Override
    public String toString()
    {
        //  Prints all cards in deck separated by commas.
        StringBuilder all_cards = new StringBuilder();
        for(PlayCard card : this.deck)
        {
            all_cards.append(card).append(", ");
        }
        return all_cards.substring(0,all_cards.length()-2);
    }

    public static void main(String[] args) throws InvaildNum {
        //  Preliminary tests
        GameDeck D = new GameDeck();
        System.out.println(D);
        System.out.println(D.DealCards(4));
    }
}


/*
* Custom Error used for Validation when dealing cards.
*/
class InvaildNum extends  Exception{

    InvaildNum(String msg){
        super(msg);
    }

}
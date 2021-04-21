package Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class Player implements Comparable<Player> {

    private int score;
    private String name;
    protected ArrayList<PlayCard> playerHand;

    // Holds all the available trump options
    final List<String> trumpOptions = Arrays.asList("H", "S", "D", "C");
    //  Maps each non digit suit to an integer value.
    final HashMap<String, Integer> colorValDict = new HashMap<String, Integer>() {{
       put("J",11); put("Q", 12); put("K", 13); put("A", 14);
    }};


    public Player(String name)
    {
        this.score = 0;
        this.name = name;
        this.playerHand = new ArrayList<>();
    }

    public abstract String getTrump(String tInput);
    public abstract PlayCard playCInTurn(String userI);

    /*
    * Method to return a int value for corresponding card value
    * This includes picture cards and Ace.
    */
    protected int mapValue(String cardVal)
    {
        if(colorValDict.containsKey(cardVal))
        {
            return colorValDict.get(cardVal);
        }
        return Integer.parseInt(cardVal);
    }

    /*
    * Method adds a single card to user's hand
    */
    public void addCards(PlayCard card)
    {
        this.playerHand.add(card);
    }

    /*
    * Method adds multiple cards to user's hand
    */
    public void addCards(ArrayList<PlayCard> dealtCards)
    {
        this.playerHand.addAll(dealtCards);
    }

    /*
    * Method to simulate play effect in a game when a card is given.
    * If the card is not in the deck null is returned.
    */
    public PlayCard getCardToPlay(PlayCard card)
    {
        for(PlayCard pC : this.playerHand)
        {
            if(card.suit.equals(pC.suit) && card.value.equals(pC.value))
            {
                this.playerHand.remove(pC);
                return card;
            }
        }
        return null;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getScore()
    {
        return this.score;
    }

    public void addScore(int num)
    {
        this.score += num;
    }

    public void setScore(int num){
        this.score = num;
    }

    /*
    * Returns user hand
    */
    public ArrayList<PlayCard> getPlayerHand() {
        return playerHand;
    }

    /*
    * Returns number of card the user has.
    */
    public int getPlayerDeckSize()
    {
        return this.playerHand.size();
    }


    @Override
    public int compareTo(Player other_player) {
        // Get the Score of current player and other player to be compared.
        int currPlayerVal = this.score;
        int otherPlayerVal = other_player.score;

        if(currPlayerVal > otherPlayerVal)
        {
            return 1;
        } else if(currPlayerVal < otherPlayerVal)
        {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString()
    {
        //  Prints all cards in player's hand separated by commas.
        StringBuilder all_cards = new StringBuilder();
        for(PlayCard card : this.playerHand)
        {
            all_cards.append(card).append(", ");
        }
        if(all_cards.length() <2){
            return "";
        }
        return all_cards.substring(0,all_cards.length()-2);
    }
}

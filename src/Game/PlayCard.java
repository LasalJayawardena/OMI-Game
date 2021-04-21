package Game;

import java.util.HashMap;

public class PlayCard implements Comparable<PlayCard> {

    //  Maps each non digit suit to an integer value.
    final HashMap<String, Integer> colorValDict = new HashMap<String, Integer>(){{
       put("J",11); put("Q", 12); put("K", 13); put("A", 14);
    }};

    // Helpful in displaying cards
    final HashMap<String, String> suitDict = new HashMap<String, String>() {{
       put("H","♥"); put("S","♠"); put("D","♦"); put("C","♣");
    }};

    public final String suit;
    public final String value;
    public final String imgURL;

    public PlayCard(String value, String suit)
    {
        this.suit = suit;
        this.value = value;
        this.imgURL = "Card_Images/"+value+suit+".jpg";
    }

    /*
    *  Helper method in comparing two cards.
    *  Allows to map any card value into an integer.
    *  This includes any color card or Ace cards as well.
    */
    private int mapValue(String cardVal)
    {
        if(this.colorValDict.containsKey(cardVal))
        {
            return this.colorValDict.get(cardVal);
        }
        return Integer.parseInt(cardVal);
    }

    @Override
    public int compareTo(PlayCard other_card) {
        // Get the Card value of current card and card to be compared.
        int currCardVal = this.mapValue(this.value);
        int otherCardVal = this.mapValue(other_card.value);

        if(currCardVal > otherCardVal)
        {
            return 1;
        } else if(currCardVal < otherCardVal)
        {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString()
    {
        // Helpful when printing a crd
        return this.value+" of "+suitDict.get(this.suit);
    }


    public static void main(String[] args) {
        // Preliminary tests
        PlayCard kj = new PlayCard("D","9");
        System.out.println(kj);
    }

}

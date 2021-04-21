package Game;

import java.util.*;

public class RobotPlayer extends Player{

    public RobotPlayer(String name)
    {
        super(name);
    }

   /*
   *  Method initializes two hashmaps used for trump selection
   *  One used to keep track of number of occurrences for each suit
   *  Other used to hold each card for respective suit.
   */
    private void extractCardList(HashMap<String,ArrayList<PlayCard>>suitCardLists,
                                    HashMap<String,Integer> suitCardOccurrences)
    {
        // This is to initialize a new arraylist for each suit
        // And Initialize every occurrence to 0 at the beginning.
        for(String S : this.trumpOptions)
        {
            suitCardLists.put(S, new ArrayList<>());
            suitCardOccurrences.put(S,0);
        }

        // Add a card to each suit lists.
        // Increment when a card from that suit is found
        for(PlayCard card : this.playerHand)
        {
            String s = card.suit;
            ArrayList<PlayCard> cardList = suitCardLists.get(s);
            cardList.add(card);
            int count = suitCardOccurrences.get(s);
            suitCardOccurrences.put(s, count+1);
        }
    }

    /*
    *  Check whether a set of cards contain an Ace card
    */
    private boolean ContainsAce(ArrayList<PlayCard> cList)
    {
        boolean found = false;
        for(PlayCard card : cList)
        {
            if (card.value.equals("A")) {
                found = true;
                break;
            }
        }
        return found;
    }

    /*
    *  Method calculates an integer sum for a certain set of cards.
    */
    private int CalculateSuitSum(ArrayList<PlayCard> cList)
    {
        int total = 0;
        for(PlayCard card : cList)
        {
            total += this.mapValue(card.value);
        }
        return total;
    }

    /*
    *  Method calculates an integer range for a certain set of cards.
    */
    private int CalculateSuitRange(ArrayList<PlayCard> cList)
    {
        int firstEl = this.mapValue(cList.get(0).value);
        int secondEl = this.mapValue(cList.get(1).value);
        return Math.abs(firstEl - secondEl);
    }

    /*
    *  Helper Method to select trumps when one suit has occurred twice
    */
    private String handleTwo(HashMap<String,ArrayList<PlayCard>>suitCardLists,
                                    HashMap<String,Integer> suitCardOccurrences, String maxSuit)
    {
        // Get Suit with least occurrence
        String lowestSuit = Collections.min(suitCardOccurrences.entrySet(), Map.Entry.comparingByValue()).getKey();

        // Set the current max suit occurrence to 0 to help the suit with next most occurrence
        suitCardOccurrences.put(maxSuit, 0);

        // Suit ith next most occurrences
        String nextMaxSuit = Collections.max(suitCardOccurrences.entrySet(), Map.Entry.comparingByValue()).getKey();

        // Get the cards which belong to maxSuit
        ArrayList<PlayCard> maxList = suitCardLists.get(maxSuit);

        // Get the cards which belong to next maxSuit
        ArrayList<PlayCard> nextMaxList = suitCardLists.get(nextMaxSuit);

        // Check how many time the next max suit occurs , whether it is 1 or 2
        int nextMaxSuitOccur = suitCardOccurrences.get(nextMaxSuit);

        // Get a sum and range for the cards which belong to maxSuit which will be used in bh scenarios
        int maxLSum = CalculateSuitSum(maxList);
        int maxLRange = CalculateSuitRange(maxList);

        if(nextMaxSuitOccur == 2)
        {
            // Select the opposite suit if one has an Ace
            if(ContainsAce(maxList))
            {
                return nextMaxSuit;
            } else if(ContainsAce(nextMaxList))
            {
                return maxSuit;
            }

            int nextMaxLSum = CalculateSuitSum(nextMaxList);

            // Return suit with highest sum
            if(maxLSum > nextMaxLSum)
            {
                return maxSuit;
            } else if(maxLSum < nextMaxLSum)
            {
                  return nextMaxSuit;
            }

            int nextMaxLRange = CalculateSuitRange(nextMaxList);

            // Return suit with the higher range
            if(maxLRange > nextMaxLRange)
            {
                return maxSuit;
            } else if(maxLRange < nextMaxLRange)
            {
                return nextMaxSuit;
            }
            //  Last Case when both are dead equal return first maxSuit (doesnt matter which).
            // Coded below
        }else{
            // Check if the Two cards in maxSuit are very small cards,If so trust to luck
            // and return the suit with no cards ( according to tactics )
            int avg_sum = maxLSum/2;
            if( (avg_sum <10) && maxLRange < 4)
            {
                return lowestSuit;
            }
        }
        return maxSuit;
    }

    /*
    * Method which simulates choosing a trump when it is Robot's turn
    */
    @Override
    public String getTrump(String tInput)
    {
        // First create two hashmaps,  one to keep track of number of occurrences
        // Other to hold the Cards of each type
        HashMap<String,ArrayList<PlayCard>> suitCardLists = new HashMap<>();
        HashMap<String,Integer> suitCardOccurrences = new HashMap<>();

        // Method written to populate the two hashmaps
        extractCardList(suitCardLists, suitCardOccurrences);

        // Choose the suit with most occurrences and how many times it occurs
        String maxSuit = Collections.max(suitCardOccurrences.entrySet(), Map.Entry.comparingByValue()).getKey();
        int maxSuitOccur = suitCardOccurrences.get(maxSuit);

        String trump;
        if (maxSuitOccur >= 3)
        {
            trump =  maxSuit;
        }else if(maxSuitOccur == 2)
        {
            // Make use of my own helper method to get the trump when two suit have occurred twice.
            trump = handleTwo(suitCardLists, suitCardOccurrences, maxSuit);
        } else {
            // Returns the card with highest value when each suit has occurred once.
            PlayCard lowestCard = Collections.max(this.playerHand);
            trump  = lowestCard.suit;
        }

        return trump;
    }

    /*
    *  Method simulating robot playing a card when he leads the trick.
    *  A random card out of robots' hand is played.
    */
    @Override
    public PlayCard playCInTurn(String userI) {
        Random cardR = new Random();
        int randI = cardR.nextInt(this.getPlayerDeckSize());
        PlayCard selected_Card = this.getPlayerHand().get(randI);
        return this.getCardToPlay(selected_Card);
    }


    /*
    *  Method simulating robot playing a card when he does not lead the trick.
    *  Card is returned according to the play Starter and game trumps.
    */
    public PlayCard playCInTurn(PlayCard firstCard, String trump)
    {
        // First create two hashmaps,  one to keep track of number of occurrences
        // Other to hold the Cards of each type
        HashMap<String,ArrayList<PlayCard>> suitCardLists = new HashMap<>();
        HashMap<String,Integer> suitCardOccurrences = new HashMap<>();

        // Method written to populate the two hashmaps
        extractCardList(suitCardLists, suitCardOccurrences);

        // When cards of same suit are available
        ArrayList<PlayCard> sameSuitCards = suitCardLists.get(firstCard.suit);
        if(sameSuitCards != null && sameSuitCards.size() > 0)
        {
            return this.getCardToPlay(Collections.max(sameSuitCards));
        }else{
            // Get cards of trump suit if available
            ArrayList<PlayCard> trumpSuitCards = suitCardLists.get(trump);
            if(trumpSuitCards != null && trumpSuitCards.size() > 0)
            {
                return this.getCardToPlay(Collections.min(trumpSuitCards));
            }
        }

        // Last case when no trumps or cards of same suit is available.
        return this.getCardToPlay(Collections.min(this.playerHand));
    }

    public static void main(String[] args) throws Exception{
        // Preliminary tests
        Scanner sc = new Scanner(System.in);
        String cont;
        do {
            GameDeck GD = new GameDeck();
            ArrayList<PlayCard> l= GD.DealCards(4);
            RobotPlayer r1 = new RobotPlayer("jay");
            r1.addCards(l);
            System.out.println(r1);
            PlayCard c = new PlayCard("10","D");
            System.out.println(r1.playCInTurn(c,"S"));
            cont = sc.nextLine();
        }while (!cont.equals("no"));

    }
}

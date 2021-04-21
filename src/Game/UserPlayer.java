package Game;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserPlayer extends Player {

    //  Maps each symbol suit to a string suit.
    final HashMap<String, String> suitDict = new HashMap<String, String>() {{
       put("♥","H"); put("♠", "S"); put("♦", "D"); put("♣", "C");
    }};

    public UserPlayer(String name)
    {
        super(name);
    }

    /*
    *  Method returns a valid trump given a user input
    *  If the users input is too messy or an invalid option is provided it will return null.
    */
    @Override
    public String getTrump(String tInput)
    {
        if(tInput.trim().equals(""))
        {
            return null;
        }
        String cleanedT = Character.toString(tInput.trim().charAt(0)).toUpperCase();

        if(trumpOptions.contains(cleanedT))
        {
            return cleanedT;
        }else if(this.suitDict.containsKey(cleanedT))
        {
            return this.suitDict.get(cleanedT);
        }
        return null;
    }

    /*
    *  Helper method to map symbol suit to String suit if possible
    */
    private String handleUserCardVal(String val)
    {
        if(this.suitDict.containsKey(val))
        {
            return this.suitDict.get(val);
        }
        return val;
    }

    /*
    *  Helper Method where it takes user input, cleanse it and generate a new card if possible.
    *  If user gives an invalid or very messy input, null will be returned.
    */
    private PlayCard cleanseUserInput(String userInput)
    {
        // Remove spaces
        String cleanedT = userInput.replaceAll("\\s+", "").toUpperCase();
        // remove of
        cleanedT = cleanedT.replace("OF", "");

        if(cleanedT.equals("") || cleanedT.length() < 2)
        {
            return null;
        }

        String userSuit;
        String userValue;
        // Check if user entered a valid card value
        Pattern pattern = Pattern.compile("(\\d{1,2}|[QJKA])");
        Matcher matcher = pattern.matcher(cleanedT);
        if(!matcher.find())
        {
            return null;
        }

        String extract = matcher.group(0);
        if(extract.equals("10") && cleanedT.length() >2)
        {
            userValue = "10";
            userSuit = cleanedT.substring(2,3);
        }else{
            userValue = extract.substring(0,1);
            userSuit = cleanedT.substring(1,2);
        }

        userSuit = handleUserCardVal(userSuit);
        return new PlayCard(userValue, userSuit);
    }

    /*
    * Play Card depending on user Input.
    * Null return if card not in user's deck.
    */
    @Override
    public PlayCard playCInTurn(String userI)
    {
        PlayCard selected_Card = this.cleanseUserInput(userI);
        if(selected_Card == null)
        {
            return null;
        }
        return this.getCardToPlay(selected_Card);
    }

    public static void main(String[] args) throws Exception
    {
        GameDeck GD = new GameDeck();
        ArrayList<PlayCard> l= GD.DealCards(4);
        UserPlayer user = new UserPlayer("Lasal");
        user.addCards(l);
        System.out.println(user.getName());
        System.out.println(user.getTrump("♣"));
        System.out.println(user);
        Scanner sc = new Scanner(System.in);
        System.out.println(user.playCInTurn(sc.nextLine()));
        System.out.println(user);
    }

}

package Game;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TheGame {

    final int TOTAL_TRICKS = 8;

    private UserPlayer userPlayer;
    private ArrayList<Player> playerList;
    private ArrayList<RobotPlayer> robotList;
    private HashMap<Player, Integer> GameTurn;
    private final HashMap<Player, PlayCard> cardPlist;
    private String currTrump;
    private Player playStarter;
    private GameDeck OfficialDeck;
    private LeaderBoard gameLeaderBoard;

    // For Storing Game History Details
    private int userWins;
    private int userDraws;
    private int userGames;

    public TheGame()
    {
        OfficialDeck = new GameDeck();
        this.cardPlist = new HashMap<>();
        initializePlayers();
        initializeTurn();
        this.gameLeaderBoard = new LeaderBoard(this.playerList);
        this.currTrump = "";
    }

    /*
    *  Helper method to initialize each player,player list, robot list and the player-card list.
    */
    private void initializePlayers()
    {
        this.playerList = new ArrayList<>();
        UserPlayer gameUser = new UserPlayer("Player");
        RobotPlayer R1 = new RobotPlayer("Robot 1");
        RobotPlayer R2 = new RobotPlayer("Robot 2");
        RobotPlayer R3 = new RobotPlayer("Robot 3");
        this.userPlayer = gameUser;
        this.playerList.add(gameUser);
        this.playerList.add(R1);
        this.playerList.add(R2);
        this.playerList.add(R3);
        this.robotList = new ArrayList<>();
        this.robotList.add(R1);
        this.robotList.add(R2);
        this.robotList.add(R3);
        for(Player P : this.playerList)
        {
            this.cardPlist.put(P,null);
        }
    }

   /*
    *  Helper method to initialize game turn for each player.
    */
    private void initializeTurn()
    {
        GameTurn = new HashMap<>();
        for(int i=1; i<=this.playerList.size(); i++)
        {
            GameTurn.put(this.playerList.get(i-1), i);
        }
    }

   /*
    *  Change User player name
    */
    public void setUsername(String username){
        this.userPlayer.setName(username);
    }

    public ArrayList<RobotPlayer> getRobotList() {
        return robotList;
    }

    public HashMap<Player, Integer> getGameTurn() {
        return GameTurn;
    }

    public HashMap<Player, PlayCard> getCardPlist() {
        return cardPlist;
    }

    public GameDeck getOfficialDeck() {
        return OfficialDeck;
    }

    public LeaderBoard getGameLeaderBoard() {
        return gameLeaderBoard;
    }

    public Player getPlayStarter() {
        return playStarter;
    }

    public void setPlayStarter(Player playStarter) {
        this.playStarter = playStarter;
    }

    public String getCurrTrump() {
        return currTrump;
    }

    public void setCurrTrump(String currTrump) {
        this.currTrump = currTrump;
    }

    public int getUserWins() {
        return userWins;
    }

    public int getUserDraws() {
        return userDraws;
    }

    public int getUserGames() {
        return userGames;
    }

   /*
    *  Updates each players turn.
    *  Usually called after each trick.
    */
    public void updateTurn()
    {
        for(Player P : this.GameTurn.keySet())
        {
            int turn = this.GameTurn.get(P);
            // if turn is greater 4 which is the number of tricks then make the player start the next trick
            if(++turn > 4){
                turn = 1;
            }
            this.GameTurn.put(P, turn);
        }
    }

   /*
    *  Method to get a player's turn.
    */
    public Player getPlayerTurn(int Turn)
    {
        for(Player player : this.playerList)
        {
            if( this.GameTurn.get(player) == Turn )
            {
                return player;
            }
        }
        return null;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

   /*
    *  Update Score of any player by 2.
    *  Called on the player who wins the trick.
    */
    public void updateScore(Player P)
    {
        this.gameLeaderBoard.updateScore(P, 2);
    }

   /*
    *  Helper method to get the Player who played highest in a gven suit.
    */
    private Player getHighestFromSuit(String S)
    {
        PlayCard max = null;
        Player maxPlayer = null;
        for(Player P : this.playerList)
        {
            PlayCard card = this.cardPlist.get(P);
            if( ( max== null && card.suit.equals(S) ) || ( ( card.suit.equals(S) ) && card.compareTo(max) > 0 ) )
            {
                max = card;
                maxPlayer = P;
            }
        }
        return maxPlayer;
    }

    /*
    *  Returns a winner of a trick check according to game rules.
    *  Make use of cardPList which stores each card any player played.
    */
    public Player getWinner()
    {
        //  Check is start suit is trumps, if so then return player who is highets of that suit.
        //  Else return player who played highest trumps if he exists.
        // In case none played trumps, return highets from starting suit.
        PlayCard firstCard = this.cardPlist.get(this.playStarter);
        Player maxPlayer = getHighestFromSuit(firstCard.suit);
        if( firstCard.suit.equals(this.currTrump) )
        {
            return maxPlayer;
        } else
        {
            Player maxTrumpPlayer = getHighestFromSuit(this.currTrump);
            if (maxTrumpPlayer != null)
            {
                return maxTrumpPlayer;
            }
        }
        return maxPlayer;
    }

   /*
    *  Check if user has followed OMI trickRules.
    *  I.E whether he played another card even though he had cards of same suit as first player's card.
    */
    public Boolean userValidate(PlayCard card, Player user)
    {
        Player firstPlayer=this.playStarter;
        PlayCard firstPCard = this.cardPlist.get(firstPlayer);
        if(user == firstPlayer)
        {
            return true;
        }else{
            int numSame = 0;
            for(PlayCard c : user.playerHand)
            {
                if(c.suit.equals(firstPCard.suit)){
                    numSame++;
                }
            }
            return (firstPCard.suit.equals(card.suit)) || numSame <= 0;
        }
    }

    /*
    *  Method for Console App which handles a single trick.
    */
    private void  SingleTrick()
    {
        for(int i=1; i<=4; i++)
        {
            Player currPlayer = this.getPlayerTurn(i);
            PlayCard card;

            // Robots turn to play
            if(currPlayer != this.userPlayer)
            {
                if(i ==1)
                {
                    card = currPlayer.playCInTurn("");
                }else
                {
                    Player firstPlayer = this.getPlayerTurn(1);
                    card = ((RobotPlayer)currPlayer).playCInTurn(this.cardPlist.get(firstPlayer), this.currTrump);
                }
                System.out.println(card);
            }else
            {
                // Users's turn to play
                Scanner sc = new Scanner(System.in);
                while (true){
                   System.out.println("Trump is "+this.currTrump);
                   System.out.println("Enter Card:");
                   System.out.println("Your cards - "+currPlayer);
                   card = currPlayer.playCInTurn(sc.nextLine());
                   if(card != null){
                        if(!userValidate(card, currPlayer))
                        {
                            currPlayer.addCards(card);
                            System.out.println("Follow Game Rules");
                        }else{
                            break;
                        }
                   }else{
                       System.out.println("Enter card which is in your hand");
                   }
                }
            }
            this.cardPlist.put(currPlayer, card);
        }

        this.updateTurn();

        Player winner = this.getWinner();
        System.out.println(winner.getName()+" won the trick");
        this.updateScore(winner);
    }

    /*
    *  Method for Console App gets trumpfrom user until a valid option is chosen.
    */
    private void getTrump(Player trumpCaller)
    {

        //Robots to get trumps
        if(trumpCaller != this.userPlayer)
        {
            this.currTrump = trumpCaller.getTrump("");
        }
        else{
            Scanner sc = new Scanner(System.in);
            while(true)
            {
                System.out.println("Your cards-"+trumpCaller+"\nYou will choose trumps.\nPlease Enter:");
                String user_t = sc.nextLine();
                String trump= trumpCaller.getTrump(user_t);
                if(trump!= null)
                {
                    this.currTrump = trump;
                    break;
                }
                System.out.println("Please enter a valid suit.\nYour cards-"+trumpCaller);
            }
        }
        //  Set trump calling player to start first
        while (this.playStarter != this.getPlayerTurn(1))
        {
            this.updateTurn();
        }
    }

    private Player GameWinner(){
        return this.gameLeaderBoard.getWinner();
    }

    /*
    *  It is method which gets information from data.txt file.
    *  It gets the number of wins,draws, total games and most importantly whse going to call trumps.
    *  If file is corrupt or any error persists default values will be used.
    */
    public void getTrumpCaller()
    {
        String filepath = "src/Game/data.txt";
        File data = new File(filepath);
        String playStarter = "user";
        int userWins = 0;
        int userDraws = 0;
        int totalGames = 0;
        try{
            Scanner myReader = new Scanner(data);

            playStarter = myReader.nextLine();
            userWins = Integer.parseInt(myReader.nextLine());
            userDraws = Integer.parseInt(myReader.nextLine());
            totalGames = Integer.parseInt(myReader.nextLine());
            myReader.close();

        }catch (Exception e)
        {
             e.printStackTrace();
        }
        //  Setting user as default
        this.playStarter = this.getPlayerTurn(1);
        //   "Robot1"
        for (Player p : this.playerList)
        {
            if(p.getName().equalsIgnoreCase(playStarter))
            {
                this.playStarter = p;
                break;
            }
        }
        this.userWins = userWins;
        this.userDraws = userDraws;
        this.userGames = totalGames;
    }

    /*
    *  Updates total games and total wins to be stored in data.txt .
    */
    public void update_Details()
    {
        if(this.gameLeaderBoard.getWinner() == this.userPlayer)
        {
            this.userWins += 1;
        }else if(this.gameLeaderBoard.getWinner().getScore() == this.userPlayer.getScore())
        {
            this.userDraws +=1;
        }
        this.userGames += 1;
    }

    /*
    *  Method for Save all tracked details needed for next game.
    */
    public void save_Details()
    {
        try{
            FileWriter writer= new FileWriter("src/Game/data.txt");
            //  Select next person to start game
            this.updateTurn();
            writer.write(this.getPlayerTurn(1).getName()+"\n");
            writer.write(this.userWins+"\n");
            writer.write(this.userDraws+"\n");
            writer.write(this.userGames+"\n");
            writer.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /*
    *  Method for Console App which simulates an Entire Game with 8 Tricks (default).
    */
    private void EntireGame() throws Exception{

        // Trump calling face
        Player trumpCaller = this.playStarter;

        trumpCaller.addCards(this.OfficialDeck.DealCards(4));
        this.getTrump(trumpCaller);

        //Deal rest of cards to players
        for(Player p: this.playerList)
        {
            if(p != trumpCaller)
            {
                p.addCards(this.OfficialDeck.DealCards(8));
            }else {
                p.addCards(this.OfficialDeck.DealCards(4));
            }
        }
        // Tricks are handle here
        for(int i =0; i< this.TOTAL_TRICKS; i++)
        {
            this.SingleTrick();
            this.playStarter = this.getPlayerTurn(1);
        }
        // Get winner from all tricks and update details.
        System.out.println("Winner of the entire round is: "+GameWinner().getName());
        System.out.println(this.gameLeaderBoard);
        this.update_Details();
        this.save_Details();
    }

    /*
    *  Method for Console App which resets everything to start a new game.
    */
    private void resetGame()
    {
        OfficialDeck = new GameDeck();
        System.out.println(OfficialDeck);
        this.gameLeaderBoard = new LeaderBoard(this.playerList);
        this.currTrump = "";
        for(Player p: this.playerList){
            p.setScore(0);
            p.playerHand = new ArrayList<>();
        }
    }

    /*
    *  Method for Console App which simulates multiple Games which encapsulates the entire app.
    */
    public void MultiGames() throws Exception
    {
        Scanner sc = new Scanner(System.in);
        do {
            this.getTrumpCaller();
            this.EntireGame();
            this.resetGame();
            System.out.println("Play again?");
        }while(!sc.nextLine().equals("no"));
    }

    public static void main(String[] args) throws Exception {
        TheGame g = new TheGame();
        g.MultiGames();
    }

}

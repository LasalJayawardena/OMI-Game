package OMI_Game;

import Game.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;


public class GamePlatform implements Initializable {

    public String username;

    private TheGame game;
    private LeaderBoard currLB;
    private ArrayList<Player> playerList;
    private ArrayList<RobotPlayer> robotList;
    private HashMap<Player, Integer> GameTurn;
    private HashMap<Player, PlayCard> cardPlist;
    private HashMap<RobotPlayer, ImageView> robotCardImages;
    private GameDeck OfficialDeck;
    private boolean trumpChosen = false;
    private int trick_number = 1;
    private Player playStarter;
    private Player user;
    private String currTrump;
    private String trickHistory = "";

    // Total Number of tricks in a game
    final int TOTAL_TRICKS = 8;
    // Helps to map each suit to acommon symbol suit to ensure consistency throughout the Game
    final HashMap<String, String> suitDict = new HashMap<String, String>() {{
       put("H","♥"); put("S","♠"); put("D","♦"); put("C","♣");
    }};
    // Fixed path to the default back image of the card
    final String backImage = "Card_Images/back.jpg";


    @FXML
    private ListView<String> rankings= new ListView<>();

    @FXML
    private ListView<ImageView> userDeck = new ListView<>();

    @FXML
    private GridPane entireGrid;

    @FXML
    private ImageView r1Card = new ImageView();

    @FXML
    private ImageView r2Card = new ImageView();

    @FXML
    private ImageView r3Card = new ImageView();

    @FXML
    private Label Title;

    @FXML
    private TextField userBox;

    @FXML
    private Label playBox;

    @FXML
    private Label instructBox;

    @FXML
    private Label messageBox;

    @FXML
    private Label trumpBox;

    @FXML
    private Label numTricks;

    @FXML
    private Label trickStarter;

    @FXML
    private Label trickMoves;

    @FXML
    public void ExitProgram()
    {
        // Exit program
        System.exit(0);
    }

    @FXML
    public void handleSuitBtn(Event e)
    {
        // Handle action when user clicks on each suit image.
        // Created to give better UI experience.
        // Also if text box is not empty, append instead of just replacing in the user box .

        ImageView suitBtn = (ImageView) e.getSource();
        String id  = suitBtn.getId();
        String suit;
        switch (id) {
            case "diamondBtn":
                suit = "♦";
                break;
            case "heartBtn":
                suit = "♥";
                break;
            case "spadeBtn":
                suit = "♠";
                break;
            default:
                suit = "♣";
                break;
        }
        String userInp = userBox.getText();
        if(userInp == null || userInp.equals(""))
        {
            userBox.setText(suit);
        }else{
            userBox.setText(userInp+" "+suit);
        }
    }

    @FXML
    public void handleUserMoves()
    //  Method written to handle whenever user enters and submit hiw move.
    //  Includes in handling user entering for trumps and also card moves for each trick.
    {
        String user_input = userBox.getText();
        userBox.setText("");

        if(!this.trumpChosen){
            // Handle user move to enter trumps
            this.inUserTrump(user_input);
        }else
        {
            // Handle user move for each trick move.

            // Validate user's move.
            PlayCard user_card = this.userValidate(user_input);
            // Invalid move by user.
            if(user_card == null) return;
            // Play all remaining cards of players and handle trick moves.
            this.playGCards(user_card);
            // Winner phase
            this.winnerPhase();
            // After round handle pre trick or end Game.
            if(this.trick_number < TOTAL_TRICKS)
            {
                this.preTurn();
            }else{
                // After Game ends
                game.update_Details();
                game.save_Details();
                continueGame();
            }

        }
    }

    /*
    *  Method to handle when game is over and directs user to final window.
    */
    private void continueGame()
    {
        // load fxml and create stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("continue.fxml"));
        Stage newStage = new Stage();
        Parent root= null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert root != null;
        newStage.setScene(new Scene(root, 1400, 800));
        // Get controller to call necessary methods
        ContinueGame continueController = loader.getController();
        int userDraws = game.getUserDraws();
        int userWins = game.getUserWins();
        int userGames = game.getUserGames();
        LeaderBoard gameL = this.currLB;
        // Pass the updated details to final window.
        // User wins, draws, total games along with the game leaderboard.
        continueController.setdetails(userDraws, userWins, userGames, gameL, user);
        newStage.show();

        // Close current window.
        Stage stage = (Stage) entireGrid.getScene().getWindow();
        stage.close();
    }


    /*
    *  Method to handle cards played after user's move.
    */
    private void playGCards(PlayCard user_card)
    {
        // Put user's card into player-card hash map.
        // And update the trick move history.
        this.cardPlist.put(this.user, user_card);
        this.trickHistory += "You played " +user_card.value+" of "+this.suitDict.get(user_card.suit)+"\n";

        int userTurn = this.GameTurn.get(user);
        // Play cards for robots who are after user.
        for(int i=userTurn+1; i<=4; i++)
        {
            // Get robot according to game turn.
            Player p = game.getPlayerTurn(i);
            // Play card from robots's hand.
            PlayCard card = ((RobotPlayer)p).playCInTurn(user_card, this.currTrump);
            // Put robot's card into player-card hash map.
            // And update the trick move history.
            this.cardPlist.put(p, card);
            this.trickHistory += p.getName()+" played " +card.value+" of "+this.suitDict.get(card.suit)+"\n";
        }
    }

    /*
    *  Method which playscard before user's turn.
    */
    private void preTurn()
    {

        this.trick_number++;
        // Set trick number
        this.numTricks.setText("Trick : "+this.trick_number);
        // Get user's turn
        int userTurn = this.GameTurn.get(user);
        for(int i=1; i<=4; i++)
        {
            Player p = game.getPlayerTurn(i);
            if(i == 1 && p != user)
            {
                // Robot to play card when it the playStarter.
                PlayCard card = p.playCInTurn("");
                this.cardPlist.put(p, card);
                this.playBox.setText(p.getName()+" played "+card.value+" of "+this.suitDict.get(card.suit));
                this.instructBox.setText("Play card");
                this.trickHistory += p.getName()+" started with " +card.value+" of "+this.suitDict.get(card.suit)+"\n";
            }
            else if(i < userTurn && p!= user)
            {
                // Robot to play card after playStarter.
                PlayCard card = ((RobotPlayer)p).playCInTurn(this.cardPlist.get(this.playStarter), this.currTrump);
                this.cardPlist.put(p, card);
                this.trickHistory += p.getName()+" played " +card.value+" of "+this.suitDict.get(card.suit)+"\n";
            }else{
                // Put blank car  for users and robot after user's turn.
                this.cardPlist.put(p,null);
            }
        }
        // Display Robot's images from player-card hash map.
        this.displayRobotImages();
    }

    /*
    *  Method called to find winner after each trick.
    *  Also Update Ui accordingly
    */
    private void winnerPhase()
    {
        // Display user cards and robot images
        this.display_userCards();
        this.displayRobotImages();
        // Display Winner
        Player winner = game.getWinner();
        game.updateScore(winner);
        messageBox.setText("The winner is "+winner.getName());
        // Update play Starter
        game.updateTurn();
        this.playStarter = game.getPlayerTurn(1);
        game.setPlayStarter(this.playStarter);
        // Update rankings,  set trick starter name and display trick move history.
        this.updateRanking();
        this.trickStarter.setText(this.playStarter.getName());
        this.trickHistory =  "Trick : "+this.trick_number+"\n--------------\n"+this.trickHistory;
        this.trickMoves.setText(this.trickHistory);
        this.trickHistory="";
    }

    /*
    *  Helper Method which validates user's move for each trick move.
    */
    private PlayCard userValidate(String user_input)
    {
        PlayCard user_card = user.playCInTurn(user_input);
        if (user_card == null)
        {
            // User's move has not played card from his hand.
            messageBox.setText("Enter a Card from your hand");
            return null;
        }
        if(!game.userValidate(user_card, user))
        {
            // User's move does not follow game rules.
            user.addCards(user_card);
            String suitStartP = this.cardPlist.get(this.playStarter).suit;
            messageBox.setText("Follow Game Rules.\n"+"Play a card from "+this.suitDict.get(suitStartP)+" suit.");
            return null;
        }

        // Return card if all is valid.
        return user_card;
    }

    /*
    *  Helper Method which handles UI and logic when user chooses trumps
    */
    private void inUserTrump(String user_input)
    {
        String trump= user.getTrump(user_input);
        if(trump!=null){
            this.trumpChosen = true;
            this.currTrump = trump;
            game.setCurrTrump(trump);
            trumpBox.setText("Trump : "+this.suitDict.get(trump));
            messageBox.setText("User chose trump as "+this.suitDict.get(trump));
            // Deals rest ofcards to user
            this.dealRestCards();
            instructBox.setText("Play Card");
            playBox.setText("You start the Game!");
            Platform.runLater(new TitleChanger("", this.Title, 2000));
        }else{
            messageBox.setText("Invalid Trump option.\nSelect from '♦' '♥' '♠' '♣'");
        }
    }

    /*
    *  Helper Method which displays all robot cards appropriately.
    */
    private void displayRobotImages()
    {
        for(RobotPlayer p : this.robotList)
        {
            ImageView robotImage = this.robotCardImages.get(p);
            if(this.cardPlist.get(p) == null)
            {
                Image image = new Image(backImage);
                robotImage.setImage(image);
            }else{
                Image image = new Image(this.cardPlist.get(p).imgURL);
                robotImage.setImage(image);
            }
        }
    }


    /*
    *  Helper methodd that Deals cards after trumps are called accordingly.
    */
    private void dealRestCards(){
        for(Player p :this.playerList)
        {
            if(p != this.playStarter)
            {
                // For players who didnt call trumps
                try {
                    p.addCards(this.OfficialDeck.DealCards(8));
                } catch (Exception invaildNum) {
                    invaildNum.printStackTrace();
                }
            }else{
                // For player who called trumps (has 4 cards already)
                try {
                    p.addCards(this.OfficialDeck.DealCards(4));
                } catch (Exception invaildNum) {
                    invaildNum.printStackTrace();
                }
            }
        }
        this.display_userCards();
    }

    /*
    *  Updates ranking of leaderboard, according to player scores.
    */
    public void updateRanking()
    {
        this.rankings.getItems().clear();
        this.playerList.sort(Collections.reverseOrder());
        ArrayList<Player> tempList = new ArrayList<>(this.playerList);
        for(Player p : tempList){
            // Display the relative ranks for each payer
            String rank_result;
            rank_result = currLB.getRank(p) + ".\t"+ p.getName()+"\t-\t"+p.getScore()+" points";
            this.rankings.getItems().add(rank_result);
        }
    }

    @FXML
    public void DisplayImages()
    {
        // Method written to display the card selected from user's deck from UI to the input box
        // Improvement to increase interactivity.
        try{
            ObservableList<ImageView> cardSelected = userDeck.getSelectionModel().getSelectedItems();
            if(cardSelected == null)
            {
                return;
            }

            Object info = cardSelected.get(0).getUserData();
            if(suitDict.containsKey(info)){
                userBox.setText(suitDict.get(info));
            }else
            {
                userBox.setText(info.toString());
            }
        }catch (Exception ignored)
        {

        }
    }


    /*
    *  Helper method that diplays the latest cards in user's hand.
    */
    private void display_userCards()
    {
        userDeck.getItems().clear();
        ArrayList<PlayCard> userHand = this.user.getPlayerHand();
        for (PlayCard playCard : userHand) {
            Image imc = new Image(playCard.imgURL);
            ImageView card = new ImageView(imc);

            card.setUserData(playCard);

            card.setFitHeight(300);
            card.setFitWidth(200);
            userDeck.getItems().add(card);
        }
    }

    /*
    *  Helper method that updates the latest cards for each robot.
    */
    private void setRobotImages()
    {
        this.robotCardImages.put(this.robotList.get(0),r1Card);
        this.robotCardImages.put(this.robotList.get(1),r2Card);
        this.robotCardImages.put(this.robotList.get(2),r3Card);
    }

    /*
    *  Some preparation done when user is going to call trumps.
    *  Called from StartGame.
    */
    public void initialize_cards(){
        if(user != this.playStarter)
        {
            return;
        }
        // Fixed back image
        Image image = new Image(backImage);

        // Add 4 cards to user'shand
        ArrayList<PlayCard> userHand = this.user.getPlayerHand();
        try {
            userHand.addAll(this.OfficialDeck.DealCards(4));
        } catch (Exception invaildNum) {
            invaildNum.printStackTrace();
        }

        // Display the cards in user's deck UI
        // 4 dealt cards and 4 blank cards
        for(int i=0; i<8; i++)
        {
            if(i<4)
            {
                Image imc = new Image(userHand.get(i).imgURL);
                ImageView card = new ImageView(imc);
                // Additional improveent to which sets suit instead of card
                card.setUserData(userHand.get(i).suit);
                card.setFitHeight(300);
                card.setFitWidth(200);
                userDeck.getItems().add(card);
            }else{
                ImageView blankCard = new ImageView(image);
                blankCard.setUserData("");
                blankCard.setFitHeight(300);
                blankCard.setFitWidth(200);
                userDeck.getItems().add(blankCard);
            }
        }
        // Display robot images
        r1Card.setImage(image);
        r2Card.setImage(image);
        r3Card.setImage(image);
        this.setRobotImages();
    }

    /*
    *  Some preparation done when a robot is going to call trumps.
    *  Called from StartGame.
    */
    public void playRtrumps()
    {
        if(this.playStarter == this.user)
        {
            return;
        }

        // Deal 4 cards
        ArrayList<PlayCard> robotHand = this.playStarter.getPlayerHand();
        try {
            robotHand.addAll(this.OfficialDeck.DealCards(4));
        } catch (Exception invaildNum) {
            invaildNum.printStackTrace();
        }

        // Choose trumps and display message foruser
        this.trumpChosen = true;
        this.currTrump = this.playStarter.getTrump("");
        game.setCurrTrump(this.currTrump);
        trumpBox.setText("Trump : "+this.suitDict.get(this.currTrump));
        messageBox.setText(this.playStarter.getName()+" chose trump as "+this.suitDict.get(this.currTrump));

        //  Set player who called trumps to start game
        while (this.GameTurn.get(playStarter) != 1)
        {
            game.updateTurn();
        }

        // Welcome title
        Platform.runLater(new TitleChanger("", this.Title, 2000));
        this.trickStarter.setText(this.playStarter.getName());
        Image image = new Image(backImage);
        // Set Robot images
        r1Card.setImage(image);
        r2Card.setImage(image);
        r3Card.setImage(image);
        this.setRobotImages();
        this.dealRestCards();
        // Get ready for trick 0ne
        this.trick_number --;
        this.preTurn();
    }

    /*
    *  Method to get data from startGame stage to current stage
    *  Called from StartGame.
    */
    public void receiveData(String name) {
        this.username = name;
    }

    /*
    *  Sets the welcome text accordingly for user to start t0 play the game
    *  Called from StartGame.
    */
    public void  PlayGame(){
        Title.setText("Welcome to the Game "+this.username);
        if(this.username.trim().equals("")){
            // If no name is provided
            Title.setText("Welcome to the Game Player");
            return;
        }
        game.setUsername(this.username);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Initialize Game and LeaderBoard
        game = new TheGame();
        currLB = game.getGameLeaderBoard();

        //  Get playerlist, official deck, rbotlist, game turn hashmap and player-card map.
        this.playerList = game.getPlayerList();
        this.OfficialDeck = game.getOfficialDeck();
        this.robotList = game.getRobotList();
        this.GameTurn = game.getGameTurn();
        this.cardPlist = game.getCardPlist();

        //  Set user to play first as default
        game.setPlayStarter(game.getPlayerTurn(1));
        this.playStarter = game.getPlayStarter();

        //  Set user player
        this.user = game.getPlayerTurn(1);

        //  Set reference to trump
        this.currTrump = game.getCurrTrump();

        //  configure robot images
        robotCardImages = new HashMap<>();

        // Set playStarter from saved data.txt file
        game.getTrumpCaller();
        this.playStarter = game.getPlayStarter();
        this.trickStarter.setText(this.playStarter.getName());
    }
}

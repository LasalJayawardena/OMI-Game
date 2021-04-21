package OMI_Game;

import Game.LeaderBoard;
import Game.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;


public class ContinueGame {

    @FXML
    private Label winner;

    @FXML
    private Label isUna;

    @FXML
    private Label rank1;

    @FXML
    private Label rank2;

    @FXML
    private Label rank3;

    @FXML
    private Label rank4;

    @FXML
    private Label totalgame;

    @FXML
    private Label totalwin;

    @FXML
    private Label totalDraw;

    @FXML
    private Label totalLoss;

    private Player user;

    @FXML
    public void endGame()
    {
        System.exit(0);
    }

    @FXML
    public void restartGame()
    {
        // Method which directs user to play OMI game after he has already finished one.

        // load FXML and create stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("OmiPlatform.fxml"));
        Stage newStage = new Stage();
        Parent root= null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert root != null;
        newStage.setScene(new Scene(root, 1400, 1000));
        // get controller
        GamePlatform controller = loader.getController();
        // Pass username to Game platform
        controller.receiveData(this.user.getName());
        // Sets title appropriately to welcome to to play the game.
        controller.PlayGame();
        // Sets intital Ranking
        controller.updateRanking();
        // Sets cards for calling trumps appropriately if user is the playSatrter
        controller.initialize_cards();
        // Sets cards for calling trumps appropriately if a robot is the playSatrter
        controller.playRtrumps();

        // show game platform
        newStage.show();
        newStage.setTitle("Omi Game");

        // Close current stage
        Stage stage = (Stage) winner.getScene().getWindow();
        stage.close();
    }

    /*
    *  Method which helps transfer informaion from Game platform to current stage.
    *  Get user wins, draws, total games, Game Leader board and user Player instance.
    */
    public void setdetails(int userDraws, int userWins, int userGames, LeaderBoard gameL, Player user)
    {
        this.user = user;
        ArrayList<Player> playerList = gameL.getPlayerList();
        totalgame.setText(String.valueOf(userGames));
        totalwin.setText(String.valueOf(userWins));
        totalDraw.setText(String.valueOf(userDraws));
        totalLoss.setText(String.valueOf(userGames - (userDraws + userWins)));

        Player plauerRank1 = playerList.get(0);
        Player plauerRank2 = playerList.get(1);
        Player plauerRank3 = playerList.get(2);
        Player plauerRank4 = playerList.get(3);

        winner.setText("The Winner is "+plauerRank1.getName());
        if(plauerRank1.getScore() == plauerRank2.getScore())
        {
            // If many players got same score as first player.
            isUna.setText("By Unanimous Decision");
        }else{
            isUna.setText("Congratulations "+plauerRank1.getName()+" !!!");
        }

        // Set each score
        rank1.setText(plauerRank1.getName() +" - " + plauerRank1.getScore());
        rank2.setText(plauerRank2.getName() +" - " + plauerRank2.getScore());
        rank3.setText(plauerRank3.getName() +" - " + plauerRank3.getScore());
        rank4.setText(plauerRank4.getName() +" - " + plauerRank4.getScore());
    }

}

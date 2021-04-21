package OMI_Game;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartGame implements Initializable {

    private Thread progThread;

    static boolean clicked = false;

    @FXML
    private Button UserBtn;

    @FXML
    private TextField UserIn;

    @FXML
    private ProgressBar prog;

    /*
    *  Task object created to simulate the game laoding effect.
    *  First it will load upto 60% , nad waits till the user enters the nameor exits.
    *  If user enters name or decides to continue then load the reast 40 %.
    */
    Task task = new Task<Void>() {
    @Override public Void call() {
        final int max = 2000;
        for (int i = 1; i <= max; i++) {
            try{
                Thread.sleep(2);
            }catch (Exception e){
                e.printStackTrace();
            }
            if(!clicked && i > 3*max/5){
                i--;
            }
            updateProgress(i, max);
        }
        return null;
        }
    };


    /*
    *  Directs user to the Main Game Window where the game will take place
    */
    public void newWin()
    {
        // Load FXML and create new stage
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
        // Get the controller of new stage
        GamePlatform controller = loader.getController();
        // Transfer the user name from current stage to Game Platfrom.
        controller.receiveData(UserIn.getText());
        // Sets title appropriately to welcome to to play the game.
        controller.PlayGame();
        // Sets intital Ranking
        controller.updateRanking();
        // Sets cards for calling trumps appropriately if user is the playSatrter
        controller.initialize_cards();
        // Sets cards for calling trumps appropriately if a robot is the playSatrter
        controller.playRtrumps();
        // show the created stage
        newStage.show();
        newStage.setTitle("Omi Game");

        // Clsoe current stage
        Stage stage = (Stage) UserBtn.getScene().getWindow();
        stage.close();
    }

    public void onSubmit() throws InterruptedException
    {
        // Called when user decidesto play game
        // Finishes the progress bar loading and then calls the new stage to action
        clicked = true;
        synchronized (this.progThread){
            this.progThread.wait();
        }
        Platform.runLater(this::newWin);
    }

    @FXML
    public void checkEnter(KeyEvent event) throws InterruptedException
    {
        // onSubmit is called when user enters the enter key within the input
        if(event.getCode() == KeyCode.ENTER)
        {
            onSubmit();
        }
    }

    @FXML
    public void submitName() throws Exception {
        // onSubmit is called when user clicks on Start Game
        onSubmit();
    }

    @FXML
    public void ExitProgram()
    {
        // Exit program
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Start the laoding actions of progressbar
        prog.progressProperty().bind(task.progressProperty());
        this.progThread = new Thread(task);
        this.progThread.start();
    }
}


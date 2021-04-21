package OMI_Game;

import javafx.scene.control.Label;

public class TitleChanger implements Runnable {

    private String text;
    private Label Title;
    private int waiter;

    public TitleChanger(String text, Label title, int waiter) {
        this.text = text;
        Title = title;
        this.waiter = waiter;
    }

    @Override
    public void run() {
        // Changes title of a label within a specified time
        try {
            Thread.sleep(this.waiter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.Title.setText(text);

    }
}

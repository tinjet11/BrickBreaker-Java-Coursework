package brickGame;

import javafx.fxml.FXML;

public class EndGameSceneController {
    private Main main;

    public EndGameSceneController(Main main){
        this.main = main;
    }

    public EndGameSceneController(){

    }

    @FXML
    public void exit(){
        System.exit(0);
    }

    @FXML
    public void restart(){
        this.main.restartGame();
    }

}

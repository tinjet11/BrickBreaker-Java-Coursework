package brickGame;

import javafx.fxml.FXML;

import static brickGame.Main.gameStateManager;

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
        gameStateManager.restartGame();
    }

}

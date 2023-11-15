package brickGame;

import javafx.fxml.FXML;

public class WinController {
    private Main main;

    public WinController(Main main){
        this.main = main;
    }

    public WinController(){

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

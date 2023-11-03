package brickGame;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuController {
    Main main = new Main();
    @FXML private Button startButton;
    @FXML private Button exitButton;

    @FXML
    public void exit(){
        System.exit(0);
    }





}

package brickGame.controller;

import brickGame.handler.GameLogicHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameDescriptionController {

    private Stage primaryStage;
    private Scene menuScene;

    public GameDescriptionController(Scene menuScene,Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.menuScene = menuScene;
    }
    /**
     * Navigates back to the main menu scene when the back button is clicked.
     */
    @FXML
    public void back() {
        try {
            primaryStage.setTitle("Brick Breaker Game");
            primaryStage.setScene(menuScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

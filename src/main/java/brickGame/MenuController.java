package brickGame;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

import static brickGame.GameStateManager.gameState;
import static brickGame.Main.*;

public class MenuController {

    public MenuController() {
        // Constructor logic, if needed
    }


    private Main main;
    private Stage primaryStage;

    @FXML
    private Button startButton;

    @FXML
    private VBox resultBox;

    @FXML
    private Button tutorialButton;

    public MenuController(Main main, Stage primaryStage) {
        this.main = main;
        this.primaryStage = primaryStage;
    }

    @FXML
    public void exit() {
        System.exit(0);
    }

    @FXML
    public void onStartOrResume() {

        if (gameState == GameStateManager.GameState.ON_START) {
            try {
                main.startGame(primaryStage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            gameState = GameStateManager.GameState.IN_PROGRESS;
        } else if (gameState == GameStateManager.GameState.PAUSED) {
            gameStateManager.loadGame();
            gameState = GameStateManager.GameState.IN_PROGRESS;
        } else if (gameState == GameStateManager.GameState.GAME_OVER) {
            gameStateManager.restartGame();
            resultBox.setVisible(false);
            gameState = GameStateManager.GameState.IN_PROGRESS;
        }
    }

    @FXML
    public void onOpenSettings() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Settings.fxml"));
            fxmlLoader.setControllerFactory(c -> {
                return new SettingsController(this.main, primaryStage);
            });

            Scene tutorialScene = new Scene(fxmlLoader.load());
            primaryStage.setTitle("Brick Breaker Game");
            primaryStage.setScene(tutorialScene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showMenuScene(String state, Scene winMenuScene) throws IOException {

        VBox resultBox = (VBox) winMenuScene.lookup("#resultBox");
        resultBox.setVisible(true);

        Label scorePlaceholder = (Label) winMenuScene.lookup("#scoreLabel");
        scorePlaceholder.setText(scorePlaceholder.getText() + String.valueOf(score));

        if (state == "Lose") {
            Label resultLabel = (Label) winMenuScene.lookup("#resultLabel");
            resultLabel.setText("Game Over :(");
        }

        Button restartButton = (Button) winMenuScene.lookup("#startButton");
        restartButton.setText("Play Again");

        primaryStage.setTitle("Brick Breaker Game");
        primaryStage.setScene(winMenuScene);
        primaryStage.show();

    }

}

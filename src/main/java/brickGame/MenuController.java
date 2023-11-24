package brickGame;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.io.IOException;
import static brickGame.GameStateManager.gameState;
import static brickGame.Main.*;
import static brickGame.GameLogicHandler.*;

public class MenuController {

    @FXML
    private VBox resultBox;

    @FXML
    public void onExit() {
        System.exit(0);
    }

    @FXML
    public void onStartOrResume() {

        if (gameState == GameStateManager.GameState.ON_START) {
            try {
                gameStateManager.startGame();
                System.out.println("clicked");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            gameState = GameStateManager.GameState.IN_PROGRESS;
            //gameSoundManager.play();
        } else if (gameState == GameStateManager.GameState.PAUSED) {
            gameStateManager.loadGame();
            gameState = GameStateManager.GameState.IN_PROGRESS;
            gameSoundManager.resume();
        } else if (gameState == GameStateManager.GameState.GAME_OVER) {
            gameStateManager.restartGame();
            resultBox.setVisible(false);
            gameState = GameStateManager.GameState.IN_PROGRESS;
        }
        gameSoundManager.resume();
    }

    @FXML
    public void onOpenSettings() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/Settings.fxml"));
            fxmlLoader.setControllerFactory(c -> {
                return new SettingsController();
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

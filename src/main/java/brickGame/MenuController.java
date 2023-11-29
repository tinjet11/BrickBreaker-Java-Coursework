package brickGame;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;


public class MenuController {

    private GameLogicHandler gameLogicHandler;
    private GameStateManager gameStateManager;

    private GameSceneController gameSceneController;

    private GameStateManager.GameState gameState;

    private Stage primaryStage;

    private int highestScore;
    public MenuController(){
        gameLogicHandler = GameLogicHandler.getInstance();
        gameStateManager = GameStateManager.getInstance();
        gameSceneController = GameSceneController.getInstance();

        primaryStage = gameSceneController.getPrimaryStage();

    }

    @FXML
    private VBox resultBox;

    @FXML
    public void onExit() {
        System.exit(0);
    }

    @FXML
    public void onStartOrResume() {
        gameState = gameStateManager.getGameState();
        if (gameState == GameStateManager.GameState.ON_START) {
                gameStateManager.startGame();
                System.out.println("clicked");
                gameStateManager.setGameState(GameStateManager.GameState.IN_PROGRESS);
        } else if (gameState == GameStateManager.GameState.PAUSED) {
            gameStateManager.loadGame();
            gameStateManager.setGameState(GameStateManager.GameState.IN_PROGRESS);
        } else if (gameState == GameStateManager.GameState.GAME_OVER || gameState == GameStateManager.GameState.WIN) {
            gameStateManager.restartGame();
            resultBox.setVisible(false);
            gameStateManager.setGameState(GameStateManager.GameState.IN_PROGRESS);
        }
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
        HighScore highScore = new HighScore();
        highestScore = highScore.getHighestGameScore();
        highScore.setHighestGameScore(gameLogicHandler.getScore());


        VBox resultBox = (VBox) winMenuScene.lookup("#resultBox");
        resultBox.setVisible(true);

        Label scorePlaceholder = (Label) winMenuScene.lookup("#scoreLabel");
        Label highestScorePlaceholder = (Label) winMenuScene.lookup("#highestScoreLabel");
        scorePlaceholder.setText(scorePlaceholder.getText() + String.valueOf(gameLogicHandler.getScore()));

        if(highestScore > gameLogicHandler.getScore()){
            highestScorePlaceholder.setText(highestScorePlaceholder.getText() + String.valueOf(highestScore));
        }else{
            highestScorePlaceholder.setText("Your new allTime highest score is: " + String.valueOf(gameLogicHandler.getScore()));
        }

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

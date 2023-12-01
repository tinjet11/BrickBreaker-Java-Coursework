package brickGame.controller;

import brickGame.*;
import brickGame.handler.GameLogicHandler;
import brickGame.model.GameStateManager;
import brickGame.serialization.HighestScore;
import brickGame.serialization.LoadSave;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    private Button loadButton;

    @FXML
    public void onExit() {
        System.exit(0);
    }

    @FXML
    public void onStartOrResume() {
        gameState = gameStateManager.getGameState();
        if (gameState == GameStateManager.GameState.ON_START) {
            gameLogicHandler.setHeart(gameLogicHandler.getInitialHeart());
                gameStateManager.startGame();
                System.out.println("clicked");
                gameStateManager.setGameState(GameStateManager.GameState.IN_PROGRESS);
              //  loadButton.setVisible(false);
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
    public void onLoad(){
        LoadSave loadSave = new LoadSave();
        //previous saved file exists
        if( loadSave.checkSaveGameFileExist()){
            gameStateManager.loadGame();
            gameStateManager.setGameState(GameStateManager.GameState.IN_PROGRESS);
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("No saved game found. Do you want to start a new game?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // User clicked OK
                    // Perform actions for starting a new game
                    gameStateManager.startGame();
                }
            });
        }
    }

    @FXML
    public void onOpenSettings() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml/Settings.fxml"));
            fxmlLoader.setControllerFactory(c -> {
                return new SettingsController(primaryStage.getScene());
            });

            Scene settingsScene = new Scene(fxmlLoader.load());
            primaryStage.setTitle("Brick Breaker Game");
            primaryStage.setScene(settingsScene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showMenuScene(String state, Scene winMenuScene) throws IOException {

        //destroy saveGame file if player lose or win
        LoadSave loadSave = new LoadSave();
        loadSave.destroySaveGameFile();

        HighestScore highScore = new HighestScore();
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
            highestScorePlaceholder.setText("Your new all time highest score is: " + String.valueOf(gameLogicHandler.getScore()));
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

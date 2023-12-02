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

/**
 * The MenuController class is responsible for managing the main menu interactions and controlling the game's menu scenes.
 * It handles actions such as starting or resuming the game, loading saved games, exiting the application, and opening the settings.
 * Additionally, it displays result scenes upon winning or losing the game.
 *
 * @author Leong Tin Jet
 * @version 1.0
 */
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

    /**
     * Exits the application when the exit button is clicked.
     */
    @FXML
    public void onExit() {
        System.exit(0);
    }

    /**
     * Handles the action when the start or resume button is clicked.
     * Depending on the current game state, it starts a new game, resumes a paused game, or restarts the game after a game over or win.
     */
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

    /**
     * Handles the action when the load button is clicked.
     * It checks for saved games and loads an existing game,
     * if no saved game exists then prompts the user to start a new game.
     */
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

    /**
     * Handles the action when the settings button is clicked. It opens the settings scene.
     */
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

    /**
     * Displays the result scene based on the game outcome (win or lose) and updates the highest score.
     *
     * @param state The game outcome, either "Win" or "Lose".
     * @param winMenuScene The scene to be displayed after winning or losing.
     * @throws IOException If an I/O error occurs while loading the result scene.
     */
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

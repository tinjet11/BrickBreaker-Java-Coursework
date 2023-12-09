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

import static brickGame.Constants.GAME_DESCRIPTION_SCENE_FXML;
import static brickGame.Constants.SETTINGS_SCENE_FXML;

/**
 * The MenuController class is responsible for managing the main menu interactions and controlling the game's menu scenes.
 * It handles actions such as starting or resuming the game, loading saved games, exiting the application, and opening the settings.
 * Additionally, it displays result scenes upon winning or losing the game.
 *
 * @author Leong Tin Jet
 * @version 1.0
 */
public class MenuController {

    /**
     * The instance of Mediator for handling communication between different components.
     */
    Mediator mediator;

    /**
     * The current game state.
     */
    private GameStateManager.GameState gameState;

    /**
     * The primary stage of the application.
     */
    private Stage primaryStage;

    /**
     * The highest score achieved in the game.
     */
    private int highestScore;


    public MenuController(){
        mediator = Mediator.getInstance();
        primaryStage = mediator.getGameSceneController().getPrimaryStage();
    }

    /**
     * The VBox representing the result box in the menu scene.
     */
    @FXML
    private VBox resultBox;

    /**
     * Exits the application when the exit button is clicked.
     */
    @FXML
    public void onExit() {
        SoundController soundController = SoundController.getInstance();
        soundController.dispose();
        System.exit(0);
    }

    /**
     * Handles the action when the start or resume button is clicked.
     * Depending on the current game state, it starts a new game, resumes a paused game, or restarts the game after a game over or win.
     */
    @FXML
    public void onStartOrResume() {
        gameState = mediator.getGameStateManager().getGameState();
        if (gameState == GameStateManager.GameState.ON_START) {
            mediator.getGameLogicHandler().setHeart(mediator.getGameLogicHandler().getInitialHeart());
                mediator.getGameStateManager().startGame();
                System.out.println("clicked");
                mediator.getGameStateManager().setGameState(GameStateManager.GameState.IN_PROGRESS);
              //  loadButton.setVisible(false);
        } else if (gameState == GameStateManager.GameState.PAUSED) {
            mediator.getGameStateManager().loadGame();
            mediator.getGameStateManager().setGameState(GameStateManager.GameState.IN_PROGRESS);
        } else if (gameState == GameStateManager.GameState.GAME_OVER || gameState == GameStateManager.GameState.WIN) {
            mediator.getGameStateManager().restartGame();
            resultBox.setVisible(false);
            mediator.getGameStateManager().setGameState(GameStateManager.GameState.IN_PROGRESS);
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
            mediator.getGameStateManager().loadGame();
            mediator.getGameStateManager().setGameState(GameStateManager.GameState.IN_PROGRESS);
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("No saved game found. Do you want to start a new game?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // User clicked OK
                    // Perform actions for starting a new game
                    mediator.getGameStateManager().startGame();
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
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(SETTINGS_SCENE_FXML));
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
     * Handles the action when the "Game Description" button is clicked. It opens the game description scene.
     * The game description scene provides information about the game rules and controls.
     * Uses JavaFX components and FXML for scene setup.
     */
    @FXML
    public void onOpenGameDescription() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(GAME_DESCRIPTION_SCENE_FXML));
            fxmlLoader.setControllerFactory(c -> {
                return new GameDescriptionController(primaryStage.getScene(),primaryStage);
            });

            Scene gameDescriptionScene = new Scene(fxmlLoader.load());
            primaryStage.setTitle("Brick Breaker Game");
            primaryStage.setScene(gameDescriptionScene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Displays the result scene based on the game outcome (win or lose) and updates the highest score.
     *
     * @param winMenuScene The scene to be displayed after winning or losing.
     * @throws IOException If an I/O error occurs while loading the result scene.
     */
    public void showMenuScene(Scene winMenuScene) throws IOException {

        //destroy saveGame file if player lose or win
        LoadSave loadSave = new LoadSave();
        loadSave.destroySaveGameFile();

        HighestScore highScore = new HighestScore();
        highestScore = highScore.getHighestGameScore();
        highScore.setHighestGameScore(mediator.getGameLogicHandler().getScore());

        VBox resultBox = (VBox) winMenuScene.lookup("#resultBox");
        resultBox.setVisible(true);

        Label scorePlaceholder = (Label) winMenuScene.lookup("#scoreLabel");
        Label highestScorePlaceholder = (Label) winMenuScene.lookup("#highestScoreLabel");
        scorePlaceholder.setText(scorePlaceholder.getText() + String.valueOf(mediator.getGameLogicHandler().getScore()));

        if(highestScore > mediator.getGameLogicHandler().getScore()){
            highestScorePlaceholder.setText(highestScorePlaceholder.getText() + String.valueOf(highestScore));
        }else{
            highestScorePlaceholder.setText("Your new all time highest score is: " + String.valueOf(mediator.getGameLogicHandler().getScore()));
        }

        if (mediator.getGameStateManager().getGameState() == GameStateManager.GameState.GAME_OVER) {
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

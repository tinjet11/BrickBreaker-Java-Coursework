package brickGame.controller;

import brickGame.handler.GameLogicHandler;
import brickGame.model.GameStateManager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


/**
 * The SettingsController class is responsible for managing the game settings UI,
 * allowing users to customize parameters such as initial hearts and end levels.
 * It provides methods for incrementing and decrementing heart and level values,
 * as well as navigating back to the main menu.
 * <p>
 * This controller interacts with the GameLogicHandler and GameSceneController
 * to update and retrieve game settings. It also handles the transition back to the main menu scene.
 * </p>
 * <p>
 * The class utilizes JavaFX components and FXML for scene setup and UI interactions.
 * </p>
 *
 * @author Leong Tin Jet
 * @version 1.0
 */
public class SettingsController {

    @FXML
    private Label initialHeartLabel;
    @FXML
    private Label endLevelLabel;

    private int maxHeart = 10;
    private int minHeart = 3;

    private int maxLevel = 18;
    private int minLevel = 10;
    private int currentHeart;
    private int currentEndLevel;

    private GameLogicHandler gameLogicHandler;
    private GameSceneController gameSceneController;

    private Scene menuScene;

    private Stage primaryStage;

    /**
     * Creates an instance of SettingsController with the specified menu scene.
     *
     * @param menuScene The Scene object representing the main menu scene.
     */
    public SettingsController(Scene menuScene) {
        gameLogicHandler = GameLogicHandler.getInstance();
        gameSceneController = GameSceneController.getInstance();
        primaryStage = gameSceneController.getPrimaryStage();
        this.menuScene = menuScene;
    }

    /**
     * Initializes the settings UI with the current initial heart and end level values.
     */
    @FXML
    public void initialize(){
        currentEndLevel = gameLogicHandler.getEndLevel();
        currentHeart = gameLogicHandler.getInitialHeart();
        initialHeartLabel.setText(String.valueOf(gameLogicHandler.getInitialHeart()));
        endLevelLabel.setText(String.valueOf(gameLogicHandler.getEndLevel()));
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

    /**
     * Increments the initial heart value when the increment heart button is clicked.
     * Only allows incrementing when the game is not currently running.
     */
    @FXML
    private void incrementHeart() {

        if (!gameLogicHandler.isGameRun()) {
            if (currentHeart != maxHeart) {
                currentHeart++;
                initialHeartLabel.setText(String.valueOf(currentHeart));
                gameLogicHandler.setInitialHeart(currentHeart);
            }
        }
    }

    /**
     * Decrements the initial heart value when the decrement heart button is clicked.
     * Only allows decrementing when the game is not currently running.
     */
    @FXML
    private void decrementHeart() {
        if (!gameLogicHandler.isGameRun()) {
            if (currentHeart != minHeart) {
                currentHeart--;
                initialHeartLabel.setText(String.valueOf(currentHeart));
                gameLogicHandler.setInitialHeart(currentHeart);
            }
        }
    }

    /**
     * Increments the end level value when the increment level button is clicked.
     * Only allows incrementing when the game is not currently running.
     */
    @FXML
    private void incrementLevel() {
        if (!gameLogicHandler.isGameRun()) {
            if (currentEndLevel != maxLevel) {
                currentEndLevel++;
                endLevelLabel.setText(String.valueOf(currentEndLevel));
                gameLogicHandler.setEndLevel(currentEndLevel);
            }
        }

    }

    /**
     * Decrements the end level value when the decrement level button is clicked.
     * Only allows decrementing when the game is not currently running.
     */
    @FXML
    private void decrementLevel() {
        if (!gameLogicHandler.isGameRun()) {
            if (currentEndLevel != minLevel) {
                currentEndLevel--;
                endLevelLabel.setText(String.valueOf(currentEndLevel));
                gameLogicHandler.setEndLevel(currentEndLevel);
            }
        }
    }
}

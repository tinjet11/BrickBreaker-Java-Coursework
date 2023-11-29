package brickGame.Controller;

import brickGame.Controller.GameSceneController;
import brickGame.GameLogicHandler;
import brickGame.GameStateManager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SettingsController {

    @FXML
    private Slider heartNumberSlider;

    @FXML
    private ScrollBar endLevelScrollbar;

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

    @FXML
    private CheckBox soundCheckBox;

    private GameLogicHandler gameLogicHandler;
    private GameStateManager gameStateManager;

    private GameSceneController gameSceneController;

    private GameStateManager.GameState gameState;

    private Scene menuScene;

    private Stage primaryStage;
    public SettingsController(Scene menuScene) {
        gameLogicHandler = GameLogicHandler.getInstance();
        gameStateManager = GameStateManager.getInstance();
        gameSceneController = GameSceneController.getInstance();
        primaryStage = gameSceneController.getPrimaryStage();
        this.menuScene = menuScene;
    }

    @FXML
    public void initialize(){
        currentEndLevel = gameLogicHandler.getEndLevel();
        currentHeart = gameLogicHandler.getInitialHeart();
        initialHeartLabel.setText(String.valueOf(gameLogicHandler.getInitialHeart()));
        endLevelLabel.setText(String.valueOf(gameLogicHandler.getEndLevel()));
    }
    @FXML
    public void back() {
        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(SettingsController.class.getResource("fxml/Menu.fxml"));
//
//            Scene menuScene = new Scene(fxmlLoader.load());

//            Button startButton = (Button) menuScene.lookup("#startButton");
//
//            gameState = gameStateManager.getGameState();
//            if (gameState == GameStateManager.GameState.ON_START) {
//                startButton.setText("Start Game");
//            } else if (gameState == GameStateManager.GameState.PAUSED) {
//                startButton.setText("Resume");
//            } else if ( gameState == GameStateManager.GameState.GAME_OVER || gameState == GameStateManager.GameState.WIN) {
//                startButton.setText("Play Again");
//            }

            primaryStage.setTitle("Brick Breaker Game");
            primaryStage.setScene(menuScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




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

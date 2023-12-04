package brickGame.controller;

import brickGame.handler.GameLogicHandler;
import brickGame.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

import static brickGame.Constants.GOLD_ROOT;
import static brickGame.Constants.MENU_SCENE_FXML;


/**
 * The GameSceneController class is providing method for setting the label value,
 * such as level,heart, and score. It also provides user functionality to move the paddle,
 * It also provides functionality for pause button and calling function to show win or lose scene
 *
 * @author Leong Tin Jet
 * @version 1.0
 */
public class GameSceneController {
    @FXML
    private AnchorPane gamePane;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label heartLabel;
    @FXML
    private Label levelLabel;


    private Stage primaryStage;
    private final int LEFT = 1;
    private final int RIGHT = 2;
    private MenuController menuController;


    private BallControl ballControl;
    private GameLogicHandler gameLogicHandler;
    private GameStateManager gameStateManager;
    private InitGameComponent initGameComponent;

    /**
     * Singleton instance of the GameSceneController.
     */
    private static GameSceneController instance;

    /**
     * Private constructor for the GameSceneController to enforce singleton pattern.
     * Instances of this class should be created using {@link #getInstance()} method.
     */
    private GameSceneController() {
    }

    /**
     * Retrieves the singleton instance of the GameSceneController.
     *
     * @return The singleton instance of GameSceneController.
     */
    public static GameSceneController getInstance() {
        if (instance == null) {
            instance = new GameSceneController();
        }
        return instance;
    }

    /**
     * Loads and returns the menu scene from the FXML file.
     *
     * @return The JavaFX scene representing the menu scene.
     * @throws IOException If an I/O error occurs while loading the FXML file or creating the menu scene.
     */
    private Scene loadMenuScene(){
        Scene menuScene = null;
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(MENU_SCENE_FXML));
            fxmlLoader.setControllerFactory(c -> {
                return menuController = new MenuController();
            });
            menuScene = new Scene(fxmlLoader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return menuScene;
    }

    /**
     * Displays the "Lose" scene by setting the game state to GAME_OVER and showing the menu scene.
     * If an exception occurs during the process, it is printed to the standard error stream.
     */
    public void showLoseScene() {
        try {
            gameLogicHandler.setGameRun(false);
            gameStateManager.setGameState(GameStateManager.GameState.GAME_OVER);
            Scene loseMenuScene = loadMenuScene();
            menuController.showMenuScene("Lose", loseMenuScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the "Win" scene by setting the game state to WIN and showing the menu scene.
     * If an exception occurs during the process, it is printed to the standard error stream.
     */
    public void showWinScene() {
        try {
            gameStateManager.setGameState(GameStateManager.GameState.WIN);
            gameLogicHandler.setGameRun(false);
            Scene winMenuScene = loadMenuScene();
            menuController.showMenuScene("Win", winMenuScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the main game scene with the specified Scene object.
     * Initializes game components like blocks, ball, and paddle based on the game state.
     * If the game is not loaded from a save, it initializes these components from scratch,show level up as well as set level label
     * If the game is loaded from a save, it may initialize the ball and paddle if it's the first game open
     * and may add a "goldRoot" style to the gamePane if the GoldStatus is true.
     *
     * @param gameScene The Scene object representing the main game scene.
     * @throws IOException If an input or output exception occurs during the process of setting up the scene.
     *                     This usually happens when loading the FXML file or setting the scene on the primaryStage.
     */
    public void showGameScene(Scene gameScene) throws IOException {

        gamePane = (AnchorPane) gameScene.lookup("#gamePane");
        scoreLabel = (Label) gameScene.lookup("#scoreLabel");
        heartLabel = (Label) gameScene.lookup("#heartLabel");
        levelLabel = (Label) gameScene.lookup("#levelLabel");

        setLevelLabel("Level: " + gameLogicHandler.getLevel());

        //if is not load from save
        if (!gameStateManager.isLoadFromSave()) {
            //init game component like block,ball and paddle
            initGameComponent.initBall();
            initGameComponent.initPaddle();
            initGameComponent.initBoard();
            gameLogicHandler.setRemainingBlockCount(initGameComponent.getBlocks().size());
            new ScoreAnimation(getGamePane()).showMessage("Level Up :)", 300, 300);
        } else {

            //if the game first open,call initBall and initPaddle method
            //because ball and paddle isn't loadFromSave
            if (gameStateManager.isGameFirstOpen()) {
                initGameComponent.initBall();
                initGameComponent.initPaddle();
            }

            // if the GoldStatus is true, add goldRoot to style.css
            if (gameLogicHandler.isGoldStatus()) {
                getGamePane().getStyleClass().add(GOLD_ROOT);
            }
        }

        gameStateManager.setGameFirstOpen(false);

        //add ball and paddle to gamePane
        gamePane.getChildren().addAll(initGameComponent.getPaddle(), ballControl.getBall());

        // add blocks to the gamePane
        for (Block block : initGameComponent.getBlocks()) {
            gamePane.getChildren().add(block.getRect());
        }

        gameScene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);

        try {
            primaryStage.setScene(gameScene);
            primaryStage.setTitle("Brick Breaker Game");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the key-pressed event for controlling the paddle movement.
     * Moves the paddle to the left or right based on the arrow keys.
     *
     * @param event The KeyEvent representing the key-pressed event.
     */
    @FXML
    private void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                move(LEFT);
                break;
            case RIGHT:
                move(RIGHT);
                break;
        }
    }

    /**
     * Moves the paddle in the specified direction with a smooth animation.
     * The method launches a new thread to gradually move the paddle over a short duration.
     *
     * @param direction An integer representing the direction of the movement.
     *                  Use constants LEFT (1) or RIGHT (2) from the class.
     *                  If LEFT, the paddle moves to the left; if RIGHT, the paddle moves to the right.
     */
    private void move(final int direction) {
        //Move the paddle
        new Thread(new Runnable() {
            @Override
            public void run() {
                int sleepTime = 4;
                for (int i = 0; i < 30; i++) {
                    if (initGameComponent.getxPaddle() == (initGameComponent.getSCENE_WIDTH() - initGameComponent.getPADDLE_WIDTH()) && direction == RIGHT) {
                        return;
                    }
                    if (initGameComponent.getxPaddle() == 0 && direction == LEFT) {
                        return;
                    }
                    if (direction == RIGHT) {
                        initGameComponent.setxPaddle(initGameComponent.getxPaddle() + 1);
                    } else {
                        initGameComponent.setxPaddle(initGameComponent.getxPaddle() - 1);
                    }
                    initGameComponent.setCenterPaddleX(initGameComponent.getxPaddle() + initGameComponent.getHALF_PADDLE_WIDTH());
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i >= 20) {
                        sleepTime = i;
                    }
                }
            }
        }).start();
    }

    /**
     * Handles the action when the pause button is clicked.
     * Saves the game state, loads the menu scene, updates the start button text to "Resume,"
     * sets the game state to PAUSED, and displays the menu scene.
     *
     * @param event The ActionEvent triggered when the pause button is clicked.
     */
    @FXML
    private void handlePauseButton(ActionEvent event) {
        gameStateManager.saveGame();
        gameStateManager.setGameState(GameStateManager.GameState.PAUSED);

        Scene menuScene = loadMenuScene();

        Button startBtn = (Button) menuScene.lookup("#startButton");
        startBtn.setText("Resume");

        primaryStage.setTitle("Brick Breaker Game");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    /**
     * Gets the game pane.
     *
     * @return The AnchorPane representing the game pane.
     */
    public AnchorPane getGamePane() {
        return gamePane;
    }

    /**
     * Sets the text of the score label.
     *
     * @param scoreLabelText The text to set for the score label.
     */
    public void setScoreLabel(String scoreLabelText) {
        scoreLabel.setText(scoreLabelText);
    }

    /**
     * Sets the text of the heart label.
     *
     * @param heartLabelText The text to set for the heart label.
     */
    public void setHeartLabel(String heartLabelText) {
        heartLabel.setText(heartLabelText);
    }

    /**
     * Sets the text of the level label.
     *
     * @param levelLabelText The text to set for the level label.
     */
    public void setLevelLabel(String levelLabelText) {
        levelLabel.setText(levelLabelText);
    }

    /**
     * Sets the primary stage.
     *
     * @param primaryStage The Stage to set as the primary stage.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Gets the primary stage.
     *
     * @return The primary Stage.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Sets the initialization game component.
     *
     * @param initGameComponent The InitGameComponent to set.
     */
    public void setInitGameComponent(InitGameComponent initGameComponent) {
        this.initGameComponent = initGameComponent;
    }

    /**
     * Sets the ball control.
     *
     * @param ballControl The BallControl to set.
     */
    public void setBallControl(BallControl ballControl) {
        this.ballControl = ballControl;
    }

    /**
     * Sets the game logic handler.
     *
     * @param gameLogicHandler The GameLogicHandler to set.
     */
    public void setGameLogicHandler(GameLogicHandler gameLogicHandler) {
        this.gameLogicHandler = gameLogicHandler;
    }

    /**
     * Sets the game state manager.
     *
     * @param gameStateManager The GameStateManager to set.
     */
    public void setGameStateManager(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
    }


}

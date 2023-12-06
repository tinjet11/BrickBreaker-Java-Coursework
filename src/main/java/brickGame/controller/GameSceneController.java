package brickGame.controller;

import brickGame.Mediator;
import brickGame.handler.BallControlHandler;
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

import static brickGame.Constants.*;


/**
 * The GameSceneController class is providing method for setting the label value,
 * such as level,heart, and score. It also provides user functionality to move the paddle,
 * It also provides functionality for pause button and calling function to show win or lose scene
 *
 * @author Leong Tin Jet
 * @version 1.0
 */
public class GameSceneController {

    private Mediator mediator;


    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }
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
            mediator.getGameLogicHandler().setGameRun(false);
            mediator.getGameStateManager().setGameState(GameStateManager.GameState.GAME_OVER);
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
            mediator.getGameStateManager().setGameState(GameStateManager.GameState.WIN);
            mediator.getGameLogicHandler().setGameRun(false);
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

        setLevelLabel("Level: " + mediator.getGameLogicHandler().getLevel());

        //if is not load from save
        if (!mediator.getGameStateManager().isLoadFromSave()) {
            //init game component like block,ball and paddle
            mediator.getInitGameComponent().initBall();
            mediator.getInitGameComponent().initPaddle();
            mediator.getInitGameComponent().initBoard();
            mediator.getGameLogicHandler().setRemainingBlockCount(mediator.getInitGameComponent().getBlocks().size());
            new ScoreAnimation(getGamePane()).showMessage("Level Up :)", 300, 300);
        } else {

            //if the game first open,call initBall and initPaddle method
            //because ball and paddle isn't loadFromSave
            if (mediator.getGameStateManager().isGameFirstOpen()) {
                mediator.getInitGameComponent().initBall();
                mediator.getInitGameComponent().initPaddle();
            }

            // if the GoldStatus is true, add goldRoot to style.css
            if (mediator.getGameLogicHandler().isGoldStatus()) {
                getGamePane().getStyleClass().add(GOLD_ROOT);
            }
        }

        mediator.getGameStateManager().setGameFirstOpen(false);

        //add ball and paddle to gamePane
        gamePane.getChildren().addAll(mediator.getPaddleInstance().getPaddle(), mediator.getBallInstance().getBall());

        // add blocks to the gamePane
        for (Block block : mediator.getInitGameComponent().getBlocks()) {
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
                mediator.getPaddleInstance().move(LEFT);
                break;
            case RIGHT:
                mediator.getPaddleInstance().move(RIGHT);
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

    /**
     * Handles the action when the pause button is clicked.
     * Saves the game state, loads the menu scene, updates the start button text to "Resume,"
     * sets the game state to PAUSED, and displays the menu scene.
     *
     * @param event The ActionEvent triggered when the pause button is clicked.
     */
    @FXML
    private void handlePauseButton(ActionEvent event) {
        mediator.getGameStateManager().saveGame();
        mediator.getGameStateManager().setGameState(GameStateManager.GameState.PAUSED);

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

}

package brickGame.model;

import brickGame.handler.BallControlHandler;
import brickGame.serialization.BlockSerialize;
import brickGame.serialization.LoadSave;
import brickGame.controller.GameSceneController;
import brickGame.handler.GameLogicHandler;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.*;
import java.util.ArrayList;

import static brickGame.Constants.*;

/**
 * The GameStateManager class manages the state of the game, including saving, loading, starting, and restarting the game.
 * It handles the game logic, user input, and interaction with other components.
 * <p>
 * This class follows the singleton pattern, allowing only one instance to exist throughout the application.
 * @author Leong Tin Jet
 * @version 1.0
 */
public class GameStateManager {

    /**
     * Enumeration representing the possible states of the game.
     */
    public enum GameState {
        ON_START,
        IN_PROGRESS,
        PAUSED,
        GAME_OVER,
        WIN,
    }

    /**
     * The current state of the game.
     */
    private GameState gameState = GameState.ON_START;

    /**
     * Flag indicating whether the game is loaded from a saved state.
     */
    private boolean loadFromSave = false;

    /**
     * Flag indicating whether it's the first time the game is opened.
     */
    private boolean gameFirstOpen = true;
    private BallControlHandler ballControlHandler;
    private GameLogicHandler gameLogicHandler;
    private GameSceneController gameSceneController;
    private InitGameComponent initGameComponent;

    private Paddle paddle;
    private Ball ball;


    private static GameStateManager instance;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private GameStateManager() {
    }

    /**
     * Retrieves the singleton instance of the GameStateManager.
     *
     * @return The singleton instance of the GameStateManager.
     */
    public static GameStateManager getInstance() {
        if (instance == null) {
            instance = new GameStateManager();
        }
        return instance;
    }

    /**
     * Starts the game by setting up the game scene, initializing the engine, and showing the game scene.
     * If the level is equal to the end level, it shows the win scene.
     */
    public void startGame() {
        gameLogicHandler.setGameRun(true);
        try {
            //if level equal to endLevel
            if (gameLogicHandler.getLevel() == gameLogicHandler.getEndLevel()) {
                gameSceneController.showWinScene();
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(GAME_SCENE_FXML));
                fxmlLoader.setControllerFactory(c -> gameSceneController);

                Scene gameScene = new Scene(fxmlLoader.load());
                gameSceneController.showGameScene(gameScene);
                gameLogicHandler.setUpEngine();

                if (isLoadFromSave()) {
                    setLoadFromSave(false);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Flag to indicate is nextLevel running to prevent it being executed multiple time in a short period of time
     */
    private boolean nextLevelInProgress = false;

    /**
     * Moves to the next level of the game, handling the logic and resetting necessary components.
     */
    public void nextLevel() {
        gameLogicHandler.setGameRun(false);
        // Check if nextLevel is already in progress, if yes, return
        if (nextLevelInProgress) {
            return;
        }

        // Set the flag to indicate that nextLevel is in progress
        try {
            Platform.runLater(() -> {

                gameLogicHandler.stopEngine();

                ballControlHandler.setvX(1.000);
                ballControlHandler.setvY(1.000);

                ballControlHandler.resetcollideFlags();
                ballControlHandler.setGoDownBall(true);
                gameLogicHandler.setGoldStatus(false);

                initGameComponent.setExistHeartBlock(false);
                initGameComponent.setExistBombBlock(false);

                gameLogicHandler.setTime(0);
                gameLogicHandler.setGoldTime(0);

                initGameComponent.getBlocks().clear();
                initGameComponent.getChocos().clear();
                initGameComponent.getBombs().clear();

                gameLogicHandler.setRemainingBlockCount(0);

                if (gameLogicHandler.getLevel() < gameLogicHandler.getEndLevel()) {
                    gameLogicHandler.addLevel();
                }
                startGame();
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Reset the flag to indicate that nextLevel is completed
            nextLevelInProgress = false;
        }


    }


    /**
     * Saves the current state of the game to a file.
     */
    public void saveGame() {
        new Thread(() -> {
            new File(SAVE_PATH_DIR).mkdirs();
            File file = new File(SAVE_PATH);
            ObjectOutputStream outputStream = null;

            gameLogicHandler.stopEngine();

            try {
                outputStream = new ObjectOutputStream(new FileOutputStream(file));

                outputStream.writeInt(gameLogicHandler.getLevel());
                outputStream.writeInt(gameLogicHandler.getScore());
                outputStream.writeInt(gameLogicHandler.getHeart());
                outputStream.writeInt(gameLogicHandler.getRemainingBlockCount());

                outputStream.writeDouble(ball.getxBall());
                outputStream.writeDouble(ball.getyBall());
                outputStream.writeDouble(paddle.getxPaddle());
                outputStream.writeDouble(paddle.getyPaddle());
                outputStream.writeDouble(paddle.getCenterPaddleX());
                outputStream.writeLong(gameLogicHandler.getTime());
                outputStream.writeLong(gameLogicHandler.getGoldTime());
                outputStream.writeDouble(ballControlHandler.getvX());

                outputStream.writeBoolean(initGameComponent.isExistHeartBlock());
                outputStream.writeBoolean(gameLogicHandler.isGoldStatus());
                outputStream.writeBoolean(ballControlHandler.isGoDownBall());
                outputStream.writeBoolean(ballControlHandler.isGoRightBall());
                outputStream.writeBoolean(ballControlHandler.isCollideToPaddle());
                outputStream.writeBoolean(ballControlHandler.isCollideToPaddleAndMoveToRight());
                outputStream.writeBoolean(ballControlHandler.isCollideToRightWall());
                outputStream.writeBoolean(ballControlHandler.isCollideToLeftWall());
                outputStream.writeBoolean(ballControlHandler.isCollideToRightBlock());
                outputStream.writeBoolean(ballControlHandler.isCollideToBottomBlock());
                outputStream.writeBoolean(ballControlHandler.isCollideToLeftBlock());
                outputStream.writeBoolean(ballControlHandler.isCollideToTopBlock());

                ArrayList<BlockSerialize> blockSerializables = new ArrayList<>();
                for (Block block : initGameComponent.getBlocks()) {
                    if (block.isDestroyed()) {
                        continue;
                    }
                    blockSerializables.add(new BlockSerialize(block.getRow(), block.getColumn(), block.getType()));
                }

                outputStream.writeObject(blockSerializables);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Loads the game state from a saved file and initializes the game components accordingly.
     */
    public void loadGame() {

        LoadSave loadSave = new LoadSave();
        loadSave.read();

        initGameComponent.setExistHeartBlock(loadSave.isExistHeartBlock());
        gameLogicHandler.setGoldStatus(loadSave.isGoldStatus());
        ballControlHandler.setGoDownBall(loadSave.isGoDownBall());
        ballControlHandler.setGoRightBall(loadSave.isGoRightBall());
        ballControlHandler.setCollideToPaddle(loadSave.isCollideToPaddle());
        ballControlHandler.setCollideToPaddleAndMoveToRight(loadSave.isCollideTopPaddleAndMoveToRight());
        ballControlHandler.setCollideToRightWall(loadSave.isCollideToRightWall());
        ballControlHandler.setCollideToLeftWall(loadSave.isCollideToLeftWall());
        ballControlHandler.setCollideToRightBlock(loadSave.isCollideToRightBlock());
        ballControlHandler.setCollideToBottomBlock(loadSave.isCollideToBottomBlock());
        ballControlHandler.setCollideToLeftBlock(loadSave.isCollideToLeftBlock());
        ballControlHandler.setCollideToTopBlock(loadSave.isCollideToTopBlock());
        gameLogicHandler.setLevel(loadSave.getLevel());
        gameLogicHandler.setScore(loadSave.getScore());
        gameLogicHandler.setHeart(loadSave.getHeart());
        gameLogicHandler.setRemainingBlockCount(loadSave.getRemainingBlockCount());

        ball.setxBall(loadSave.getxBall());
        ball.setyBall(loadSave.getyBall());

        paddle.setxPaddle(loadSave.getxPaddle());
        paddle.setyPaddle(loadSave.getyPaddle());
        paddle.setCenterPaddleX(loadSave.getCenterPaddleX());

        gameLogicHandler.setTime(loadSave.getTime());
        gameLogicHandler.setGoldTime(loadSave.getGoldTime());
        ballControlHandler.setvX(loadSave.getvX());

        initGameComponent.getBlocks().clear();
        initGameComponent.getChocos().clear();
        initGameComponent.getBombs().clear();


        for (BlockSerialize ser : loadSave.getBlocks()) {
            initGameComponent.getBlocks().add(new Block(ser.row, ser.j, ser.type));
        }


        try {
            loadFromSave = true;
            startGame();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * Restarts the game by resetting the level, score, and other relevant components.
     */
    public void restartGame() {

        try {

            gameLogicHandler.setLevel(1);
            gameLogicHandler.setHeart(gameLogicHandler.getInitialHeart());
            gameLogicHandler.setScore(0);

            ballControlHandler.setvX(1.000);
            gameLogicHandler.setRemainingBlockCount(0);

            ballControlHandler.resetcollideFlags();
            ballControlHandler.setGoDownBall(true);

            gameLogicHandler.setGoldStatus(false);

            initGameComponent.setExistHeartBlock(false);
            initGameComponent.setExistBombBlock(false);
            gameLogicHandler.setTime(0);
            gameLogicHandler.setGoldTime(0);


            initGameComponent.getBlocks().clear();
            initGameComponent.getChocos().clear();
            initGameComponent.getBombs().clear();

            startGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public boolean isGameFirstOpen() {
        return gameFirstOpen;
    }

    public void setGameFirstOpen(boolean gameFirstOpen) {
        this.gameFirstOpen = gameFirstOpen;
    }

    public boolean isLoadFromSave() {
        return loadFromSave;
    }

    public void setLoadFromSave(boolean loadFromSave) {
        this.loadFromSave = loadFromSave;
    }

    public void setGameSceneController(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    public void setInitGameComponent(InitGameComponent initGameComponent) {
        this.initGameComponent = initGameComponent;
    }

    public void setBallControl(BallControlHandler ballControlHandler) {
        this.ballControlHandler = ballControlHandler;
    }

    public void setGameLogicHandler(GameLogicHandler gameLogicHandler) {
        this.gameLogicHandler = gameLogicHandler;
    }

    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }
    public void setBall(Ball ball) {
        this.ball = ball;
    }

}

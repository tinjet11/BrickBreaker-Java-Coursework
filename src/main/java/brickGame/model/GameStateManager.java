package brickGame.model;

import brickGame.serialization.BlockSerialize;
import brickGame.serialization.LoadSave;
import brickGame.controller.GameSceneController;
import brickGame.handler.GameLogicHandler;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.*;
import java.util.ArrayList;

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
     * The directory path for saving game files.
     */
    private final String SAVE_PATH_DIR = "save"; // Relative to the project directory

    /**
     * The complete path for saving the game state file.
     */
    private final String SAVE_PATH = SAVE_PATH_DIR + "/save.mdds";

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
    private BallControl ballControl;
    private GameLogicHandler gameLogicHandler;
    private GameSceneController gameSceneController;
    private InitGameComponent initGameComponent;

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
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/GameScene.fxml"));
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

                ballControl.setvX(1.000);
                ballControl.setvY(1.000);

                ballControl.resetcollideFlags();
                ballControl.setGoDownBall(true);
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

                outputStream.writeDouble(ballControl.getxBall());
                outputStream.writeDouble(ballControl.getyBall());
                outputStream.writeDouble(initGameComponent.getxPaddle());
                outputStream.writeDouble(initGameComponent.getyPaddle());
                outputStream.writeDouble(initGameComponent.getCenterPaddleX());
                outputStream.writeLong(gameLogicHandler.getTime());
                outputStream.writeLong(gameLogicHandler.getGoldTime());
                outputStream.writeDouble(ballControl.getvX());

                outputStream.writeBoolean(initGameComponent.isExistHeartBlock());
                outputStream.writeBoolean(gameLogicHandler.isGoldStatus());
                outputStream.writeBoolean(ballControl.isGoDownBall());
                outputStream.writeBoolean(ballControl.isGoRightBall());
                outputStream.writeBoolean(ballControl.isCollideToPaddle());
                outputStream.writeBoolean(ballControl.isCollideToPaddleAndMoveToRight());
                outputStream.writeBoolean(ballControl.isCollideToRightWall());
                outputStream.writeBoolean(ballControl.isCollideToLeftWall());
                outputStream.writeBoolean(ballControl.isCollideToRightBlock());
                outputStream.writeBoolean(ballControl.isCollideToBottomBlock());
                outputStream.writeBoolean(ballControl.isCollideToLeftBlock());
                outputStream.writeBoolean(ballControl.isCollideToTopBlock());

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
        ballControl.setGoDownBall(loadSave.isGoDownBall());
        ballControl.setGoRightBall(loadSave.isGoRightBall());
        ballControl.setCollideToPaddle(loadSave.isCollideToPaddle());
        ballControl.setCollideToPaddleAndMoveToRight(loadSave.isCollideTopPaddleAndMoveToRight());
        ballControl.setCollideToRightWall(loadSave.isCollideToRightWall());
        ballControl.setCollideToLeftWall(loadSave.isCollideToLeftWall());
        ballControl.setCollideToRightBlock(loadSave.isCollideToRightBlock());
        ballControl.setCollideToBottomBlock(loadSave.isCollideToBottomBlock());
        ballControl.setCollideToLeftBlock(loadSave.isCollideToLeftBlock());
        ballControl.setCollideToTopBlock(loadSave.isCollideToTopBlock());
        gameLogicHandler.setLevel(loadSave.getLevel());
        gameLogicHandler.setScore(loadSave.getScore());
        gameLogicHandler.setHeart(loadSave.getHeart());
        gameLogicHandler.setRemainingBlockCount(loadSave.getRemainingBlockCount());

        ballControl.setxBall(loadSave.getxBall());
        ballControl.setyBall(loadSave.getyBall());

        initGameComponent.setxPaddle(loadSave.getxPaddle());
        initGameComponent.setyPaddle(loadSave.getyPaddle());
        initGameComponent.setCenterPaddleX(loadSave.getCenterPaddleX());

        gameLogicHandler.setTime(loadSave.getTime());
        gameLogicHandler.setGoldTime(loadSave.getGoldTime());
        ballControl.setvX(loadSave.getvX());

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

            ballControl.setvX(1.000);
            gameLogicHandler.setRemainingBlockCount(0);

            ballControl.resetcollideFlags();
            ballControl.setGoDownBall(true);

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

    public void setBallControl(BallControl ballControl) {
        this.ballControl = ballControl;
    }

    public void setGameLogicHandler(GameLogicHandler gameLogicHandler) {
        this.gameLogicHandler = gameLogicHandler;
    }
}

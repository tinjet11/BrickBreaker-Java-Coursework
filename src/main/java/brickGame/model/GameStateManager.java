package brickGame.model;

import brickGame.Mediator;
import brickGame.serialization.BlockSerialize;
import brickGame.serialization.LoadSave;
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
 *
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
    /**
     * The mediator to handle communication between different components.
     */
    private Mediator mediator;

    /**
     * The singleton instance of the {@code GameStateManager} class.
     */
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
        mediator.getGameLogicHandler().setGameRun(true);
        try {
            //if level equal to endLevel
            if (mediator.getGameLogicHandler().getLevel() == mediator.getGameLogicHandler().getEndLevel()) {
                mediator.getGameSceneController().showWinScene();
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(GAME_SCENE_FXML));
                fxmlLoader.setControllerFactory(c -> mediator.getGameSceneController());

                Scene gameScene = new Scene(fxmlLoader.load());
                mediator.getGameSceneController().showGameScene(gameScene);
                mediator.getGameLogicHandler().setUpEngine();

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
        mediator.getGameLogicHandler().setGameRun(false);
        // Check if nextLevel is already in progress, if yes, return
        if (nextLevelInProgress) {
            return;
        }

        // Set the flag to indicate that nextLevel is in progress
        try {
            Platform.runLater(() -> {

                mediator.getGameLogicHandler().stopEngine();

                mediator.getBallControlHandler().setvX(1.000);
                mediator.getBallControlHandler().setvY(1.000);

                mediator.getBallControlHandler().resetcollideFlags();
                mediator.getBallControlHandler().setGoDownBall(true);
                mediator.getGameLogicHandler().setGoldStatus(false);

                mediator.getInitGameComponent().setExistHeartBlock(false);
                mediator.getInitGameComponent().setExistBombBlock(false);

                mediator.getGameLogicHandler().setTime(0);
                mediator.getGameLogicHandler().setGoldTime(0);

                mediator.getInitGameComponent().getBlocks().clear();
                mediator.getInitGameComponent().getChocos().clear();
                mediator.getInitGameComponent().getBombs().clear();

                mediator.getGameLogicHandler().setRemainingBlockCount(0);

                if (mediator.getGameLogicHandler().getLevel() < mediator.getGameLogicHandler().getEndLevel()) {
                    mediator.getGameLogicHandler().addLevel();
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

            mediator.getGameLogicHandler().stopEngine();

            try {
                outputStream = new ObjectOutputStream(new FileOutputStream(file));

                outputStream.writeInt(mediator.getGameLogicHandler().getLevel());
                outputStream.writeInt(mediator.getGameLogicHandler().getScore());
                outputStream.writeInt(mediator.getGameLogicHandler().getHeart());
                outputStream.writeInt(mediator.getGameLogicHandler().getRemainingBlockCount());

                outputStream.writeDouble(mediator.getBallInstance().getxBall());
                outputStream.writeDouble(mediator.getBallInstance().getyBall());
                outputStream.writeDouble(mediator.getPaddleInstance().getxPaddle());
                outputStream.writeDouble(mediator.getPaddleInstance().getyPaddle());
                outputStream.writeDouble(mediator.getPaddleInstance().getCenterPaddleX());
                outputStream.writeLong(mediator.getGameLogicHandler().getTime());
                outputStream.writeLong(mediator.getGameLogicHandler().getGoldTime());
                outputStream.writeDouble(mediator.getBallControlHandler().getvX());

                outputStream.writeBoolean(mediator.getInitGameComponent().isExistHeartBlock());
                outputStream.writeBoolean(mediator.getGameLogicHandler().isGoldStatus());
                outputStream.writeBoolean(mediator.getBallControlHandler().isGoDownBall());
                outputStream.writeBoolean(mediator.getBallControlHandler().isGoRightBall());
                outputStream.writeBoolean(mediator.getBallControlHandler().isCollideToPaddle());
                outputStream.writeBoolean(mediator.getBallControlHandler().isCollideToPaddleAndMoveToRight());
                outputStream.writeBoolean(mediator.getBallControlHandler().isCollideToRightWall());
                outputStream.writeBoolean(mediator.getBallControlHandler().isCollideToLeftWall());
                outputStream.writeBoolean(mediator.getBallControlHandler().isCollideToRightBlock());
                outputStream.writeBoolean(mediator.getBallControlHandler().isCollideToBottomBlock());
                outputStream.writeBoolean(mediator.getBallControlHandler().isCollideToLeftBlock());
                outputStream.writeBoolean(mediator.getBallControlHandler().isCollideToTopBlock());

                ArrayList<BlockSerialize> blockSerializables = new ArrayList<>();
                for (Block block : mediator.getInitGameComponent().getBlocks()) {
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

        mediator.getInitGameComponent().setExistHeartBlock(loadSave.isExistHeartBlock());
        mediator.getGameLogicHandler().setGoldStatus(loadSave.isGoldStatus());
        mediator.getBallControlHandler().setGoDownBall(loadSave.isGoDownBall());
        mediator.getBallControlHandler().setGoRightBall(loadSave.isGoRightBall());
        mediator.getBallControlHandler().setCollideToPaddle(loadSave.isCollideToPaddle());
        mediator.getBallControlHandler().setCollideToPaddleAndMoveToRight(loadSave.isCollideTopPaddleAndMoveToRight());
        mediator.getBallControlHandler().setCollideToRightWall(loadSave.isCollideToRightWall());
        mediator.getBallControlHandler().setCollideToLeftWall(loadSave.isCollideToLeftWall());
        mediator.getBallControlHandler().setCollideToRightBlock(loadSave.isCollideToRightBlock());
        mediator.getBallControlHandler().setCollideToBottomBlock(loadSave.isCollideToBottomBlock());
        mediator.getBallControlHandler().setCollideToLeftBlock(loadSave.isCollideToLeftBlock());
        mediator.getBallControlHandler().setCollideToTopBlock(loadSave.isCollideToTopBlock());
        mediator.getGameLogicHandler().setLevel(loadSave.getLevel());
        mediator.getGameLogicHandler().setScore(loadSave.getScore());
        mediator.getGameLogicHandler().setHeart(loadSave.getHeart());
        mediator.getGameLogicHandler().setRemainingBlockCount(loadSave.getRemainingBlockCount());

        mediator.getBallInstance().setxBall(loadSave.getxBall());
        mediator.getBallInstance().setyBall(loadSave.getyBall());

        mediator.getPaddleInstance().setxPaddle(loadSave.getxPaddle());
        mediator.getPaddleInstance().setyPaddle(loadSave.getyPaddle());
        mediator.getPaddleInstance().setCenterPaddleX(loadSave.getCenterPaddleX());

        mediator.getGameLogicHandler().setTime(loadSave.getTime());
        mediator.getGameLogicHandler().setGoldTime(loadSave.getGoldTime());
        mediator.getBallControlHandler().setvX(loadSave.getvX());

        mediator.getInitGameComponent().getBlocks().clear();

        mediator.getInitGameComponent().getBombs().forEach((bomb -> {
            bomb.element.setVisible(true);
            bomb.setTimeCreated(mediator.getGameLogicHandler().getTime());
            Platform.runLater(() -> mediator.getGameSceneController().getGamePane().getChildren().add(bomb.element));
        }));

        mediator.getInitGameComponent().getChocos().forEach((bonus -> {
            bonus.element.setVisible(true);
            bonus.setTimeCreated(mediator.getGameLogicHandler().getTime());
            Platform.runLater(() -> mediator.getGameSceneController().getGamePane().getChildren().add(bonus.element));
        }));

        for (BlockSerialize ser : loadSave.getBlocks()) {
            mediator.getInitGameComponent().getBlocks().add(new Block(ser.row, ser.column, ser.type));
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

            mediator.getGameLogicHandler().setLevel(1);
            mediator.getGameLogicHandler().setHeart(mediator.getGameLogicHandler().getInitialHeart());
            mediator.getGameLogicHandler().setScore(0);

            mediator.getBallControlHandler().setvX(1.000);
            mediator.getGameLogicHandler().setRemainingBlockCount(0);

            mediator.getBallControlHandler().resetcollideFlags();
            mediator.getBallControlHandler().setGoDownBall(true);

            mediator.getGameLogicHandler().setGoldStatus(false);

            mediator.getInitGameComponent().setExistHeartBlock(false);
            mediator.getInitGameComponent().setExistBombBlock(false);
            mediator.getGameLogicHandler().setTime(0);
            mediator.getGameLogicHandler().setGoldTime(0);


            mediator.getInitGameComponent().getBlocks().clear();
            mediator.getInitGameComponent().getChocos().clear();
            mediator.getInitGameComponent().getBombs().clear();

            startGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the current state of the game.
     *
     * @return The current state of the game.
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Sets the current state of the game.
     *
     * @param gameState The state to set.
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Checks if it's the first time the game is opened.
     *
     * @return True if it's the first time; false otherwise.
     */
    public boolean isGameFirstOpen() {
        return gameFirstOpen;
    }

    /**
     * Sets the flag indicating whether it's the first time the game is opened.
     *
     * @param gameFirstOpen True if it's the first time; false otherwise.
     */
    public void setGameFirstOpen(boolean gameFirstOpen) {
        this.gameFirstOpen = gameFirstOpen;
    }

    /**
     * Checks if the game is loaded from a saved state.
     *
     * @return True if loaded from save; false otherwise.
     */
    public boolean isLoadFromSave() {
        return loadFromSave;
    }

    /**
     * Sets the flag indicating whether the game is loaded from a saved state.
     *
     * @param loadFromSave True if loaded from save; false otherwise.
     */
    public void setLoadFromSave(boolean loadFromSave) {
        this.loadFromSave = loadFromSave;
    }


    /**
     * Sets the mediator to handle communication between different components.
     *
     * @param mediator The mediator instance.
     */
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }
}

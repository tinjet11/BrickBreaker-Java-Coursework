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

public class GameStateManager {

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setInitGameComponent(InitGameComponent initGameComponent) {
        this.initGameComponent = initGameComponent;
    }

    public boolean isGameFirstOpen() {
        return gameFirstOpen;
    }

    public void setGameFirstOpen(boolean gameFirstOpen) {
        this.gameFirstOpen = gameFirstOpen;
    }

    public enum GameState {
        ON_START,
        IN_PROGRESS,
        PAUSED,
        GAME_OVER,
        WIN,
    }

    private final String SAVE_PATH_DIR = "save"; // Relative to the project directory

    // Construct the complete path using the directory and filename
    private final String SAVE_PATH = SAVE_PATH_DIR + "/save.mdds";
    private GameState gameState = GameState.ON_START;

    public boolean isLoadFromSave() {
        return loadFromSave;
    }

    public void setLoadFromSave(boolean loadFromSave) {
        this.loadFromSave = loadFromSave;
    }

    private boolean loadFromSave = false;

    private boolean gameFirstOpen = true;

    private BallControl ballControl;
    private GameLogicHandler gameLogicHandler;
    private GameSceneController gameSceneController;
    private InitGameComponent initGameComponent;

    public void setGameSceneController(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    public void setBallControl(BallControl ballControl) {
        this.ballControl = ballControl;
    }

    public void setGameLogicHandler(GameLogicHandler gameLogicHandler) {
        this.gameLogicHandler = gameLogicHandler;
    }

    private static GameStateManager instance;

    private GameStateManager() {
    }

    public static GameStateManager getInstance() {
        if (instance == null) {
            instance = new GameStateManager();
        }
        return instance;
    }

    public void startGame() {
        gameLogicHandler.setGameRun(true);
        try {
            //if level equal to endLevel
            if (gameLogicHandler.getLevel() == gameLogicHandler.getEndLevel()) {
                gameSceneController.showWinScene();
            } else {
                FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("/fxml/GameScene.fxml"));
                fxmlLoader1.setControllerFactory(c -> gameSceneController);

                Scene gameScene = new Scene(fxmlLoader1.load());
                gameSceneController.showGameScene(gameScene);
                gameLogicHandler.setUpEngine();

                if (isLoadFromSave()) {
                    setLoadFromSave(false);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading GameScene.fxml: " + e.getMessage());
        }
    }

    private boolean nextLevelInProgress = false;

    public void nextLevel() {
        gameLogicHandler.setGameRun(false);
        // Check if nextLevel is already in progress, if yes, return
        if (nextLevelInProgress) {
            return;
        }
        System.out.println("level: " + gameLogicHandler.getLevel());

        // Set the flag to indicate that nextLevel is in progress
        //   new Thread(() -> {
        try {
            //  Thread.sleep(1);
            Platform.runLater(() -> {
                System.out.println("inside try");

                gameLogicHandler.stopEngine();
                System.out.println("engine stopped");

                ballControl.setvX(1.000);
                ballControl.setvY(1.000);

                ballControl.resetcollideFlags();
                ballControl.setGoDownBall(true);
                gameLogicHandler.setGoldStatus(false);

                initGameComponent.setExistHeartBlock(false);
                initGameComponent.setExistBombBlock(false);

                //gameLogicHandler.setHitTime(0);
                gameLogicHandler.setTime(0);
                gameLogicHandler.setGoldTime(0);

                initGameComponent.getBlocks().clear();
                initGameComponent.getChocos().clear();
                initGameComponent.getBombs().clear();

                gameLogicHandler.setRemainingBlockCount(0);

                if (gameLogicHandler.getLevel() < gameLogicHandler.getEndLevel()) {
                    gameLogicHandler.addLevel();
                    System.out.println("level added: current level: " + gameLogicHandler.getLevel());
                }
                // gameLogicHandler.setGoldStatus(true);
                System.out.println("before start game");
                startGame();
                System.out.println("after start game");
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("%s", "nextLevel function in Main.java:");
        } finally {
            // Reset the flag to indicate that nextLevel is completed
            nextLevelInProgress = false;
            //gameLogicHandler.setGameRun(true);
            System.out.println("nextlevel run completed");
        }
        // }).start();

    }

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

                System.out.println(blockSerializables.size());

                outputStream.writeObject(blockSerializables);

                // new Score(this).showMessage("Game Saved", 300, 300);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.printf("%s", "savegame (FNTE) function in Main.java:");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.printf("%s", "savegame (IOE) function in Main.java:");
            } finally {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.printf("%s", "savegame (IOE2) function in Main.java:");
                }
            }
        }).start();

    }

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
            //gameLogicHandler.setHitTime(0);
            gameLogicHandler.setTime(0);
            gameLogicHandler.setGoldTime(0);


            initGameComponent.getBlocks().clear();
            initGameComponent.getChocos().clear();
            initGameComponent.getBombs().clear();

            startGame();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("%s", "restart  function in Main.java:");
        }
    }


}

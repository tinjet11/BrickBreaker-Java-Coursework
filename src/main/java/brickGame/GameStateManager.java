package brickGame;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.*;
import java.util.ArrayList;

import static brickGame.BallControl.*;
import static brickGame.Main.*;
import static brickGame.InitGameComponent.*;
import static brickGame.GameLogicHandler.*;

public class GameStateManager {

    public void setEngine(GameEngine engine) {
        this.engine = engine;
    }

    public enum GameState {
        ON_START,
        IN_PROGRESS,
        PAUSED,
        GAME_OVER
    }

    public static final String SAVE_PATH_DIR = "save"; // Relative to the project directory

    // Construct the complete path using the directory and filename
    public static final String SAVE_PATH = SAVE_PATH_DIR + "/save.mdds";
    public static GameState gameState = GameState.ON_START;

    public boolean isLoadFromSave() {
        return loadFromSave;
    }

    public void setLoadFromSave(boolean loadFromSave) {
        this.loadFromSave = loadFromSave;
    }

    private boolean loadFromSave = false;

    private GameEngine engine;
    private Scene gameScene;
    private BallControl ballControl;
    private GameLogicHandler gameLogicHandler;
    private  GameSceneController gameSceneController;


    public void setGameSceneController(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }
    public void setBallControl(BallControl ballControl) {
        this.ballControl = ballControl;
    }

    public void setGameLogicHandler(GameLogicHandler gameLogicHandler) {
        this.gameLogicHandler = gameLogicHandler;
    }
    private  static  GameStateManager instance;
    private GameStateManager() {}
    public static GameStateManager getInstance() {
        if (instance == null) {
            instance = new GameStateManager();
        }
        return instance;
    }
    public void startGame(){
        gameLogicHandler.setGameRun(true);
//        try {
//
//
//            FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("fxml/GameScene.fxml"));
//            fxmlLoader1.setControllerFactory(c -> {
//                return gameSceneController = GameSceneController.getInstance();
//            });
//
//            gameScene = new Scene(fxmlLoader1.load());
//            gameSceneController.showScene(gameScene);
//            root = gameSceneController.getGamePane();
//            gameSceneController.setLevelLabel("Level: " + gameLogicHandler.getLevel());
//            System.out.println("start game run completed");
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.err.println("Error loading GameScene.fxml: " + e.getMessage());
//        }
        if (gameLogicHandler.getLevel() == 1) {
            gameLogicHandler.setHeart(gameLogicHandler.getInitialHeart());
        }

            try {
                FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("fxml/GameScene.fxml"));
                fxmlLoader1.setControllerFactory(c -> gameSceneController);

                gameScene = new Scene(fxmlLoader1.load());
                gameSceneController.showScene(gameScene);
                root = gameSceneController.getGamePane();
                gameSceneController.setLevelLabel("Level: " + gameLogicHandler.getLevel());
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
        nextLevelInProgress = true;
            try {
                System.out.println("inside try");

                engine.stop();
                System.out.println("engine stopped");

                ballControl.setvX(1.000);
                ballControl.setvY(1.000);

                ballControl.resetcollideFlags();
                ballControl.setGoDownBall(true);
                gameLogicHandler.setGoldStatus(false);

                isExistHeartBlock = false;

                gameLogicHandler.setHitTime(0);
                gameLogicHandler.setTime(0);
                gameLogicHandler.setGoldTime(0);

                blocks.clear();
                chocos.clear();
                gameLogicHandler.setRemainingBlockCount(0);

                if (gameLogicHandler.getLevel() < gameLogicHandler.getEndLevel()) {
                    gameLogicHandler.addLevel();
                    System.out.println("level added: current level: "+  gameLogicHandler.getLevel());
                }
                // gameLogicHandler.setGoldStatus(true);
                System.out.println("before start game");
                startGame();
                System.out.println("after start game");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.printf("%s", "nextLevel function in Main.java:");
            } finally {
                // Reset the flag to indicate that nextLevel is completed
                nextLevelInProgress = false;
                //gameLogicHandler.setGameRun(true);
                System.out.println("nextlevel run completed");
            }

    }

    public void saveGame() {
        new Thread(() -> {
            new File(SAVE_PATH_DIR).mkdirs();
            File file = new File(SAVE_PATH);
            ObjectOutputStream outputStream = null;

            engine.stop();
            try {
                outputStream = new ObjectOutputStream(new FileOutputStream(file));

                outputStream.writeInt(gameLogicHandler.getLevel());
                outputStream.writeInt(gameLogicHandler.getScore());
                outputStream.writeInt(gameLogicHandler.getHeart());
                outputStream.writeInt(gameLogicHandler.getRemainingBlockCount());

                outputStream.writeDouble(ballControl.getxBall());
                outputStream.writeDouble(ballControl.getyBall());
                outputStream.writeDouble(xPaddle);
                outputStream.writeDouble(yPaddle);
                outputStream.writeDouble(centerPaddleX);
                outputStream.writeLong(gameLogicHandler.getTime());
                outputStream.writeLong(gameLogicHandler.getGoldTime());
                outputStream.writeDouble(ballControl.getvX());

                outputStream.writeBoolean(isExistHeartBlock);
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
                for (Block block : blocks) {
                    if (block.isDestroyed) {
                        continue;
                    }
                    blockSerializables.add(new BlockSerialize(block.row, block.column, block.type));
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

        isExistHeartBlock = loadSave.isExistHeartBlock;
        gameLogicHandler.setGoldStatus(loadSave.isGoldStatus);
        ballControl.setGoDownBall(loadSave.goDownBall);
        ballControl.setGoRightBall(loadSave.goRightBall);
        ballControl.setCollideToPaddle(loadSave.collideToPaddle);
        ballControl.setCollideToPaddleAndMoveToRight(loadSave.collideTopPaddleAndMoveToRight);
        ballControl.setCollideToRightWall(loadSave.collideToRightWall);
        ballControl.setCollideToLeftWall(loadSave.collideToLeftWall);
        ballControl.setCollideToRightBlock(loadSave.collideToRightBlock);
        ballControl.setCollideToBottomBlock(loadSave.collideToBottomBlock);
        ballControl.setCollideToLeftBlock(loadSave.collideToLeftBlock);
        ballControl.setCollideToTopBlock(loadSave.collideToTopBlock);
        gameLogicHandler.setLevel(loadSave.level);
        gameLogicHandler.setScore(loadSave.score);
        gameLogicHandler.setHeart(loadSave.heart);
        gameLogicHandler.setRemainingBlockCount(loadSave.remainingBlockCount);

        ballControl.setxBall(loadSave.xBall);
        ballControl.setyBall(loadSave.yBall);
        xPaddle = loadSave.xPaddle;
        yPaddle = loadSave.yPaddle;
        centerPaddleX = loadSave.centerPaddleX;
        gameLogicHandler.setTime(loadSave.time);
        gameLogicHandler.setGoldTime(loadSave.goldTime);
        ballControl.setvX(loadSave.vX);

        blocks.clear();
        chocos.clear();

        for (BlockSerialize ser : loadSave.blocks) {
            blocks.add(new Block(ser.row, ser.j, ser.type));
        }

        try {
            loadFromSave = true;
            if (loadFromSave) {
                // Delete the saved game file
                File saveFile = new File(SAVE_PATH);
                if (saveFile.exists()) {
                    if (saveFile.delete()) {
                        System.out.println("Deleted the saved game file.");
                    } else {
                        System.err.println("Failed to delete the saved game file.");
                    }
                }
            }
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

            isExistHeartBlock = false;
            gameLogicHandler.setHitTime(0);
            gameLogicHandler.setTime(0);
            gameLogicHandler.setGoldTime(0);


            blocks.clear();
            chocos.clear();

            startGame();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("%s", "restart  function in Main.java:");
        }
    }


}

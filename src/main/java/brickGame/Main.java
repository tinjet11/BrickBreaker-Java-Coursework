package brickGame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

import java.util.Random;

import static brickGame.BallControl.*;

public class Main extends Application implements Actionable {

    public static boolean isGameRun = false;
    public static int endLevel = 18;
    public static int level = 15;

    public static double xPaddle = 0.0f;
    public static double centerPaddleX;
    public static double yPaddle = 640.0f;
    public static final int paddleWidth = 130;
    public static final int paddleHeight = 10;
    public static final int halfPaddleWidth = paddleWidth / 2;
    public static final int sceneWidth = 500;
    public static final int sceneHeight = 700;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static Circle ball;
    public static double xBall;
    public static double yBall;

    public static boolean isGoldStatus = false;
    public static boolean isExistHeartBlock = false;

    public static Rectangle rect;
    public static final int ballRadius = 10;

    public static int remainingBlockCount = 0;

    public static double v = 1.000;

    public static int heart = 10;
    public static int initialHeart = 3;
    public static int score = 0;
    public static long time = 0;
    public static long hitTime = 0;
    public static long goldTime = 0;

    public static GameEngine engine;

    private Scene gameScene;
    public static final String savePathDir = "save"; // Relative to the project directory

    // Construct the complete path using the directory and filename
    public static final String savePath = savePathDir + "/save.mdds";

    public static ArrayList<Block> blocks = new ArrayList<>();
    private ArrayList<Bonus> chocos = new ArrayList<>();

    public Pane root;
    public AnchorPane gameRoot;

    public static boolean loadFromSave = false;

    public static Stage primaryStage;

    private GameSceneController gameSceneController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        initializeMenuScene();
        FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("Settings.fxml"));
        fxmlLoader1.setControllerFactory(c -> {
            return new SettingsController(this, primaryStage);
        });
    }


    private void initializeMenuScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Menu.fxml"));
        Scene menuScene = null;
        fxmlLoader.setControllerFactory(c -> {
            return new MenuController(this, primaryStage);
        });
        try {
            menuScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        primaryStage.setTitle("Brick Breaker Game");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    public void startGame(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        if(!isGameRun){
            heart =  initialHeart;
        }
        isGameRun = true;

        FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("GameScene.fxml"));
        fxmlLoader1.setControllerFactory(c -> {
            return gameSceneController = new GameSceneController(this, primaryStage);
        });
        gameScene = new Scene(fxmlLoader1.load());
        gameSceneController.showScene(gameScene);
        root = gameSceneController.getGamePane();
        gameRoot = gameSceneController.getGameAnchorPane();
        gameSceneController.setLevelLabel("Level: " + level);
    }


    public static void main(String[] args) {
        launch(args);
    }


    private void checkDestroyedCount() {
        //System.out.println(remainingBlockCount);
            if (remainingBlockCount == 0 && level != endLevel) {
                System.out.println("Level Up!");
                nextLevel();
            }

    }

    public static void saveGame() {
        new Thread(() -> {
            new File(savePathDir).mkdirs();
            File file = new File(savePath);
            ObjectOutputStream outputStream = null;

            engine.stop();
            try {
                outputStream = new ObjectOutputStream(new FileOutputStream(file));

                outputStream.writeInt(level);
                outputStream.writeInt(score);
                outputStream.writeInt(heart);
                outputStream.writeInt(remainingBlockCount);


                outputStream.writeDouble(xBall);
                outputStream.writeDouble(yBall);
                outputStream.writeDouble(xPaddle);
                outputStream.writeDouble(yPaddle);
                outputStream.writeDouble(centerPaddleX);
                outputStream.writeLong(time);
                outputStream.writeLong(goldTime);
                outputStream.writeDouble(vX);


                outputStream.writeBoolean(isExistHeartBlock);
                outputStream.writeBoolean(isGoldStatus);
                outputStream.writeBoolean(goDownBall);
                outputStream.writeBoolean(goRightBall);
                outputStream.writeBoolean(collideToPaddle);
                outputStream.writeBoolean(collideToPaddleAndMoveToRight);
                outputStream.writeBoolean(collideToRightWall);
                outputStream.writeBoolean(collideToLeftWall);
                outputStream.writeBoolean(collideToRightBlock);
                outputStream.writeBoolean(collideToBottomBlock);
                outputStream.writeBoolean(collideToLeftBlock);
                outputStream.writeBoolean(collideToTopBlock);

                ArrayList<BlockSerialize> blockSerializables = new ArrayList<>();
                for (Block block : blocks) {
                    if (block.isDestroyed) {
                        continue;
                    }
                    blockSerializables.add(new BlockSerialize(block.row, block.column, block.type));
                }

                System.out.println(blockSerializables.size());

                outputStream.writeObject(blockSerializables);

                //new Score(this).showMessage("Game Saved", 300, 300);


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
        isGoldStatus = loadSave.isGoldStatus;
        goDownBall = loadSave.goDownBall;
        goRightBall = loadSave.goRightBall;
        collideToPaddle = loadSave.collideToPaddle;
        collideToPaddleAndMoveToRight = loadSave.collideTopPaddleAndMoveToRight;
        collideToRightWall = loadSave.collideToRightWall;
        collideToLeftWall = loadSave.collideToLeftWall;
        collideToRightBlock = loadSave.collideToRightBlock;
        collideToBottomBlock = loadSave.collideToBottomBlock;
        collideToLeftBlock = loadSave.collideToLeftBlock;
        collideToTopBlock = loadSave.collideToTopBlock;
        level = loadSave.level;
        score = loadSave.score;
        heart = loadSave.heart;
        remainingBlockCount = loadSave.remainingBlockCount;
        System.out.printf("remainingBlockCount : %d", remainingBlockCount);
        xBall = loadSave.xBall;
        yBall = loadSave.yBall;
        xPaddle = loadSave.xPaddle;
        yPaddle = loadSave.yPaddle;
        centerPaddleX = loadSave.centerPaddleX;
        time = loadSave.time;
        goldTime = loadSave.goldTime;
        vX = loadSave.vX;

        blocks.clear();
        chocos.clear();

        for (BlockSerialize ser : loadSave.blocks) {
            int r = new Random().nextInt(200);
            blocks.add(new Block(ser.row, ser.j, ser.type));
        }


        try {
            loadFromSave = true;
            if (loadFromSave) {
                // Delete the saved game file
                File saveFile = new File(Main.savePath);
                if (saveFile.exists()) {
                    if (saveFile.delete()) {
                        System.out.println("Deleted the saved game file.");
                    } else {
                        System.err.println("Failed to delete the saved game file.");
                    }
                }
            }
            startGame(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("%s", "loadGame function in Main.java:");
        }


    }

    private boolean nextLevelInProgress = false;

    private void nextLevel() {
        // Check if nextLevel is already in progress, if yes, return
        if (nextLevelInProgress) {
            return;
        }

        // Set the flag to indicate that nextLevel is in progress
        nextLevelInProgress = true;

        Platform.runLater(() -> {
            try {
                vX = 1.000;
                vY = 1.000;

                engine.stop();
                resetcollideFlags();
                goDownBall = true;

                isGoldStatus = false;
                isExistHeartBlock = false;

                hitTime = 0;
                time = 0;
                goldTime = 0;

                engine.stop();
                blocks.clear();
                chocos.clear();
                remainingBlockCount = 0;
                if (level < endLevel) {
                    level++;
                }
                startGame(primaryStage);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.printf("%s", "nextLevel function in Main.java:");
            } finally {
                // Reset the flag to indicate that nextLevel is completed
                nextLevelInProgress = false;
            }
        });
    }


    public void restartGame() {

        try {
            level = 1;
            heart = initialHeart;
            score = 0;
            vX = 1.000;
            remainingBlockCount = 0;
            resetcollideFlags();
            goDownBall = true;

            isGoldStatus = false;
            isExistHeartBlock = false;
            hitTime = 0;
            time = 0;
            goldTime = 0;

            blocks.clear();
            chocos.clear();

            startGame(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("%s", "restart  function in Main.java:");
        }
    }


    @Override
    public void onUpdate() {
        Platform.runLater(() -> {
                    gameSceneController.setScoreLabel("Score: " + score);
                    gameSceneController.setHeartLabel("Heart : " + heart);

                    rect.setX(xPaddle);
                    rect.setY(yPaddle);
                    ball.setCenterX(xBall);
                    ball.setCenterY(yBall);
                }
        );

        if (yBall >= Block.getPaddingTop() && yBall <= (Block.getHeight() * (level + 1)) + Block.getPaddingTop()) {
            for (final Block block : blocks) {
                try {
                    int hitCode = block.checkHitToBlock(xBall, yBall);
                    if (hitCode != Block.NO_HIT) {
                        score += 1;

                        new Score(this).show(block.x, block.y, 1);

                        block.rect.setVisible(false);
                        block.isDestroyed = true;
                        remainingBlockCount--;
                        resetcollideFlags();

                        if (block.type == Block.BLOCK_CHOCO) {
                            final Bonus choco = new Bonus(block.row, block.column);
                            choco.setTimeCreated(time);
                            Platform.runLater(() -> root.getChildren().add(choco.choco));

                            chocos.add(choco);
                        }

                        if (block.type == Block.BLOCK_STAR) {
                            Platform.runLater(() -> {
                                goldTime = time;
                                ball.setFill(new ImagePattern(new Image("goldball.png")));
                                System.out.println("gold ball");
                                gameRoot.getStyleClass().add("goldRoot");
                                isGoldStatus = true;
                            });
                        }

                        if (block.type == Block.BLOCK_HEART) {
                            heart++;
                            System.out.println("heart hitted");
                        }

                        Platform.runLater(() -> {
                            if (hitCode == Block.HIT_RIGHT) {
                                collideToRightBlock = true;
                            } else if (hitCode == Block.HIT_BOTTOM) {
                                collideToBottomBlock = true;
                            } else if (hitCode == Block.HIT_LEFT) {
                                collideToLeftBlock = true;
                            } else if (hitCode == Block.HIT_TOP) {
                                collideToTopBlock = true;
                            }
                        });
                    }

                    //TODO hit to break and some work here....
                    //System.out.println("Break in row:" + block.row + " and column:" + block.column + " hit");

                } catch (Exception e) {
                    e.printStackTrace();

                    System.out.println("Onupdate function");
                    break;
                }
            }

        }
    }


    @Override
    public void onInit() {


    }

    @Override
    public void onPhysicsUpdate() {
        if (level != endLevel) {

                checkDestroyedCount();


        }
        setPhysicsToBall(this);

        if (time - goldTime > 5000) {
            Platform.runLater(() -> {
                ball.setFill(new ImagePattern(new Image("ball.png")));
                gameRoot.getStyleClass().remove("goldRoot");
                isGoldStatus = false;
            });

        }
        //this part is about the Bonus object
        for (Bonus choco : chocos) {
            if (choco.getY() > sceneHeight || choco.isTaken()) {
                continue;
            }

            if (choco.getY() >= yPaddle && choco.getY() <= yPaddle + paddleHeight && choco.getX() >= xPaddle && choco.getX() <= xPaddle + paddleWidth) {
                System.out.println("You Got it and +3 score for you");
                choco.setTaken(true);
                choco.choco.setVisible(false);
                System.out.println("choco hited");
                score += 3;
                new Score(this).show(choco.getX(), choco.getY(), 3);
            }
            choco.setY(choco.getY() +((time - choco.getTimeCreated()) / 1000.000) + 1.000);
            Platform.runLater(() -> {
                // Update UI to reflect the new position
                choco.choco.setY(choco.getY());
            });
        }
    }


    @Override
    public void onTime(long time) {
        this.time = time;
    }
}

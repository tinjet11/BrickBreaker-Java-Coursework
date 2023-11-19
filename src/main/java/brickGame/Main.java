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

public class Main extends Application {

    public static boolean isGameRun = false;
    public static int endLevel = 18;
    public static int level = 1;

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

    public static Scene gameScene;
    public static final String savePathDir = "save"; // Relative to the project directory

    // Construct the complete path using the directory and filename
    public static final String savePath = savePathDir + "/save.mdds";

    public static ArrayList<Block> blocks = new ArrayList<>();
    public static ArrayList<Bonus> chocos = new ArrayList<>();

    public static Pane root;
    public static AnchorPane gameRoot;

    public static boolean loadFromSave = false;

    public static Stage primaryStage;

    public static GameSceneController gameSceneController;
    public static GameStateManager gameStateManager ;


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        initializeMenuScene();
        FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("Settings.fxml"));
        fxmlLoader1.setControllerFactory(c -> {
            return new SettingsController(this, primaryStage);
        });

        gameStateManager = new GameStateManager(this);
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
        if (!isGameRun) {
            heart = initialHeart;
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

    private boolean nextLevelInProgress = false;

    public void nextLevel() {
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
}

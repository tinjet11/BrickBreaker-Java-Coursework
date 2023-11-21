package brickGame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

import static brickGame.BallControl.*;

public class Main extends Application {

    public static boolean isGameRun = false;

    public static int endLevel = 18;

    public static int level = 1;
    public static boolean isGoldStatus = false;

    public static int remainingBlockCount = 0;

    public static double xPaddle = 0.0f;
    public static double centerPaddleX;
    public static double yPaddle = 640.0f;
    public static final int PADDLE_WIDTH = 130;
    public static final int PADDLE_HEIGHT = 10;
    public static final int HALF_PADDLE_WIDTH = PADDLE_WIDTH / 2;
    public static final int SCENE_WIDTH = 500;
    public static final int SCENE_HEIGHT = 700;
    public static boolean isExistHeartBlock = false;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;



    public static Rectangle rect;
    public static final int BALL_RADIUS = 10;



    public static double v = 1.000;

    public static int heart = 10;
    public static int initialHeart = 3;

    public static int score = 0;
    public static long time = 0;
    public static long hitTime = 0;
    public static long goldTime = 0;

    public static GameEngine engine;
    public static Scene gameScene;
    public static final String SAVE_PATH_DIR = "save"; // Relative to the project directory

    // Construct the complete path using the directory and filename
    public static final String SAVE_PATH = SAVE_PATH_DIR + "/save.mdds";

    public static ArrayList<Block> blocks = new ArrayList<>();
    public static ArrayList<Bonus> chocos = new ArrayList<>();

    public static Pane root;
    public static AnchorPane gameRoot;
    public static GameStateManager gameStateManager;
    public static boolean loadFromSave = false;

    public static Stage primaryStage;






    @Override
    public void start(Stage primaryStage) throws Exception {
        gameStateManager = new GameStateManager();
        this.primaryStage = primaryStage;
        initializeMenuScene();
        FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("fxml/Settings.fxml"));
        fxmlLoader1.setControllerFactory(c -> {
            return new SettingsController();
        });

    }


    private void initializeMenuScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/Menu.fxml"));
        Scene menuScene = null;
        fxmlLoader.setControllerFactory(c -> {
            return new MenuController();
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




    public static void main(String[] args) {
        launch(args);
    }

}

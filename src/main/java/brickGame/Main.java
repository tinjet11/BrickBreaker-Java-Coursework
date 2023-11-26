package brickGame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.*;

public class Main extends Application {

    public static AnchorPane root;

    public static Stage primaryStage;
    public static SoundManager gameSoundManager;

    private GameLogicHandler gameLogicHandler;
    private BallControl ballControl;
    private GameSceneController gameSceneController;
    private  GameStateManager gameStateManager;
    @Override
    public void start(Stage primaryStage) throws Exception {
        gameLogicHandler = GameLogicHandler.getInstance();
        ballControl = BallControl.getInstance();
        gameSceneController = GameSceneController.getInstance();
        gameStateManager = GameStateManager.getInstance();


        gameLogicHandler.setBallControl(ballControl);
        gameLogicHandler.setGameSceneController(gameSceneController);
        gameLogicHandler.setGameStateManager(gameStateManager);

        ballControl.setGameLogicHandler(gameLogicHandler);

        gameSceneController.setBallControl(ballControl);
        gameSceneController.setGameLogicHandler(gameLogicHandler);
        gameSceneController.setGameStateManager(gameStateManager);

        gameStateManager.setBallControl(ballControl);
        gameStateManager.setGameLogicHandler(gameLogicHandler);
        gameStateManager.setGameSceneController(gameSceneController);



        this.primaryStage = primaryStage;
        initializeMenuScene();
        FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("fxml/Settings.fxml"));
        fxmlLoader1.setControllerFactory(c -> {
            return new SettingsController();
        });

        gameSoundManager = new SoundManager(Main.class.getResource("/bg-music.mp3"), SoundManager.MusicType.BG_MUSIC);
        Platform.runLater(()->{
            gameSoundManager.play();
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

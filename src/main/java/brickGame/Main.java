package brickGame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.*;

public class Main extends Application {

    public static GameEngine engine;

    public static AnchorPane root;
    public static GameStateManager gameStateManager;
    public static boolean loadFromSave = false;
    public static Stage primaryStage;
    public static SoundManager gameSoundManager;

    @Override
    public void start(Stage primaryStage) throws Exception {
        gameStateManager = new GameStateManager();
        this.primaryStage = primaryStage;
        initializeMenuScene();
        FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("fxml/Settings.fxml"));
        fxmlLoader1.setControllerFactory(c -> {
            return new SettingsController();
        });

        gameSoundManager = new SoundManager(Main.class.getResource("/bg-music.mp3"), SoundManager.MusicType.BG_MUSIC);
        gameSoundManager.play();
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

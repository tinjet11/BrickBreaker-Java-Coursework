package brickGame;

import brickGame.controller.GameSceneController;
import brickGame.controller.MenuController;
import brickGame.handler.GameLogicHandler;
import brickGame.model.BallControl;
import brickGame.model.GameStateManager;
import brickGame.model.InitGameComponent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.*;


/**
 * The main class that serves as the entry point for the Brick Breaker Game.
 * It extends the JavaFX Application class and initializes the game components,
 * such as the game logic handler, ball control, game scene controller, and game state manager.
 * It also handles the initialization of the menu scene.
 * @author Leong Tin Jet
 * @version 1.0
 */
public class Main extends Application {
    private  Stage primaryStage;
    private GameLogicHandler gameLogicHandler;
    private BallControl ballControl;
    private GameSceneController gameSceneController;
    private GameStateManager gameStateManager;
    private InitGameComponent initGameComponent;

    /**
     * The entry point of the JavaFX application. Initializes the game components and
     * sets up the menu scene as the initial scene.
     *
     * @param primaryStage The primary stage for the application.
     * @throws Exception If an exception occurs during application startup.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        gameLogicHandler = GameLogicHandler.getInstance();
        ballControl = BallControl.getInstance();
        gameSceneController = GameSceneController.getInstance();
        gameStateManager = GameStateManager.getInstance();
        initGameComponent = InitGameComponent.getInstance();


        gameLogicHandler.setBallControl(ballControl);
        gameLogicHandler.setGameSceneController(gameSceneController);
        gameLogicHandler.setGameStateManager(gameStateManager);
        gameLogicHandler.setInitGameComponent(initGameComponent);

        ballControl.setGameLogicHandler(gameLogicHandler);
        ballControl.setInitGameComponent(initGameComponent);

        gameSceneController.setBallControl(ballControl);
        gameSceneController.setGameLogicHandler(gameLogicHandler);
        gameSceneController.setGameStateManager(gameStateManager);
        gameSceneController.setInitGameComponent(initGameComponent);
        gameSceneController.setPrimaryStage(primaryStage);

        gameStateManager.setBallControl(ballControl);
        gameStateManager.setGameLogicHandler(gameLogicHandler);
        gameStateManager.setGameSceneController(gameSceneController);
        gameStateManager.setInitGameComponent(initGameComponent);

        initGameComponent.setBallControl(ballControl);
        initGameComponent.setGameLogicHandler(gameLogicHandler);
        this.primaryStage = primaryStage;
        Platform.runLater(this::initializeMenuScene);
    }

    /**
     * Initializes the menu scene by loading the corresponding FXML file.
     * Displays the menu scene with the option to load a game.
     */
    private void initializeMenuScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(Constants.MENU_SCENE_FXML));
        Scene menuScene = null;
        fxmlLoader.setControllerFactory(c -> {
            return new MenuController();
        });
        try {
            menuScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //only set the loadBtn visible when the Game first open
        Button loadBtn = (Button) menuScene.lookup("#loadButton");
        loadBtn.setVisible(true);

        primaryStage.setTitle("Brick Breaker Game");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

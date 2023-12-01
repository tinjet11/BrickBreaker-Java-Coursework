package brickGame;

import brickGame.controller.GameSceneController;
import brickGame.controller.MenuController;
import brickGame.handler.GameLogicHandler;
import brickGame.model.BallControl;
import brickGame.model.GameStateManager;
import brickGame.model.InitGameComponent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.*;

public class Main extends Application {
    private  Stage primaryStage;
    private GameLogicHandler gameLogicHandler;
    private BallControl ballControl;
    private GameSceneController gameSceneController;
    private GameStateManager gameStateManager;
    private InitGameComponent initGameComponent;

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
        System.out.println("All set");
        this.primaryStage = primaryStage;
        initializeMenuScene();
    }

    private void initializeMenuScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml/Menu.fxml"));
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

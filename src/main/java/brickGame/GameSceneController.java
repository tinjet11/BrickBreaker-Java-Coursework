package brickGame;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;

import static brickGame.Main.*;
import static brickGame.InitGameComponent.*;


public class GameSceneController {
    @FXML
    private AnchorPane gamePane;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label heartLabel;
    @FXML
    private Label levelLabel;
    @FXML
    private Button pauseButton;

    private MenuController menuController;

    private final int LEFT = 1;
    private final int RIGHT = 2;
    private BallControl ballControl;
    private GameLogicHandler gameLogicHandler;

    private static GameSceneController instance;
    private GameStateManager gameStateManager;

    private InitGameComponent initGameComponent;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private Stage primaryStage;

    private GameSceneController() {
    }

    public void setBallControl(BallControl ballControl) {
        this.ballControl = ballControl;
    }

    public void setGameLogicHandler(GameLogicHandler gameLogicHandler) {
        this.gameLogicHandler = gameLogicHandler;
    }

    public void setGameStateManager(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
    }

    public static GameSceneController getInstance() {
        if (instance == null) {
            instance = new GameSceneController();
        }
        return instance;
    }


    public void showLoseScene() {
        gameLogicHandler.stopEngine();

        System.out.println("heart: " + gameLogicHandler.getHeart());

        try {
            gameLogicHandler.setGameRun(false);
            gameStateManager.setGameState(GameStateManager.GameState.GAME_OVER);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/Menu.fxml"));
            fxmlLoader.setControllerFactory(c -> {
                return menuController = new MenuController();
            });
            Scene loseMenuScene = new Scene(fxmlLoader.load());

            menuController.showMenuScene("Lose", loseMenuScene);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showWinScene() {
        try {
            gameStateManager.setGameState(GameStateManager.GameState.WIN);
            gameLogicHandler.setGameRun(false);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/Menu.fxml"));
            fxmlLoader.setControllerFactory(c -> {
                return menuController = new MenuController();
            });

            Scene winMenuScene = new Scene(fxmlLoader.load());
            menuController.showMenuScene("Win", winMenuScene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Example: Method to show the scene
    public void showScene(Scene gameScene) throws IOException {
        if (primaryStage != null) {
            gamePane = (AnchorPane) gameScene.lookup("#gamePane");
            scoreLabel = (Label) gameScene.lookup("#scoreLabel");
            heartLabel = (Label) gameScene.lookup("#heartLabel");
            levelLabel = (Label) gameScene.lookup("#levelLabel");
            pauseButton = (Button) gameScene.lookup("#pauseButton");

            if (!gameStateManager.isLoadFromSave()) {

                if (gameLogicHandler.getLevel() > 1) {
                    new Score(getGamePane()).showMessage("Level Up :)", 300, 300);
                    setLevelLabel("Level: " + gameLogicHandler.getLevel());
                }
                //if level equal to endLevel
                if (gameLogicHandler.getLevel() == gameLogicHandler.getEndLevel()) {
                    showWinScene();
                    return;
                }

                //init game component like block,ball and paddle
                initGameComponent.initBall();
                initGameComponent.initPaddle();
                initGameComponent.initBoard();
            } else {
                if (gameLogicHandler.isGoldStatus()) {
                   // Platform.runLater(() -> {
                    System.out.println("Before modification: " + getGamePane().getStyleClass());

                    getGamePane().getStyleClass().add("goldRoot");
                    System.out.println("gold root added. time: " + gameLogicHandler.getTime());
                    System.out.println("gold root added. gold time: " + gameLogicHandler.getGoldTime());
                    //});
                }
            }

            //   Platform.runLater(() -> {
            System.out.println("add to root start");
            //add ball and paddle to gamePane
            gamePane.getChildren().addAll(initGameComponent.getPaddle(), ballControl.getBall());

            // add blocks to the gamePane
            for (Block block : initGameComponent.getBlocks()) {
                gamePane.getChildren().add(block.rect);
            }
            System.out.println("add to root end");
            //      });
            gameScene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
//this line very important, it makes sure the game scene can properly load
            //  Platform.runLater(() -> {
            try {
                System.out.println("set Scene show start");
                primaryStage.setScene(gameScene);
                primaryStage.setTitle("Brick Breaker Game");
                primaryStage.show();
                System.out.println("set Scene show end");
            } catch (Exception e) {
                e.printStackTrace();
            }
            //   });


            if (gameLogicHandler.getLevel() != gameLogicHandler.getEndLevel()) {

                gameLogicHandler.setUpEngine();
                if (!gameStateManager.isLoadFromSave()) {
                    gameLogicHandler.setRemainingBlockCount(initGameComponent.getBlocks().size());
                } else {
                    gameStateManager.setLoadFromSave(false);
                }
            }
        }

        System.out.println("Scene show end");
    }

    @FXML
    public void handleKeyPressed(KeyEvent event) {

        //System.out.println("Key pressed: " + event.getCode()); // Add this line
        switch (event.getCode()) {
            case LEFT:
                move(LEFT);
                break;
            case RIGHT:
                move(RIGHT);
                break;
            case DOWN:
                // Uncomment if needed: setPhysicsToBall();
                break;
            case S:
                // Uncomment if needed: saveGame();
                break;
        }
    }


    private void move(final int direction) {
        //Move the paddle
        new Thread(new Runnable() {
            @Override
            public void run() {
                int sleepTime = 4;
                for (int i = 0; i < 30; i++) {
                    if (initGameComponent.getxPaddle() == (initGameComponent.getSCENE_WIDTH() - initGameComponent.getPADDLE_WIDTH()) && direction == RIGHT) {
                        return;
                    }
                    if (initGameComponent.getxPaddle() == 0 && direction == LEFT) {
                        return;
                    }
                    if (direction == RIGHT) {
                        initGameComponent.setxPaddle(initGameComponent.getxPaddle() + 1);
                    } else {
                        initGameComponent.setxPaddle(initGameComponent.getxPaddle() - 1);
                    }
                    initGameComponent.setCenterPaddleX(initGameComponent.getxPaddle() + initGameComponent.getHALF_PADDLE_WIDTH());
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.printf("%s", "move function in Main.java:");
                    }
                    if (i >= 20) {
                        sleepTime = i;
                    }
                }
            }
        }).start();
    }

    @FXML
    private void handlePauseButton(ActionEvent event) {
        System.out.println("Pause button clicked");

        gameStateManager.saveGame();
        //  gameSoundManager.stop();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/Menu.fxml"));
        fxmlLoader.setControllerFactory(c -> {
            return new MenuController();
        });
        Scene menuScene = null;
        try {
            menuScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        menuScene.getStylesheets().add("style.css");
        Button startbtn = (Button) menuScene.lookup("#startButton");
        startbtn.setText("Resume");
        gameStateManager.setGameState(GameStateManager.GameState.PAUSED);

        if (primaryStage != null) {
            primaryStage.setTitle("Brick Breaker Game");
            primaryStage.setScene(menuScene);
            primaryStage.show();

        }
    }

    public AnchorPane getGamePane() {
        return gamePane;
    }

    public void setScoreLabel(String scoreLabelText) {
        scoreLabel.setText(scoreLabelText);
    }

    public void setHeartLabel(String heartLabelText) {
        heartLabel.setText(heartLabelText);
    }

    public void setLevelLabel(String levelLabelText) {
        levelLabel.setText(levelLabelText);
    }

    public void setInitGameComponent(InitGameComponent initGameComponent) {
        this.initGameComponent = initGameComponent;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}

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


import java.io.IOException;

import static brickGame.GameStateManager.gameState;

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

    private GameEngine engine;


    private MenuController menuController;

    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    private BallControl ballControl;
    private GameLogicHandler gameLogicHandler;

    private static GameSceneController instance;
    private GameStateManager gameStateManager;
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

    public void showLoseScene() {
        engine.stop();

        System.out.println("heart: " + gameLogicHandler.getHeart());

        try {
            gameLogicHandler.setGameRun(false);
            gameState = GameStateManager.GameState.GAME_OVER;
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
            gameState = GameStateManager.GameState.GAME_OVER;
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
                //if level equal to endLevel
                if (gameLogicHandler.getLevel() == gameLogicHandler.getEndLevel()) {
                    showWinScene();
                    return;
                } else if (gameLogicHandler.getLevel() > 1) {
                    new Score().showMessage("Level Up :)", 300, 300);
                    setLevelLabel("Level: " + gameLogicHandler.getLevel());
                }

                //init game component like block,ball and paddle
                InitGameComponent init = new InitGameComponent(ballControl,gameLogicHandler);
                init.initBall();
                init.initPaddle();
                init.initBoard();
            }
            //this line very important, it makes sure the game scene can properly load
            Platform.runLater(() -> {
                System.out.println("add to root start");
                //add ball and paddle to gamePane
                gamePane.getChildren().addAll(paddle, ballControl.getBall());

                // add blocks to the gamePane
                for (Block block : blocks) {
                    gamePane.getChildren().add(block.rect);
                }
                System.out.println("add to root end");
            });

            Platform.runLater(() -> {
                try {
                    System.out.println("set Scene show start");
                    primaryStage.setScene(gameScene);
                    primaryStage.setTitle("Brick Breaker Game");
                    primaryStage.show();
                    System.out.println("set Scene show end");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });



            gameScene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);

            if (gameLogicHandler.getLevel() != gameLogicHandler.getEndLevel()) {
                System.out.println("132");
                if (!gameStateManager.isLoadFromSave()) {
                    System.out.println("134");
                    gameLogicHandler.setRemainingBlockCount(blocks.size());
                    if (gameLogicHandler.getLevel() >= 1 && gameLogicHandler.getLevel() < gameLogicHandler.getEndLevel()) {
                        System.out.println("137");

                        engine = new GameEngine();
                        engine.setOnAction(gameLogicHandler);
                        engine.setFps(120);
                        engine.start();
                        gameStateManager.setEngine(engine);
                        System.out.println("142");
                    }

                } else {
                    System.out.println("146");
                    engine = new GameEngine();
                    engine.setOnAction(gameLogicHandler);
                    engine.setFps(120);
                    engine.start();
                    gameStateManager.setEngine(engine);
                    gameStateManager.setLoadFromSave(false);
                }
                System.out.println("153");
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
        //Move the platform
        new Thread(new Runnable() {
            @Override
            public void run() {
                int sleepTime = 4;
                for (int i = 0; i < 30; i++) {
                    if (xPaddle == (SCENE_WIDTH - PADDLE_WIDTH) && direction == RIGHT) {
                        return;
                    }
                    if (xPaddle == 0 && direction == LEFT) {
                        return;
                    }
                    if (direction == RIGHT) {
                        xPaddle++;
                    } else {
                        xPaddle--;
                    }
                    centerPaddleX = xPaddle + HALF_PADDLE_WIDTH;
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
        gameSoundManager.stop();

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
        gameState = GameStateManager.GameState.PAUSED;

        if (primaryStage != null) {
            primaryStage.setTitle("Brick Breaker Game");
            primaryStage.setScene(menuScene);
            primaryStage.show();

        }
    }


}

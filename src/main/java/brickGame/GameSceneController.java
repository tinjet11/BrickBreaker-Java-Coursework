package brickGame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

import static brickGame.Main.*;
import static brickGame.WinController.showGameWinningScreen;

public class GameSceneController {
    @FXML
    private Pane gamePane;
    Scene gameScene;
    private Stage primaryStage;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label heartLabel;
    @FXML
    private Label levelLabel;

    @FXML
    private Button pauseButton;

    private Main main;

    public GameSceneController() {

    }


    public GameSceneController(Main main, Stage primaryStage) {
        this.main = main;
        this.primaryStage = primaryStage;
    }

    // Example: Method to show the scene
    public void showScene(Scene gameScene) throws IOException {
//        this.main = main;
        if (primaryStage != null) {
          /*  FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("Game.fxml"));

            gameScene = new Scene(fxmlLoader1.load());*/
            gamePane = (Pane) gameScene.lookup("#gamePane");
            scoreLabel = (Label) gameScene.lookup("#scoreLabel");
            heartLabel = (Label) gameScene.lookup("#heartLabel");
            levelLabel = (Label) gameScene.lookup("#levelLabel");
            pauseButton = (Button) gameScene.lookup("#pauseButton");

            if (!loadFromSave) {
                System.out.println("test: " + level);
                if (level > 1) {
                    new Score(main).showMessage("Level Up :)", 300, 300);
                }
                if (level == endLevel) {
                    showGameWinningScreen(primaryStage);
                    return;
                }

                Init init = new Init();
                init.initBall();
                init.initBreak();
                init.initBoard();
                gamePane.getChildren().addAll(rect, ball);
            } else {
                gamePane.getChildren().addAll(rect, ball);
            }

            // add blocks to the screen
            for (Block block : blocks) {
                gamePane.getChildren().add(block.rect);
            }

            gameScene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);

            primaryStage.setScene(gameScene);
            primaryStage.setTitle("Brick Breaker Game");
            primaryStage.show();

            if (level != endLevel) {
                if (!loadFromSave) {
                    remainingBlockCount = blocks.size();
                    if (level >= 1 && level < endLevel) {
                        engine = new GameEngine();
                        engine.setOnAction(main);
                        engine.setFps(120);
                        engine.start();
                    }
                } else {
                    engine = new GameEngine();
                    engine.setOnAction(main);
                    engine.setFps(120);
                    engine.start();
                    loadFromSave = false;
                }
            }
        }
    }

    public Pane getGamePane() {
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
                    if (xBreak == (sceneWidth - breakWidth) && direction == RIGHT) {
                        return;
                    }
                    if (xBreak == 0 && direction == LEFT) {
                        return;
                    }
                    if (direction == RIGHT) {
                        xBreak++;
                    } else {
                        xBreak--;
                    }
                    centerBreakX = xBreak + halfBreakWidth;
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
        // Implement the pause functionality here
        saveGame();
        // Rest of your code

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Menu.fxml"));
        fxmlLoader.setControllerFactory(c -> {
            return new MenuController(this.main, primaryStage);
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

        if (primaryStage != null) {
            primaryStage.setTitle("Brick Breaker Game");
            primaryStage.setScene(menuScene);
            primaryStage.show();

            startbtn.setOnAction(actionEvent -> {
                main.loadGame();
            });
        }
    }

    public void setMain(Main main) {
        this.main = main;
    }

}

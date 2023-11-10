package brickGame;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

import static brickGame.Main.isGameRun;

public class MenuController {

    public MenuController() {
        // Constructor logic, if needed
    }



    private Main main;
    private Stage primaryStage;

    @FXML
    private Button startButton;

    @FXML
    private Button tutorialButton;
    public MenuController(Main main, Stage primaryStage) {
        this.main = main;
        this.primaryStage = primaryStage;
    }
    @FXML
    public void exit() {
        System.exit(0);
    }

    @FXML
    public void onStartOrResume() {
        if ("Start Game".equals(startButton.getText())) {
            System.out.println("start");
            try {
                main.startGame(primaryStage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("start 2");
        } else {
            main.loadGame();
        }
    }

    @FXML
    public void onOpenTutorial() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Tutorial.fxml"));
            fxmlLoader.setControllerFactory(c -> {
                return new TutorialController(this.main, primaryStage);
            });

            Scene tutorialScene = new Scene(fxmlLoader.load());
            primaryStage.setTitle("Brick Breaker Game");
            primaryStage.setScene(tutorialScene);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}

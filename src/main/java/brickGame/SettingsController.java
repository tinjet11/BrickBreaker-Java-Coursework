package brickGame;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import static brickGame.Main.*;

public class SettingsController {

    private Main main;
    private Stage primaryStage;

    @FXML
    private Slider heartNumberSlider;

    @FXML
    private ScrollBar endLevelScrollbar;

    @FXML
    private Label initialHeartLabel;
    @FXML
    private Label endLevelLabel;

    private int maxHeart = 10;
    private int minHeart = 3;

    private int maxLevel = 18;
    private int minLevel = 10;
    private int currentHeart;
    private int currentEndLevel;


    public SettingsController() {

    }

    public SettingsController(Main main, Stage primaryStage) {
        this.main = main;
        this.primaryStage = primaryStage;

    }

    @FXML
    public void back() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SettingsController.class.getResource("Menu.fxml"));

            fxmlLoader.setControllerFactory(c -> {
                return new MenuController(this.main, primaryStage);
            });
            Scene menuScene = new Scene(fxmlLoader.load());

            Button startButton = (Button) menuScene.lookup("#startButton");

            if (isGameRun) {
                startButton.setText("Resume");
            } else {
                startButton.setText("Start Game");
            }

            primaryStage.setTitle("Brick Breaker Game");
            primaryStage.setScene(menuScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void initialize(){
        currentEndLevel = endLevel;
        currentHeart = initialHeart;
        initialHeartLabel.setText(String.valueOf(initialHeart));
        endLevelLabel.setText(String.valueOf(endLevel));
    }

    @FXML
    private void incrementHeart() {
        if (!isGameRun) {
            if (currentHeart != maxHeart) {
                currentHeart++;
                initialHeartLabel.setText(String.valueOf(currentHeart));
                initialHeart = currentHeart;
            }
        }

    }

    @FXML
    private void decrementHeart() {
        if (!isGameRun) {
            if (currentHeart != minHeart) {
                currentHeart--;
                initialHeartLabel.setText(String.valueOf(currentHeart));
                initialHeart = currentHeart;
            }
        }
    }


    @FXML
    private void incrementLevel() {
        if (!isGameRun) {
            if (currentEndLevel != maxLevel) {
                currentEndLevel++;
                endLevelLabel.setText(String.valueOf(currentEndLevel));
                endLevel = currentEndLevel;
            }
        }

    }

    @FXML
    private void decrementLevel() {
        if (!isGameRun) {
            if (currentEndLevel != minLevel) {
                currentEndLevel--;
                endLevelLabel.setText(String.valueOf(currentEndLevel));
                endLevel = currentEndLevel;
            }
        }
    }
}

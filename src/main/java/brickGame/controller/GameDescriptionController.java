package brickGame.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * The GameDescriptionController class is responsible for managing the game description scene interactions
 * and controlling navigation back to the main menu.
 * It provides a method for handling the back button click to return to the main menu scene.
 * Uses JavaFX components for scene setup.
 * <br>
 * <a href="https://github.com/tinjet11/COMP2042_CW_hcytl1/blob/master/src/main/java/brickGame/controller/GameDescriptionController.java">Current Code</a>
 * @author Leong Tin Jet
 * @version 1.0
 */
public class GameDescriptionController {

    /**
     * The primary stage of the application.
     */
    private Stage primaryStage;

    /**
     * The main menu scene to navigate back to.
     */
    private Scene menuScene;

    /**
     * Creates an instance of GameDescriptionController with the specified main menu scene and primary stage.
     *
     * @param menuScene    The Scene object representing the main menu scene.
     * @param primaryStage The primary stage of the application.
     */
    public GameDescriptionController(Scene menuScene, Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.menuScene = menuScene;
    }

    /**
     * Navigates back to the main menu scene when the back button is clicked.
     */
    @FXML
    public void back() {
        try {
            primaryStage.setTitle("Brick Breaker Game");
            primaryStage.setScene(menuScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

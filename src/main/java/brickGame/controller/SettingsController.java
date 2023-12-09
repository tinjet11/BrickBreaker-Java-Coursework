package brickGame.controller;

import brickGame.Mediator;
import brickGame.handler.GameLogicHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * The SettingsController class is responsible for managing the game settings UI,
 * allowing users to customize parameters such as initial hearts and end levels.
 * It provides methods for incrementing and decrementing heart and level values,
 * as well as navigating back to the main menu.
 * <p>
 * This controller interacts with the GameLogicHandler and GameSceneController
 * to update and retrieve game settings. It also handles the transition back to the main menu scene.
 * </p>
 * <p>
 * The class utilizes JavaFX components and FXML for scene setup and UI interactions.
 * </p>
 *
 * @author Leong Tin Jet
 * @version 1.0
 */
public class SettingsController {

    /**
     * The JavaFX Label representing the display of the initial heart value.
     */
    @FXML
    private Label initialHeartLabel;

    /**
     * The JavaFX Label representing the display of the end level value.
     */
    @FXML
    private Label endLevelLabel;

    /**
     * The JavaFX Slider for adjusting the sound volume.
     */
    @FXML
    private Slider soundSlider;

    /**
     * The JavaFX CheckBox for toggling the sound on or off.
     */
    @FXML
    private CheckBox soundCheckBox;


    /**
     * The maximum number of initial hearts allowed.
     */
    private final int maxHeart = 10;

    /**
     * The minimum number of initial hearts allowed.
     */
    private final int minHeart = 3;

    /**
     * The maximum level the player can reach.
     */
    private final int maxLevel = 21;

    /**
     * The minimum level the player can reach.
     */
    private final int minLevel = 10;

    /**
     * The current number of initial hearts.
     */
    private int currentHeart;

    /**
     * The current maximum level the player can reach.
     */
    private int currentEndLevel;

    /**
     * The mediator used for communication between different components of the game.
     */
    private Mediator mediator;

    /**
     * The sound controller used to control the sound
     */
    private SoundController soundController;

    /**
     * The Scene of menu scene
     */
    private Scene menuScene;

    /**
     * The primary stage of the application.
     */
    private Stage primaryStage;

    /**
     * Creates an instance of SettingsController with the specified menu scene.
     *
     * @param menuScene The Scene object representing the main menu scene.
     */
    public SettingsController(Scene menuScene) {
        mediator = Mediator.getInstance();
        primaryStage = mediator.getGameSceneController().getPrimaryStage();
        soundController = SoundController.getInstance();
        this.menuScene = menuScene;
    }

    /**
     * Initializes the settings UI with the current initial heart, end level values, sound checkbox and volume slider
     */
    @FXML
    public void initialize() {
        currentEndLevel = mediator.getGameLogicHandler().getEndLevel();
        currentHeart = mediator.getGameLogicHandler().getInitialHeart();
        initialHeartLabel.setText(String.valueOf(mediator.getGameLogicHandler().getInitialHeart()));
        endLevelLabel.setText(String.valueOf(mediator.getGameLogicHandler().getEndLevel()));
        if (soundController.getStatus() == MediaPlayer.Status.PAUSED) {
            soundCheckBox.setSelected(true);
        } else {
            soundCheckBox.setSelected(false);
        }
        soundController.bindVolumeSlider(soundSlider);
        soundController.bindMuteCheckbox(soundCheckBox);
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

    /**
     * Increments the initial heart value when the increment heart button is clicked.
     * Only allows incrementing when the game is not currently running.
     */
    @FXML
    private void incrementHeart() {

        if (!mediator.getGameLogicHandler().isGameRun()) {
            if (currentHeart != maxHeart) {
                currentHeart++;
                initialHeartLabel.setText(String.valueOf(currentHeart));
                mediator.getGameLogicHandler().setInitialHeart(currentHeart);
            }
        }
    }

    /**
     * Decrements the initial heart value when the decrement heart button is clicked.
     * Only allows decrementing when the game is not currently running.
     */
    @FXML
    private void decrementHeart() {
        if (!mediator.getGameLogicHandler().isGameRun()) {
            if (currentHeart != minHeart) {
                currentHeart--;
                initialHeartLabel.setText(String.valueOf(currentHeart));
                mediator.getGameLogicHandler().setInitialHeart(currentHeart);
            }
        }
    }

    /**
     * Increments the end level value when the increment level button is clicked.
     * Only allows incrementing when the game is not currently running.
     */
    @FXML
    private void incrementLevel() {
        if (!mediator.getGameLogicHandler().isGameRun()) {
            if (currentEndLevel != maxLevel) {
                currentEndLevel++;
                endLevelLabel.setText(String.valueOf(currentEndLevel));
                mediator.getGameLogicHandler().setEndLevel(currentEndLevel);
            }
        }

    }

    /**
     * Decrements the end level value when the decrement level button is clicked.
     * Only allows decrementing when the game is not currently running.
     */
    @FXML
    private void decrementLevel() {
        if (!mediator.getGameLogicHandler().isGameRun()) {
            if (currentEndLevel != minLevel) {
                currentEndLevel--;
                endLevelLabel.setText(String.valueOf(currentEndLevel));
                mediator.getGameLogicHandler().setEndLevel(currentEndLevel);
            }
        }
    }

}

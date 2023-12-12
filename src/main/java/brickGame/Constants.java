package brickGame;

import javafx.scene.paint.Color;
import java.util.Arrays;
import java.util.List;

/**
 * Class containing constants for file paths and image paths used in the brick game application.
 * <br>
 * <a href="https://github.com/tinjet11/COMP2042_CW_hcytl1/blob/master/src/main/java/brickGame/Constants.java">Current Code</a>
 * @author Leong Tin Jet
 * @version 1.0
 */
public final class Constants {
    /**
     * The directory path for saving game files.
     */
    public static final String SAVE_PATH_DIR = "save"; // Relative to the project directory
    public static final String SAVE_PATH = SAVE_PATH_DIR + "/save.mdds";
    public static final String HIGHEST_SCORE_SAVE_PATH = SAVE_PATH_DIR + "/highestScore.mdds";

    /**
     * The file path for the background music.
     */
    public static final String SOUND_FILE_PATH = "/bg-music.mp3";

    // Scene Path
    public static final String GAME_SCENE_FXML = "/fxml/GameScene.fxml";
    public static final String MENU_SCENE_FXML = "/fxml/Menu.fxml";
    public static final String SETTINGS_SCENE_FXML = "/fxml/Settings.fxml";
    public static final String GAME_DESCRIPTION_SCENE_FXML = "/fxml/GameDescription.fxml";

    /**
     * List of colors used for the paddle.
     */
    public static final List<Color> PADDLE_COLOR_LIST = Arrays.asList(
            Color.RED,
            Color.ORANGE,
            Color.YELLOW,
            Color.GREEN,
            Color.BLUE,
            Color.INDIGO,
            Color.VIOLET
    );


    // Game component Image Path
    public static final String GOLD_BALL_IMAGE_PATH = "/images/goldball.png";
    public static final String BALL_IMAGE_PATH = "/images/ball.png";

    public static final String IMAGE_PATH_CHOCO = "/images/choco.jpg";
    public static final String IMAGE_PATH_HEART = "/images/heart.jpg";
    public static final String IMAGE_PATH_STAR = "/images/star.jpg";
    public static final String IMAGE_PATH_PENALTY = "/images/penalty.jpeg";
    public static final String IMAGE_PATH_CONCRETE = "/images/brick-concrete.jpeg";
    public static final String IMAGE_PATH_NORMAL_BRICK = "/images/brick.jpg";
    public static final String IMAGE_PATH_BOMB = "/images/bomb.png";
    public static final String IMAGE_PATH_BONUS_1 = "/images/bonus1.png";
    public static final String IMAGE_PATH_BONUS_2 = "/images/bonus2.png";

    /**
     * The root name for gold-related components.
     */
    public static final String GOLD_ROOT = "goldRoot";
    /**
     * The width of the game scene.
     */
    public static final int SCENE_WIDTH = 500;
    /**
     * The height of the game scene.
     */
    public static final int SCENE_HEIGHT = 750;

}

package brickGame;

/**
 * Class containing constants for file paths and image paths used in the brick game application.
 */
public class Constants {
    /**
     * The directory path for saving game files.
     */
    public static final String SAVE_PATH_DIR = "save"; // Relative to the project directory
    public static final String SAVE_PATH = SAVE_PATH_DIR + "/save.mdds";
    public static final String HIGHEST_SCORE_SAVE_PATH = SAVE_PATH_DIR + "/highestScore.mdds";

    // Scene Path
    public static final String GAME_SCENE_FXML = "/fxml/GameScene.fxml";
    public static final String MENU_SCENE_FXML = "/fxml/Menu.fxml";
    public static final String SETTINGS_SCENE_FXML = "/fxml/Settings.fxml";


    // Game component Image Path
    public static final String PADDLE_IMAGE_PATH = "/images/paddle.jpg";
    public static final String GOLD_BALL_IMAGE_PATH = "/images/goldball.png";
    public static final String BALL_IMAGE_PATH = "/images/ball.png";

    public static  final String IMAGE_PATH_CHOCO = "/images/choco.jpg";
    public static  final String IMAGE_PATH_HEART = "/images/heart.jpg";
    public static  final String IMAGE_PATH_STAR = "/images/star.jpg";
    public static  final String IMAGE_PATH_PENALTY  = "/images/penalty.jpeg";
    public static  final String IMAGE_PATH_CONCRETE = "/images/brick-concrete.jpeg";
    public static  final String IMAGE_PATH_NORMAL_BRICK = "/images/brick.jpg";
    public static final String IMAGE_PATH_BOMB =  "/images/bomb.png";
    public static final String IMAGE_PATH_BONUS_1= "/images/bonus1.png";
    public static final String IMAGE_PATH_BONUS_2= "/images/bonus2.png";

    // Gold root name
    public static  final String GOLD_ROOT = "goldRoot";

}

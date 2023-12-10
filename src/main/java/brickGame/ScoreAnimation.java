package brickGame;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;


/**
 * The ScoreAnimation class represents an animation for displaying score changes in a JavaFX application.
 * It provides methods to show score changes with a fading and scaling animation.
 * <p>
 * The animation is applied to a JavaFX Label, and the animation duration, opacity end value, and scale end value
 * can be customized. The showMessage method is used to display a specific message at a given position (x, y),
 * and the show method simplifies showing score changes by automatically determining the sign of the score
 * (positive or negative) and formatting the message accordingly.
 * </p>
 * <p>
 * The class uses JavaFX Timeline and KeyFrame to create the animation, adjusting the opacity and scale properties
 * of the Label over the specified animation duration. The animation is run on the JavaFX application thread using
 * Platform.runLater to ensure that UI updates are performed safely within the JavaFX UI thread.
 * </p>
 * <p>
 * The ScoreAnimation class is typically used in conjunction with a JavaFX AnchorPane to display score changes
 * at specific positions on the screen.
 * </p>
 *
 * @author Leong Tin Jet
 * @version 1.0
 */
public class ScoreAnimation {
    /**
     * The duration of the animation in seconds.
     */
    private final Duration ANIMATION_DURATION = Duration.seconds(1.5);
    /**
     * The end value of opacity in the animation.
     */
    private final double OPACITY_END_VALUE = 0.0;

    /**
     * The end value of scale in the animation.
     */
    private final double SCALE_END_VALUE = 0.0;

    /**
     * The root {@link AnchorPane} used in the animation.
     */
    private AnchorPane root;

    /**
     * Constructs a ScoreAnimation instance with the specified JavaFX AnchorPane.
     *
     * @param root The JavaFX AnchorPane where the score animation will be displayed.
     */
    public ScoreAnimation(AnchorPane root) {
        this.root = root;
    }

    /**
     * Displays a score change message at the specified position (x, y) with a fading and scaling animation.
     *
     * @param x     The x-coordinate position for displaying the message.
     * @param y     The y-coordinate position for displaying the message.
     * @param score The score value to be displayed (positive or negative).
     */
    public void showScoreAnimation(double x, double y, int score) {
        String sign = (score >= 0) ? "+" : "";
        showMessage(sign + score, x, y);
    }


    /**
     * Displays a custom message at the specified position (x, y) with a fading and scaling animation.
     *
     * @param message The custom message to be displayed.
     * @param x       The x-coordinate position for displaying the message.
     * @param y       The y-coordinate position for displaying the message.
     */
    public void showMessage(String message, double x, double y) {
        Label label = new Label(message);
        label.setTranslateX(x);
        label.setTranslateY(y);

        Platform.runLater(() -> root.getChildren().add(label));

        Timeline timeline = new Timeline();
        KeyFrame opacityFrame = new KeyFrame(ANIMATION_DURATION, new KeyValue(label.opacityProperty(), OPACITY_END_VALUE));
        KeyFrame scaleXFrame = new KeyFrame(ANIMATION_DURATION, new KeyValue(label.scaleXProperty(), SCALE_END_VALUE));
        KeyFrame scaleYFrame = new KeyFrame(ANIMATION_DURATION, new KeyValue(label.scaleYProperty(), SCALE_END_VALUE));
        timeline.getKeyFrames().addAll(opacityFrame, scaleXFrame, scaleYFrame);

        timeline.setOnFinished(event -> {
            Platform.runLater(() -> root.getChildren().remove(label));
        });

        Platform.runLater(() -> timeline.play());
    }

}

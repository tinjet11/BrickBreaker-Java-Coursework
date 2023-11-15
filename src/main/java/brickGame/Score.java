package brickGame;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
//import sun.plugin2.message.Message;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
public class Score {
    private Main main;
    private static final Duration ANIMATION_DURATION = Duration.seconds(1.5);
    private static final double OPACITY_END_VALUE = 0.0;
    private static final double SCALE_END_VALUE = 0.0;

    public Score(Main main) {
        this.main = main;
    }

    public void show(double x, double y, int score) {
        String sign = (score >= 0) ? "+" : "";
        showMessage(sign + score, x, y);
    }

    public void showMessage(String message, double x, double y) {
        Label label = new Label(message);
        label.setTranslateX(x);
        label.setTranslateY(y);

        Platform.runLater(() -> main.root.getChildren().add(label));

        Timeline timeline = new Timeline();
        KeyFrame opacityFrame = new KeyFrame(ANIMATION_DURATION, new KeyValue(label.opacityProperty(), OPACITY_END_VALUE));
        KeyFrame scaleXFrame = new KeyFrame(ANIMATION_DURATION, new KeyValue(label.scaleXProperty(), SCALE_END_VALUE));
        KeyFrame scaleYFrame = new KeyFrame(ANIMATION_DURATION, new KeyValue(label.scaleYProperty(), SCALE_END_VALUE));
        timeline.getKeyFrames().addAll(opacityFrame, scaleXFrame, scaleYFrame);
        timeline.setOnFinished(event -> main.root.getChildren().remove(label));
        Platform.runLater(() -> timeline.play());
    }

}

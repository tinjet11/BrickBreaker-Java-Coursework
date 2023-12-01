package brickGame.model;

import javafx.application.Platform;
import javafx.scene.control.Label;
//import sun.plugin2.message.Message;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class ScoreAnimation {
    private final Duration ANIMATION_DURATION = Duration.seconds(1.5);
    private final double OPACITY_END_VALUE = 0.0;
    private final double SCALE_END_VALUE = 0.0;

    private AnchorPane root;

    public ScoreAnimation(AnchorPane root){

        this.root = root;
    }

    public void show(double x, double y, int score) {
        String sign = (score >= 0) ? "+" : "";
        showMessage(sign + score, x, y);
    }

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

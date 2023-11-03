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
    private final Object lock = new Object(); // Lock to synchronize access to the show method

    public void show(final double x, final double y, int score, final Main main) {
        String sign;
        if (score >= 0) {
            sign = "+";
        } else {
            sign = "";
        }
        final Label label = new Label(sign + score);
        label.setTranslateX(x);
        label.setTranslateY(y);

        // Synchronize access to the show method to prevent concurrent modification
        synchronized (lock) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    main.root.getChildren().add(label);
                }
            });
        }

        // Create a Timeline for the animation
        Timeline timeline = new Timeline();

        // Create KeyFrames for opacity and scale animations
        KeyFrame opacityFrame = new KeyFrame(
                Duration.seconds(1), // Duration for the animation
                new KeyValue(label.opacityProperty(), 0) // End value for opacity
        );

        KeyFrame scaleXFrame = new KeyFrame(
                Duration.seconds(1), // Duration for the animation
                new KeyValue(label.scaleXProperty(), 0) // End value for scaleX
        );

        KeyFrame scaleYFrame = new KeyFrame(
                Duration.seconds(1), // Duration for the animation
                new KeyValue(label.scaleYProperty(), 0) // End value for scaleY
        );

        // Add key frames to the timeline
        timeline.getKeyFrames().addAll(opacityFrame, scaleXFrame, scaleYFrame);

        // Set an event handler for when the timeline finishes
        timeline.setOnFinished(event -> {
            main.root.getChildren().remove(label);
        });

        // Synchronize access to the timeline to prevent concurrent modification
        synchronized (lock) {
            // Play the timeline to start the animation
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    timeline.play();
                }
            });
        }

    }


        public void showMessage(String message, final Main main) {
            final Label label = new Label(message);
            label.setTranslateX(220);
            label.setTranslateY(340);

            // Synchronize access to the show method to prevent concurrent modification
            synchronized (lock) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        main.root.getChildren().add(label);
                    }
                });
            }

            // Create a Timeline for the animation
            Timeline timeline = new Timeline();

            // Create KeyFrames for opacity and scale animations
            KeyFrame opacityFrame = new KeyFrame(
                    Duration.seconds(1), // Duration for the animation
                    new KeyValue(label.opacityProperty(), 0) // End value for opacity
            );

            KeyFrame scaleXFrame = new KeyFrame(
                    Duration.seconds(1), // Duration for the animation
                    new KeyValue(label.scaleXProperty(), 0) // End value for scaleX
            );

            KeyFrame scaleYFrame = new KeyFrame(
                    Duration.seconds(1), // Duration for the animation
                    new KeyValue(label.scaleYProperty(), 0) // End value for scaleY
            );

            // Add key frames to the timeline
            timeline.getKeyFrames().addAll(opacityFrame, scaleXFrame, scaleYFrame);

            // Set an event handler for when the timeline finishes
            timeline.setOnFinished(event -> {
                main.root.getChildren().remove(label);
            });

            // Synchronize access to the timeline to prevent concurrent modification
            synchronized (lock) {
                // Play the timeline to start the animation
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        timeline.play();
                    }
                });
            }
        }



    public void showGameOver(final Main main) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Label label = new Label("Game Over :(");
                label.setTranslateX(200);
                label.setTranslateY(250);
                label.setScaleX(2);
                label.setScaleY(2);

                Button restart = new Button("Restart");
                restart.setTranslateX(220);
                restart.setTranslateY(300);
                restart.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        main.restartGame();
                    }
                });

                main.root.getChildren().addAll(label, restart);

            }
        });
    }

    public void showWin(final Main main) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Label label = new Label("You Win :)");
                label.setTranslateX(200);
                label.setTranslateY(250);
                label.setScaleX(2);
                label.setScaleY(2);


                main.root.getChildren().addAll(label);

            }
        });
    }
}

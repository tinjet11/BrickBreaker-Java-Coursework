package brickGame.handler.blockhit;

import brickGame.model.Block;
import brickGame.handler.GameLogicHandler;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;

public class ConcreteBlockHitHandler implements BlockHitHandler {
    @Override
    public void handleBlockHit(Block block, Block.HIT_STATE hitCode, GameLogicHandler gameLogicHandler) {
        Platform.runLater(() -> {
            block.setDestroyed(true);
            block.setType(Block.BLOCK_TYPE.BLOCK_NORMAL);

            Image image = new Image(getClass().getResourceAsStream("/images/brick.jpg"));
            ImagePattern imagePattern = new ImagePattern(image);

            block.getRect().setFill(imagePattern);
            System.out.println("Block replaced with normal block");

            PauseTransition pause = new PauseTransition(Duration.millis(100));
            pause.setOnFinished(event -> {
                block.setDestroyed(false);
                System.out.println("isDestroyed set to false");
            });
            pause.play();
        });


    }
}

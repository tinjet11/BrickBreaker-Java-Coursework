package brickGame.handler.blockhit;

import brickGame.model.Block;
import brickGame.handler.GameLogicHandler;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;

import static brickGame.Constants.IMAGE_PATH_NORMAL_BRICK;

public class ConcreteBlockHitHandler implements BlockHitHandler {
    @Override
    public void handleBlockHit(Block block, Block.HIT_STATE hitCode, GameLogicHandler gameLogicHandler) {
        Platform.runLater(() -> {
            block.setDestroyed(true);
            block.setType(Block.BLOCK_TYPE.BLOCK_NORMAL);

            Image image = new Image(getClass().getResourceAsStream(IMAGE_PATH_NORMAL_BRICK));
            ImagePattern imagePattern = new ImagePattern(image);

            block.getRect().setFill(imagePattern);

            PauseTransition pause = new PauseTransition(Duration.millis(200));
            pause.setOnFinished(event -> {
                block.setDestroyed(false);
            });
            pause.play();
        });


    }
}

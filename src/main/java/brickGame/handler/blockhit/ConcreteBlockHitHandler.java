package brickGame.handler.blockhit;

import brickGame.model.Block;
import brickGame.handler.GameLogicHandler;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;

import static brickGame.Constants.IMAGE_PATH_NORMAL_BRICK;

/**
 * BlockHitHandler implementation for handling hits on Concrete blocks in a brick game.
 * Extends BlockHitHandler and provides specific behavior for Concrete block hits.
 */
public class ConcreteBlockHitHandler implements BlockHitHandler {

    /**
     * {@inheritDoc}
     * Handles the hit on a Concrete block by setting its type to BLOCK_NORMAL,
     * changing its fill to the normal brick image,set Destroyed to true and applying a pause,
     * after set destroyed to false in order to prevent the block being directly destroy due to the response time is rapid
     *
     * @param block             The Concrete Block instance that was hit.
     * @param hitCode           The HIT_STATE code representing the type of hit.
     * @param gameLogicHandler  The GameLogicHandler responsible for managing game logic.
     */
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

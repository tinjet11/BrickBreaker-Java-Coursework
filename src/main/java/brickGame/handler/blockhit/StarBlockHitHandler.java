package brickGame.handler.blockhit;

import brickGame.model.BallControl;
import brickGame.model.Block;
import brickGame.controller.GameSceneController;
import brickGame.handler.GameLogicHandler;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import static brickGame.Constants.GOLD_BALL_IMAGE_PATH;
import static brickGame.Constants.GOLD_ROOT;

/**
 * BlockHitHandler implementation for handling hits on Star blocks in a brick game.
 * Extends BlockHitHandler and provides specific behavior for Star block hits.
 */
public class StarBlockHitHandler implements BlockHitHandler {
    private final GameSceneController gameSceneController;
    private final BallControl ballControl;

    /**
     * Constructs a StarBlockHitHandler with the specified GameSceneController and BallControl.
     *
     * @param gameSceneController The GameSceneController associated with the game.
     * @param ballControl         The BallControl instance responsible for ball movement.
     */
    public StarBlockHitHandler(GameSceneController gameSceneController, BallControl ballControl) {
        this.gameSceneController = gameSceneController;
        this.ballControl = ballControl;
    }

    /**
     * {@inheritDoc}
     * Handles the hit on a Star block by performing default hit actions,
     * setting the gold time, and changing the ball and game pane to gold if not in gold status.
     *
     * @param block             The Star Block instance that was hit.
     * @param hitCode           The HIT_STATE code representing the type of hit.
     * @param gameLogicHandler  The GameLogicHandler responsible for managing game logic.
     */
    @Override
    public void handleBlockHit(Block block, Block.HIT_STATE hitCode, GameLogicHandler gameLogicHandler) {
        onHitAction(block,gameLogicHandler);
        gameLogicHandler.setGoldTime(gameLogicHandler.getTime());

        // Change the ball and game pane to gold if not in gold status
        if (!gameLogicHandler.isGoldStatus()) {
            Platform.runLater(() -> {
                ballControl.getBall().setFill(new ImagePattern(new Image(getClass().getResourceAsStream(GOLD_BALL_IMAGE_PATH))));
                gameSceneController.getGamePane().getStyleClass().add(GOLD_ROOT);
            });
        }
        gameLogicHandler.setGoldStatus(true);
    }
}

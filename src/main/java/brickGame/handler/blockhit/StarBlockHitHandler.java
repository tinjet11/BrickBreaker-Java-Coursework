package brickGame.handler.blockhit;

import brickGame.model.Ball;
import brickGame.handler.BallControlHandler;
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
 * @author Leong Tin Jet
 * @version 1.0
 */
public class StarBlockHitHandler implements BlockHitHandler {
    /**
     * The game scene controller handling user interface and scene transitions.
     */
    private final GameSceneController gameSceneController;

    /**
     * The ball instance
     */
    private final Ball ball;

    /**
     * Constructs a StarBlockHitHandler with the specified GameSceneController and BallControl.
     *
     * @param gameSceneController The GameSceneController associated with the game.
     */
    public StarBlockHitHandler(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
        this.ball = Ball.getInstance();
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
                ball.getBall().setFill(new ImagePattern(new Image(getClass().getResourceAsStream(GOLD_BALL_IMAGE_PATH))));
                gameSceneController.getGamePane().getStyleClass().add(GOLD_ROOT);
            });
        }
        gameLogicHandler.setGoldStatus(true);
    }
}

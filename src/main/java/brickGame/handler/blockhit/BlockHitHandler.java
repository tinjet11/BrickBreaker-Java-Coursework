package brickGame.handler.blockhit;

import brickGame.model.Block;
import brickGame.handler.GameLogicHandler;
import brickGame.model.ScoreAnimation;


/**
 * Interface defining methods for handling block hits in a brick game.
 */
public interface BlockHitHandler {

    /**
     * Handles the hit on a block by updating game logic based on the block, hit code, and game logic handler.
     * TO BE overwritten by class that implement this class
     * @param block             The Block instance that was hit.
     * @param hitCode           The HIT_STATE code representing the type of hit.
     * @param gameLogicHandler  The GameLogicHandler responsible for managing game logic.
     */
    void handleBlockHit(Block block, Block.HIT_STATE hitCode, GameLogicHandler gameLogicHandler);

    /**
     * Default implementation for on-hit actions, including updating the score, showing a score animation,
     * hiding the block, marking it as destroyed, and updating the remaining block count in the game logic.
     *
     * @param block             The Block instance that was hit.
     * @param gameLogicHandler  The GameLogicHandler responsible for managing game logic.
     */
    default void onHitAction(Block block, GameLogicHandler gameLogicHandler) {
        gameLogicHandler.setScore(gameLogicHandler.getScore() + 1);
        new ScoreAnimation(gameLogicHandler.getGameSceneController().getGamePane())
                .showScoreAnimation(block.getX(), block.getY(), 1);
        block.getRect().setVisible(false);
        block.setDestroyed(true);
        gameLogicHandler.setRemainingBlockCount(gameLogicHandler.getRemainingBlockCount() -1 );
    }
}

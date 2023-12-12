package brickGame.handler.blockhit;

import brickGame.Mediator;
import brickGame.model.Block;
import brickGame.handler.GameLogicHandler;
import brickGame.ScoreAnimation;


/**
 * Interface defining methods for handling block hits in a brick game.
 * <br>
 * <a href="https://github.com/tinjet11/COMP2042_CW_hcytl1/blob/master/src/main/java/brickGame/handler/blockhit/BlockHitHandler.java">Current Code</a>
 * @author Leong Tin Jet
 * @version 1.0
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
     * If is goldTime, then each block hit will add 2 point, else is 1 point.
     * @param block             The Block instance that was hit.
     * @param gameLogicHandler  The GameLogicHandler responsible for managing game logic.
     */
    default void onHitAction(Block block, GameLogicHandler gameLogicHandler) {
        Mediator mediator = Mediator.getInstance();
        if(mediator.getGameLogicHandler().isGoldStatus()){
            gameLogicHandler.setScore(gameLogicHandler.getScore() + 2);
            new ScoreAnimation(mediator.getGameSceneController().getGamePane())
                    .showScoreAnimation(block.getX(), block.getY(), 2);
        }else{
            gameLogicHandler.setScore(gameLogicHandler.getScore() + 1);
            new ScoreAnimation(mediator.getGameSceneController().getGamePane())
                    .showScoreAnimation(block.getX(), block.getY(), 1);
        }

        block.getRect().setVisible(false);
        block.setDestroyed(true);
        gameLogicHandler.setRemainingBlockCount(gameLogicHandler.getRemainingBlockCount() -1 );
    }
}

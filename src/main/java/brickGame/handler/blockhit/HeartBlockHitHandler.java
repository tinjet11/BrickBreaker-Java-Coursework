package brickGame.handler.blockhit;

import brickGame.model.Block;
import brickGame.handler.GameLogicHandler;

/**
 * BlockHitHandler implementation for handling hits on Heart blocks in a brick game.
 * Extends BlockHitHandler and provides specific behavior for Heart block hits.
 * @author Leong Tin Jet
 * @version 1.0
 */
public class HeartBlockHitHandler implements BlockHitHandler {
    /**
     * {@inheritDoc}
     * Handles the hit on a Heart block by performing default hit actions and increasing the player's heart count.
     *
     * @param block             The Heart Block instance that was hit.
     * @param hitCode           The HIT_STATE code representing the type of hit.
     * @param gameLogicHandler  The GameLogicHandler responsible for managing game logic.
     */
    @Override
    public void handleBlockHit(Block block, Block.HIT_STATE hitCode, GameLogicHandler gameLogicHandler) {
        onHitAction(block,gameLogicHandler);
        gameLogicHandler.setHeart(gameLogicHandler.getHeart()+1);
    }
}

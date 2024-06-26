package brickGame.handler.blockhit;

import brickGame.model.Block;
import brickGame.handler.GameLogicHandler;


/**
 * BlockHitHandler implementation for handling hits on Normal blocks in a brick game.
 * Extends BlockHitHandler and provides specific behavior for Normal block hits.
 * <br>
 * <a href="https://github.com/tinjet11/COMP2042_CW_hcytl1/blob/master/src/main/java/brickGame/handler/blockhit/NormalBlockHitHandler.java">Current Code</a>
 * @author Leong Tin Jet
 * @version 1.0
 */
public class NormalBlockHitHandler implements BlockHitHandler {

    /**
     * {@inheritDoc}
     * Handles the hit on a Normal block by performing default hit actions.
     *
     * @param block             The Normal Block instance that was hit.
     * @param hitCode           The HIT_STATE code representing the type of hit.
     * @param gameLogicHandler  The GameLogicHandler responsible for managing game logic.
     */
    @Override
    public void handleBlockHit(Block block, Block.HIT_STATE hitCode, GameLogicHandler gameLogicHandler) {
        onHitAction(block,gameLogicHandler);
    }
}

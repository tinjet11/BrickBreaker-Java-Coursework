package brickGame.handler.blockhit;

import brickGame.Mediator;
import brickGame.model.Block;
import brickGame.factory.DropItemFactory;
import brickGame.handler.GameLogicHandler;

/**
 * BlockHitHandler implementation for handling hits on Choco blocks in a brick game.
 * Extends BlockHitHandler and provides specific behavior for Choco block hits.
 *
 * <br>
 * <a href="https://github.com/tinjet11/COMP2042_CW_hcytl1/blob/master/src/main/java/brickGame/handler/blockhit/ChocoBlockHitHandler.java">Current Code</a>
 * @author Leong Tin Jet
 * @version 1.0
 */
public class ChocoBlockHitHandler implements BlockHitHandler {
    private final DropItemFactory dropItemFactory;

    /**
     * Constructs a ChocoBlockHitHandler with the specified GameSceneController and InitGameComponent.
     * Initialized the dropItemFactory by creating a instance of DropItemFactory and assign it
     *
     */
    public ChocoBlockHitHandler() {
        Mediator mediator = Mediator.getInstance();
        this.dropItemFactory = new DropItemFactory(mediator.getGameSceneController(), mediator.getInitGameComponent());
    }

    /**
     * {@inheritDoc}
     * Handles the hit on a Choco block by performing default hit actions and creating a Choco bonus drop item.
     *
     * @param block             The Choco Block instance that was hit.
     * @param hitCode           The HIT_STATE code representing the type of hit.
     * @param gameLogicHandler  The GameLogicHandler responsible for managing game logic.
     */
    @Override
    public void handleBlockHit(Block block, Block.HIT_STATE hitCode, GameLogicHandler gameLogicHandler) {
        onHitAction(block,gameLogicHandler);
        dropItemFactory.createChocoBonus(block.getRow(), block.getColumn(), gameLogicHandler.getTime());
    }
}

package brickGame.handler.blockhit;

import brickGame.Mediator;
import brickGame.model.Block;
import brickGame.factory.DropItemFactory;
import brickGame.handler.GameLogicHandler;

/**
 * BlockHitHandler implementation for handling hits on Penalty blocks in a brick game.
 * Extends BlockHitHandler and provides specific behavior for Penalty block hits.
 * @author Leong Tin Jet
 * @version 1.0
 */
public class PenaltyBlockHitHandler implements BlockHitHandler {
    /**
     * The DropItemFactory instance responsible for creating drop items in the game.
     */
    private final DropItemFactory dropItemFactory;

    /**
     * Constructs a PenaltyBlockHitHandler with the specified GameSceneController and InitGameComponent.
     * Initialized the dropItemFactory by creating a instance of DropItemFactory and assign it
     *
     */
    public PenaltyBlockHitHandler() {
        Mediator mediator = Mediator.getInstance();
        this.dropItemFactory = new DropItemFactory(mediator.getGameSceneController(), mediator.getInitGameComponent());
    }

    /**
     * {@inheritDoc}
     * Handles the hit on a Penalty block by performing default hit actions and creating a bomb penalty drop item.
     *
     * @param block             The Penalty Block instance that was hit.
     * @param hitCode           The HIT_STATE code representing the type of hit.
     * @param gameLogicHandler  The GameLogicHandler responsible for managing game logic.
     */
    @Override
    public void handleBlockHit(Block block, Block.HIT_STATE hitCode, GameLogicHandler gameLogicHandler) {
        onHitAction(block,gameLogicHandler);
        dropItemFactory.createBombPenalty(block.getRow(), block.getColumn(), gameLogicHandler.getTime());
    }
}

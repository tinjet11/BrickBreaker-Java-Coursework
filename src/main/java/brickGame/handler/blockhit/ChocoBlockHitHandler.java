package brickGame.handler.blockhit;

import brickGame.model.Block;
import brickGame.controller.GameSceneController;
import brickGame.factory.DropItemFactory;
import brickGame.handler.GameLogicHandler;
import brickGame.model.InitGameComponent;

/**
 * BlockHitHandler implementation for handling hits on Choco blocks in a brick game.
 * Extends BlockHitHandler and provides specific behavior for Choco block hits.
 *
 */
public class ChocoBlockHitHandler implements BlockHitHandler {
    private final DropItemFactory dropItemFactory;

    /**
     * Constructs a ChocoBlockHitHandler with the specified GameSceneController and InitGameComponent.
     * Initialized the dropItemFactory by creating a instance of DropItemFactory and assign it
     *
     * @param gameSceneController The GameSceneController associated with the game.
     * @param initGameComponent   The InitGameComponent used to initialize the game.
     */
    public ChocoBlockHitHandler(GameSceneController gameSceneController, InitGameComponent initGameComponent) {
        this.dropItemFactory = new DropItemFactory(gameSceneController, initGameComponent);
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

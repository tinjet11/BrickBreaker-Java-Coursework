package brickGame.handler.blockhit;

import brickGame.model.Block;
import brickGame.controller.GameSceneController;
import brickGame.factory.DropItemFactory;
import brickGame.handler.GameLogicHandler;
import brickGame.model.InitGameComponent;

public class PenaltyBlockHitHandler implements BlockHitHandler {
    private final DropItemFactory dropItemFactory;

    public PenaltyBlockHitHandler(GameSceneController gameSceneController, InitGameComponent initGameComponent) {
        this.dropItemFactory = new DropItemFactory(gameSceneController, initGameComponent);
    }

    @Override
    public void handleBlockHit(Block block, Block.HIT_STATE hitCode, GameLogicHandler gameLogicHandler) {
        onHitAction(block,gameLogicHandler);
        dropItemFactory.createBombPenalty(block.getRow(), block.getColumn(), gameLogicHandler.getTime());
    }
}

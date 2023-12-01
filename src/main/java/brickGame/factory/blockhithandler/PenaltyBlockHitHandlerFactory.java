package brickGame.factory.blockhithandler;

import brickGame.model.BallControl;
import brickGame.controller.GameSceneController;
import brickGame.model.InitGameComponent;
import brickGame.handler.blockhit.BlockHitHandler;
import brickGame.handler.blockhit.PenaltyBlockHitHandler;

public class PenaltyBlockHitHandlerFactory implements BlockHitHandlerFactory {
    @Override
    public BlockHitHandler createHandler(GameSceneController gameSceneController, InitGameComponent initGameComponent, BallControl ballControl) {
        return new PenaltyBlockHitHandler(gameSceneController, initGameComponent);
    }
}

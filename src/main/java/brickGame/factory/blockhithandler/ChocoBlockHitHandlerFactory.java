package brickGame.factory.blockhithandler;

import brickGame.model.BallControl;
import brickGame.controller.GameSceneController;
import brickGame.model.InitGameComponent;
import brickGame.handler.blockhit.BlockHitHandler;
import brickGame.handler.blockhit.ChocoBlockHitHandler;

public class ChocoBlockHitHandlerFactory implements BlockHitHandlerFactory {
    @Override
    public BlockHitHandler createHandler(GameSceneController gameSceneController, InitGameComponent initGameComponent, BallControl ballControl) {
        return new ChocoBlockHitHandler(gameSceneController, initGameComponent);
    }
}




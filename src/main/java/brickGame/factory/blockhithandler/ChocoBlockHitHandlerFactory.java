package brickGame.factory.blockhithandler;

import brickGame.handler.BallControlHandler;
import brickGame.controller.GameSceneController;
import brickGame.model.InitGameComponent;
import brickGame.handler.blockhit.BlockHitHandler;
import brickGame.handler.blockhit.ChocoBlockHitHandler;

/**
 * Factory class for creating ChocoBlockHitHandler instances.
 * @author Leong Tin Jet
 * @version 1.0
 */
public class ChocoBlockHitHandlerFactory implements BlockHitHandlerFactory {
    /**
     * {@inheritDoc}
     */
    @Override
    public BlockHitHandler createHandler(GameSceneController gameSceneController, InitGameComponent initGameComponent, BallControlHandler ballControlHandler) {
        return new ChocoBlockHitHandler(gameSceneController, initGameComponent);
    }
}




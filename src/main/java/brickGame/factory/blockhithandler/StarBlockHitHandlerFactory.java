package brickGame.factory.blockhithandler;

import brickGame.handler.BallControlHandler;
import brickGame.controller.GameSceneController;
import brickGame.model.InitGameComponent;
import brickGame.handler.blockhit.BlockHitHandler;
import brickGame.handler.blockhit.StarBlockHitHandler;

/**
 * Factory class for creating StarBlockHitHandler instances.
 * @author Leong Tin Jet
 * @version 1.0
 */
public class StarBlockHitHandlerFactory implements BlockHitHandlerFactory {
    /**
     * {@inheritDoc}
     */
    @Override
    public BlockHitHandler createHandler(GameSceneController gameSceneController, InitGameComponent initGameComponent, BallControlHandler ballControlHandler) {
        return new StarBlockHitHandler(gameSceneController);
    }
}

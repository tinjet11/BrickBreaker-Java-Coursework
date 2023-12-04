package brickGame.factory.blockhithandler;

import brickGame.model.BallControl;
import brickGame.controller.GameSceneController;
import brickGame.model.InitGameComponent;
import brickGame.handler.blockhit.BlockHitHandler;
import brickGame.handler.blockhit.PenaltyBlockHitHandler;

/**
 * Factory class for creating PenaltyBlockHitHandler instances.
 * @author Leong Tin Jet
 * @version 1.0
 */
public class PenaltyBlockHitHandlerFactory implements BlockHitHandlerFactory {
    /**
     * {@inheritDoc}
     */
    @Override
    public BlockHitHandler createHandler(GameSceneController gameSceneController, InitGameComponent initGameComponent, BallControl ballControl) {
        return new PenaltyBlockHitHandler(gameSceneController, initGameComponent);
    }
}

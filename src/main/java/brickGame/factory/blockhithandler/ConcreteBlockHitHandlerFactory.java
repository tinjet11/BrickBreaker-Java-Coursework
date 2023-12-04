package brickGame.factory.blockhithandler;

import brickGame.model.BallControl;
import brickGame.controller.GameSceneController;
import brickGame.model.InitGameComponent;
import brickGame.handler.blockhit.BlockHitHandler;
import brickGame.handler.blockhit.ConcreteBlockHitHandler;

/**
 * Factory class for creating ConcreteBlockHitHandler instances.
 * @author Leong Tin Jet
 * @version 1.0
 */
public class ConcreteBlockHitHandlerFactory implements BlockHitHandlerFactory {
    /**
     * {@inheritDoc}
     */
    @Override
    public BlockHitHandler createHandler(GameSceneController gameSceneController, InitGameComponent initGameComponent, BallControl ballControl) {
        return new ConcreteBlockHitHandler();
    }
}

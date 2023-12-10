package brickGame.factory.blockhithandler;

import brickGame.handler.blockhit.BlockHitHandler;
import brickGame.handler.blockhit.HeartBlockHitHandler;

/**
 * Factory class for creating HeartBlockHitHandler instances.
 * @author Leong Tin Jet
 * @version 1.0
 */
public class HeartBlockHitHandlerFactory implements BlockHitHandlerFactory {
    /**
     * {@inheritDoc}
     */
    @Override
    public BlockHitHandler createHandler() {
        return new HeartBlockHitHandler();
    }
}

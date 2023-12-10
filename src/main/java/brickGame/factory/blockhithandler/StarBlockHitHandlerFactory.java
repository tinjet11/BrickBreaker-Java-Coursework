package brickGame.factory.blockhithandler;

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
    public BlockHitHandler createHandler() {
        return new StarBlockHitHandler();
    }
}

package brickGame.factory.blockhithandler;

import brickGame.handler.blockhit.BlockHitHandler;
import brickGame.handler.blockhit.NormalBlockHitHandler;

/**
 * Factory class for creating NormalBlockHitHandler instances.
 * @author Leong Tin Jet
 * @version 1.0
 */
public class NormalBlockHitHandlerFactory implements BlockHitHandlerFactory {
    /**
     * {@inheritDoc}
     */
    @Override
    public BlockHitHandler createHandler() {
        return new NormalBlockHitHandler();
    }
}

package brickGame.factory.blockhithandler;

import brickGame.handler.blockhit.BlockHitHandler;
import brickGame.handler.blockhit.ConcreteBlockHitHandler;

/**
 * Factory class for creating ConcreteBlockHitHandler instances.
 * <br>
 * <a href="https://github.com/tinjet11/COMP2042_CW_hcytl1/blob/master/src/main/java/brickGame/factory/blockhithandler/ConcreteBlockHitHandlerFactory.java">Current Code</a>
 * @author Leong Tin Jet
 * @version 1.0
 */
public class ConcreteBlockHitHandlerFactory implements BlockHitHandlerFactory {
    /**
     * {@inheritDoc}
     */
    @Override
    public BlockHitHandler createHandler() {
        return new ConcreteBlockHitHandler();
    }
}

package brickGame.factory.blockhithandler;

import brickGame.handler.blockhit.BlockHitHandler;

/**
 * This interface defines a factory for creating instances of BlockHitHandler.
 * Implementing classes must provide a concrete implementation of the createHandler method,
 * which returns a specific type of BlockHitHandler based on the provided parameters.
 * <br>
 * <a href="https://github.com/tinjet11/COMP2042_CW_hcytl1/blob/master/src/main/java/brickGame/factory/blockhithandler/BlockHitHandlerFactory.java">Current Code</a>
 * @author Leong Tin Jet
 * @version 1.0
 */
public interface BlockHitHandlerFactory {
    /**
     * Creates and returns a specific instance of BlockHitHandler based on the provided parameters.
     *
     * @return A specific BlockHitHandler instance.
     */
    BlockHitHandler createHandler();
}

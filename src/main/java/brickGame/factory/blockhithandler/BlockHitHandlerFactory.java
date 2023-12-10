package brickGame.factory.blockhithandler;

import brickGame.handler.blockhit.BlockHitHandler;

/**
 * This interface defines a factory for creating instances of BlockHitHandler.
 * Implementing classes must provide a concrete implementation of the createHandler method,
 * which returns a specific type of BlockHitHandler based on the provided parameters.
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

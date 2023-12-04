package brickGame.factory.blockhithandler;

import brickGame.model.BallControl;
import brickGame.controller.GameSceneController;
import brickGame.model.InitGameComponent;
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
     * @param gameSceneController The GameSceneController associated with the game.
     * @param initGameComponent   The InitGameComponent used to initialize the game.
     * @param ballControl         The BallControl instance responsible for ball movement.
     * @return A specific BlockHitHandler instance.
     */
    BlockHitHandler createHandler(GameSceneController gameSceneController, InitGameComponent initGameComponent, BallControl ballControl);
}

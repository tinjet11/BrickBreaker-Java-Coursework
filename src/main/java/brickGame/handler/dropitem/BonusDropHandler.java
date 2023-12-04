package brickGame.handler.dropitem;

import brickGame.controller.GameSceneController;
import brickGame.model.dropitem.DropItem;
import brickGame.handler.GameLogicHandler;
import brickGame.model.InitGameComponent;

/**
 * Handler class for managing bonus drop items in a brick game.
 * Extends DropItemHandler and inherits methods for handling movement, collision detection, and scoring.
 */
public class BonusDropHandler extends DropItemHandler {

    /**
     * Constructs a BonusDropHandler with the specified InitGameComponent, GameSceneController, GameLogicHandler, and DropItem.
     *
     * @param initGameComponent   The InitGameComponent used to initialize the game.
     * @param gameSceneController The GameSceneController associated with the game.
     * @param gameLogicHandler    The GameLogicHandler responsible for managing game logic.
     * @param dropItem            The DropItem instance to be handled.
     */
    public BonusDropHandler(InitGameComponent initGameComponent, GameSceneController gameSceneController, GameLogicHandler gameLogicHandler, DropItem dropItem) {
        super(initGameComponent, gameSceneController, gameLogicHandler, dropItem);
    }

}

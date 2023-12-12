package brickGame.handler.dropitem;

import brickGame.model.dropitem.DropItem;

/**
 * Handler class for managing bonus drop items in a brick game.
 * Extends DropItemHandler and inherits methods for handling movement, collision detection, and scoring.
 * <br>
 * <a href="https://github.com/tinjet11/COMP2042_CW_hcytl1/blob/master/src/main/java/brickGame/handler/dropitem/BonusDropHandler.java">Current Code</a>
 * @author Leong Tin Jet
 * @version 1.0
 */
public class BonusDropHandler extends DropItemHandler {

    /**
     * Constructs a BonusDropHandler with the specified InitGameComponent, GameSceneController, GameLogicHandler, and DropItem.
     * @param dropItem            The DropItem instance to be handled.
     */
    public BonusDropHandler(DropItem dropItem) {
        super(dropItem);
    }

}

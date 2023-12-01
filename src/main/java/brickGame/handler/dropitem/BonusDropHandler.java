package brickGame.handler.dropitem;

import brickGame.controller.GameSceneController;
import brickGame.model.dropitem.DropItem;
import brickGame.handler.GameLogicHandler;
import brickGame.model.InitGameComponent;

public class BonusDropHandler extends DropItemHandler {

    public BonusDropHandler(InitGameComponent initGameComponent, GameSceneController gameSceneController, GameLogicHandler gameLogicHandler, DropItem dropItem) {
        super(initGameComponent, gameSceneController, gameLogicHandler, dropItem);
    }

}

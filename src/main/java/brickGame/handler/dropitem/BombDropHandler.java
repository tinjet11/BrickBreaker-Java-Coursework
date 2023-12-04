package brickGame.handler.dropitem;

import brickGame.controller.GameSceneController;
import brickGame.model.dropitem.DropItem;
import brickGame.handler.GameLogicHandler;
import brickGame.model.InitGameComponent;
import brickGame.model.ScoreAnimation;

public class BombDropHandler extends DropItemHandler{


    public BombDropHandler(InitGameComponent initGameComponent, GameSceneController gameSceneController, GameLogicHandler gameLogicHandler, DropItem dropItem) {
        super(initGameComponent, gameSceneController, gameLogicHandler, dropItem);
    }


    @Override
    public void handleTaken() {
        dropItem.setTaken(true);
        dropItem.element.setVisible(false);
    }

    public void executePenalty(){
        gameLogicHandler.setScore(gameLogicHandler.getScore() - 10);
        new ScoreAnimation(gameSceneController.getGamePane()).showScoreAnimation(initGameComponent.getSCENE_WIDTH() / 2, initGameComponent.getSCENE_HEIGHT() / 2, -10);
        dropItem.element.setVisible(false);
    }

}

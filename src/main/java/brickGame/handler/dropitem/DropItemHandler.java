package brickGame.handler.dropitem;

import brickGame.controller.GameSceneController;
import brickGame.model.dropitem.DropItem;
import brickGame.handler.GameLogicHandler;
import brickGame.model.InitGameComponent;
import brickGame.model.ScoreAnimation;
import javafx.application.Platform;

public class DropItemHandler {
    public   InitGameComponent initGameComponent;
    public GameSceneController gameSceneController;
    public  GameLogicHandler gameLogicHandler;

public DropItem dropItem;
    public DropItemHandler(InitGameComponent initGameComponent, GameSceneController gameSceneController, GameLogicHandler gameLogicHandler,DropItem dropItem){
        this.initGameComponent= initGameComponent;
        this.gameSceneController = gameSceneController;
        this.gameLogicHandler= gameLogicHandler;
        this.dropItem = dropItem;
    }

    public boolean shouldRemove() {
        return dropItem.getY() > initGameComponent.getSCENE_HEIGHT() || dropItem.isTaken();
    }

    public boolean isTaken() {
        return dropItem.getY() >= initGameComponent.getyPaddle() &&
                dropItem.getY() <= initGameComponent.getyPaddle() + initGameComponent.getPADDLE_HEIGHT() &&
                dropItem.getX() >= initGameComponent.getxPaddle() &&
                dropItem.getX() <= initGameComponent.getxPaddle() + initGameComponent.getPADDLE_WIDTH();
    }

    public void handleTaken() {
        dropItem.setTaken(true);
        dropItem.element.setVisible(false);
        gameLogicHandler.setScore(gameLogicHandler.getScore() + 3);
        new ScoreAnimation(gameSceneController.getGamePane()).show(dropItem.getX(), dropItem.getY(), 3);
    }

    public void moveDropItem() {
        dropItem.setY(dropItem.getY() + ((gameLogicHandler.getTime() - dropItem.getTimeCreated()) / 1000.000) + 1.000);
        Platform.runLater(() -> {
            dropItem.element.setY(dropItem.getY());
        });
    }
}

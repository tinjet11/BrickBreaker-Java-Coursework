package brickGame.handler.dropitem;

import brickGame.controller.GameSceneController;
import brickGame.model.Paddle;
import brickGame.model.dropitem.DropItem;
import brickGame.handler.GameLogicHandler;
import brickGame.model.InitGameComponent;
import brickGame.model.ScoreAnimation;
import javafx.application.Platform;

import static brickGame.Constants.SCENE_HEIGHT;

/**
 * Handler class for managing drop items in a brick game.
 * Handles movement, collision detection with the paddle, and scoring for taken drop items.
 */
public class DropItemHandler {
    public InitGameComponent initGameComponent;
    public GameSceneController gameSceneController;
    public GameLogicHandler gameLogicHandler;
    public Paddle paddle;

    public DropItem dropItem;

    /**
     * Constructs a DropItemHandler with the specified InitGameComponent, GameSceneController, GameLogicHandler, and DropItem.
     *
     * @param initGameComponent   The InitGameComponent used to initialize the game.
     * @param gameSceneController The GameSceneController associated with the game.
     * @param gameLogicHandler    The GameLogicHandler responsible for managing game logic.
     * @param dropItem            The DropItem instance to be handled.
     */
    public DropItemHandler(InitGameComponent initGameComponent, GameSceneController gameSceneController, GameLogicHandler gameLogicHandler, DropItem dropItem) {
        this.initGameComponent = initGameComponent;
        this.gameSceneController = gameSceneController;
        this.gameLogicHandler = gameLogicHandler;
        this.dropItem = dropItem;
        this.paddle = Paddle.getInstance();
    }

    /**
     * Checks if the drop item should be removed based on its position or if it has been taken.
     *
     * @return true if the drop item should be removed, false otherwise.
     */
    public boolean shouldRemove() {
        return dropItem.getY() > SCENE_HEIGHT || dropItem.isTaken();
    }

    /**
     * Checks if the drop item has been taken by the paddle.
     *
     * @return true if the drop item is taken by the paddle, false otherwise.
     */
    public boolean isTaken() {
        return dropItem.getY() >= paddle.getyPaddle() &&
                dropItem.getY() <= paddle.getyPaddle() + paddle.getPADDLE_HEIGHT() &&
                dropItem.getX() >= paddle.getxPaddle() &&
                dropItem.getX() <= paddle.getxPaddle() + paddle.getPADDLE_WIDTH();
    }

    /**
     * Handles the action when the drop item is taken, including updating the score and displaying a score animation.
     */
    public void handleTaken() {
        dropItem.setTaken(true);
        dropItem.element.setVisible(false);
        gameLogicHandler.setScore(gameLogicHandler.getScore() + 3);
        new ScoreAnimation(gameSceneController.getGamePane()).showScoreAnimation(dropItem.getX(), dropItem.getY(), 3);
    }

    /**
     * Moves the drop item based on the elapsed time since its creation.
     */
    public void moveDropItem() {
        dropItem.setY(dropItem.getY() + ((gameLogicHandler.getTime() - dropItem.getTimeCreated()) / 1000.000) + 1.000);
        Platform.runLater(() -> {
            dropItem.element.setY(dropItem.getY());
        });
    }
}

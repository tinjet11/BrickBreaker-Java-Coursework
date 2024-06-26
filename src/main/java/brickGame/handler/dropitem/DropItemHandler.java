package brickGame.handler.dropitem;

import brickGame.Mediator;
import brickGame.controller.GameSceneController;
import brickGame.model.Paddle;
import brickGame.model.dropitem.DropItem;
import brickGame.handler.GameLogicHandler;
import brickGame.ScoreAnimation;
import javafx.application.Platform;

import static brickGame.Constants.SCENE_HEIGHT;

/**
 * Handler class for managing drop items in a brick game.
 * Handles movement, collision detection with the paddle, and scoring for taken drop items.
 * <br>
 * <a href="https://github.com/tinjet11/COMP2042_CW_hcytl1/blob/master/src/main/java/brickGame/handler/dropitem/DropItemHandler.java">Current Code</a>
 * @author Leong Tin Jet
 * @version 1.0
 */
public class DropItemHandler {

    /**
     * The game scene controller handling user interface and scene transitions.
     */
    public GameSceneController gameSceneController;

    /**
     * The game logic handler managing the core game logic, player stats, and overall game state.
     */
    public GameLogicHandler gameLogicHandler;

    /**
     * The paddle component representing the player's control in the game.
     */
    public Paddle paddle;

    /**
     * The DropItem instance to be handled by this DropItemHandler.
     */
    public DropItem dropItem;

    /**
     * Constructs a DropItemHandler with the specified InitGameComponent, GameSceneController, GameLogicHandler, and DropItem.
     *
     * @param dropItem The DropItem instance to be handled.
     */
    public DropItemHandler(DropItem dropItem) {
        this.gameLogicHandler = Mediator.getInstance().getGameLogicHandler();
        this.gameSceneController = Mediator.getInstance().getGameSceneController();
        this.dropItem = dropItem;
        this.paddle = Mediator.getInstance().getPaddleInstance();
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

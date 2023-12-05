package brickGame.handler.dropitem;

import brickGame.controller.GameSceneController;
import brickGame.model.dropitem.DropItem;
import brickGame.handler.GameLogicHandler;
import brickGame.model.InitGameComponent;
import brickGame.model.ScoreAnimation;

import static brickGame.Constants.SCENE_HEIGHT;
import static brickGame.Constants.SCENE_WIDTH;


/**
 * Handler class for managing bomb drop items in a brick game.
 * Extends DropItemHandler and provides specific behavior for bomb drop items.
 */
public class BombDropHandler extends DropItemHandler{

    /**
     * Constructs a BombDropHandler with the specified InitGameComponent, GameSceneController, GameLogicHandler, and DropItem.
     *
     * @param initGameComponent   The InitGameComponent used to initialize the game.
     * @param gameSceneController The GameSceneController associated with the game.
     * @param gameLogicHandler    The GameLogicHandler responsible for managing game logic.
     * @param dropItem            The DropItem instance to be handled.
     */
    public BombDropHandler(InitGameComponent initGameComponent, GameSceneController gameSceneController, GameLogicHandler gameLogicHandler, DropItem dropItem) {
        super(initGameComponent, gameSceneController, gameLogicHandler, dropItem);
    }

    /**
     * overriding the handleTaken method in the superclass
     * Handles the action when the bomb drop item is taken, including executing a penalty and updating the score.
     */
    @Override
    public void handleTaken() {
        dropItem.setTaken(true);
        dropItem.element.setVisible(false);
    }

    /**
     * Executes a penalty by decreasing the score and showing a negative score animation.
     */
    public void executePenalty(){
        gameLogicHandler.setScore(gameLogicHandler.getScore() - 10);
        new ScoreAnimation(gameSceneController.getGamePane()).showScoreAnimation(SCENE_WIDTH / 2, SCENE_HEIGHT / 2, -10);
        dropItem.element.setVisible(false);
    }

}

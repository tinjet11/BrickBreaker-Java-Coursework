package brickGame.handler.dropitem;

import brickGame.model.dropitem.DropItem;
import brickGame.ScoreAnimation;

import static brickGame.Constants.SCENE_HEIGHT;
import static brickGame.Constants.SCENE_WIDTH;


/**
 * Handler class for managing bomb drop items in a brick game.
 * Extends DropItemHandler and provides specific behavior for bomb drop items.
 * @author Leong Tin Jet
 * @version 1.0
 */
public class BombDropHandler extends DropItemHandler{

    /**
     * Constructs a BombDropHandler with the specified InitGameComponent, GameSceneController, GameLogicHandler, and DropItem.
     * @param dropItem            The DropItem instance to be handled.
     */
    public BombDropHandler(DropItem dropItem) {
        super(dropItem);
    }

    /**
     * overriding the handleTaken method in the superclass
     * Handles the action when the bomb drop item is taken, including executing a penalty and updating the score.
     */
    @Override
    public void handleTaken() {
        dropItem.setTaken(true);
        dropItem.element.setVisible(false);
        new ScoreAnimation(gameSceneController.getGamePane()).showMessage("You catch the bomb",dropItem.getX(),dropItem.getY());
    }

    /**
     * Executes a penalty by decreasing the score and showing a negative score animation.
     */
    public void executePenalty(){
        if(gameLogicHandler.getScore() <= 10){
            gameLogicHandler.setScore(0);
        }else{
            gameLogicHandler.setScore(gameLogicHandler.getScore() - 10);
        }

        new ScoreAnimation(gameSceneController.getGamePane()).showScoreAnimation(SCENE_WIDTH / 2, SCENE_HEIGHT / 2, -10);
        dropItem.element.setVisible(false);
    }

}

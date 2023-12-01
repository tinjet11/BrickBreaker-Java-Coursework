package brickGame.handler.blockhit;

import brickGame.model.Block;
import brickGame.handler.GameLogicHandler;
import brickGame.model.ScoreAnimation;

public interface BlockHitHandler {
    void handleBlockHit(Block block, Block.HIT_STATE hitCode, GameLogicHandler gameLogicHandler);

    default void onHitAction(Block block, GameLogicHandler gameLogicHandler) {
        gameLogicHandler.setScore(gameLogicHandler.getScore() + 1);
        new ScoreAnimation(gameLogicHandler.getGameSceneController().getGamePane())
                .show(block.getX(), block.getY(), 1);
        block.getRect().setVisible(false);
        block.setDestroyed(true);
        gameLogicHandler.setRemainingBlockCount(gameLogicHandler.getRemainingBlockCount() -1 );
    }
}

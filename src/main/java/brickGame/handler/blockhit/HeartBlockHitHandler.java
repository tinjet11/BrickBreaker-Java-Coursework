package brickGame.handler.blockhit;

import brickGame.model.Block;
import brickGame.handler.GameLogicHandler;

public class HeartBlockHitHandler implements BlockHitHandler {
    @Override
    public void handleBlockHit(Block block, Block.HIT_STATE hitCode, GameLogicHandler gameLogicHandler) {
        onHitAction(block,gameLogicHandler);
        gameLogicHandler.setHeart(gameLogicHandler.getHeart()+1);
        System.out.println("heart hitted");
    }
}

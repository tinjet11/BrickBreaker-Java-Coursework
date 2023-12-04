package brickGame.handler.blockhit;

import brickGame.model.BallControl;
import brickGame.model.Block;
import brickGame.controller.GameSceneController;
import brickGame.handler.GameLogicHandler;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import static brickGame.Constants.GOLD_BALL_IMAGE_PATH;
import static brickGame.Constants.GOLD_ROOT;

public class StarBlockHitHandler implements BlockHitHandler {
    private final GameSceneController gameSceneController;
    private final BallControl ballControl;

    public StarBlockHitHandler(GameSceneController gameSceneController, BallControl ballControl) {
        this.gameSceneController = gameSceneController;
        this.ballControl = ballControl;
    }
    @Override
    public void handleBlockHit(Block block, Block.HIT_STATE hitCode, GameLogicHandler gameLogicHandler) {
        onHitAction(block,gameLogicHandler);
        gameLogicHandler.setGoldTime(gameLogicHandler.getTime());

        if (!gameLogicHandler.isGoldStatus()) {
            Platform.runLater(() -> {
                ballControl.getBall().setFill(new ImagePattern(new Image(getClass().getResourceAsStream(GOLD_BALL_IMAGE_PATH))));
                gameSceneController.getGamePane().getStyleClass().add(GOLD_ROOT);
            });
        }
        gameLogicHandler.setGoldStatus(true);
    }
}

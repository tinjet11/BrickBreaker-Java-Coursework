package brickGame.handler.blockhit;

import brickGame.model.BallControl;
import brickGame.model.Block;
import brickGame.controller.GameSceneController;
import brickGame.handler.GameLogicHandler;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

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
                ballControl.getBall().setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/images/goldball.png"))));
                System.out.println("gold ball");
                gameSceneController.getGamePane().getStyleClass().add("goldRoot");
            });
        }
        gameLogicHandler.setGoldStatus(true);
    }
}

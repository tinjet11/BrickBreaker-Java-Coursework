package brickGame;

import brickGame.controller.GameSceneController;
import brickGame.handler.BallControlHandler;
import brickGame.handler.GameLogicHandler;
import brickGame.model.Ball;
import brickGame.model.GameStateManager;
import brickGame.model.InitGameComponent;
import brickGame.model.Paddle;

public class Mediator {
    private GameLogicHandler gameLogicHandler;
    private BallControlHandler ballControlHandler;
    private GameSceneController gameSceneController;
    private GameStateManager gameStateManager;
    private InitGameComponent initGameComponent;
    private Ball ball;
    private Paddle paddle;


    // Private constructor to enforce singleton pattern
    private Mediator() {
        // Initialize instances as needed
        gameLogicHandler = GameLogicHandler.getInstance();
        ballControlHandler = BallControlHandler.getInstance();
        gameSceneController = GameSceneController.getInstance();
        gameStateManager = GameStateManager.getInstance();
        initGameComponent = InitGameComponent.getInstance();
        paddle = Paddle.getInstance();
        ball = Ball.getInstance();
    }

    // Singleton instance
    private static Mediator instance ;

    public static Mediator getInstance() {
        if (instance == null) {
            instance = new Mediator();
        }
        return instance;
    }

    public GameLogicHandler getGameLogicHandler() {
        return gameLogicHandler;
    }


    public BallControlHandler getBallControlHandler() {
        return ballControlHandler;
    }


    public GameSceneController getGameSceneController() {
        return gameSceneController;
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public InitGameComponent getInitGameComponent() {
        return initGameComponent;
    }


    public Ball getBallInstance() {
        return ball;
    }

    public Paddle getPaddleInstance(){
        return paddle;
    }
}

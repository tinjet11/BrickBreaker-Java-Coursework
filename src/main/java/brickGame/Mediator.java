package brickGame;

import brickGame.controller.GameSceneController;
import brickGame.handler.BallControlHandler;
import brickGame.handler.GameLogicHandler;
import brickGame.model.Ball;
import brickGame.model.GameStateManager;
import brickGame.model.InitGameComponent;
import brickGame.model.Paddle;

/**
 * The Mediator class serves as a central coordination point for communication and interaction
 * between various components in the Brick Breaker game. It follows the singleton pattern to
 * ensure a single instance responsible for managing the flow of information among different
 * parts of the application.
 *
 * @author Leong Tin Jet
 * @version 1.0
 */

public class Mediator {

    /**
     * The singleton instance of the {@code Mediator} class.
     */
    private static Mediator instance;

    /**
     * The game logic handler responsible for managing core game logic,
     * player stats, and overall game state.
     */
    private GameLogicHandler gameLogicHandler;

    /**
     * The ball control handler responsible for managing the movement and behavior
     * of the game ball.
     */
    private BallControlHandler ballControlHandler;

    /**
     * The game scene controller responsible for handling user interface and
     * scene transitions in the game.
     */
    private GameSceneController gameSceneController;

    /**
     * The game state manager responsible for tracking and managing the state
     * of the game (e.g., in progress, paused, game over).
     */
    private GameStateManager gameStateManager;

    /**
     * The initialization game component responsible for setting up
     * the initial state of the game.
     */
    private InitGameComponent initGameComponent;

    /**
     * The ball component representing the game ball.
     */
    private Ball ball;

    /**
     * The paddle component representing the player's control in the game.
     */
    private Paddle paddle;



    /**
     * Private constructor to enforce the singleton pattern and initialize instances of
     * key components in the game.
     */
    private Mediator() {
        gameLogicHandler = GameLogicHandler.getInstance();
        ballControlHandler = BallControlHandler.getInstance();
        gameSceneController = GameSceneController.getInstance();
        gameStateManager = GameStateManager.getInstance();
        initGameComponent = InitGameComponent.getInstance();
        paddle = Paddle.getInstance();
        ball = Ball.getInstance();
    }

    /**
     * Returns the singleton instance of the Mediator class.
     *
     * @return The singleton instance of the Mediator.
     */
    public static Mediator getInstance() {
        if (instance == null) {
            instance = new Mediator();
        }
        return instance;
    }

    /**
     * Gets the instance of the GameLogicHandler.
     *
     * @return The instance of the GameLogicHandler.
     */
    public GameLogicHandler getGameLogicHandler() {
        return gameLogicHandler;
    }

    /**
     * Gets the instance of the BallControlHandler.
     *
     * @return The instance of the BallControlHandler.
     */
    public BallControlHandler getBallControlHandler() {
        return ballControlHandler;
    }


    /**
     * Gets the instance of the GameSceneController.
     *
     * @return The instance of the GameSceneController.
     */
    public GameSceneController getGameSceneController() {
        return gameSceneController;
    }

    /**
     * Gets the instance of the GameStateManager.
     *
     * @return The instance of the GameStateManager.
     */
    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    /**
     * Gets the instance of the InitGameComponent.
     *
     * @return The instance of the InitGameComponent.
     */
    public InitGameComponent getInitGameComponent() {
        return initGameComponent;
    }

    /**
     * Gets the singleton instance of the Ball class.
     *
     * @return The singleton instance of the Ball class.
     */
    public Ball getBallInstance() {
        return ball;
    }

    /**
     * Gets the singleton instance of the Paddle class.
     *
     * @return The singleton instance of the Paddle class.
     */
    public Paddle getPaddleInstance() {
        return paddle;
    }
}

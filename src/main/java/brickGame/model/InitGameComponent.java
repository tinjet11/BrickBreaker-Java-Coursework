package brickGame.model;

import brickGame.Mediator;
import brickGame.handler.BallControlHandler;
import brickGame.handler.GameLogicHandler;
import brickGame.model.dropitem.Bonus;
import brickGame.model.dropitem.Bomb;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

import static brickGame.Constants.*;
import static brickGame.model.Block.BLOCK_HEIGHT;

/**
 * The InitGameComponent class represents a singleton instance responsible for initializing various game components
 * such as the paddle, ball, blocks. It also sets up the initial configuration of the game board
 * based on the current level. This class follows the Singleton pattern to ensure that only one instance exists
 * throughout the application.
 * <p>
 * The InitGameComponent class initializes the game components, including the paddle, ball, and different types of blocks
 * (normal, choco, heart, star, concrete, penalty). It also manages the existence of special blocks like the heart and
 * bomb block, ensuring they are appropriately placed in the game board. The class provides methods to set up the initial
 * state of the game components and retrieve them when needed.
 * </p>
 * <p>
 * The class uses JavaFX Rectangle and Circle shapes to represent the paddle and ball, and it manages their positions,
 * sizes, and image patterns. Additionally, it initializes an ArrayList of Block, Bonus, and Bomb objects to represent
 * the game board's blocks and special items.
 * </p>
 * <p>
 * The singleton pattern is implemented using a private constructor and a static getInstance method to ensure that only
 * one instance of the class is created. The class also encapsulates the state and behavior related to game initialization,
 * promoting a modular and organized game structure.
 * </p>
 *
 * @author Leong Tin Jet
 * @version 1.0
 */
public class InitGameComponent {
    private  static  InitGameComponent instance;
    private InitGameComponent() {

    }
    public static InitGameComponent getInstance() {
        if (instance == null) {
            instance = new InitGameComponent();
        }
        return instance;
    }

    private Mediator mediator;

    private boolean isExistHeartBlock = false;
    private boolean isExistBombBlock = false;


    private ArrayList<Block> blocks = new ArrayList<>();
    private ArrayList<Bonus> chocos = new ArrayList<>();
    private ArrayList<Bomb> bombs = new ArrayList<>();

    /**
     * set up the all the brick in the game according to the current level
     */
    public void initBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < mediator.getGameLogicHandler().getLevel() + 1; j++) {
                int r = new Random().nextInt(500);
                if (r % 5 == 0) {
                    continue;
                }
                Block.BLOCK_TYPE type;
                if (r % 10 == 1) {
                    type = Block.BLOCK_TYPE.BLOCK_CHOCO;
                } else if (r % 10 == 2) {
                    if (!isExistHeartBlock()) {
                        type = Block.BLOCK_TYPE.BLOCK_HEART;
                        setExistHeartBlock(true);
                    } else {
                        type =Block.BLOCK_TYPE.BLOCK_NORMAL;
                    }
                } else if (r % 10 == 3) {
                    type = Block.BLOCK_TYPE.BLOCK_STAR;
                }
                else if (r % 10 == 4) {
                    if (!isExistBombBlock) {
                        type = Block.BLOCK_TYPE.BLOCK_PENALTY;
                        setExistBombBlock(true);
                    } else {
                        type = Block.BLOCK_TYPE.BLOCK_NORMAL;
                    }
                }
                else if (mediator.getGameLogicHandler().getLevel() >= 10) {
                    type = Block.BLOCK_TYPE.BLOCK_CONCRETE;
                }
                else {
                    type = Block.BLOCK_TYPE.BLOCK_NORMAL;
                }
                getBlocks().add(new Block(j, i, type));
            }
        }
    }

    /**
     * set up the UI and position of ball in the game
     * if isGoldStatus, set ball fill to goldBall, else set to normal
     */
    public void initBall() {
        Ball ball = Ball.getInstance();
        Random random = new Random();

        // Randomly set the x-coordinate within the width of the game scene
        double xBall = Math.max(ball.getBALL_RADIUS(), Math.min(random.nextInt(SCENE_WIDTH - ball.getBALL_RADIUS()) + 1, SCENE_WIDTH - ball.getBALL_RADIUS()));

        // Ensure that the ball starts above the screen's bottom edge
        int minYBall = ((mediator.getGameLogicHandler().getLevel() + 1) * BLOCK_HEIGHT) + 2 * ball.getBALL_RADIUS();

        // Randomly set the y-coordinate within the valid range
        double yBall = Math.max(minYBall, Math.min(random.nextInt(SCENE_HEIGHT - minYBall) + minYBall, SCENE_HEIGHT- minYBall));

        // Create the ball object with the specified radius and image pattern
        Circle newBall = new Circle();
        newBall.setRadius(ball.getBALL_RADIUS());
        if(mediator.getGameLogicHandler().isGoldStatus()){
            newBall.setFill(new ImagePattern(new Image(getClass().getResourceAsStream(GOLD_BALL_IMAGE_PATH))));
        }else{
            newBall.setFill(new ImagePattern(new Image(getClass().getResourceAsStream(BALL_IMAGE_PATH))));
        }

        // Set the ball's properties in the Ball instance
        ball.setxBall(xBall);
        ball.setyBall(yBall);
        ball.setBall(newBall);
    }

    // the paddle
    /**
     * set up the UI and position of  paddle in the game
     */
    public void initPaddle() {
        Paddle paddle = Paddle.getInstance();
        paddle.setPaddle(new Rectangle());
        paddle.getPaddle().setWidth(paddle.getPADDLE_WIDTH());
        paddle.getPaddle().setHeight(paddle.getPADDLE_HEIGHT());
        paddle.getPaddle().setX(paddle.getxPaddle());
        paddle.getPaddle().setY(paddle.getyPaddle());

        Color color = Color.ALICEBLUE;
        paddle.getPaddle().setFill(color);
    }

    public boolean isExistHeartBlock() {
        return isExistHeartBlock;
    }

    public void setExistHeartBlock(boolean existHeartBlock) {
        isExistHeartBlock = existHeartBlock;
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    public ArrayList<Bonus> getChocos() {
        return chocos;
    }

    public ArrayList<Bomb> getBombs() {
        return bombs;
    }

    public void setExistBombBlock(boolean existBombBlock) {
        isExistBombBlock = existBombBlock;
    }


    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }
}
package brickGame.model;

import brickGame.handler.GameLogicHandler;
import javafx.scene.shape.Circle;

/**
 * The BallControl class manages the movement and physics of the ball in the game.
 * It includes methods for controlling the ball's velocity, handling collisions, and updating its position.
 *
 * This class follows the singleton pattern, ensuring that only one instance of BallControl exists in the game.
 * It is designed to work in conjunction with the GameLogicHandler and InitGameComponent classes to provide
 * comprehensive control over the ball's behavior.
 *
 * @author Leong Tin Jet
 */
public class BallControl {

    /**
     * Private constructor to enforce the singleton pattern.
     */
    private BallControl() {
    }

    /**
     * Gets the instance of the BallControl class using the singleton pattern.
     *
     * @return The singleton instance of BallControl.
     */
    public static BallControl getInstance() {
        if (instance == null) {
            instance = new BallControl();
        }
        return instance;
    }

    /**
     * The singleton instance of the BallControl class.
     */
    private static BallControl instance;

    /**
     * The GameLogicHandler associated with the BallControl instance.
     */
    private GameLogicHandler gameLogicHandler;

    /**
     * The InitGameComponent associated with the BallControl instance.
     */
    private InitGameComponent initGameComponent;



    /**
     * The graphical representation of the ball in the game scene.
     */
    private Circle ball;

    /**
     * The x-coordinate of the ball's center.
     */
    private double xBall;

    /**
     * The y-coordinate of the ball's center.
     */
    private double yBall;

    /**
     * Flag indicating whether the ball is currently moving downward.
     */
    private boolean goDownBall = true;

    /**
     * Flag indicating whether the ball is currently moving to the right.
     */
    private boolean goRightBall = true;

    /**
     * Flag indicating whether the ball has collided with the paddle.
     */
    private boolean collideToPaddle = false;

    /**
     * Flag indicating whether the ball has collided with the paddle and is moving to the right.
     */
    private boolean collideToPaddleAndMoveToRight = true;

    /**
     * Flag indicating whether the ball has collided with the right wall.
     */
    private boolean collideToRightWall = false;

    /**
     * Flag indicating whether the ball has collided with the left wall.
     */
    private boolean collideToLeftWall = false;

    /**
     * Flag indicating whether the ball has collided with the right side of a block.
     */
    private boolean collideToRightBlock = false;

    /**
     * Flag indicating whether the ball has collided with the bottom side of a block.
     */
    private boolean collideToBottomBlock = false;

    /**
     * Flag indicating whether the ball has collided with the left side of a block.
     */
    private boolean collideToLeftBlock = false;

    /**
     * Flag indicating whether the ball has collided with the top side of a block.
     */
    private boolean collideToTopBlock = false;

    /**
     * The velocity of the ball along the horizontal axis (x-axis).
     */
    private double vX = 1.000;

    /**
     * The velocity of the ball along the vertical axis (y-axis).
     */
    private double vY = 1.000;


    /**
     * Resets the flags indicating collisions with paddle,wall and block.
     */
    public void resetcollideFlags() {

        setCollideToPaddle(false);
        setCollideToPaddleAndMoveToRight(false);
        setCollideToRightWall(false);
        setCollideToLeftWall(false);

        setCollideToRightBlock(false);
        setCollideToBottomBlock(false);
        setCollideToLeftBlock(false);
        setCollideToTopBlock(false);
    }

    /**
     * Sets up the physics for the ball, by detecting the collision with various game element
     * such as paddle,wall and block, then updating the collision flag based on different conditions
     * then change the ball movement based on the current flag
     */
    public void setPhysicsToBall() {
        controlBallMovement();
        checkCollisionWithVerticalWall();

        if (getyBall() >= initGameComponent.getyPaddle() - initGameComponent.getBALL_RADIUS()) {
            if (getxBall() >= initGameComponent.getxPaddle() && getxBall() <= initGameComponent.getxPaddle() + initGameComponent.getPADDLE_WIDTH()) {
                //gameLogicHandler.setHitTime(gameLogicHandler.getTime());
                resetcollideFlags();
                setCollideToPaddle(true);
                setGoDownBall(false);

                double relation = (getxBall() - initGameComponent.getCenterPaddleX()) / (initGameComponent.getPADDLE_WIDTH() / 2);

                if (Math.abs(relation) <= 0.3) {
                    setvX(Math.abs(relation));
                } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
                    setvX((Math.abs(relation) * 1.5) + (gameLogicHandler.getLevel() / 3.500));
                } else {
                    setvX((Math.abs(relation) * 2) + (gameLogicHandler.getLevel() / 3.500));
                }

                if (getxBall() - initGameComponent.getCenterPaddleX() > 0) {
                    setCollideToPaddleAndMoveToRight(true);
                } else {
                    setCollideToPaddleAndMoveToRight(false);
                }
            }
        }
        checkCollisionWithHorizontalWall();
        collideToPaddlePhysics();
        collideToWallPhysics();
        collideToBlockPhysics();
    }

    /**
     * Control the ball position by checking value of isGoDownBall and isGoRightBall
     */
    private void controlBallMovement() {
        if (isGoDownBall()) {
            setyBall(getyBall() + getvY());
        } else {
            setyBall(getyBall() - getvY());
        }

        if (isGoRightBall()) {
            setxBall(getxBall() + getvX());
        } else {
            setxBall(getxBall() - getvX());
        }
    }

    /**
     * Checks for collisions between the ball and the vertical walls of the game scene.
     * If a collision is detected, appropriate flags are reset and updated based on the collision scenario.
     * Additionally, if the ball exceeds the bottom boundary of the scene and the game is not in "gold status,"
     * a heart is deducted from the player's lives by the GameLogicHandler.
     */
    private void checkCollisionWithVerticalWall() {

        //collide with top wall
        if (getyBall() <= 0) {
            resetcollideFlags();
            setGoDownBall(true);
        }

        //collide with bottom wall
        if (getyBall() >= initGameComponent.getSCENE_HEIGHT()) {
            setGoDownBall(false);
            resetcollideFlags();
            if (!gameLogicHandler.isGoldStatus()) {
                gameLogicHandler.deductHeart();
            }
        }

    }

    /**
     * Checks for collisions between the ball and the horizontal walls of the game scene.
     * If a collision is detected with the right wall, appropriate flags are reset and updated.
     * If a collision is detected with the left wall, appropriate flags are reset and updated as well.
     */
    private void checkCollisionWithHorizontalWall() {
        if (getxBall() >= initGameComponent.getSCENE_WIDTH()) {
            resetcollideFlags();
            setCollideToRightWall(true);
        }

        if (getxBall() <= 0) {
            resetcollideFlags();
            setCollideToLeftWall(true);
        }
    }

    /**
     * Manages the physics of the ball when it collides with the paddle.
     * If a collision with the paddle is detected, it adjusts the direction of the ball's movement.
     * If the collision indicates movement towards the right side of the paddle,
     * the ball is set to move to the right; otherwise, it is set to move to the left.
     */
    private void collideToPaddlePhysics() {
        // Check if the ball has collided with the paddle
        if (isCollideToPaddle()) {
            // Check the direction of collision with the paddle
            if (isCollideToPaddleAndMoveToRight()) {
                setGoRightBall(true);  // Set the ball to move to the right
            } else {
                setGoRightBall(false); // Set the ball to move to the left
            }
        }
    }


    /**
     * Manages the physics of the ball when it collides with left and right walls.
     * If a collision with the right wall is detected, it stops the ball from moving to the right.
     * If a collision with the left wall is detected, it stops the ball from moving to the left.
     */
    private void collideToWallPhysics() {
        // Check if the ball has collided with the right wall
        if (isCollideToRightWall()) {
            setGoRightBall(false);  // Stop the ball from moving to the right
        }

        // Check if the ball has collided with the left wall
        if (isCollideToLeftWall()) {
            setGoRightBall(true);   // Stop the ball from moving to the left
        }
    }


    /**
     * Manages the physics of the ball when it collides with blocks.
     * If a collision with the right side of a block is detected, it sets the ball to move to the right.
     * If a collision with the left side of a block is detected, it sets the ball to move to the left.
     * If a collision with the top side of a block is detected, it stops the ball from moving downward.
     * If a collision with the bottom side of a block is detected, it sets the ball to move downward.
     */
    private void collideToBlockPhysics() {
        // Check if the ball has collided with the right side of a block
        if (isCollideToRightBlock()) {
            setGoRightBall(true);  // Set the ball to move to the right
        }

        // Check if the ball has collided with the left side of a block
        // (solving a logic error in the original code)
        if (isCollideToLeftBlock()) {
            setGoRightBall(false); // Set the ball to move to the left
        }

        // Check if the ball has collided with the top side of a block
        if (isCollideToTopBlock()) {
            setGoDownBall(false);  // Stop the ball from moving downward
        }

        // Check if the ball has collided with the bottom side of a block
        if (isCollideToBottomBlock()) {
            setGoDownBall(true);   // Set the ball to move downward
        }
    }


    /**
     * Gets the Circle object representing the ball.
     *
     * @return The Circle object representing the ball.
     */
    public Circle getBall() {
        return ball;
    }

    /**
     * Sets the Circle object representing the ball.
     *
     * @param ball The Circle object to set as the ball.
     */
    public void setBall(Circle ball) {
        this.ball = ball;
    }


    /**
     * Gets the x-coordinate of the ball's center.
     *
     * @return The x-coordinate of the ball's center.
     */
    public double getxBall() {
        return xBall;
    }

    /**
     * Sets the x-coordinate of the ball's center.
     *
     * @param xBall The new x-coordinate of the ball's center.
     */
    public void setxBall(double xBall) {
        this.xBall = xBall;
    }

    /**
     * Gets the y-coordinate of the ball's center.
     *
     * @return The y-coordinate of the ball's center.
     */
    public double getyBall() {
        return yBall;
    }

    /**
     * Sets the y-coordinate of the ball's center.
     *
     * @param yBall The new y-coordinate of the ball's center.
     */
    public void setyBall(double yBall) {
        this.yBall = yBall;
    }

    /**
     * Checks if the ball is currently moving downward.
     *
     * @return True if the ball is moving downward, false otherwise.
     */
    public boolean isGoDownBall() {
        return goDownBall;
    }

    /**
     * Sets the direction of the ball's movement along the vertical axis.
     *
     * @param goDownBall True to make the ball move downward, false otherwise.
     */
    public void setGoDownBall(boolean goDownBall) {
        this.goDownBall = goDownBall;
    }

    /**
     * Checks if the ball is currently moving to the right.
     *
     * @return True if the ball is moving to the right, false otherwise.
     */
    public boolean isGoRightBall() {
        return goRightBall;
    }

    /**
     * Sets the direction of the ball's movement along the horizontal axis.
     *
     * @param goRightBall True to make the ball move to the right, false otherwise.
     */
    public void setGoRightBall(boolean goRightBall) {
        this.goRightBall = goRightBall;
    }

    /**
     * Checks if the ball has collided with the paddle.
     *
     * @return True if the ball has collided with the paddle, false otherwise.
     */
    public boolean isCollideToPaddle() {
        return collideToPaddle;
    }

    /**
     * Sets the collision state of the ball with the paddle.
     *
     * @param collideToPaddle True to indicate a collision with the paddle, false otherwise.
     */
    public void setCollideToPaddle(boolean collideToPaddle) {
        this.collideToPaddle = collideToPaddle;
    }


    /**
     * Checks if the ball has collided with the paddle and is moving to the right.
     *
     * @return True if the ball has collided with the paddle and is moving to the right, false otherwise.
     */
    public boolean isCollideToPaddleAndMoveToRight() {
        return collideToPaddleAndMoveToRight;
    }

    /**
     * Sets the collision state of the ball with the paddle and its movement direction.
     *
     * @param collideToPaddleAndMoveToRight True to indicate a collision with the paddle and movement to the right,
     *                                       false otherwise.
     */
    public void setCollideToPaddleAndMoveToRight(boolean collideToPaddleAndMoveToRight) {
        this.collideToPaddleAndMoveToRight = collideToPaddleAndMoveToRight;
    }

    /**
     * Checks if the ball has collided with the right wall.
     *
     * @return True if the ball has collided with the right wall, false otherwise.
     */
    public boolean isCollideToRightWall() {
        return collideToRightWall;
    }

    /**
     * Sets the collision state of the ball with the right wall.
     *
     * @param collideToRightWall True to indicate a collision with the right wall, false otherwise.
     */
    public void setCollideToRightWall(boolean collideToRightWall) {
        this.collideToRightWall = collideToRightWall;
    }

    /**
     * Checks if the ball has collided with the left wall.
     *
     * @return True if the ball has collided with the left wall, false otherwise.
     */
    public boolean isCollideToLeftWall() {
        return collideToLeftWall;
    }

    /**
     * Sets the collision state of the ball with the left wall.
     *
     * @param collideToLeftWall True to indicate a collision with the left wall, false otherwise.
     */
    public void setCollideToLeftWall(boolean collideToLeftWall) {
        this.collideToLeftWall = collideToLeftWall;
    }

    /**
     * Checks if the ball has collided with the right side of a block.
     *
     * @return True if the ball has collided with the right side of a block, false otherwise.
     */
    public boolean isCollideToRightBlock() {
        return collideToRightBlock;
    }

    /**
     * Sets the collision state of the ball with the right side of a block.
     *
     * @param collideToRightBlock True to indicate a collision with the right side of a block, false otherwise.
     */
    public void setCollideToRightBlock(boolean collideToRightBlock) {
        this.collideToRightBlock = collideToRightBlock;
    }

    /**
     * Checks if the ball has collided with the bottom side of a block.
     *
     * @return True if the ball has collided with the bottom side of a block, false otherwise.
     */
    public boolean isCollideToBottomBlock() {
        return collideToBottomBlock;
    }

    /**
     * Sets the collision state of the ball with the bottom side of a block.
     *
     * @param collideToBottomBlock True to indicate a collision with the bottom side of a block, false otherwise.
     */
    public void setCollideToBottomBlock(boolean collideToBottomBlock) {
        this.collideToBottomBlock = collideToBottomBlock;
    }

    /**
     * Checks if the ball has collided with the left side of a block.
     *
     * @return True if the ball has collided with the left side of a block, false otherwise.
     */
    public boolean isCollideToLeftBlock() {
        return collideToLeftBlock;
    }

    /**
     * Sets the collision state of the ball with the left side of a block.
     *
     * @param collideToLeftBlock True to indicate a collision with the left side of a block, false otherwise.
     */
    public void setCollideToLeftBlock(boolean collideToLeftBlock) {
        this.collideToLeftBlock = collideToLeftBlock;
    }

    /**
     * Checks if the ball has collided with the top side of a block.
     *
     * @return True if the ball has collided with the top side of a block, false otherwise.
     */
    public boolean isCollideToTopBlock() {
        return collideToTopBlock;
    }

    /**
     * Sets the collision state of the ball with the top side of a block.
     *
     * @param collideToTopBlock True to indicate a collision with the top side of a block, false otherwise.
     */
    public void setCollideToTopBlock(boolean collideToTopBlock) {
        this.collideToTopBlock = collideToTopBlock;
    }


    /**
     * Gets the velocity of the ball along the horizontal axis (x-axis).
     *
     * @return The velocity of the ball along the horizontal axis.
     */
    public double getvX() {
        return vX;
    }

    /**
     * Sets the velocity of the ball along the horizontal axis (x-axis).
     *
     * @param vX The new velocity of the ball along the horizontal axis.
     */
    public void setvX(double vX) {
        this.vX = vX;
    }

    /**
     * Gets the velocity of the ball along the vertical axis (y-axis).
     *
     * @return The velocity of the ball along the vertical axis.
     */
    public double getvY() {
        return vY;
    }

    /**
     * Sets the velocity of the ball along the vertical axis (y-axis).
     *
     * @param vY The new velocity of the ball along the vertical axis.
     */
    public void setvY(double vY) {
        this.vY = vY;
    }

    /**
     * Sets the GameLogicHandler for the BallControl instance.
     *
     * @param gameLogicHandler The GameLogicHandler instance to set.
     */
    public void setGameLogicHandler(GameLogicHandler gameLogicHandler) {
        this.gameLogicHandler = gameLogicHandler;
    }

    /**
     * Sets the InitGameComponent for the BallControl instance.
     *
     * @param initGameComponent The InitGameComponent instance to set.
     */
    public void setInitGameComponent(InitGameComponent initGameComponent) {
        this.initGameComponent = initGameComponent;
    }

}

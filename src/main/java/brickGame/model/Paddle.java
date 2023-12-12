package brickGame.model;

import javafx.scene.shape.Rectangle;

import static brickGame.Constants.SCENE_WIDTH;

/**
 * The {@code Paddle} class represents the paddle used in the brick game application.
 *
 * <p>
 * The class includes methods and properties related to the paddle's movement, dimensions, and position.
 * The paddle is a singleton class, ensuring only one instance is created.
 * </p>
 *
 * <p>
 * The paddle can be moved to the left or right, and its position is updated accordingly.
 * The class also provides methods to retrieve the paddle's rectangle shape and various dimensions.
 * </p>
 *
 * <p>
 * This class is part of the game's model, managing the behavior of the paddle in the game.
 * </p>
 * <br>
 * <a href="https://github.com/tinjet11/COMP2042_CW_hcytl1/blob/master/src/main/java/brickGame/model/Paddle.java">Current Code</a>
 * @author Leong Tin Jet
 * @version 1.0
 */
public class Paddle {
    /**
     * The singleton instance of the {@code Paddle} class.
     */
    private static Paddle instance;

    /**
     * The private constructor to enforce singleton pattern.
     */
    private Paddle() {
    }
    /**
     * Returns the singleton instance of the {@code Paddle} class.
     *
     * @return The singleton instance.
     */
    public static Paddle getInstance() {
        if (instance == null) {
            instance = new Paddle();
        }
        return instance;
    }

    /**
     * The rectangle shape representing the paddle.
     */
    private Rectangle paddle;

    /**
     * The x-coordinate of the paddle's position.
     */
    private double xPaddle = 0.0f;

    /**
     * The x-coordinate of the center of the paddle.
     */
    private double centerPaddleX;

    /**
     * The y-coordinate of the paddle's position.
     */
    private double yPaddle = 690.0f;

    /**
     * The width of the paddle.
     */
    private final int PADDLE_WIDTH = 130;

    /**
     * The height of the paddle.
     */
    private final int PADDLE_HEIGHT = 10;

    /**
     * Half of the width of the paddle.
     */
    private final int HALF_PADDLE_WIDTH = PADDLE_WIDTH / 2;

    /**
     * Constant representing the direction to move the paddle to the left.
     */
    private final int LEFT = 1;

    /**
     * Constant representing the direction to move the paddle to the right.
     */
    private final int RIGHT = 2;

    /**
     * Moves the paddle in the specified direction.
     *
     * @param direction The direction to move the paddle (LEFT or RIGHT).
     */
    public void move(final int direction) {
        //Move the paddle
        new Thread(new Runnable() {
            @Override
            public void run() {
                int sleepTime = 4;
                for (int i = 0; i < 30; i++) {
                    if (getxPaddle() == (SCENE_WIDTH - getPADDLE_WIDTH()) && direction == RIGHT) {
                        return;
                    }
                    if (getxPaddle() == 0 && direction == LEFT) {
                        return;
                    }
                    if (direction == RIGHT) {
                        setxPaddle(getxPaddle() + 1);
                    } else {
                        setxPaddle(getxPaddle() - 1);
                    }
                    setCenterPaddleX(getxPaddle() + getHALF_PADDLE_WIDTH());
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i >= 20) {
                        sleepTime = i;
                    }
                }
            }
        }).start();
    }


    /**
     * Returns the rectangle shape representing the paddle.
     *
     * @return The paddle's rectangle shape.
     */
    public Rectangle getPaddle() {
        return paddle;
    }
    /**
     * Sets the new rectangle shape representing the paddle.
     *
     * @param paddle The new paddle
     */
    public void setPaddle(Rectangle paddle) {
        this.paddle = paddle;
    }

    /**
     * Returns the x-coordinate of the paddle's position.
     *
     * @return The x-coordinate of the paddle.
     */
    public double getxPaddle() {
        return xPaddle;
    }

    /**
     * Sets the x-coordinate of the paddle's position.
     *
     * @param xPaddle The new x-coordinate for the paddle.
     */
    public void setxPaddle(double xPaddle) {
        this.xPaddle = xPaddle;
    }

    /**
     * Returns the x-coordinate of the center of the paddle.
     *
     * @return The x-coordinate of the center of the paddle.
     */
    public double getCenterPaddleX() {
        return centerPaddleX;
    }

    /**
     * Sets the x-coordinate of the center of the paddle.
     *
     * @param centerPaddleX The new x-coordinate for the center of the paddle.
     */
    public void setCenterPaddleX(double centerPaddleX) {
        this.centerPaddleX = centerPaddleX;
    }

    /**
     * Returns the y-coordinate of the paddle's position.
     *
     * @return The y-coordinate of the paddle.
     */
    public double getyPaddle() {
        return yPaddle;
    }

    /**
     * Sets the y-coordinate of the paddle's position.
     *
     * @param yPaddle The new y-coordinate for the paddle.
     */
    public void setyPaddle(double yPaddle) {
        this.yPaddle = yPaddle;
    }

    /**
     * Returns the width of the paddle.
     *
     * @return The width of the paddle.
     */
    public int getPADDLE_WIDTH() {
        return PADDLE_WIDTH;
    }

    /**
     * Returns the height of the paddle.
     *
     * @return The height of the paddle.
     */
    public int getPADDLE_HEIGHT() {
        return PADDLE_HEIGHT;
    }

    /**
     * Returns half of the width of the paddle.
     *
     * @return Half of the width of the paddle.
     */
    public int getHALF_PADDLE_WIDTH() {
        return HALF_PADDLE_WIDTH;
    }
}

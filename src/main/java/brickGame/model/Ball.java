package brickGame.model;

import javafx.scene.shape.Circle;
/**
 * The Ball class represents the game ball in the Brick Breaker game.
 * It follows the Singleton pattern, ensuring only one instance exists throughout the application.
 * The class contains information about the ball's position, the Circle object representing the ball,
 * and the ball's radius.
 *
 * @author Leong Tin Jet
 * @version 1.0
 */
public class Ball {
    /**
     * The singleton instance of the {@code Ball} class.
     */
    private static Ball instance;

    /**
     *  Private constructor to enforce Singleton pattern
     */
    private Ball() {
    }

    /**
     * Gets the singleton instance of the Ball class.
     *
     * @return The singleton instance of the Ball class.
     */
    public static Ball getInstance() {
        if (instance == null) {
            instance = new Ball();
        }
        return instance;
    }

    /**
     * The Circle object representing the ball
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
     * The radius of ball
     */
    private final int BALL_RADIUS = 10;


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
     * Gets the ball's radius.
     *
     * @return the ball's radius.
     */
    public int getBALL_RADIUS() {
        return BALL_RADIUS;
    }

}

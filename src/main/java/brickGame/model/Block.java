package brickGame.model;


import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

import static brickGame.Constants.*;

/**
 * The Block class represents a block in the game's playing area.
 * Blocks can have different types and visual representations.
 * It contains information about its position, type, and whether it is destroyed or not.
 * The class also provides methods for drawing the block, loading its image pattern, and checking for collisions with the block.
 * <p>
 * Block types include normal, choco, star, heart, concrete, and penalty blocks.
 * </p>
 * <p>
 * The class implements the Serializable interface to support saving and loading game states.
 * </p>
 * <br></br>
 * <a href="https://github.com/kooitt/CourseworkGame/blob/master/src/main/java/brickGame/Block.java">Original Code</a>
 * @author Leong Tin Jet
 * @version 1.0
 */
public class Block implements Serializable {
    /**
     * The row position of the block.
     */
    private int row;

    /**
     * The column position of the block.
     */
    private int column;

    /**
     * A flag indicating whether the block is destroyed.
     */
    private boolean isDestroyed = false;

    /**
     * The type of the block.
     */
    private BLOCK_TYPE type;

    /**
     * The x-coordinate of the block.
     */
    private int x;

    /**
     * The y-coordinate of the block.
     */
    private int y;

    /**
     * The width of the block.
     */
    public static final int BLOCK_WIDTH = 100;

    /**
     * The height of the block.
     */
    public static final int BLOCK_HEIGHT = 30;

    /**
     * The top padding for the block.
     */
    public static final int BLOCK_PADDING_TOP = BLOCK_HEIGHT * 2;

    /**
     * The horizontal padding for the block.
     */
    public static final int BLOCK_PADDING_H = 50;

    /**
     * The JavaFX Rectangle representing the block.
     */
    private Rectangle rect;

    /**
     * The radius of the ball associated with the block.
     */
    private final int BALL_RADIUS = 10;


    /**
     * Enumeration representing different hit states during a collision check.
     */
    public enum HIT_STATE {
        NO_HIT, HIT_RIGHT, HIT_BOTTOM, HIT_LEFT, HIT_TOP
    }

    /**
     * Enumeration representing different block types.
     */
    public enum BLOCK_TYPE {
        BLOCK_NORMAL, BLOCK_CHOCO, BLOCK_STAR, BLOCK_HEART, BLOCK_CONCRETE, BLOCK_PENALTY,
    }


    /**
     * Constructs a new Block instance with the specified row, column, and type.
     *
     * @param row    The row position of the block.
     * @param column The column position of the block.
     * @param type   The type of the block.
     */

    public Block(int row, int column, BLOCK_TYPE type) {
        this.setRow(row);
        this.setColumn(column);
        this.setType(type);
        draw();
    }

    /**
     * Draws the block on the game scene based on its position, size, and type.
     */
    private void draw() {
        setX((getColumn() * BLOCK_WIDTH) + BLOCK_PADDING_H);
        setY((getRow() * BLOCK_HEIGHT) + BLOCK_PADDING_TOP);

        setRect(new Rectangle());
        getRect().setWidth(BLOCK_WIDTH);
        getRect().setHeight(BLOCK_HEIGHT);
        getRect().setX(getX());
        getRect().setY(getY());

        ImagePattern pattern = loadImagePattern();
        getRect().setFill(pattern);
    }

    /**
     * Loads and returns the image pattern for the block based on its type.
     *
     * @return The ImagePattern representing the visual appearance of the block.
     */
    public ImagePattern loadImagePattern() {
        String imagePath;
        switch (getType()) {
            case BLOCK_CHOCO:
                imagePath = IMAGE_PATH_CHOCO;
                break;
            case BLOCK_HEART:
                imagePath = IMAGE_PATH_HEART;
                break;
            case BLOCK_STAR:
                imagePath = IMAGE_PATH_STAR;
                break;
            case BLOCK_PENALTY:
                imagePath = IMAGE_PATH_PENALTY;
                break;
            case BLOCK_CONCRETE:
                imagePath = IMAGE_PATH_CONCRETE;
                break;
            default:
                imagePath = IMAGE_PATH_NORMAL_BRICK;
        }

        Image image = new Image(getClass().getResourceAsStream(imagePath));
        return new ImagePattern(image);
    }

    /**
     * Checks if the provided coordinates represent a collision with the block.
     * Determines the hit state based on the collision point.
     *
     * @param xBall The x-coordinate of the collision point.
     * @param yBall The y-coordinate of the collision point.
     * @return The hit state based on the collision point.
     */
    public HIT_STATE checkHitToBlock(double xBall, double yBall) {

        if (isDestroyed()) {
            return HIT_STATE.NO_HIT;
        }

        if (xBall + BALL_RADIUS >= getX() && xBall - BALL_RADIUS <= getX() + BLOCK_WIDTH && yBall + BALL_RADIUS >= getY() && yBall - BALL_RADIUS <= getY() + BLOCK_HEIGHT) {
            // Collision detected
            double dx = Math.min(Math.abs(xBall - getX()), Math.abs(xBall - (getX() + BLOCK_WIDTH)));
            double dy = Math.min(Math.abs(yBall - getY()), Math.abs(yBall - (getY() + BLOCK_HEIGHT)));

            if (dx < dy) {
                // Collision on the x-axis
                return xBall < getX() + BLOCK_WIDTH / 2 ? HIT_STATE.HIT_LEFT : HIT_STATE.HIT_RIGHT;
            } else {
                // Collision on the y-axis
                return yBall < getY() + BLOCK_HEIGHT / 2 ? HIT_STATE.HIT_TOP : HIT_STATE.HIT_BOTTOM;
            }
        }

        return HIT_STATE.NO_HIT;
    }

    /**
     * Gets the row position of the block.
     *
     * @return The row position.
     */
    public int getRow() {
        return row;
    }

    /**
     * Sets the row position of the block.
     *
     * @param row The new row position.
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Gets the column position of the block.
     *
     * @return The column position.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Sets the column position of the block.
     *
     * @param column The new column position.
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * Checks if the block is destroyed.
     *
     * @return True if the block is destroyed, false otherwise.
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }

    /**
     * Sets whether the block is destroyed.
     *
     * @param destroyed The new value indicating whether the block is destroyed.
     */
    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }

    /**
     * Gets the type of the block.
     *
     * @return The type of the block.
     */
    public BLOCK_TYPE getType() {
        return type;
    }

    /**
     * Sets the type of the block.
     *
     * @param type The new type of the block.
     */
    public void setType(BLOCK_TYPE type) {
        this.type = type;
    }

    /**
     * Gets the x-coordinate of the block.
     *
     * @return The x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of the block.
     *
     * @param x The new x-coordinate.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gets the y-coordinate of the block.
     *
     * @return The y-coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of the block.
     *
     * @param y The new y-coordinate.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets the JavaFX Rectangle representing the block.
     *
     * @return The JavaFX Rectangle.
     */
    public Rectangle getRect() {
        return rect;
    }

    /**
     * Sets the JavaFX Rectangle representing the block.
     *
     * @param rect The new JavaFX Rectangle.
     */
    public void setRect(Rectangle rect) {
        this.rect = rect;
    }


}

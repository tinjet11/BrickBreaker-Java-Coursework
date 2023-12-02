package brickGame.model;


import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

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
 *
 * @author Leong Tin Jet
 * @version 1.0
 */
public class Block implements Serializable {
    private int row;
    private int column;
    private boolean isDestroyed = false;
    private BLOCK_TYPE type;
    private int x;
    private int y;
    public static final int BLOCK_WIDTH = 100;
    public static final int BLOCK_HEIGHT = 30;
    public static final int BLOCK_PADDING_TOP = BLOCK_HEIGHT * 2;
    public static final int BLOCK_PADDING_H = 50;
    private Rectangle rect;
    private final int BALL_RADIUS = 10;

    private  final String IMAGE_PATH_CHOCO = "/images/choco.jpg";
    private  final String IMAGE_PATH_HEART = "/images/heart.jpg";
    private  final String IMAGE_PATH_STAR = "/images/star.jpg";
    private  final String IMAGE_PATH_PENALTY  = "/images/penalty.jpeg";
    private  final String IMAGE_PATH_CONCRETE = "/images/brick-concrete.jpeg";
    private  final String IMAGE_PATH_NORMAL = "/images/brick.jpg";

    /**
     * Enumeration representing different hit states during a collision check.
     */
    public enum HIT_STATE {
        NO_HIT,
        HIT_RIGHT,
        HIT_BOTTOM,
        HIT_LEFT,
        HIT_TOP
    }

    /**
     * Enumeration representing different block types.
     */
    public enum BLOCK_TYPE {
        BLOCK_NORMAL,
        BLOCK_CHOCO,
        BLOCK_STAR,
        BLOCK_HEART,
        BLOCK_CONCRETE,
        BLOCK_PENALTY,
    }


    /**
     * Constructs a new Block instance with the specified row, column, and type.
     *
     * @param row   The row position of the block.
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
                imagePath = IMAGE_PATH_NORMAL;
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

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }

    public BLOCK_TYPE getType() {
        return type;
    }

    public void setType(BLOCK_TYPE type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

}

package brickGame;


import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

import static brickGame.Main.BALL_RADIUS;

public class Block implements Serializable {
    private static Block block = new Block(-1, -1, 99);

    public int row;
    public int column;

    public boolean isDestroyed = false;

    public int type;

    public int x;
    public int y;

    private final int blockWidth = 100;
    private final int blockHeight = 30;
    private final int paddingTop = blockHeight * 2;
    private final int paddingH = 50;
    public Rectangle rect;


    public static final int NO_HIT = -1;
    public static final int HIT_RIGHT = 0;
    public static final int HIT_BOTTOM = 1;
    public static final int HIT_LEFT = 2;
    public static final int HIT_TOP = 3;

    public static final int BLOCK_NORMAL = 99;
    public static final int BLOCK_CHOCO = 100;
    public static final int BLOCK_STAR = 101;
    public static final int BLOCK_HEART = 102;


    public Block(int row, int column, int type) {
        this.row = row;
        this.column = column;
        this.type = type;

        draw();
    }

    private void draw() {
        x = (column * blockWidth) + paddingH;
        y = (row * blockHeight) + paddingTop;

        rect = new Rectangle();
        rect.setWidth(blockWidth);
        rect.setHeight(blockHeight);
        rect.setX(x);
        rect.setY(y);

        if (type == BLOCK_CHOCO) {
            Image image = new Image("/choco.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_HEART) {
            Image image = new Image("/heart.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_STAR) {
            Image image = new Image("/star.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else {
            // rect.setFill(color);
            Image image = new Image("/brick.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        }

    }

    public int checkHitToBlock(double xBall, double yBall) {
        if (isDestroyed) {
            return NO_HIT;
        }


        if (xBall + BALL_RADIUS >= x && xBall - BALL_RADIUS <= x + blockWidth && yBall + BALL_RADIUS >= y && yBall - BALL_RADIUS <= y + blockHeight) {
            // Collision detected
            double dx = Math.min(Math.abs(xBall - x), Math.abs(xBall - (x + blockWidth)));
            double dy = Math.min(Math.abs(yBall - y), Math.abs(yBall - (y + blockHeight)));

            if (dx < dy) {
                // Collision on the x-axis
                return xBall < x + blockWidth / 2 ? HIT_LEFT : HIT_RIGHT;
            } else {
                // Collision on the y-axis
                return yBall < y + blockHeight / 2 ? HIT_TOP : HIT_BOTTOM;
            }
        }

        return NO_HIT;
    }


    public static int getPaddingTop() {
        return block.paddingTop;
    }

    public static int getPaddingH() {
        return block.paddingH;
    }

    public static int getHeight() {
        return block.blockHeight;
    }

    public static int getWidth() {
        return block.blockWidth;
    }

}

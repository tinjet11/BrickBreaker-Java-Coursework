package brickGame;


import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;


public class Block implements Serializable {
    private static Block block = new Block(-1, -1, BLOCK_TYPE.BLOCK_NORMAL);

    public int row;
    public int column;

    public boolean isDestroyed = false;

    public BLOCK_TYPE type;

    public int x;
    public int y;

    private final int blockWidth = 100;
    private final int blockHeight = 30;
    private final int paddingTop = blockHeight * 2;
    private final int paddingH = 50;
    public Rectangle rect;
    private final int BALL_RADIUS = 10;

    public enum HIT_STATE {
        NO_HIT,
        HIT_RIGHT,
        HIT_BOTTOM,
        HIT_LEFT,
        HIT_TOP
    }

    public enum BLOCK_TYPE {
        BLOCK_NORMAL,
        BLOCK_CHOCO,
        BLOCK_STAR,
        BLOCK_HEART,

        BLOCK_BOMB,
    }

    public Block(int row, int column, BLOCK_TYPE type) {
        this.row = row;
        this.column = column;
        this.type = type;
        //soundManager = new SoundManager(Main.class.getResource("/brick-break.mp3"), SoundManager.MusicType.BRICK_BREAK);
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

        if (type == BLOCK_TYPE.BLOCK_CHOCO) {
            Image image = new Image("/choco.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type ==  BLOCK_TYPE.BLOCK_HEART) {
            Image image = new Image("/heart.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type ==  BLOCK_TYPE.BLOCK_STAR) {
            Image image = new Image("/star.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        }else if (type ==  BLOCK_TYPE.BLOCK_BOMB) {
            Image image = new Image("/penalty.jpeg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        }
        else {
            Image image = new Image("/brick.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        }

    }

    public HIT_STATE checkHitToBlock(double xBall, double yBall) {
        if (isDestroyed) {
            return HIT_STATE.NO_HIT;
        }

        if (xBall + BALL_RADIUS >= x && xBall - BALL_RADIUS <= x + blockWidth && yBall + BALL_RADIUS >= y && yBall - BALL_RADIUS <= y + blockHeight) {
            // Collision detected
            double dx = Math.min(Math.abs(xBall - x), Math.abs(xBall - (x + blockWidth)));
            double dy = Math.min(Math.abs(yBall - y), Math.abs(yBall - (y + blockHeight)));

            if (dx < dy) {
                // Collision on the x-axis
               // soundManager.play();
                return xBall < x + blockWidth / 2 ? HIT_STATE.HIT_LEFT : HIT_STATE.HIT_RIGHT;
            } else {
                // Collision on the y-axis
                //soundManager.play();
                return yBall < y + blockHeight / 2 ? HIT_STATE.HIT_TOP : HIT_STATE.HIT_BOTTOM;
            }
        }

        return HIT_STATE.NO_HIT;
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

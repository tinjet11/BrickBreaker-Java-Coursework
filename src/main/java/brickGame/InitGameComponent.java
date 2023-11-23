package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

import static brickGame.Main.*;
import static brickGame.BallControl.*;
import static brickGame.GameLogicHandler.*;

public class InitGameComponent {
    public static Rectangle rect;
    public static double xPaddle = 0.0f;
    public static double centerPaddleX;
    public static double yPaddle = 640.0f;
    public static final int PADDLE_WIDTH = 130;
    public static final int PADDLE_HEIGHT = 10;
    public static final int HALF_PADDLE_WIDTH = PADDLE_WIDTH / 2;
    public static final int SCENE_WIDTH = 500;
    public static final int SCENE_HEIGHT = 700;
    public static final int BALL_RADIUS = 10;
    public static boolean isExistHeartBlock = false;

    public static ArrayList<Block> blocks = new ArrayList<>();
    public static ArrayList<Bonus> chocos = new ArrayList<>();
    //set up the brick in the game according to the current level
    public void initBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < level + 1; j++) {
                int r = new Random().nextInt(500);
                if (r % 5 == 0) {
                    continue;
                }
                int type;
                if (r % 10 == 1) {
                    type = Block.BLOCK_CHOCO;
                } else if (r % 10 == 2) {
                    if (!isExistHeartBlock) {
                        type = Block.BLOCK_HEART;
                        isExistHeartBlock = true;
                    } else {
                        type = Block.BLOCK_NORMAL;
                    }
                } else if (r % 10 == 3) {
                    type = Block.BLOCK_STAR;
                } else {
                    type = Block.BLOCK_NORMAL;
                }
                blocks.add(new Block(j, i, type));
                //System.out.println("colors " + r % (colors.length));
            }
        }
    }

    public void initBall() {
        Random random = new Random();
        xBall = random.nextInt(SCENE_WIDTH) + 1;
        // Ensure that the ball starts above the screen's bottom edge
        int minYBall = ((level + 1) * Block.getHeight()) + 2 * BALL_RADIUS;
        yBall = random.nextInt(SCENE_HEIGHT - minYBall) + minYBall;

        ball = new Circle();
        ball.setRadius(BALL_RADIUS);
        ball.setFill(new ImagePattern(new Image("/ball.png")));
    }

    // the paddle
    public void initPaddle() {
        rect = new Rectangle();
        rect.setWidth(PADDLE_WIDTH);
        rect.setHeight(PADDLE_HEIGHT);
        rect.setX(xPaddle);
        rect.setY(yPaddle);

        ImagePattern pattern = new ImagePattern(new Image("/block.jpg"));

        rect.setFill(pattern);
    }
}
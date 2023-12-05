package brickGame.model;

import javafx.scene.shape.Rectangle;

import static brickGame.Constants.SCENE_WIDTH;

public class Paddle {
    private  static  Paddle instance;
    private Paddle() {

    }
    public static Paddle getInstance() {
        if (instance == null) {
            instance = new Paddle();
        }
        return instance;
    }
    private Rectangle paddle;
    private double xPaddle = 0.0f;
    private double centerPaddleX;
    private double yPaddle = 690.0f;
    private final int PADDLE_WIDTH = 130;
    private final int PADDLE_HEIGHT = 10;
    private final int HALF_PADDLE_WIDTH = PADDLE_WIDTH / 2;

    private final int LEFT = 1;
    private final int RIGHT = 2;

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


    public Rectangle getPaddle() {
        return paddle;
    }

    public void setPaddle(Rectangle paddle) {
        this.paddle = paddle;
    }

    public double getxPaddle() {
        return xPaddle;
    }

    public void setxPaddle(double xPaddle) {
        this.xPaddle = xPaddle;
    }

    public double getCenterPaddleX() {
        return centerPaddleX;
    }

    public void setCenterPaddleX(double centerPaddleX) {
        this.centerPaddleX = centerPaddleX;
    }

    public double getyPaddle() {
        return yPaddle;
    }

    public void setyPaddle(double yPaddle) {
        this.yPaddle = yPaddle;
    }

    public int getPADDLE_WIDTH() {
        return PADDLE_WIDTH;
    }

    public int getPADDLE_HEIGHT() {
        return PADDLE_HEIGHT;
    }

    public int getHALF_PADDLE_WIDTH() {
        return HALF_PADDLE_WIDTH;
    }
}

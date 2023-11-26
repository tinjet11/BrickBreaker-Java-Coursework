package brickGame;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;

import static brickGame.GameStateManager.gameState;
import static brickGame.Main.*;
import static brickGame.InitGameComponent.*;
import static brickGame.GameLogicHandler.*;

public class BallControl {

    private static BallControl instance;
    private GameLogicHandler gameLogicHandler;

    private BallControl() {
    }

    public static BallControl getInstance() {
        if (instance == null) {
            instance = new BallControl();
        }
        return instance;
    }

    private Circle ball;
    private double xBall;
    private double yBall;

    private boolean goDownBall = true;
    private boolean goRightBall = true;
    private boolean collideToPaddle = false;
    private boolean collideToPaddleAndMoveToRight = true;
    private boolean collideToRightWall = false;
    private boolean collideToLeftWall = false;

    private boolean collideToRightBlock = false;
    private boolean collideToBottomBlock = false;
    private boolean collideToLeftBlock = false;
    private boolean collideToTopBlock = false;

    private double vX = 1.000;
    private double vY = 1.000;


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

    public void setPhysicsToBall() {
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

        if (getyBall() <= 0) {
            resetcollideFlags();
            setGoDownBall(true);
        }

        //if ball exceed the scene
        if (getyBall() >= SCENE_HEIGHT) {
                setGoDownBall(false);
                resetcollideFlags();
            if (!gameLogicHandler.isGoldStatus()) {
                gameLogicHandler.deductHeart();
            }
        }

        if (getyBall() >= yPaddle - BALL_RADIUS) {
            if (getxBall() >= xPaddle && getxBall() <= xPaddle + PADDLE_WIDTH) {
                gameLogicHandler.setHitTime(gameLogicHandler.getTime());
                resetcollideFlags();
                setCollideToPaddle(true);
                setGoDownBall(false);

                double relation = (getxBall() - centerPaddleX) / (PADDLE_WIDTH / 2);

                if (Math.abs(relation) <= 0.3) {
                    //vX = 0;
                    setvX(Math.abs(relation));
                } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
                    setvX((Math.abs(relation) * 1.5) + (gameLogicHandler.getLevel() / 3.500));
                    //System.out.println("vX " + vX);
                } else {
                    setvX((Math.abs(relation) * 2) + (gameLogicHandler.getLevel() / 3.500));
                    //System.out.println("vX " + vX);
                }

                if (getxBall() - centerPaddleX > 0) {
                    setCollideToPaddleAndMoveToRight(true);
                } else {
                    setCollideToPaddleAndMoveToRight(false);
                }
                //System.out.println("collide2");
            }
        }

        if (getxBall() >= SCENE_WIDTH) {
            resetcollideFlags();
            setCollideToRightWall(true);
        }

        if (getxBall() <= 0) {
            resetcollideFlags();
            setCollideToLeftWall(true);
        }

        if (isCollideToPaddle()) {
            if (isCollideToPaddleAndMoveToRight()) {
                setGoRightBall(true);
            } else {
                setGoRightBall(false);
            }
        }

        //Wall collide

        if (isCollideToRightWall()) {
            setGoRightBall(false);
        }

        if (isCollideToLeftWall()) {
            setGoRightBall(true);
        }

        //Block collide

        if (isCollideToRightBlock()) {
            setGoRightBall(true);
        }
        //solve logic error
        if (isCollideToLeftBlock()) {
            setGoRightBall(false);
        }

        if (isCollideToTopBlock()) {
            setGoDownBall(false);
        }

        if (isCollideToBottomBlock()) {
            setGoDownBall(true);
        }
    }

    public Circle getBall() {
        return ball;
    }

    public void setBall(Circle ball) {
        this.ball = ball;
    }

    public double getxBall() {
        return xBall;
    }

    public void setxBall(double xBall) {
        this.xBall = xBall;
    }

    public double getyBall() {
        return yBall;
    }

    public void setyBall(double yBall) {
        this.yBall = yBall;
    }

    public boolean isGoDownBall() {
        return goDownBall;
    }

    public void setGoDownBall(boolean goDownBall) {
        this.goDownBall = goDownBall;
    }

    public boolean isGoRightBall() {
        return goRightBall;
    }

    public void setGoRightBall(boolean goRightBall) {
        this.goRightBall = goRightBall;
    }

    public boolean isCollideToPaddle() {
        return collideToPaddle;
    }

    public void setCollideToPaddle(boolean collideToPaddle) {
        this.collideToPaddle = collideToPaddle;
    }

    public boolean isCollideToPaddleAndMoveToRight() {
        return collideToPaddleAndMoveToRight;
    }

    public void setCollideToPaddleAndMoveToRight(boolean collideToPaddleAndMoveToRight) {
        this.collideToPaddleAndMoveToRight = collideToPaddleAndMoveToRight;
    }

    public boolean isCollideToRightWall() {
        return collideToRightWall;
    }

    public void setCollideToRightWall(boolean collideToRightWall) {
        this.collideToRightWall = collideToRightWall;
    }

    public boolean isCollideToLeftWall() {
        return collideToLeftWall;
    }

    public void setCollideToLeftWall(boolean collideToLeftWall) {
        this.collideToLeftWall = collideToLeftWall;
    }

    public boolean isCollideToRightBlock() {
        return collideToRightBlock;
    }

    public void setCollideToRightBlock(boolean collideToRightBlock) {
        this.collideToRightBlock = collideToRightBlock;
    }

    public boolean isCollideToBottomBlock() {
        return collideToBottomBlock;
    }

    public void setCollideToBottomBlock(boolean collideToBottomBlock) {
        this.collideToBottomBlock = collideToBottomBlock;
    }

    public boolean isCollideToLeftBlock() {
        return collideToLeftBlock;
    }

    public void setCollideToLeftBlock(boolean collideToLeftBlock) {
        this.collideToLeftBlock = collideToLeftBlock;
    }

    public boolean isCollideToTopBlock() {
        return collideToTopBlock;
    }

    public void setCollideToTopBlock(boolean collideToTopBlock) {
        this.collideToTopBlock = collideToTopBlock;
    }

    public double getvX() {
        return vX;
    }

    public void setvX(double vX) {
        this.vX = vX;
    }

    public double getvY() {
        return vY;
    }

    public void setvY(double vY) {
        this.vY = vY;
    }

    public void setGameLogicHandler(GameLogicHandler gameLogicHandler) {
        this.gameLogicHandler = gameLogicHandler;
    }

}

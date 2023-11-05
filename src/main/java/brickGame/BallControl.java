package brickGame;

import javafx.application.Platform;

import static brickGame.Main.*;

public class BallControl {

    public static boolean goDownBall                  = true;
    public static boolean goRightBall                 = true;
    public static boolean collideToBreak               = false;
    public static boolean collideToBreakAndMoveToRight = true;
    public static boolean collideToRightWall           = false;
    public static boolean collideToLeftWall            = false;

    public static boolean collideToRightBlock          = false;
    public static boolean collideToBottomBlock         = false;
    public static boolean collideToLeftBlock           = false;
    public static boolean collideToTopBlock            = false;

    public static double vX = 1.000;
    public static double vY = 1.000;



    public static void resetcollideFlags() {

        collideToBreak = false;
        collideToBreakAndMoveToRight = false;
        collideToRightWall = false;
        collideToLeftWall = false;

        collideToRightBlock = false;
        collideToBottomBlock = false;
        collideToLeftBlock = false;
        collideToTopBlock = false;

        ballOutOfBounds = false;
    }
    public static boolean ballOutOfBounds = false; // Add this flag
    public static void setPhysicsToBall(Main main) {
        //v = ((time - hitTime) / 1000.000) + 1.000;

        if (goDownBall) {
            yBall += vY;
        } else {
            yBall -= vY;
        }

        if (goRightBall) {
            xBall += vX;
        } else {
            xBall -= vX;
        }

        if (yBall <= 0) {
            //vX = 1.000;
            resetcollideFlags();
            goDownBall = true;
        //    return;
        }
        if (yBall >= sceneHeight) {
            Platform.runLater(() -> {
                goDownBall = false;
                resetcollideFlags();
            });
                if (!isGoldStatus) {
                    //TODO gameover
                    heart = heart - 1;
                    ballOutOfBounds = true;
                    new Score(main).show(sceneWidth / 2, sceneHeight / 2, -1);

                    if (heart == 0) {
                        new Score(main).showGameOver();
                        engine.stop();
                    }

            }
           // return;
        }

        if (yBall >= yBreak - ballRadius) {
            //System.out.println("collide1");
            if (xBall >= xBreak && xBall <= xBreak + breakWidth) {
                hitTime = time;
                resetcollideFlags();
                collideToBreak = true;
                goDownBall = false;

                double relation = (xBall - centerBreakX) / (breakWidth / 2);

                if (Math.abs(relation) <= 0.3) {
                    //vX = 0;
                    vX = Math.abs(relation);
                } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
                    vX = (Math.abs(relation) * 1.5) + (level / 3.500);
                    //System.out.println("vX " + vX);
                } else {
                    vX = (Math.abs(relation) * 2) + (level / 3.500);
                    //System.out.println("vX " + vX);
                }

                if (xBall - centerBreakX > 0) {
                    collideToBreakAndMoveToRight = true;
                } else {
                    collideToBreakAndMoveToRight = false;
                }
                //System.out.println("collide2");
            }
        }

        if (xBall >= sceneWidth) {
            resetcollideFlags();
            //vX = 1.000;
            collideToRightWall = true;
        }

        if (xBall <= 0) {
            resetcollideFlags();
            //vX = 1.000;
            collideToLeftWall = true;
        }

        if (collideToBreak) {
            if (collideToBreakAndMoveToRight) {
                goRightBall = true;
            } else {
                goRightBall = false;
            }
        }

        //Wall collide

        if (collideToRightWall) {
            goRightBall = false;
        }

        if (collideToLeftWall) {
            goRightBall = true;
        }

        //Block collide

        if (collideToRightBlock) {
            goRightBall = true;
        }
        //solve logic error
        if (collideToLeftBlock) {
            goRightBall = false;
        }

        if (collideToTopBlock) {
            goDownBall = false;
        }

        if (collideToBottomBlock) {
            goDownBall = true;
        }


    }

}

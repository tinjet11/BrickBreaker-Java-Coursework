package brickGame;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;

import static brickGame.GameStateManager.gameState;
import static brickGame.Main.*;

public class BallControl {
    public static Circle ball;
    public static double xBall;
    public static double yBall;

    public static boolean goDownBall                  = true;
    public static boolean goRightBall                 = true;
    public static boolean collideToPaddle = false;
    public static boolean collideToPaddleAndMoveToRight = true;
    public static boolean collideToRightWall           = false;
    public static boolean collideToLeftWall            = false;

    public static boolean collideToRightBlock          = false;
    public static boolean collideToBottomBlock         = false;
    public static boolean collideToLeftBlock           = false;
    public static boolean collideToTopBlock            = false;

    public static double vX = 1.000;
    public static double vY = 1.000;

    private static MenuController menuController;

    public static void resetcollideFlags() {

        collideToPaddle = false;
        collideToPaddleAndMoveToRight = false;
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
        if (yBall >= SCENE_HEIGHT) {
            Platform.runLater(() -> {
                goDownBall = false;
                resetcollideFlags();
            });
            if (!isGoldStatus) {
                    //TODO gameover
                    heart = heart - 1;
                    ballOutOfBounds = true;
                    new Score(main).show(SCENE_WIDTH / 2, SCENE_HEIGHT / 2, -1);

                    if (heart <= 0) {
                    //  new Score(main).showGameOver();
                        engine.stop();

                        try{
                            isGameRun = false;
                            gameState =GameStateManager.GameState.GAME_OVER;
                            FXMLLoader fxmlLoader = new FXMLLoader(main.getClass().getResource("fxml/Menu.fxml"));
                            fxmlLoader.setControllerFactory(c -> {
                                return menuController = new MenuController(main,primaryStage);
                            });
                            Scene winMenuScene= new Scene(fxmlLoader.load());

                            menuController.showMenuScene("Lose",winMenuScene);


                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
            }
           // return;
        }

        if (yBall >= yPaddle - BALL_RADIUS) {
            //System.out.println("collide1");
            if (xBall >= xPaddle && xBall <= xPaddle + PADDLE_WIDTH) {
                hitTime = time;
                resetcollideFlags();
                collideToPaddle = true;
                goDownBall = false;

                double relation = (xBall - centerPaddleX) / (PADDLE_WIDTH / 2);

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

                if (xBall - centerPaddleX > 0) {
                    collideToPaddleAndMoveToRight = true;
                } else {
                    collideToPaddleAndMoveToRight = false;
                }
                //System.out.println("collide2");
            }
        }

        if (xBall >= SCENE_WIDTH) {
            resetcollideFlags();
            //vX = 1.000;
            collideToRightWall = true;
        }

        if (xBall <= 0) {
            resetcollideFlags();
            //vX = 1.000;
            collideToLeftWall = true;
        }

        if (collideToPaddle) {
            if (collideToPaddleAndMoveToRight) {
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

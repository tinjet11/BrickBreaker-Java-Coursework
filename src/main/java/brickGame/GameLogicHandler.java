package brickGame;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import static brickGame.BallControl.*;
import static brickGame.GameStateManager.gameSceneController;
import static brickGame.Main.*;
import static brickGame.InitGameComponent.*;

public class GameLogicHandler implements Actionable {

    BallControl ballControl;
    public static int heart = 10;
    public static int initialHeart = 3;

    public static int score = 0;
    public static long time = 0;
    public static long hitTime = 0;
    public static long goldTime = 0;
    public static boolean isGameRun = false;
    public static int endLevel = 18;

    public static int level = 1;
    public static boolean isGoldStatus = false;

    public static int remainingBlockCount = 0;

    public  GameLogicHandler(){
        ballControl = new BallControl();
    }

    @Override
    public void onUpdate() {
        Platform.runLater(() -> {
                    gameSceneController.setScoreLabel("Score: " + score);
                    gameSceneController.setHeartLabel("Heart : " + heart);

                    rect.setX(xPaddle);
                    rect.setY(yPaddle);
                    ball.setCenterX(xBall);
                    ball.setCenterY(yBall);
                }
        );

        if (yBall >= Block.getPaddingTop() && yBall <= (Block.getHeight() * (level + 1)) + Block.getPaddingTop()) {
            for (final Block block : blocks) {
                try {
                    int hitCode = block.checkHitToBlock(xBall, yBall);
                    if (hitCode != Block.NO_HIT) {
                        score += 1;

                        new Score().show(block.x, block.y, 1);

                        block.rect.setVisible(false);
                        block.isDestroyed = true;
                        remainingBlockCount--;
                        resetcollideFlags();

                        if (block.type == Block.BLOCK_CHOCO) {
                            final Bonus choco = new Bonus(block.row, block.column);
                            choco.setTimeCreated(time);
                            Platform.runLater(() -> root.getChildren().add(choco.choco));

                            chocos.add(choco);
                        }

                        if (block.type == Block.BLOCK_STAR) {
                            Platform.runLater(() -> {
                                goldTime = time;
                                ball.setFill(new ImagePattern(new Image("goldball.png")));
                                System.out.println("gold ball");
                                root.getStyleClass().add("goldRoot");
                                isGoldStatus = true;
                            });
                        }

                        if (block.type == Block.BLOCK_HEART) {
                            heart++;
                            System.out.println("heart hitted");
                        }

                        Platform.runLater(() -> {
                            if (hitCode == Block.HIT_RIGHT) {
                                collideToRightBlock = true;
                            } else if (hitCode == Block.HIT_BOTTOM) {
                                collideToBottomBlock = true;
                            } else if (hitCode == Block.HIT_LEFT) {
                                collideToLeftBlock = true;
                            } else if (hitCode == Block.HIT_TOP) {
                                collideToTopBlock = true;
                            }
                        });
                    }

                    //TODO hit to break and some work here....
                    //System.out.println("Break in row:" + block.row + " and column:" + block.column + " hit");

                } catch (Exception e) {
                    e.printStackTrace();

                    System.out.println("Onupdate function");
                    break;
                }
            }

        }
    }


    @Override
    public void onInit() {


    }




    private void checkDestroyedCount() {
        //System.out.println(remainingBlockCount);
        if (remainingBlockCount == 0 && level != endLevel) {
            System.out.println("Level Up!");
            gameStateManager.nextLevel();
        }
    }
    @Override
    public void onPhysicsUpdate() {
        if (level != endLevel) {

            checkDestroyedCount();

        }
        ballControl.setPhysicsToBall();

        if (time - goldTime > 5000) {
            Platform.runLater(() -> {
                ball.setFill(new ImagePattern(new Image("ball.png")));
                root.getStyleClass().remove("goldRoot");
                isGoldStatus = false;
            });

        }
        //this part is about the Bonus object
        for (Bonus choco : chocos) {
            if (choco.getY() > SCENE_HEIGHT || choco.isTaken()) {
                continue;
            }

            if (choco.getY() >= yPaddle && choco.getY() <= yPaddle + PADDLE_HEIGHT && choco.getX() >= xPaddle && choco.getX() <= xPaddle + PADDLE_WIDTH) {
                System.out.println("You Got it and +3 score for you");
                choco.setTaken(true);
                choco.choco.setVisible(false);
                System.out.println("choco hited");
                score += 3;
                new Score().show(choco.getX(), choco.getY(), 3);
            }
            choco.setY(choco.getY() +((time - choco.getTimeCreated()) / 1000.000) + 1.000);
            Platform.runLater(() -> {
                // Update UI to reflect the new position
                choco.choco.setY(choco.getY());
            });
        }
    }


    @Override
    public void onTime(long newTime) {
        time = newTime;
    }
}

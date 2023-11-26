package brickGame;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import static brickGame.Main.*;
import static brickGame.InitGameComponent.*;

public class GameLogicHandler implements Actionable {
    private static GameLogicHandler instance;

    private GameLogicHandler() {
    }

    public static GameLogicHandler getInstance() {
        if (instance == null) {
            instance = new GameLogicHandler();
        }
        return instance;
    }

    private BallControl ballControl;
    private GameSceneController gameSceneController;

    private GameStateManager gameStateManager;

    public void setGameSceneController(GameSceneController gameSceneController) {this.gameSceneController = gameSceneController;}
    public void setBallControl(BallControl ballControl) {
        this.ballControl = ballControl;
    }

    public void setGameStateManager(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
    }

    private int heart = 1000;
    private int initialHeart = 3;

    private int score = 0;
    private long time = 0;
    private long hitTime = 0;
    private long goldTime = 0;
    private boolean isGameRun = false;
    private int endLevel = 18;

    private int level = 11;
    private boolean isGoldStatus = false;

    private int remainingBlockCount = 0;


    @Override
    public void onUpdate() {
        Platform.runLater(() -> {
                    gameSceneController.setScoreLabel("Score: " + getScore());
                    gameSceneController.setHeartLabel("Heart : " + getHeart());

                    paddle.setX(xPaddle);
                    paddle.setY(yPaddle);
                    ballControl.getBall().setCenterX(ballControl.getxBall());
                    ballControl.getBall().setCenterY(ballControl.getyBall());
                }
        );

        if (ballControl.getyBall() >= Block.getPaddingTop() && ballControl.getyBall() <= (Block.getHeight() * (getLevel() + 1)) + Block.getPaddingTop()) {
            for (final Block block : blocks) {
                try {
                    Block.HIT_STATE hitCode = block.checkHitToBlock(ballControl.getxBall(), ballControl.getyBall());
                    if (hitCode != Block.HIT_STATE.NO_HIT) {

                        setScore(getScore() + 1);

                        new Score().show(block.x, block.y, 1);

                        block.rect.setVisible(false);
                        block.isDestroyed = true;
                        remainingBlockCount--;
                        ballControl.resetcollideFlags();

                        if (block.type == Block.BLOCK_TYPE.BLOCK_CHOCO) {
                            final Bonus choco = new Bonus(block.row, block.column);
                            choco.setTimeCreated(getTime());
                            Platform.runLater(() -> root.getChildren().add(choco.choco));

                            chocos.add(choco);
                        }

                        if (block.type == Block.BLOCK_TYPE.BLOCK_STAR) {
                            Platform.runLater(() -> {
                                setGoldTime(getTime());
                                ballControl.getBall().setFill(new ImagePattern(new Image("goldball.png")));
                                System.out.println("gold ball");
                                root.getStyleClass().add("goldRoot");
                                setGoldStatus(true);
                            });
                        }

                        if (block.type == Block.BLOCK_TYPE.BLOCK_HEART) {
                            heart++;
                            System.out.println("heart hitted");
                        }

                        Platform.runLater(() -> {
                            if (hitCode == Block.HIT_STATE.HIT_RIGHT) {
                                ballControl.setCollideToRightBlock(true);
                            } else if (hitCode == Block.HIT_STATE.HIT_BOTTOM) {
                                ballControl.setCollideToBottomBlock(true);
                            } else if (hitCode == Block.HIT_STATE.HIT_LEFT) {
                                ballControl.setCollideToLeftBlock(true);
                            } else if (hitCode == Block.HIT_STATE.HIT_TOP) {
                                ballControl.setCollideToTopBlock(true);
                            }
                        });
                    }
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

    public void deductHeart() {
        heart = heart - 1;
        new Score().show(SCENE_WIDTH / 2, SCENE_HEIGHT / 2, -1);
        System.out.println("heart: " + heart);

        if (heart <= 0) {
            gameSceneController.showLoseScene();
        }
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
        if (level != endLevel && isGameRun) {
            checkDestroyedCount();
        }
        ballControl.setPhysicsToBall();

        if (time - goldTime > 5000) {
            Platform.runLater(() -> {
                ballControl.getBall().setFill(new ImagePattern(new Image("ball.png")));
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
                choco.setTaken(true);
                choco.choco.setVisible(false);
                setScore(getScore() + 3);
                new Score().show(choco.getX(), choco.getY(), 3);
            }
            choco.setY(choco.getY() + ((getTime() - choco.getTimeCreated()) / 1000.000) + 1.000);
            Platform.runLater(() -> {
                choco.choco.setY(choco.getY());
            });
        }
    }


    @Override
    public void onTime(long newTime) {
        setTime(newTime);
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public int getInitialHeart() {
        return initialHeart;
    }

    public void setInitialHeart(int initialHeart) {
        this.initialHeart = initialHeart;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getHitTime() {
        return hitTime;
    }

    public void setHitTime(long hitTime) {
        this.hitTime = hitTime;
    }

    public long getGoldTime() {
        return goldTime;
    }

    public void setGoldTime(long goldTime) {
        this.goldTime = goldTime;
    }

    public boolean isGameRun() {
        return isGameRun;
    }

    public void setGameRun(boolean gameRun) {
        isGameRun = gameRun;
    }

    public int getEndLevel() {
        return endLevel;
    }

    public void setEndLevel(int endLevel) {
        this.endLevel = endLevel;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void addLevel() {
        this.level += 1;
    }

    public boolean isGoldStatus() {
        return isGoldStatus;
    }

    public void setGoldStatus(boolean goldStatus) {
        isGoldStatus = goldStatus;
    }

    public int getRemainingBlockCount() {
        return remainingBlockCount;
    }

    public void setRemainingBlockCount(int remainingBlockCount) {
        this.remainingBlockCount = remainingBlockCount;
    }


}

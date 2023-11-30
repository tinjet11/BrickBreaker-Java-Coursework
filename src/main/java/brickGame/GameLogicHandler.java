package brickGame;

import brickGame.Controller.GameSceneController;
import brickGame.DroppableItem.Bonus;
import brickGame.DroppableItem.Penalty;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;

import java.util.Iterator;

import static brickGame.Block.BLOCK_HEIGHT;
import static brickGame.Block.BLOCK_PADDING_TOP;


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
    private InitGameComponent initGameComponent;
    private GameStateManager gameStateManager;

    public void setGameSceneController(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    public void setBallControl(BallControl ballControl) {
        this.ballControl = ballControl;
    }

    public void setGameStateManager(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
    }

    private int heart = 3;
    private int initialHeart = 3;

    private int score = 0;
    private long time = 0;
    private long hitTime = 0;
    private long goldTime = 0;
    private boolean isGameRun = false;
    private int endLevel = 21;
    private int level = 18;
    private boolean isGoldStatus = false;
    private int remainingBlockCount = 0;

    private GameEngine engine;


    private void onHitAction(Block block) {
        setScore(getScore() + 1);
        new ScoreAnimation(gameSceneController.getGamePane()).show(block.getX(), block.getY(), 1);
        block.getRect().setVisible(false);
        block.setDestroyed(true);
        remainingBlockCount--;
    }

    @Override
    public void onUpdate() {
        Platform.runLater(() -> {
            gameSceneController.setScoreLabel("Score: " + getScore());
            gameSceneController.setHeartLabel("Heart : " + getHeart());

            initGameComponent.getPaddle().setX(initGameComponent.getxPaddle());
            initGameComponent.getPaddle().setY(initGameComponent.getyPaddle());

            ballControl.getBall().setCenterX(ballControl.getxBall());
            ballControl.getBall().setCenterY(ballControl.getyBall());
        });

        if (ballControl.getyBall() >= BLOCK_PADDING_TOP && ballControl.getyBall() <= (BLOCK_HEIGHT * (getLevel() + 1)) + BLOCK_PADDING_TOP) {
            Iterator<Block> iterator = initGameComponent.getBlocks().iterator();
            while (iterator.hasNext()) {
                Block block = iterator.next();
                try {
                    Block.HIT_STATE hitCode = block.checkHitToBlock(ballControl.getxBall(), ballControl.getyBall());
                    if (hitCode != Block.HIT_STATE.NO_HIT) {

                        ballControl.resetcollideFlags();

                        if (block.getType() == Block.BLOCK_TYPE.BLOCK_CHOCO) {
                            onHitAction(block);
                            final Bonus choco = new Bonus(block.getRow(), block.getColumn());
                            choco.setTimeCreated(getTime());
                            Platform.runLater(() -> gameSceneController.getGamePane().getChildren().add(choco.element));

                            initGameComponent.getChocos().add(choco);
                            iterator.remove();
                        }
                        //new bomb
                        if (block.getType() == Block.BLOCK_TYPE.BLOCK_BOMB) {
                            onHitAction(block);
                            final Penalty bomb = new Penalty(block.getRow(), block.getColumn());
                            bomb.setTimeCreated(getTime());
                            Platform.runLater(() -> gameSceneController.getGamePane().getChildren().add(bomb.element));

                            initGameComponent.getBombs().add(bomb);
                            iterator.remove();
                        }

                        if (block.getType() == Block.BLOCK_TYPE.BLOCK_STAR) {
                            onHitAction(block);
                            setGoldTime(getTime());

                            if (!isGoldStatus) {
                                Platform.runLater(() -> {
                                    ballControl.getBall().setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/images/goldball.png"))));
                                    System.out.println("gold ball");
                                    gameSceneController.getGamePane().getStyleClass().add("goldRoot");
                                });
                            }
                            setGoldStatus(true);
                            iterator.remove();
                        }

                        if (block.getType() == Block.BLOCK_TYPE.BLOCK_HEART) {
                            onHitAction(block);
                            heart++;
                            System.out.println("heart hitted");
                            iterator.remove();
                        }

                        if (block.getType() == Block.BLOCK_TYPE.BLOCK_CONCRETE) {
                            System.out.println("Concrete hit");
                            Platform.runLater(() -> {
                                block.setDestroyed(true);
                                block.setType(Block.BLOCK_TYPE.BLOCK_NORMAL);

                                Image image = new Image(getClass().getResourceAsStream("/images/brick.jpg"));
                                ImagePattern imagePattern = new ImagePattern(image);

                                block.getRect().setFill(imagePattern);
                                System.out.println("Block replaced with normal block");

                                PauseTransition pause = new PauseTransition(Duration.millis(100));
                                pause.setOnFinished(event -> {
                                    block.setDestroyed(false);
                                    System.out.println("isDestroyed set to false");
                                });
                                pause.play();
                            });
                        }


                        if (block.getType() == Block.BLOCK_TYPE.BLOCK_NORMAL) {
                            System.out.println("Normal hit");
                            onHitAction(block);
                            iterator.remove();
                        }


                        if (hitCode == Block.HIT_STATE.HIT_RIGHT) {
                            ballControl.setCollideToRightBlock(true);
                        } else if (hitCode == Block.HIT_STATE.HIT_BOTTOM) {
                            ballControl.setCollideToBottomBlock(true);
                        } else if (hitCode == Block.HIT_STATE.HIT_LEFT) {
                            ballControl.setCollideToLeftBlock(true);
                        } else if (hitCode == Block.HIT_STATE.HIT_TOP) {
                            ballControl.setCollideToTopBlock(true);
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    System.out.println("Onupdate function");
                    break;
                }
            }
        }
    }

    public void stopEngine() {
        engine.stop();
    }

    public void startEngine() {
        engine.start();
    }

    public void setUpEngine() {
        engine = new GameEngine();
        engine.setOnAction(this);
        engine.setFps(120);
        startEngine();
    }


    @Override
    public void onInit() {

    }

    public void deductHeart() {
        heart = heart - 1;
        new ScoreAnimation(gameSceneController.getGamePane()).show(initGameComponent.getSCENE_WIDTH() / 2, initGameComponent.getSCENE_HEIGHT() / 2, -1);

        System.out.println("heart: " + heart);

        if (heart <= 0) {
            gameSceneController.showLoseScene();
            stopEngine();
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
        Platform.runLater(() -> {
            ballControl.setPhysicsToBall();
        });

        if (time - goldTime > 5000 && isGoldStatus) {
            Platform.runLater(() -> {
                System.out.println("Inside Platform.runLater");
                System.out.println("Before modification: " + gameSceneController.getGamePane().getStyleClass());

                ballControl.getBall().setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/images/ball.png"))));
                gameSceneController.getGamePane().getStyleClass().remove("goldRoot");
                setGoldStatus(false);

                System.out.println("After modification: " + gameSceneController.getGamePane().getStyleClass());
                System.out.println("gold removed");
                System.out.println("gold root remove. time: " + getTime());
                System.out.println("gold root remove. gold time: " + getGoldTime());

            });
        }

        //this part is about the Bonus object
        Iterator<Bonus> iterator = initGameComponent.getChocos().iterator();
        while (iterator.hasNext()) {
            Bonus choco = iterator.next();

            if (choco.getY() > initGameComponent.getSCENE_HEIGHT() || choco.isTaken()) {
                continue;
            }
            if (choco.getY() >= initGameComponent.getyPaddle() && choco.getY() <= initGameComponent.getyPaddle() + initGameComponent.getPADDLE_HEIGHT() && choco.getX() >= initGameComponent.getxPaddle() && choco.getX() <= initGameComponent.getxPaddle() + initGameComponent.getPADDLE_WIDTH()) {

                if (choco.element != null) {
                    choco.setTaken(true);
                    choco.element.setVisible(false);
                    setScore(getScore() + 3);
                    new ScoreAnimation(gameSceneController.getGamePane()).show(choco.getX(), choco.getY(), 3);
                    iterator.remove();  // Use the iterator to remove the element
                    continue;
                }
            }
            if (choco.element != null) {
                choco.setY(choco.getY() + ((getTime() - choco.getTimeCreated()) / 1000.000) + 1.000);
                Platform.runLater(() -> {
                    choco.element.setY(choco.getY());
                });
            }
        }

        //this part is about the Penalty object
        Iterator<Penalty> penaltyIterator = initGameComponent.getBombs().iterator();
        while (penaltyIterator.hasNext()) {
            Penalty bomb = penaltyIterator.next();

            if (bomb.getY() > initGameComponent.getSCENE_HEIGHT()) {
                setScore(getScore() - 10);
                new ScoreAnimation(gameSceneController.getGamePane()).show(initGameComponent.getSCENE_WIDTH() / 2, initGameComponent.getSCENE_HEIGHT() / 2, -10);
                penaltyIterator.remove();
                continue;
            }

            if (bomb.getY() >= initGameComponent.getyPaddle()
                    && bomb.getY() <= initGameComponent.getyPaddle() + initGameComponent.getPADDLE_HEIGHT()
                    && bomb.getX() >= initGameComponent.getxPaddle()
                    && bomb.getX() <= initGameComponent.getxPaddle() + initGameComponent.getPADDLE_WIDTH()) {

                if (bomb.element != null) {
                    bomb.setTaken(true);
                    bomb.element.setVisible(false);
                    penaltyIterator.remove();  // Use the iterator to remove the element
                    continue;
                }
            }
            if (bomb.element != null) {
                bomb.setY(bomb.getY() + ((getTime() - bomb.getTimeCreated()) / 1000.000) + 1.000);
                Platform.runLater(() -> {
                    bomb.element.setY(bomb.getY());
                });
            }

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


    public void setInitGameComponent(InitGameComponent initGameComponent) {
        this.initGameComponent = initGameComponent;
    }


}

package brickGame;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;

import java.util.Iterator;



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

    public void setGameSceneController(GameSceneController gameSceneController) {this.gameSceneController = gameSceneController;}
    public void setBallControl(BallControl ballControl) {
        this.ballControl = ballControl;
    }

    public void setGameStateManager(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
    }

    private int heart = 1000;
    private int initialHeart = 1000;

    private int score = 0;
    private long time = 0;
    private long hitTime = 0;
    private long goldTime = 0;
    private boolean isGameRun = false;
    private int endLevel = 18;
    private int level = 10;
    private boolean isGoldStatus = false;
    private int remainingBlockCount = 0;

    private GameEngine engine;



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

        if (ballControl.getyBall() >= Block.getPaddingTop() && ballControl.getyBall() <= (Block.getHeight() * (getLevel() + 1)) + Block.getPaddingTop()) {
            Iterator<Block> iterator = initGameComponent.getBlocks().iterator();
            while (iterator.hasNext()) {
                Block block = iterator.next();
                try {
                    Block.HIT_STATE hitCode = block.checkHitToBlock(ballControl.getxBall(), ballControl.getyBall());
                    if (hitCode != Block.HIT_STATE.NO_HIT) {

                        setScore(getScore() + 1);

                        new Score(gameSceneController.getGamePane()).show(block.x, block.y, 1);

                        block.rect.setVisible(false);
                        block.isDestroyed = true;
                        remainingBlockCount--;
                        ballControl.resetcollideFlags();

                        if (block.type == Block.BLOCK_TYPE.BLOCK_CHOCO) {
                            final Bonus choco = new Bonus(block.row, block.column);
                            choco.setTimeCreated(getTime());
                            Platform.runLater(() -> gameSceneController.getGamePane().getChildren().add(choco.element));

                            initGameComponent.getChocos().add(choco);
                        }
                        //new bomb
                        if (block.type == Block.BLOCK_TYPE.BLOCK_BOMB) {
                            final Penalty bomb = new Penalty(block.row, block.column);
                            bomb.setTimeCreated(getTime());
                            Platform.runLater(() -> gameSceneController.getGamePane().getChildren().add(bomb.element));

                            initGameComponent.getBombs().add(bomb);
                        }

                        if (block.type == Block.BLOCK_TYPE.BLOCK_STAR) {
                            setGoldTime(getTime());

                            if(!isGoldStatus){
                                Platform.runLater(() -> {
                                    ballControl.getBall().setFill(new ImagePattern(new Image("goldball.png")));
                                    System.out.println("gold ball");
                                    gameSceneController.getGamePane().getStyleClass().add("goldRoot");
                                });
                            }

                                setGoldStatus(true);
                        }

                        if (block.type == Block.BLOCK_TYPE.BLOCK_HEART) {
                            heart++;
                            System.out.println("heart hitted");
                        }

                     //  Platform.runLater(() -> {
                            if (hitCode == Block.HIT_STATE.HIT_RIGHT) {
                                ballControl.setCollideToRightBlock(true);
                            } else if (hitCode == Block.HIT_STATE.HIT_BOTTOM) {
                                ballControl.setCollideToBottomBlock(true);
                            } else if (hitCode == Block.HIT_STATE.HIT_LEFT) {
                                ballControl.setCollideToLeftBlock(true);
                            } else if (hitCode == Block.HIT_STATE.HIT_TOP) {
                                ballControl.setCollideToTopBlock(true);
                            }
                    //    });
                        iterator.remove();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    System.out.println("Onupdate function");
                    break;
                }
            }

        }
    }

    public void stopEngine(){
        engine.stop();
    }

    public void startEngine(){
        engine.start();
    }

    public void setUpEngine(){
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
        new Score(gameSceneController.getGamePane()).show(initGameComponent.getSCENE_WIDTH() / 2, initGameComponent.getSCENE_HEIGHT() / 2, -1);

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
        Platform.runLater(()->{
            ballControl.setPhysicsToBall();
        });


        if (time - goldTime > 5000 && isGoldStatus) {
            Platform.runLater(() -> {
                System.out.println("Inside Platform.runLater");
                System.out.println("Before modification: " + gameSceneController.getGamePane().getStyleClass());

                ballControl.getBall().setFill(new ImagePattern(new Image("ball.png")));
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
            if (choco.getY() >= initGameComponent.getyPaddle() && choco.getY() <=  initGameComponent.getyPaddle() + initGameComponent.getPADDLE_HEIGHT() && choco.getX() >=  initGameComponent.getxPaddle() && choco.getX() <= initGameComponent.getxPaddle() + initGameComponent.getPADDLE_WIDTH()) {

                if (choco.element != null) {
                    choco.setTaken(true);
                    choco.element.setVisible(false);
                    setScore(getScore() + 3);
                    new Score(gameSceneController.getGamePane()).show(choco.getX(), choco.getY(), 3);
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
                new Score(gameSceneController.getGamePane()).show(initGameComponent.getSCENE_WIDTH()/2, initGameComponent.getSCENE_HEIGHT()/2, -10);
                penaltyIterator.remove();
                continue;
            }


            if (bomb.getY() >= initGameComponent.getyPaddle() && bomb.getY() <= initGameComponent.getyPaddle() + initGameComponent.getPADDLE_HEIGHT() && bomb.getX() >=  initGameComponent.getxPaddle() && bomb.getX() <= initGameComponent.getxPaddle() + initGameComponent.getPADDLE_WIDTH()) {

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

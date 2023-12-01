package brickGame.handler;

import brickGame.controller.GameSceneController;
import brickGame.model.*;
import brickGame.model.dropitem.Bonus;
import brickGame.model.dropitem.Bomb;
import brickGame.factory.BlockHitHandlerFactoryProvider;
import brickGame.factory.blockhithandler.BlockHitHandlerFactory;
import brickGame.handler.blockhit.*;
import brickGame.handler.dropitem.BonusDropHandler;
import brickGame.handler.dropitem.BombDropHandler;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import java.util.Iterator;

import static brickGame.model.Block.BLOCK_HEIGHT;
import static brickGame.model.Block.BLOCK_PADDING_TOP;


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

    public GameSceneController getGameSceneController() {
        return gameSceneController;
    }

    private GameSceneController gameSceneController;
    private InitGameComponent initGameComponent;
    private GameStateManager gameStateManager;

    private int heart = 3;
    private int initialHeart = 3;

    private int score = 0;
    private long time = 0;
    //private long hitTime = 0;
    private long goldTime = 0;
    private boolean isGameRun = false;
    private int endLevel = 21;
    private int level = 18;
    private boolean isGoldStatus = false;
    private int remainingBlockCount = 0;

    private GameEngine engine;


    @Override
    public void onUpdate() {
        Platform.runLater(this::updateUI);

        if (isBallWithinGameBounds()) {
            handleBlockCollisions();
        }
    }

    private void updateUI() {
        gameSceneController.setScoreLabel("Score: " + getScore());
        gameSceneController.setHeartLabel("Heart : " + getHeart());

        initGameComponent.getPaddle().setX(initGameComponent.getxPaddle());
        initGameComponent.getPaddle().setY(initGameComponent.getyPaddle());

        ballControl.getBall().setCenterX(ballControl.getxBall());
        ballControl.getBall().setCenterY(ballControl.getyBall());
    }

    private boolean isBallWithinGameBounds() {
        return ballControl.getyBall() >= BLOCK_PADDING_TOP &&
                ballControl.getyBall() <= (BLOCK_HEIGHT * (getLevel() + 1)) + BLOCK_PADDING_TOP;
    }

    private void handleBlockCollisions() {
        Iterator<Block> iterator = initGameComponent.getBlocks().iterator();
        while (iterator.hasNext()) {
            Block block = iterator.next();
            try {
                Block.HIT_STATE hitCode = block.checkHitToBlock(ballControl.getxBall(), ballControl.getyBall());
                if (hitCode != Block.HIT_STATE.NO_HIT) {
                    handleBlockHit(block, hitCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Onupdate function");
                break;
            }
        }
    }

    private void handleBlockHit(Block block, Block.HIT_STATE hitCode) {
        ballControl.resetcollideFlags();

        BlockHitHandlerFactory factory = BlockHitHandlerFactoryProvider.getFactory(block.getType());

        if (factory != null) {
            BlockHitHandler handler = factory.createHandler(gameSceneController, initGameComponent, ballControl);
            handler.handleBlockHit(block, hitCode, this);
        }

        handleCollisionDirection(hitCode);
    }


    private void handleCollisionDirection(Block.HIT_STATE hitCode) {
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

        handleGoldStatus();

        Platform.runLater(() -> ballControl.setPhysicsToBall());

        handleChocos();
        handlePenalties();
    }

    private void handleGoldStatus() {
        if (shouldRemoveGold()) {
            Platform.runLater(() -> {
                ballControl.getBall().setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/images/ball.png"))));
                gameSceneController.getGamePane().getStyleClass().remove("goldRoot");
                setGoldStatus(false);
            });
        }
    }

    private boolean shouldRemoveGold() {
        return time - goldTime > 5000 && isGoldStatus;
    }

    private void handleChocos() {
        Iterator<Bonus> iterator = initGameComponent.getChocos().iterator();
        while (iterator.hasNext()) {
            Bonus choco = iterator.next();

            BonusDropHandler bonusDropHandler = new BonusDropHandler(initGameComponent, gameSceneController, this, choco);

            if (bonusDropHandler.shouldRemove()) {
                iterator.remove();
                continue;
            }

            if (bonusDropHandler.isTaken()) {
                bonusDropHandler.handleTaken();
                iterator.remove();
            } else {
                bonusDropHandler.moveDropItem();
            }
        }
    }

    private void handlePenalties() {
        Iterator<Bomb> iterator = initGameComponent.getBombs().iterator();
        while (iterator.hasNext()) {
            Bomb bomb = iterator.next();
            BombDropHandler bombDropHandler = new BombDropHandler(initGameComponent, gameSceneController, this, bomb);

            if (bombDropHandler.shouldRemove()) {
                bombDropHandler.executePenalty();
                iterator.remove();
                continue;
            }

            if (bombDropHandler.isTaken()) {
                bombDropHandler.handleTaken();
                iterator.remove();
            } else {
                bombDropHandler.moveDropItem();
            }
        }
    }

    public void setGameSceneController(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    public void setBallControl(BallControl ballControl) {
        this.ballControl = ballControl;
    }

    public void setGameStateManager(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
    }

    public void setInitGameComponent(InitGameComponent initGameComponent) {
        this.initGameComponent = initGameComponent;
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

//    public void setHitTime(long hitTime) {
//        this.hitTime = hitTime;
//    }

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

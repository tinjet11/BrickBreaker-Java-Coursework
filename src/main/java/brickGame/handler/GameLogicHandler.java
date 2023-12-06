package brickGame.handler;

import brickGame.Mediator;

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

import static brickGame.Constants.*;
import static brickGame.model.Block.BLOCK_HEIGHT;
import static brickGame.model.Block.BLOCK_PADDING_TOP;

/**
 * Handles the game logic, including collision detection, scoring, and game state management.
 */
public class GameLogicHandler implements Actionable {
    private static GameLogicHandler instance;

    private GameLogicHandler() {
    }

    /**
     * Gets the singleton instance of the GameLogicHandler.
     *
     * @return The singleton instance of the GameLogicHandler.
     */
    public static GameLogicHandler getInstance() {
        if (instance == null) {
            instance = new GameLogicHandler();
        }
        return instance;
    }

    private Mediator mediator;


    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    private int heart = 3;
    private int initialHeart = 3;

    private int score = 0;
    private long time = 0;
    private long goldTime = 0;
    private boolean isGameRun = false;
    private int endLevel = 21;
    private int level = 18;
    private boolean isGoldStatus = false;
    private int remainingBlockCount = 0;

    private GameEngine engine;

    /**
     * Handle the collisions and triggers UI updates.
     */
    @Override
    public void onUpdate() {
        Platform.runLater(this::updateUI);

        if (isBallWithinGameBounds()) {
            handleBlockCollisions();
        }
    }

    /**
     * Updates the UI elements such as score labels, paddle position, and ball position.
     */
    private void updateUI() {
        mediator.getGameSceneController().setScoreLabel("Score: " + getScore());
        mediator.getGameSceneController().setHeartLabel("Heart : " + getHeart());

        mediator.getPaddleInstance().getPaddle().setX(mediator.getPaddleInstance().getxPaddle());
        mediator.getPaddleInstance().getPaddle().setY(mediator.getPaddleInstance().getyPaddle());

        mediator.getBallInstance().getBall().setCenterX(mediator.getBallInstance().getxBall());
        mediator.getBallInstance().getBall().setCenterY(mediator.getBallInstance().getyBall());
    }

    /**
     * Checks if the ball is within the game bounds.
     *
     * @return True if the ball is within the game bounds, false otherwise.
     */
    private boolean isBallWithinGameBounds() {
        return mediator.getBallInstance().getyBall() >= BLOCK_PADDING_TOP &&
                mediator.getBallInstance().getyBall() <= (BLOCK_HEIGHT * (getLevel() + 1)) + BLOCK_PADDING_TOP;
    }

    /**
     * Handles collisions between the ball and the game blocks.
     */
    private void handleBlockCollisions() {
        Iterator<Block> iterator = mediator.getInitGameComponent().getBlocks().iterator();
        while (iterator.hasNext()) {
            Block block = iterator.next();
            try {
                Block.HIT_STATE hitCode = block.checkHitToBlock(mediator.getBallInstance().getxBall(), mediator.getBallInstance().getyBall());
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
    /**
     * Handles the specific hit of the ball on a game block.
     *
     * @param block   The block that was hit.
     * @param hitCode The specific hit state.
     */
    private void handleBlockHit(Block block, Block.HIT_STATE hitCode) {
        mediator.getBallControlHandler().resetcollideFlags();

        BlockHitHandlerFactory factory = BlockHitHandlerFactoryProvider.getFactory(block.getType());

        if (factory != null) {
            BlockHitHandler handler = factory.createHandler(mediator.getGameSceneController(), mediator.getInitGameComponent(), mediator.getBallControlHandler());
            handler.handleBlockHit(block, hitCode, mediator.getGameLogicHandler());
        }

        handleCollisionDirection(hitCode);
    }

    /**
     * Update the flag of collision of ball with block based on the hit state.
     *
     * @param hitCode The specific hit state.
     */
    private void handleCollisionDirection(Block.HIT_STATE hitCode) {
        if (hitCode == Block.HIT_STATE.HIT_RIGHT) {
            mediator.getBallControlHandler().setCollideToRightBlock(true);
        } else if (hitCode == Block.HIT_STATE.HIT_BOTTOM) {
            mediator.getBallControlHandler().setCollideToBottomBlock(true);
        } else if (hitCode == Block.HIT_STATE.HIT_LEFT) {
            mediator.getBallControlHandler().setCollideToLeftBlock(true);
        } else if (hitCode == Block.HIT_STATE.HIT_TOP) {
            mediator.getBallControlHandler().setCollideToTopBlock(true);
        }
    }
    /**
     * Stops the game engine.
     */
    public void stopEngine() {
        engine.stop();
    }
    /**
     * Starts the game engine.
     */
    public void startEngine() {
        engine.start();
    }

    /**
     * Sets up the game engine with specific configurations.
     */
    public void setUpEngine() {
        engine = new GameEngine();
        engine.setOnAction(this);
        engine.setFps(120);
        startEngine();
    }

    @Override
    public void onTime(long newTime) {
        setTime(newTime);
    }

    /**
     * Deducts a heart from the player and updates the UI.
     */
    public void deductHeart() {
        heart = heart - 1;
        new ScoreAnimation(mediator.getGameSceneController().getGamePane()).showScoreAnimation(SCENE_WIDTH / 2, SCENE_HEIGHT / 2, -1);

        System.out.println("heart: " + heart);

        if (heart <= 0) {
            mediator.getGameSceneController().showLoseScene();
            stopEngine();
        }
    }

    /**
     * Checks the count of destroyed blocks and triggers level progression if necessary.
     */
    private void checkDestroyedCount() {
        if (remainingBlockCount == 0 && level != endLevel) {
            mediator.getGameStateManager().nextLevel();
        }
    }


    /**
     * Handles the physics update, including block destruction, gold status, and drop items.
     */
    @Override
    public void onPhysicsUpdate() {
        if (level != endLevel && isGameRun) {
            checkDestroyedCount();
        }

        handleGoldStatus();

        Platform.runLater(() -> mediator.getBallControlHandler().setPhysicsToBall());

        handleChocos();
        handlePenalties();
    }
    /**
     * Handles the removal of gold status based on time.
     */
    private void handleGoldStatus() {
        if (shouldRemoveGold()) {
            Platform.runLater(() -> {
                mediator.getBallInstance().getBall().setFill(new ImagePattern(new Image(getClass().getResourceAsStream(BALL_IMAGE_PATH))));
                mediator.getGameSceneController().getGamePane().getStyleClass().remove(GOLD_ROOT);
                setGoldStatus(false);
            });
        }
    }

    /**
     * Determines whether gold status should be removed based on time.
     * If exceed 5 second from the initialization of gold ball, return true
     * @return True if gold status should be removed, false otherwise.
     */
    private boolean shouldRemoveGold() {
        return time - goldTime > 5000 && isGoldStatus;
    }

    /**
     * Handles choco (bonus) drop items, including removal, scoring, and animation.
     */
    private void handleChocos() {
        Iterator<Bonus> iterator = mediator.getInitGameComponent().getChocos().iterator();
        while (iterator.hasNext()) {
            Bonus choco = iterator.next();

            BonusDropHandler bonusDropHandler = new BonusDropHandler(mediator.getInitGameComponent(), mediator.getGameSceneController(), this, choco);

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
    /**
     * Handles bomb (penalty) drop items, including removal, and penalty execution.
     */
    private void handlePenalties() {
        Iterator<Bomb> iterator = mediator.getInitGameComponent().getBombs().iterator();
        while (iterator.hasNext()) {
            Bomb bomb = iterator.next();
            BombDropHandler bombDropHandler = new BombDropHandler(mediator.getInitGameComponent(), mediator.getGameSceneController(), this, bomb);

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

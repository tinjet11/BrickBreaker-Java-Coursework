package brickGame.handler;

import brickGame.Mediator;

import brickGame.ScoreAnimation;
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
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static brickGame.Constants.*;
import static brickGame.model.Block.BLOCK_HEIGHT;
import static brickGame.model.Block.BLOCK_PADDING_TOP;

/**
 * Handles the game logic, including collision detection, scoring, and call nextLevel function.
 *
 * @author Leong Tin Jet
 * @version 1.0
 */
public class GameLogicHandler implements Actionable {

    /**
     * The singleton instance of the {@code GameLogicHandler} class.
     */
    private static GameLogicHandler instance;

    /**
     * Private constructor to enforce singleton pattern.
     */
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

    /**
     * The mediator to handle communication between different components.
     */
    private Mediator mediator;


    /**
     * The current number of hearts the player has.
     */
    private int heart = 3;

    /**
     * The initial number of hearts the player starts with.
     */
    private int initialHeart = 3;

    /**
     * The player's current score.
     */
    private int score = 0;

    /**
     * The total time elapsed in the game.
     */
    private long time = 0;

    /**
     * The total time elapsed in the game when the gold status is active.
     */
    private long goldTime = 0;

    /**
     * Flag indicating whether the game is currently running.
     */
    private boolean isGameRun = false;

    /**
     * The level at which the game ends.
     */
    private int endLevel = 21;

    /**
     * The current level of the game.
     */
    private int level = 1;

    /**
     * Flag indicating whether the gold status is active.
     */
    private boolean isGoldStatus = false;

    /**
     * The remaining count of unbroken blocks in the current level.
     */
    private int remainingBlockCount = 0;

    /**
     * The GameEngine instance associated with this game logic handler.
     */
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
        return mediator.getBallInstance().getyBall() >= BLOCK_PADDING_TOP && mediator.getBallInstance().getyBall() <= (BLOCK_HEIGHT * (getLevel() + 1)) + BLOCK_PADDING_TOP;
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
            BlockHitHandler handler = factory.createHandler();
            handler.handleBlockHit(block, hitCode, mediator.getGameLogicHandler());
        }

        handleCollisionDirection(hitCode);

        if (mediator.getBallControlHandler().isGoDownBall() == false) {
            handleCollisionDirection(Block.HIT_STATE.HIT_BOTTOM);
        } else {
            handleCollisionDirection(Block.HIT_STATE.HIT_TOP);
        }

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

        if (mediator.getBallControlHandler().isCollideToPaddle()) {
            changePaddleColor();
        }
    }

    /**
     * Flag indicating whether the paddle color can be changed.
     * When set to true, the paddle color can be changed; otherwise, color change is disabled.
     */
    private boolean canChangeColor = true;

    /**
     * The currently selected color for the paddle.
     * It is used to ensure that the next color is different from the last selected color.
     */
    private Color selectedColor;

    /**
     * Changes the color of the paddle if the ability to change color is enabled.
     * The paddle color is randomly selected from a predefined list of colors,
     * excluding the last selected color to ensure variety.
     * The color change operation is executed on the JavaFX application thread using Platform.runLater.
     * This method includes a synchronization mechanism to prevent concurrent color changes.
     *
     * Note: The canChangeColor flag is used to control the ability to change color.
     * If set to true, the method proceeds with color change; otherwise, it skips the operation.
     *
     * @throws Exception If an exception occurs during the color change process.
     */
    private void changePaddleColor() {
        if (canChangeColor) {
            try {
                canChangeColor = false;
                Random RANDOM = new Random();
                // Create a list of available colors excluding the last selected color
                List<Color> availableColors = new ArrayList<>(PADDLE_COLOR_LIST);
                availableColors.remove(selectedColor);

                selectedColor = availableColors.get(RANDOM.nextInt(availableColors.size()));

                Platform.runLater(() -> mediator.getPaddleInstance().getPaddle().setFill(selectedColor));


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                canChangeColor = true;
            }
        }
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
     *
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

            BonusDropHandler bonusDropHandler = new BonusDropHandler(choco);

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
            BombDropHandler bombDropHandler = new BombDropHandler(bomb);

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

    /**
     * Gets the current number of hearts the player has.
     *
     * @return The current number of hearts.
     */
    public int getHeart() {
        return heart;
    }

    /**
     * Sets the current number of hearts the player has.
     *
     * @param heart The new number of hearts.
     */
    public void setHeart(int heart) {
        this.heart = heart;
    }

    /**
     * Gets the initial number of hearts the player starts with.
     *
     * @return The initial number of hearts.
     */
    public int getInitialHeart() {
        return initialHeart;
    }

    /**
     * Sets the initial number of hearts the player starts with.
     *
     * @param initialHeart The new initial number of hearts.
     */
    public void setInitialHeart(int initialHeart) {
        this.initialHeart = initialHeart;
    }

    /**
     * Gets the player's current score.
     *
     * @return The current score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the player's current score.
     *
     * @param score The new score.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Gets the total time elapsed in the game.
     *
     * @return The total time elapsed.
     */
    public long getTime() {
        return time;
    }

    /**
     * Sets the total time elapsed in the game.
     *
     * @param time The new total time elapsed.
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Gets the total time elapsed in the game when the gold status is active.
     *
     * @return The total time elapsed during gold status.
     */
    public long getGoldTime() {
        return goldTime;
    }

    /**
     * Sets the total time elapsed in the game when the gold status is active.
     *
     * @param goldTime The new total time elapsed during gold status.
     */
    public void setGoldTime(long goldTime) {
        this.goldTime = goldTime;
    }

    /**
     * Checks if the game is currently running.
     *
     * @return True if the game is running; otherwise, false.
     */
    public boolean isGameRun() {
        return isGameRun;
    }

    /**
     * Sets the flag indicating whether the game is currently running.
     *
     * @param gameRun The new game run status.
     */
    public void setGameRun(boolean gameRun) {
        isGameRun = gameRun;
    }

    /**
     * Gets the level at which the game ends.
     *
     * @return The level at which the game ends.
     */
    public int getEndLevel() {
        return endLevel;
    }

    /**
     * Sets the level at which the game ends.
     *
     * @param endLevel The new level at which the game ends.
     */
    public void setEndLevel(int endLevel) {
        this.endLevel = endLevel;
    }

    /**
     * Gets the current level of the game.
     *
     * @return The current level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Sets the current level of the game.
     *
     * @param level The new level.
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Increments the current level by 1.
     */
    public void addLevel() {
        this.level += 1;
    }

    /**
     * Checks if the gold status is active.
     *
     * @return True if gold status is active; otherwise, false.
     */
    public boolean isGoldStatus() {
        return isGoldStatus;
    }

    /**
     * Sets the flag indicating whether the gold status is active.
     *
     * @param goldStatus The new gold status.
     */
    public void setGoldStatus(boolean goldStatus) {
        isGoldStatus = goldStatus;
    }

    /**
     * Gets the remaining count of unbroken blocks in the current level.
     *
     * @return The remaining block count.
     */
    public int getRemainingBlockCount() {
        return remainingBlockCount;
    }

    /**
     * Sets the remaining count of unbroken blocks in the current level.
     *
     * @param remainingBlockCount The new remaining block count.
     */
    public void setRemainingBlockCount(int remainingBlockCount) {
        this.remainingBlockCount = remainingBlockCount;
    }

    /**
     * Sets the mediator to handle communication between different components.
     *
     * @param mediator The mediator instance.
     */
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

}

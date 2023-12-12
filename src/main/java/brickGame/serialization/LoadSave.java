package brickGame.serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import static brickGame.Constants.SAVE_PATH;


/**
 * The LoadSave class provides functionality for loading and saving game states.
 * It reads and writes the game state to a serialized file, allowing players to continue their progress later.
 * The class includes methods for destroying the save file, checking its existence, and reading the saved data.
 * <p>
 * The saved game state includes information such as the level, score, heart count, remaining block count,
 * ball and paddle positions, time, gold status, and collision states.
 * </p>
 * <p>
 * The class uses an ArrayList of {@link BlockSerialize} objects to represent the state of each block in the game.
 * </p>
 * <p>
 * This class is part of the game's serialization mechanism and implements methods for managing the save file.
 * </p>
 *
 * <br>
 * <a href="https://github.com/kooitt/CourseworkGame/blob/master/src/main/java/brickGame/LoadSave.java">Original Code</a>
 * <br>
 * <a href="https://github.com/tinjet11/COMP2042_CW_hcytl1/blob/master/src/main/java/brickGame/serialization/LoadSave.java">Current Code</a>
 * @author Leong Tin Jet
 * @version 1.0
 */
public class LoadSave {
    /**
     * Flag indicating whether a heart block exists in the game.
     */
    private boolean isExistHeartBlock;

    /**
     * Flag indicating whether the game is in gold status.
     */
    private boolean isGoldStatus;

    /**
     * Flag indicating the downward movement of the ball.
     */
    private boolean goDownBall;

    /**
     * Flag indicating the rightward movement of the ball.
     */
    private boolean goRightBall;

    /**
     * Flag indicating collision with the paddle.
     */
    private boolean collideToPaddle;

    /**
     * Flag indicating collision at the top of the paddle, causing the ball to move to the right.
     */
    private boolean collideTopPaddleAndMoveToRight;

    /**
     * Flag indicating collision with the right wall.
     */
    private boolean collideToRightWall;

    /**
     * Flag indicating collision with the left wall.
     */
    private boolean collideToLeftWall;

    /**
     * Flag indicating collision with a block on the right side.
     */
    private boolean collideToRightBlock;

    /**
     * Flag indicating collision with the bottom of a block.
     */
    private boolean collideToBottomBlock;

    /**
     * Flag indicating collision with a block on the left side.
     */
    private boolean collideToLeftBlock;

    /**
     * Flag indicating collision with the top of a block.
     */
    private boolean collideToTopBlock;

    /**
     * Current level of the game.
     */
    private int level;

    /**
     * Player's score in the game.
     */
    private int score;

    /**
     * Number of remaining hearts.
     */
    private int heart;

    /**
     * Remaining count of blocks in the game.
     */
    private int remainingBlockCount;

    /**
     * X-coordinate of the ball.
     */
    private double xBall;

    /**
     * Y-coordinate of the ball.
     */
    private double yBall;

    /**
     * X-coordinate of the paddle.
     */
    private double xPaddle;

    /**
     * Y-coordinate of the paddle.
     */
    private double yPaddle;

    /**
     * X-coordinate of the center of the paddle.
     */
    private double centerPaddleX;

    /**
     * The current time in milliseconds.
     */
    private long time;

    /**
     * The time when the gold status is activated in milliseconds.
     */
    private long goldTime;

    /**
     * The velocity of the ball along the x-axis.
     */
    private double vX;

    /**
     * The list of serialized block objects in the game.
     */
    private ArrayList<BlockSerialize> blocks = new ArrayList<BlockSerialize>();


    /**
     * Destroys the existing save game file. This method is called when the player win or lose the game.
     */
    public void destroySaveGameFile() {
        File saveFile = new File(SAVE_PATH);
        if (saveFile.exists()) {
            saveFile.delete();
        }
    }

    /**
     * Checks if the save game file exists.
     *
     * @return True if the save file exists; otherwise, false.
     */
    public boolean checkSaveGameFileExist() {
        File saveFile = new File(SAVE_PATH);
        if (saveFile.exists()) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Reads the saved game state from the serialized file.
     * If the file exists, it reads the data and updates the corresponding fields in the class.
     */
    public void read() {
        File saveFile = new File(SAVE_PATH);
        if (saveFile.exists()) {
            // Proceed with reading the save file
            try {
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File(SAVE_PATH)));


                setLevel(inputStream.readInt());
                setScore(inputStream.readInt());
                setHeart(inputStream.readInt());
                setRemainingBlockCount(inputStream.readInt());


                setxBall(inputStream.readDouble());
                setyBall(inputStream.readDouble());
                setxPaddle(inputStream.readDouble());
                setyPaddle(inputStream.readDouble());
                setCenterPaddleX(inputStream.readDouble());
                setTime(inputStream.readLong());
                setGoldTime(inputStream.readLong());
                setvX(inputStream.readDouble());


                setExistHeartBlock(inputStream.readBoolean());
                setGoldStatus(inputStream.readBoolean());
                setGoDownBall(inputStream.readBoolean());
                setGoRightBall(inputStream.readBoolean());
                setCollideToPaddle(inputStream.readBoolean());
                setCollideTopPaddleAndMoveToRight(inputStream.readBoolean());
                setCollideToRightWall(inputStream.readBoolean());
                setCollideToLeftWall(inputStream.readBoolean());
                setCollideToRightBlock(inputStream.readBoolean());
                setCollideToBottomBlock(inputStream.readBoolean());
                setCollideToLeftBlock(inputStream.readBoolean());
                setCollideToTopBlock(inputStream.readBoolean());


                try {
                    Object obj = inputStream.readObject();
                    if (obj instanceof ArrayList<?>) {
                        setBlocks((ArrayList<BlockSerialize>) obj);
                    }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            // Handle the case when the save file does not exist
            System.out.println("FIle didn't exist in LOAD save function");
        }
    }

    /**
     * Gets the existence status of the heart block.
     *
     * @return True if the heart block exists; otherwise, false.
     */
    public boolean isExistHeartBlock() {
        return isExistHeartBlock;
    }

    /**
     * Sets the existence status of the heart block.
     *
     * @param existHeartBlock The existence status of the heart block in saved file.
     */
    public void setExistHeartBlock(boolean existHeartBlock) {
        isExistHeartBlock = existHeartBlock;
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
     * Sets the gold status.
     *
     * @param goldStatus The new gold status.
     */
    public void setGoldStatus(boolean goldStatus) {
        isGoldStatus = goldStatus;
    }

    /**
     * Checks if the ball is moving down.
     *
     * @return True if the ball is moving down; otherwise, false.
     */
    public boolean isGoDownBall() {
        return goDownBall;
    }

    /**
     * Sets the status of the ball moving down.
     *
     * @param goDownBall The new status of the ball moving down.
     */
    public void setGoDownBall(boolean goDownBall) {
        this.goDownBall = goDownBall;
    }

    /**
     * Checks if the ball is moving to the right.
     *
     * @return True if the ball is moving to the right; otherwise, false.
     */
    public boolean isGoRightBall() {
        return goRightBall;
    }

    /**
     * Sets the status of the ball moving to the right.
     *
     * @param goRightBall The new status of the ball moving to the right.
     */
    public void setGoRightBall(boolean goRightBall) {
        this.goRightBall = goRightBall;
    }

    /**
     * Checks if the ball collides with the paddle.
     *
     * @return True if the ball collides with the paddle; otherwise, false.
     */
    public boolean isCollideToPaddle() {
        return collideToPaddle;
    }

    /**
     * Sets the collision status of the ball with the paddle.
     *
     * @param collideToPaddle The new collision status of the ball with the paddle.
     */
    public void setCollideToPaddle(boolean collideToPaddle) {
        this.collideToPaddle = collideToPaddle;
    }

    /**
     * Checks if the ball collides with the top of the paddle and moves to the right.
     *
     * @return True if the ball collides with the top of the paddle and moves to the right; otherwise, false.
     */
    public boolean isCollideTopPaddleAndMoveToRight() {
        return collideTopPaddleAndMoveToRight;
    }

    /**
     * Sets the collision status of the ball with the top of the paddle and moves to the right.
     *
     * @param collideTopPaddleAndMoveToRight The new collision status of the ball with the top of the paddle and moves to the right.
     */
    public void setCollideTopPaddleAndMoveToRight(boolean collideTopPaddleAndMoveToRight) {
        this.collideTopPaddleAndMoveToRight = collideTopPaddleAndMoveToRight;
    }

    /**
     * Checks if the ball collides with the right wall.
     *
     * @return True if the ball collides with the right wall; otherwise, false.
     */

    public boolean isCollideToRightWall() {
        return collideToRightWall;
    }

    /**
     * Sets the collision status of the ball with the right wall.
     *
     * @param collideToRightWall The new collision status of the ball with the right wall.
     */
    public void setCollideToRightWall(boolean collideToRightWall) {
        this.collideToRightWall = collideToRightWall;
    }

    /**
     * Checks if the ball collides with the left wall.
     *
     * @return True if the ball collides with the left wall; otherwise, false.
     */
    public boolean isCollideToLeftWall() {
        return collideToLeftWall;
    }

    /**
     * Sets the collision status of the ball with the left wall.
     *
     * @param collideToLeftWall The new collision status of the ball with the left wall.
     */
    public void setCollideToLeftWall(boolean collideToLeftWall) {
        this.collideToLeftWall = collideToLeftWall;
    }

    /**
     * Checks if the ball collides with the right block.
     *
     * @return True if the ball collides with the right block; otherwise, false.
     */
    public boolean isCollideToRightBlock() {
        return collideToRightBlock;
    }

    /**
     * Sets the collision status of the ball with the right block.
     *
     * @param collideToRightBlock The new collision status of the ball with the right block.
     */
    public void setCollideToRightBlock(boolean collideToRightBlock) {
        this.collideToRightBlock = collideToRightBlock;
    }

    /**
     * Checks if the ball collides with the bottom block.
     *
     * @return True if the ball collides with the bottom block; otherwise, false.
     */
    public boolean isCollideToBottomBlock() {
        return collideToBottomBlock;
    }

    /**
     * Sets the existence status of the bottom block collision.
     *
     * @param collideToBottomBlock The new existence status of the bottom block collision.
     */
    public void setCollideToBottomBlock(boolean collideToBottomBlock) {
        this.collideToBottomBlock = collideToBottomBlock;
    }

    /**
     * Gets the existence status of the left block collision.
     *
     * @return True if the ball collides with the left block; otherwise, false.
     */
    public boolean isCollideToLeftBlock() {
        return collideToLeftBlock;
    }

    /**
     * Sets the existence status of the left block collision.
     *
     * @param collideToLeftBlock The new existence status of the left block collision.
     */
    public void setCollideToLeftBlock(boolean collideToLeftBlock) {
        this.collideToLeftBlock = collideToLeftBlock;
    }

    /**
     * Gets the existence status of the top block collision.
     *
     * @return True if the ball collides with the top block; otherwise, false.
     */
    public boolean isCollideToTopBlock() {
        return collideToTopBlock;
    }

    /**
     * Sets the existence status of the top block collision.
     *
     * @param collideToTopBlock The new existence status of the top block collision.
     */
    public void setCollideToTopBlock(boolean collideToTopBlock) {
        this.collideToTopBlock = collideToTopBlock;
    }

    /**
     * Gets the current level.
     *
     * @return The current level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Sets the current level.
     *
     * @param level The new level.
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Gets the current score.
     *
     * @return The current score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the current score.
     *
     * @param score The new score.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Gets the current heart count.
     *
     * @return The current heart count.
     */
    public int getHeart() {
        return heart;
    }

    /**
     * Sets the current heart count.
     *
     * @param heart The new heart count.
     */
    public void setHeart(int heart) {
        this.heart = heart;
    }

    /**
     * Gets the remaining block count.
     *
     * @return The remaining block count.
     */
    public int getRemainingBlockCount() {
        return remainingBlockCount;
    }

    /**
     * Sets the remaining block count.
     *
     * @param remainingBlockCount The new remaining block count.
     */
    public void setRemainingBlockCount(int remainingBlockCount) {
        this.remainingBlockCount = remainingBlockCount;
    }

    /**
     * Gets the x-coordinate of the ball.
     *
     * @return The x-coordinate of the ball.
     */
    public double getxBall() {
        return xBall;
    }

    /**
     * Sets the x-coordinate of the ball.
     *
     * @param xBall The new x-coordinate of the ball.
     */
    public void setxBall(double xBall) {
        this.xBall = xBall;
    }

    /**
     * Gets the y-coordinate of the ball.
     *
     * @return The y-coordinate of the ball.
     */
    public double getyBall() {
        return yBall;
    }

    /**
     * Sets the y-coordinate of the ball.
     *
     * @param yBall The new y-coordinate of the ball.
     */
    public void setyBall(double yBall) {
        this.yBall = yBall;
    }

    /**
     * Gets the x-coordinate of the paddle.
     *
     * @return The x-coordinate of the paddle.
     */
    public double getxPaddle() {
        return xPaddle;
    }

    /**
     * Sets the x-coordinate of the paddle.
     *
     * @param xPaddle The new x-coordinate of the paddle.
     */
    public void setxPaddle(double xPaddle) {
        this.xPaddle = xPaddle;
    }

    /**
     * Gets the y-coordinate of the paddle.
     *
     * @return The y-coordinate of the paddle.
     */
    public double getyPaddle() {
        return yPaddle;
    }

    /**
     * Sets the y-coordinate of the paddle.
     *
     * @param yPaddle The new y-coordinate of the paddle.
     */
    public void setyPaddle(double yPaddle) {
        this.yPaddle = yPaddle;
    }

    /**
     * Gets the x-coordinate of the center of the paddle.
     *
     * @return The x-coordinate of the center of the paddle.
     */
    public double getCenterPaddleX() {
        return centerPaddleX;
    }

    /**
     * Sets the x-coordinate of the center of the paddle.
     *
     * @param centerPaddleX The new x-coordinate of the center of the paddle.
     */
    public void setCenterPaddleX(double centerPaddleX) {
        this.centerPaddleX = centerPaddleX;
    }

    /**
     * Gets the elapsed time.
     *
     * @return The elapsed time.
     */
    public long getTime() {
        return time;
    }

    /**
     * Sets the elapsed time.
     *
     * @param time The new elapsed time.
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Gets the elapsed gold time.
     *
     * @return The elapsed gold time.
     */
    public long getGoldTime() {
        return goldTime;
    }

    /**
     * Sets the elapsed gold time.
     *
     * @param goldTime The new elapsed gold time.
     */
    public void setGoldTime(long goldTime) {
        this.goldTime = goldTime;
    }

    /**
     * Gets the velocity of the ball along the x-axis.
     *
     * @return The velocity of the ball along the x-axis.
     */
    public double getvX() {
        return vX;
    }

    /**
     * Sets the velocity of the ball along the x-axis.
     *
     * @param vX The new velocity of the ball along the x-axis.
     */
    public void setvX(double vX) {
        this.vX = vX;
    }

    /**
     * Gets the list of serialized blocks.
     *
     * @return The list of serialized blocks.
     */
    public ArrayList<BlockSerialize> getBlocks() {
        return blocks;
    }

    /**
     * Sets the list of serialized blocks.
     *
     * @param blocks The new list of serialized blocks.
     */
    public void setBlocks(ArrayList<BlockSerialize> blocks) {
        this.blocks = blocks;
    }

}

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
 * @author [Your Name]
 * @version 1.0
 * @since [Date of creation]
 */
public class LoadSave {
    private boolean isExistHeartBlock;
    private boolean isGoldStatus;
    private boolean goDownBall;
    private boolean goRightBall;
    private boolean collideToPaddle;
    private boolean collideTopPaddleAndMoveToRight;
    private boolean collideToRightWall;
    private boolean collideToLeftWall;
    private boolean collideToRightBlock;
    private boolean collideToBottomBlock;
    private boolean collideToLeftBlock;
    private boolean collideToTopBlock;
    private int level;
    private int score;
    private int heart;

    private int remainingBlockCount;
    private double xBall;
    private double yBall;
    private double xPaddle;
    private double yPaddle;
    private double centerPaddleX;
    private long time;
    private long goldTime;
    private double vX;
    private ArrayList<BlockSerialize> blocks = new ArrayList<BlockSerialize>();

    /**
     * Destroys the existing save game file. This method is called when the player win or lose the game.
     */
    public void destroySaveGameFile(){
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

    public boolean isExistHeartBlock() {
        return isExistHeartBlock;
    }

    public void setExistHeartBlock(boolean existHeartBlock) {
        isExistHeartBlock = existHeartBlock;
    }

    public boolean isGoldStatus() {
        return isGoldStatus;
    }

    public void setGoldStatus(boolean goldStatus) {
        isGoldStatus = goldStatus;
    }

    public boolean isGoDownBall() {
        return goDownBall;
    }

    public void setGoDownBall(boolean goDownBall) {
        this.goDownBall = goDownBall;
    }

    public boolean isGoRightBall() {
        return goRightBall;
    }

    public void setGoRightBall(boolean goRightBall) {
        this.goRightBall = goRightBall;
    }

    public boolean isCollideToPaddle() {
        return collideToPaddle;
    }

    public void setCollideToPaddle(boolean collideToPaddle) {
        this.collideToPaddle = collideToPaddle;
    }

    public boolean isCollideTopPaddleAndMoveToRight() {
        return collideTopPaddleAndMoveToRight;
    }

    public void setCollideTopPaddleAndMoveToRight(boolean collideTopPaddleAndMoveToRight) {
        this.collideTopPaddleAndMoveToRight = collideTopPaddleAndMoveToRight;
    }

    public boolean isCollideToRightWall() {
        return collideToRightWall;
    }

    public void setCollideToRightWall(boolean collideToRightWall) {
        this.collideToRightWall = collideToRightWall;
    }

    public boolean isCollideToLeftWall() {
        return collideToLeftWall;
    }

    public void setCollideToLeftWall(boolean collideToLeftWall) {
        this.collideToLeftWall = collideToLeftWall;
    }

    public boolean isCollideToRightBlock() {
        return collideToRightBlock;
    }

    public void setCollideToRightBlock(boolean collideToRightBlock) {
        this.collideToRightBlock = collideToRightBlock;
    }

    public boolean isCollideToBottomBlock() {
        return collideToBottomBlock;
    }

    public void setCollideToBottomBlock(boolean collideToBottomBlock) {
        this.collideToBottomBlock = collideToBottomBlock;
    }

    public boolean isCollideToLeftBlock() {
        return collideToLeftBlock;
    }

    public void setCollideToLeftBlock(boolean collideToLeftBlock) {
        this.collideToLeftBlock = collideToLeftBlock;
    }

    public boolean isCollideToTopBlock() {
        return collideToTopBlock;
    }

    public void setCollideToTopBlock(boolean collideToTopBlock) {
        this.collideToTopBlock = collideToTopBlock;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public int getRemainingBlockCount() {
        return remainingBlockCount;
    }

    public void setRemainingBlockCount(int remainingBlockCount) {
        this.remainingBlockCount = remainingBlockCount;
    }

    public double getxBall() {
        return xBall;
    }

    public void setxBall(double xBall) {
        this.xBall = xBall;
    }

    public double getyBall() {
        return yBall;
    }

    public void setyBall(double yBall) {
        this.yBall = yBall;
    }

    public double getxPaddle() {
        return xPaddle;
    }

    public void setxPaddle(double xPaddle) {
        this.xPaddle = xPaddle;
    }

    public double getyPaddle() {
        return yPaddle;
    }

    public void setyPaddle(double yPaddle) {
        this.yPaddle = yPaddle;
    }

    public double getCenterPaddleX() {
        return centerPaddleX;
    }

    public void setCenterPaddleX(double centerPaddleX) {
        this.centerPaddleX = centerPaddleX;
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

    public double getvX() {
        return vX;
    }

    public void setvX(double vX) {
        this.vX = vX;
    }

    public ArrayList<BlockSerialize> getBlocks() {
        return blocks;
    }

    public void setBlocks(ArrayList<BlockSerialize> blocks) {
        this.blocks = blocks;
    }
}

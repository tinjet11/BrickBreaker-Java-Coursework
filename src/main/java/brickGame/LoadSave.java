package brickGame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class LoadSave {

    private final String SAVE_PATH_DIR = "save"; // Relative to the project directory

    // Construct the complete path using the directory and filename
    private final String SAVE_PATH = SAVE_PATH_DIR + "/save.mdds";
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

    public void destroySaveGameFile(){
        File saveFile = new File(SAVE_PATH);
        if (saveFile.exists()) {
           saveFile.delete();
        }
    }
    public boolean checkSaveGameFileExist() {
        File saveFile = new File(SAVE_PATH);
        if (saveFile.exists()) {
            return true;
        } else {
            return false;
        }

    }

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

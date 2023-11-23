package brickGame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import static brickGame.GameStateManager.SAVE_PATH;

public class LoadSave {
    public boolean isExistHeartBlock;
    public boolean isGoldStatus;
    public boolean goDownBall;
    public boolean goRightBall;
    public boolean collideToPaddle;
    public boolean collideTopPaddleAndMoveToRight;
    public boolean collideToRightWall;
    public boolean collideToLeftWall;
    public boolean collideToRightBlock;
    public boolean collideToBottomBlock;
    public boolean collideToLeftBlock;
    public boolean collideToTopBlock;
    public int level;
    public int score;
    public int heart;

    public int remainingBlockCount;
    public double xBall;
    public double yBall;
    public double xPaddle;
    public double yPaddle;
    public double centerPaddleX;
    public long time;
    public long goldTime;
    public double vX;
    public ArrayList<BlockSerialize> blocks = new ArrayList<BlockSerialize>();


    public void read() {
        File saveFile = new File(SAVE_PATH);
        if (saveFile.exists()) {
            // Proceed with reading the save file
            try {
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File(SAVE_PATH)));


                level = inputStream.readInt();
                score = inputStream.readInt();
                heart = inputStream.readInt();
                remainingBlockCount = inputStream.readInt();


                xBall = inputStream.readDouble();
                yBall = inputStream.readDouble();
                xPaddle = inputStream.readDouble();
                yPaddle = inputStream.readDouble();
                centerPaddleX = inputStream.readDouble();
                time = inputStream.readLong();
                goldTime = inputStream.readLong();
                vX = inputStream.readDouble();


                isExistHeartBlock = inputStream.readBoolean();
                isGoldStatus = inputStream.readBoolean();
                goDownBall = inputStream.readBoolean();
                goRightBall = inputStream.readBoolean();
                collideToPaddle = inputStream.readBoolean();
                collideTopPaddleAndMoveToRight = inputStream.readBoolean();
                collideToRightWall = inputStream.readBoolean();
                collideToLeftWall = inputStream.readBoolean();
                collideToRightBlock = inputStream.readBoolean();
                collideToBottomBlock = inputStream.readBoolean();
                collideToLeftBlock = inputStream.readBoolean();
                collideToTopBlock = inputStream.readBoolean();


                try {
                    Object obj = inputStream.readObject();
                    if (obj instanceof ArrayList<?>) {
                        blocks = (ArrayList<BlockSerialize>) obj;
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
}

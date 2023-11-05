package brickGame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class LoadSave {
    public boolean          isExistHeartBlock;
    public boolean          isGoldStatus;
    public boolean          goDownBall;
    public boolean          goRightBall;
    public boolean          collideToBreak;
    public boolean          collideToBreakAndMoveToRight;
    public boolean          collideToRightWall;
    public boolean          collideToLeftWall;
    public boolean          collideToRightBlock;
    public boolean          collideToBottomBlock;
    public boolean          collideToLeftBlock;
    public boolean          collideToTopBlock;
    public int              level;
    public int              score;
    public int              heart;
  //  public int              destroyedBlockCount;
    public int  remainingBlockCount;
    public double           xBall;
    public double           yBall;
    public double           xBreak;
    public double           yBreak;
    public double           centerBreakX;
    public long             time;
    public long             goldTime;
    public double           vX;
    public ArrayList<BlockSerializable> blocks = new ArrayList<BlockSerializable>();


    public void read() {
        File saveFile = new File(Main.savePath);
        System.out.println(saveFile.toString());
        if (saveFile.exists()) {
            // Proceed with reading the save file
            try {
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File(Main.savePath)));


                level = inputStream.readInt();
                score = inputStream.readInt();
                heart = inputStream.readInt();
                remainingBlockCount = inputStream.readInt();


                xBall = inputStream.readDouble();
                yBall = inputStream.readDouble();
                xBreak = inputStream.readDouble();
                yBreak = inputStream.readDouble();
                centerBreakX = inputStream.readDouble();
                time = inputStream.readLong();
                goldTime = inputStream.readLong();
                vX = inputStream.readDouble();


                isExistHeartBlock = inputStream.readBoolean();
                isGoldStatus = inputStream.readBoolean();
                goDownBall = inputStream.readBoolean();
                goRightBall = inputStream.readBoolean();
                collideToBreak = inputStream.readBoolean();
                collideToBreakAndMoveToRight = inputStream.readBoolean();
                collideToRightWall = inputStream.readBoolean();
                collideToLeftWall = inputStream.readBoolean();
                collideToRightBlock = inputStream.readBoolean();
                collideToBottomBlock = inputStream.readBoolean();
                collideToLeftBlock = inputStream.readBoolean();
                collideToTopBlock = inputStream.readBoolean();


                try {
                    Object obj = inputStream.readObject();
                    if (obj instanceof ArrayList<?>) {
                        blocks = (ArrayList<BlockSerializable>) obj;
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

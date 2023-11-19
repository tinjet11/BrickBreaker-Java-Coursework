package brickGame;

import java.io.*;
import java.util.ArrayList;

import static brickGame.BallControl.*;
import static brickGame.Main.*;

public class GameStateManager {
    private Main main;

    public GameStateManager(Main main){
        this.main = main;
    }
    public static void saveGame() {
        new Thread(() -> {
            new File(savePathDir).mkdirs();
            File file = new File(savePath);
            ObjectOutputStream outputStream = null;

            engine.stop();
            try {
                outputStream = new ObjectOutputStream(new FileOutputStream(file));

                outputStream.writeInt(level);
                outputStream.writeInt(score);
                outputStream.writeInt(heart);
                outputStream.writeInt(remainingBlockCount);


                outputStream.writeDouble(xBall);
                outputStream.writeDouble(yBall);
                outputStream.writeDouble(xPaddle);
                outputStream.writeDouble(yPaddle);
                outputStream.writeDouble(centerPaddleX);
                outputStream.writeLong(time);
                outputStream.writeLong(goldTime);
                outputStream.writeDouble(vX);


                outputStream.writeBoolean(isExistHeartBlock);
                outputStream.writeBoolean(isGoldStatus);
                outputStream.writeBoolean(goDownBall);
                outputStream.writeBoolean(goRightBall);
                outputStream.writeBoolean(collideToPaddle);
                outputStream.writeBoolean(collideToPaddleAndMoveToRight);
                outputStream.writeBoolean(collideToRightWall);
                outputStream.writeBoolean(collideToLeftWall);
                outputStream.writeBoolean(collideToRightBlock);
                outputStream.writeBoolean(collideToBottomBlock);
                outputStream.writeBoolean(collideToLeftBlock);
                outputStream.writeBoolean(collideToTopBlock);

                ArrayList<BlockSerialize> blockSerializables = new ArrayList<>();
                for (Block block : blocks) {
                    if (block.isDestroyed) {
                        continue;
                    }
                    blockSerializables.add(new BlockSerialize(block.row, block.column, block.type));
                }

                System.out.println(blockSerializables.size());

                outputStream.writeObject(blockSerializables);

                //new Score(this).showMessage("Game Saved", 300, 300);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.printf("%s", "savegame (FNTE) function in Main.java:");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.printf("%s", "savegame (IOE) function in Main.java:");
            } finally {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.printf("%s", "savegame (IOE2) function in Main.java:");
                }
            }
        }).start();

    }

    public void loadGame() {

        LoadSave loadSave = new LoadSave();
        loadSave.read();

        isExistHeartBlock = loadSave.isExistHeartBlock;
        isGoldStatus = loadSave.isGoldStatus;
        goDownBall = loadSave.goDownBall;
        goRightBall = loadSave.goRightBall;
        collideToPaddle = loadSave.collideToPaddle;
        collideToPaddleAndMoveToRight = loadSave.collideTopPaddleAndMoveToRight;
        collideToRightWall = loadSave.collideToRightWall;
        collideToLeftWall = loadSave.collideToLeftWall;
        collideToRightBlock = loadSave.collideToRightBlock;
        collideToBottomBlock = loadSave.collideToBottomBlock;
        collideToLeftBlock = loadSave.collideToLeftBlock;
        collideToTopBlock = loadSave.collideToTopBlock;
        level = loadSave.level;
        score = loadSave.score;
        heart = loadSave.heart;
        remainingBlockCount = loadSave.remainingBlockCount;
        System.out.printf("remainingBlockCount : %d", remainingBlockCount);
        xBall = loadSave.xBall;
        yBall = loadSave.yBall;
        xPaddle = loadSave.xPaddle;
        yPaddle = loadSave.yPaddle;
        centerPaddleX = loadSave.centerPaddleX;
        time = loadSave.time;
        goldTime = loadSave.goldTime;
        vX = loadSave.vX;

        blocks.clear();
        chocos.clear();

        for (BlockSerialize ser : loadSave.blocks) {
            blocks.add(new Block(ser.row, ser.j, ser.type));
        }


        try {
            loadFromSave = true;
            if (loadFromSave) {
                // Delete the saved game file
                File saveFile = new File(Main.savePath);
                if (saveFile.exists()) {
                    if (saveFile.delete()) {
                        System.out.println("Deleted the saved game file.");
                    } else {
                        System.err.println("Failed to delete the saved game file.");
                    }
                }
            }
            main.startGame(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("%s", "loadGame function in Main.java:");
        }


    }


    public void restartGame() {

        try {
            level = 1;
            heart = initialHeart;
            score = 0;
            vX = 1.000;
            remainingBlockCount = 0;
            resetcollideFlags();
            goDownBall = true;

            isGoldStatus = false;
            isExistHeartBlock = false;
            hitTime = 0;
            time = 0;
            goldTime = 0;

            blocks.clear();
            chocos.clear();

            main.startGame(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("%s", "restart  function in Main.java:");
        }
    }

}
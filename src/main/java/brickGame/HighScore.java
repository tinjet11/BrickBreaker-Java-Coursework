package brickGame;

import java.io.*;

public class HighScore {
    public int highestScore;
    private   final String SAVE_PATH_DIR = "save"; // Relative to the project directory

    // Construct the complete path using the directory and filename
    private final String SAVE_PATH = SAVE_PATH_DIR + "/highestScore.mdds";

    public void setHighestGameScore(int score) {

        if(score > getHighestGameScore()){
            new Thread(() -> {
                new File(SAVE_PATH_DIR).mkdirs();
                File file = new File(SAVE_PATH);
                ObjectOutputStream outputStream = null;


                try {
                    outputStream = new ObjectOutputStream(new FileOutputStream(file));
                    outputStream.writeInt(score);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public int getHighestGameScore() {
        File saveFile = new File(SAVE_PATH);

        if (saveFile.exists()) {
            // Proceed with reading the save file
            try {
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File(SAVE_PATH)));
                highestScore = inputStream.readInt();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            highestScore = 0;
        }

        return highestScore;
    }

}

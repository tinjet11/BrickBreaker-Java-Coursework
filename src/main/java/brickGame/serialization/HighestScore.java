package brickGame.serialization;

import java.io.*;

import static brickGame.Constants.HIGHEST_SCORE_SAVE_PATH;
import static brickGame.Constants.SAVE_PATH_DIR;

/**
 * The HighestScore class provides functionality for tracking and persisting the highest score achieved in the game.
 * It allows setting and retrieving the highest score, which is stored in a serialized file.
 * <p>
 * The class includes methods for updating the highest score when a new high score is achieved,
 * as well as retrieving the current highest score.
 * </p>
 * <p>
 * The highest score is stored in a serialized file located in the "save" directory.
 * </p>
 * <p>
 * This class is part of the game's scoring mechanism and implements methods for managing the highest score file.
 * </p>
 *
 * @author Leong Tin Jet
 * @version 1.0
 */

public class HighestScore {
    public int highestScore;

    /**
     * Sets the highest game score if the provided score is greater than the current highest score.
     * This method is called when the player achieves a new high score.
     *
     * @param score The new high score to be set.
     */
    public void setHighestGameScore(int score) {

        if (score > getHighestGameScore()) {
            new Thread(() -> {
                new File(SAVE_PATH_DIR).mkdirs();
                File file = new File(HIGHEST_SCORE_SAVE_PATH);
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

    /**
     * Retrieves the current highest game score from the serialized file.
     * If the file exists, it reads the data and returns the highest score.
     * If the file does not exist, it returns 0.
     *
     * @return The current highest game score.
     */
    public int getHighestGameScore() {
        File saveFile = new File(HIGHEST_SCORE_SAVE_PATH);

        if (saveFile.exists()) {
            // Proceed with reading the save file
            try {
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File(HIGHEST_SCORE_SAVE_PATH)));
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

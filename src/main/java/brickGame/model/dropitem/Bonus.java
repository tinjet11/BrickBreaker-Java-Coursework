package brickGame.model.dropitem;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.Random;

import static brickGame.Constants.*;

/**
 * The Bonus class represents a specific type of drop item in the game, providing bonuses to the player.
 * It extends the DropItem class and implements the draw method to display the visual representation
 * of the bonus on the game screen.
 * <p>
 * Bonuses appear when Choco blocks are destroyed, and they will increase the score by 3 if player able to catch it
 * </p>
 * <p>
 * This class uses JavaFX for creating the visual appearance of the bonus, with the bonus being represented
 * as a rectangle filled with an image pattern. The image pattern is randomly selected from two possible
 * bonus images.
 * </p>
 * <p>
 * The Bonus class is designed to be instantiated and used within the game logic to provide dynamic and
 * interactive gameplay elements.
 * </p>
 *
 * <br>
 * <a href="https://github.com/kooitt/CourseworkGame/blob/master/src/main/java/brickGame/Bonus.java">Original Code</a>
 * <br>
 * <a href="https://github.com/tinjet11/COMP2042_CW_hcytl1/blob/master/src/main/java/brickGame/model/dropitem/Bonus.java">Current Code</a>
 * @author Leong Tin Jet
 * @version 1.0
 */
public class Bonus extends DropItem {

    /**
     * Constructs a new Bonus object associated with a specific block's row and column positions.
     * The constructor calculates the position of the bonus item based on the block positions and invokes the draw method to visually represent the item.
     *
     * @param row    The row position of the block associated with the bonus item.
     * @param column The column position of the block associated with the bonus item.
     */
    public Bonus(int row, int column) {
        super(row, column);
    }

    /**
     * Overrides the draw method from the DropItem class and provide implementation to visually represent the bonus.
     * The method creates a rectangle element filled with an image pattern, randomly selected
     * from two possible bonus images. The draw operation is performed on the JavaFX application
     * thread using Platform.runLater.
     */
    @Override
    protected void draw() {
        Platform.runLater(() -> {
            element = new Rectangle();
            element.setWidth(30);
            element.setHeight(30);
            element.setX(getX());
            element.setY(getY());
            String url;
            if (new Random().nextInt(20) % 2 == 0) {
                url = IMAGE_PATH_BONUS_1;
            } else {
                url = IMAGE_PATH_BONUS_2;
            }

            element.setFill(new ImagePattern(new Image(getClass().getResourceAsStream(url))));
        });
    }

}
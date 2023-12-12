package brickGame.model.dropitem;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import static brickGame.Constants.IMAGE_PATH_BOMB;

/**
 * The Bomb class represents a specific type of drop item in the game, providing Bomb to the player.
 * It extends the DropItem class and implements the draw method to display the visual representation
 * of the Bomb on the game screen.
 * <p>
 * Bomb appear when Penalty blocks are destroyed, and they will deduct the score if player not able to catch it with paddle
 * </p>
 * <p>
 * This class uses JavaFX for creating the visual appearance of the Bomb, with the Bomb being represented
 * as a rectangle filled with an image pattern.
 * </p>
 * <p>
 * The Bomb class is designed to be instantiated and used within the game logic to provide dynamic and
 * interactive gameplay elements.
 * </p>
 * <br>
 * <a href="https://github.com/tinjet11/COMP2042_CW_hcytl1/blob/master/src/main/java/brickGame/model/dropitem/Bomb.java">Current Code</a>
 * @author Leong Tin Jet
 * @version 1.0
 */
public class Bomb extends DropItem {

    /**
     * Constructs a new Bomb object associated with a specific block's row and column positions.
     * The constructor calculates the position of the bomb item based on the block positions and invokes the draw method to visually represent the item.
     *
     * @param row    The row position of the block associated with the bomb item.
     * @param column The column position of the block associated with the bomb item.
     */
    public Bomb(int row, int column) {
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
            element.setWidth(40);
            element.setHeight(50);
            element.setX(getX());
            element.setY(getY());

            element.setFill(new ImagePattern(new Image(getClass().getResourceAsStream(IMAGE_PATH_BOMB))));
        });
    }
}
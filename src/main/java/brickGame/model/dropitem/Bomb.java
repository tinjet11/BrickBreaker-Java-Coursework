package brickGame.model.dropitem;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

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
 *
 * @author Leong Tin Jet
 * @version 1.0
 */
public class Bomb extends DropItem {
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

            String url = "/images/bomb.png"; // Adjust the image path for penalty

            element.setFill(new ImagePattern(new Image(getClass().getResourceAsStream(url))));
        });
    }
}
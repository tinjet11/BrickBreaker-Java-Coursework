package brickGame.model.dropitem;

import javafx.scene.shape.Rectangle;

import java.io.Serializable;

import static brickGame.model.Block.*;

/**
 * The DropItem class represents an abstract drop item in the game.
 * It serves as a base class for specific drop item types such as Bomb and Bonus.
 * <p>
 * Drop items appear when certain blocks are destroyed in the game, providing score addition or deduction to the player upon collection.
 * </p>
 * <p>
 * This class is designed to be extended by concrete drop item classes, and it includes common
 * properties and methods for all drop items, such as their position, creation time, and whether
 * they have been taken by the player.
 * </p>
 * <p>
 * Instances of concrete drop item classes are created when certain blocks are destroyed, and
 * they are displayed on the game screen for a limited time. Players can collect these items by
 * moving the paddle into their position.
 * </p>
 * <p>
 * This class implements the Serializable interface to allow its instances to be serialized and
 * deserialized using object streams for saving and loading the game state.
 * </p>
 *
 * @author Leong Tin Jet
 * @version 1.0
 */
public abstract class DropItem implements Serializable {
    public Rectangle element;

    private double x;
    private double y;
    private long timeCreated;
    private boolean taken = false;
    /**
     * Constructs a new DropItem object with the specified row and column.
     *
     * @param row    The row position of the block associated with the drop item.
     * @param column The column position of the block associated with the drop item.
     */
    public DropItem(int row, int column) {
        x = (column * BLOCK_WIDTH) + BLOCK_PADDING_H + (BLOCK_WIDTH / 2) - 15;
        y = (row * BLOCK_HEIGHT) + BLOCK_PADDING_TOP + (BLOCK_HEIGHT / 2) - 15;

        draw();
    }
    /**
     * Abstract method to be implemented by concrete drop item classes.
     * It is responsible for drawing the visual representation of the drop item.
     */
    protected abstract void draw();

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public double getX() {
        return x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return y;
    }
}

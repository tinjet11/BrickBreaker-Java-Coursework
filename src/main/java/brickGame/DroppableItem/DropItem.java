package brickGame.DroppableItem;

import javafx.scene.shape.Rectangle;

import java.io.Serializable;

import static brickGame.Block.*;

public abstract class DropItem implements Serializable {
    public Rectangle element;

    private double x;
    private double y;
    private long timeCreated;
    private boolean taken = false;

    public DropItem(int row, int column) {
        x = (column * BLOCK_WIDTH) + BLOCK_PADDING_H + (BLOCK_WIDTH / 2) - 15;
        y = (row * BLOCK_HEIGHT) + BLOCK_PADDING_TOP + (BLOCK_HEIGHT / 2) - 15;

        draw();
    }

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

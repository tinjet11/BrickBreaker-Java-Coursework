package brickGame.DroppableItem;

import brickGame.*;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

public abstract class DropItem implements Serializable {
    public Rectangle element;

    private double x;
    private double y;
    private long timeCreated;
    private boolean taken = false;

    public DropItem(int row, int column) {
        x = (column * (Block.getWidth())) + Block.getPaddingH() + (Block.getWidth() / 2) - 15;
        y = (row * (Block.getHeight())) + Block.getPaddingTop() + (Block.getHeight() / 2) - 15;

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
package brickGame;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.Serializable;
import java.util.Random;

public class Bonus implements Serializable {
    public Rectangle choco;

    private double x;
    private double y;
    private long timeCreated;
    private boolean taken = false;

    public Bonus(int row, int column) {
        x = (column * (Block.getWidth())) + Block.getPaddingH() + (Block.getWidth() / 2) - 15;
        y = (row * (Block.getHeight())) + Block.getPaddingTop() + (Block.getHeight() / 2) - 15;

        draw();
    }

    private void draw() {
        Platform.runLater(()->{
            choco = new Rectangle();
            choco.setWidth(30);
            choco.setHeight(30);
            choco.setX(x);
            choco.setY(y);

            String url;
            if (new Random().nextInt(20) % 2 == 0) {
                url = "bonus1.png";
            } else {
                url = "bonus2.png";
            }

            choco.setFill(new ImagePattern(new Image(url)));
        });

    }

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

    public double getX(){
        return  x;
    }
    public void setY(double y){
        this.y = y;
    }
    public double getY(){
        return  y;
    }



}


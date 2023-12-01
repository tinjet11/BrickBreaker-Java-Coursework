package brickGame.model.dropitem;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Bomb extends DropItem {
    public Bomb(int row, int column) {
        super(row, column);
    }

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
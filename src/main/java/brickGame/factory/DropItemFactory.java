package brickGame.factory;



import brickGame.controller.GameSceneController;
import brickGame.model.InitGameComponent;
import brickGame.model.dropitem.Bomb;
import brickGame.model.dropitem.Bonus;
import javafx.application.Platform;

public class DropItemFactory {
    private final GameSceneController gameSceneController;
    private final InitGameComponent initGameComponent;

    public DropItemFactory(GameSceneController gameSceneController, InitGameComponent initGameComponent) {
        this.gameSceneController = gameSceneController;
        this.initGameComponent = initGameComponent;
    }
    public void createChocoBonus(int row, int column,long time) {
        Bonus choco = new Bonus(row, column);
        choco.setTimeCreated(time);
        Platform.runLater(() -> gameSceneController.getGamePane().getChildren().add(choco.element));
        initGameComponent.getChocos().add(choco);
    }

    public void createBombPenalty(int row, int column,long time) {
        Bomb bomb = new Bomb(row, column);
        bomb.setTimeCreated(time);
        Platform.runLater(() -> gameSceneController.getGamePane().getChildren().add(bomb.element));
        initGameComponent.getBombs().add(bomb);
    }
}

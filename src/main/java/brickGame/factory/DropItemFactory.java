package brickGame.factory;



import brickGame.controller.GameSceneController;
import brickGame.model.InitGameComponent;
import brickGame.model.dropitem.Bomb;
import brickGame.model.dropitem.Bonus;
import javafx.application.Platform;

/**
 * The {@code DropItemFactory} class is responsible for creating instances of drop items in the game.
 * It provides methods to create specific types of drop items and ensures their proper addition to the game scene
 * and relevant collections in the game component.
 * @author Leong Tin Jet
 * @version 1.0
 */
public class DropItemFactory {
    private final GameSceneController gameSceneController;
    private final InitGameComponent initGameComponent;

    /**
     * Constructs a {@code DropItemFactory} with the specified game scene controller and game component.
     *
     * @param gameSceneController The controller for the game scene.
     * @param initGameComponent   The component responsible for initializing the game.
     */
    public DropItemFactory(GameSceneController gameSceneController, InitGameComponent initGameComponent) {
        this.gameSceneController = gameSceneController;
        this.initGameComponent = initGameComponent;
    }

    /**
     * Creates a choco bonus item at the specified position and time.
     * The created item is added to the game scene and the list of choco bonuses in the game component.
     *
     * @param row   The row position of the choco bonus.
     * @param column The column position of the choco bonus.
     * @param time   The creation time of the choco bonus.
     */
    public void createChocoBonus(int row, int column,long time) {
        Bonus choco = new Bonus(row, column);
        choco.setTimeCreated(time);
        Platform.runLater(() -> gameSceneController.getGamePane().getChildren().add(choco.element));
        initGameComponent.getChocos().add(choco);
    }

    /**
     * Creates a bomb penalty item at the specified position and time.
     * The created item is added to the game scene and the list of bomb penalties in the game component.
     *
     * @param row   The row position of the bomb penalty.
     * @param column The column position of the bomb penalty.
     * @param time   The creation time of the bomb penalty.
     */
    public void createBombPenalty(int row, int column,long time) {
        Bomb bomb = new Bomb(row, column);
        bomb.setTimeCreated(time);
        Platform.runLater(() -> gameSceneController.getGamePane().getChildren().add(bomb.element));
        initGameComponent.getBombs().add(bomb);
    }
}

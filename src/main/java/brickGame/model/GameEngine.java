package brickGame.model;


import brickGame.handler.Actionable;
import javafx.application.Platform;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * The GameEngine class represents the core engine of the Brick Breaker game. It manages the game loop,
 * updating game components, handling physics calculations, and tracking game time.
 * <p>
 * The engine uses an ExecutorService with three threads to concurrently execute initialization, update,
 * physics calculations, and time tracking tasks. The game loop runs until the threads are interrupted,
 * and the engine provides hooks for the game logic to perform specific actions during initialization,
 * update, physics update, and time tracking.
 * </p>
 * <p>
 * The game loop is run on the JavaFX application thread using Platform.runLater to ensure that UI updates
 * are performed safely within the JavaFX UI thread.
 * </p>
 * <p>
 * The GameEngine class implements the singleton pattern, ensuring that only one instance of the game engine
 * exists during the game's lifecycle. The start and stop methods are provided to initiate and terminate the
 * game engine, respectively.
 * </p>
 *
 * @author Leong Tin Jet
 * @version 1.0
 */
public class GameEngine {
    /**
     * The {@code ExecutorService} responsible for managing threads in the game engine.
     */
    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    /**
     * The {@code Actionable} interface used for game logic callbacks during the game loop.
     */
    private Actionable onAction;

    /**
     * The frames per second (fps) for the game loop.
     */
    private int fps = 15;

    /**
     * A flag indicating whether the game engine is stopped.
     */
    public boolean isStopped = true;


    /**
     * Sets the Actionable interface to be used for game logic callbacks during the game loop.
     *
     * @param onAction The Actionable interface implementation providing game logic callbacks.
     */
    public void setOnAction(Actionable onAction) {
        this.onAction = onAction;
    }

    /**
     * @param fps set fps and we convert it to millisecond
     */
    public void setFps(int fps) {
        this.fps = (int) 500 / fps;
    }

    private long time = 0;

    /**
     * Runs the game update loop, invoking the onUpdate method of the Actionable interface.
     */
    private void update() {
        executorService.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Platform.runLater(() -> onAction.onUpdate());
                    Thread.sleep(fps);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Re-interrupt thread
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
    }

    /**
     * Runs the physics calculation loop, invoking the onPhysicsUpdate method of the Actionable interface.
     */
    private void physicsCalculation() {
        executorService.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Platform.runLater(() -> onAction.onPhysicsUpdate());
                    Thread.sleep(fps);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Re-interrupt thread
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
    }

    /**
     * Runs the game time tracking loop, invoking the onTime method of the Actionable interface.
     */
    private void timeStart() {
        executorService.execute(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    time++;
                    onAction.onTime(time);
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Re-interrupt thread
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Initiates the game engine, starting the initialization, update, physics calculation, and time tracking loops.
     */
    public void start() {
        time = 0;
        update();
        physicsCalculation();
        timeStart();
        isStopped = false;
    }

    /**
     * Stops the game engine, interrupting all running threads associated with the engine.
     */
    public void stop() {
        if (!isStopped) {
            isStopped = true;
            executorService.shutdownNow();
        }
    }

}

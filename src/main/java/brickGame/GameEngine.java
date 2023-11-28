package brickGame;


import javafx.application.Platform;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameEngine {
    private ExecutorService executorService = Executors.newFixedThreadPool(3);
    private Actionable onAction;
    private int fps = 15;
    private Thread updateThread;
    private Thread physicsThread;
    private Thread timeThread;

    public boolean isStopped = true;

    public void setOnAction(Actionable onAction) {
        this.onAction =onAction;
    }

    /**
     * @param fps set fps and we convert it to millisecond
     */
    public void setFps(int fps) {
        this.fps = (int) 500 / fps;
    }

    private long time = 0;

        private void initialize() {
        onAction.onInit();
    }

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

    public void start() {
        time = 0;
        initialize();
        update();
        physicsCalculation();
        timeStart();
        isStopped = false;
    }

    public void stop() {
        if (!isStopped) {
            isStopped = true;
            executorService.shutdownNow();
        }
    }

}

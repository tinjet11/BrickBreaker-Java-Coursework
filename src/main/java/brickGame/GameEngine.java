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

    private Thread timeThread;

//    private synchronized void Update() {
//        updateThread = new Thread(() -> {
//            try {
//                while (!Thread.currentThread().isInterrupted()) {
//                    Platform.runLater(() -> onAction.onUpdate());
//                    Thread.sleep(fps);
//                }
//            } catch (InterruptedException e) {
//                // Restore interrupted status
//                Thread.currentThread().interrupt();
//            }
//        });
//        updateThread.start();
//    }
//
//
//    private void Initialize() {
//        onAction.onInit();
//    }
//
//    private void PhysicsCalculation() {
//        physicsThread = new Thread(() -> {
//            try {
//                while (!Thread.currentThread().isInterrupted()) {
//                    Platform.runLater(() -> onAction.onPhysicsUpdate());
//                    Thread.sleep(fps);
//                }
//            } catch (InterruptedException e) {
//                // Restore interrupted status
//                Thread.currentThread().interrupt();
//            }
//        });
//
//        physicsThread.start();
//    }
//
//
//    public void start() {
//        time = 0;
//        Initialize();
//        Update();
//        PhysicsCalculation();
//        TimeStart();
//        isStopped = false;
//    }
//
//    //change from .stop to .interrupt
//    public void stop() {
//        if (!isStopped) {
//            isStopped = true;
//            try {
//                updateThread.interrupt();
//                physicsThread.interrupt();
//                timeThread.interrupt();
//
//                // Wait for threads to finish
//                updateThread.join();
//                physicsThread.join();
//                timeThread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//                // Handle the exception as needed
//            }
//        }
//    }
//
//
//
//    private void TimeStart() {
//        timeThread = new Thread(() -> {
//            try {
//                while (!Thread.currentThread().isInterrupted()) {
//                    time++;
//                    Platform.runLater(() -> onAction.onTime(time));
//                    Thread.sleep(1);
//                }
//            } catch (InterruptedException e) {
//                // Restore interrupted status
//                Thread.currentThread().interrupt();
//            }
//        });
//
//        timeThread.start();
//    }
//}

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

package brickGame;


import javafx.application.Platform;

public class GameEngine {

    private OnAction onAction;
    private int fps = 15;
    private Thread updateThread;
    private Thread physicsThread;
    public boolean isStopped = true;

    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * @param fps set fps and we convert it to millisecond
     */
    public void setFps(int fps) {
        this.fps = (int) 500 / fps;
    }

    private synchronized void Update() {
        updateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!updateThread.isInterrupted()) {
                    try {
                        // Perform your update logic in the background thread

                        // Update the UI components (JavaFX nodes) from the JavaFX application thread
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                onAction.onUpdate();
                                // You can also update other UI components here
                            }
                        });

                        Thread.sleep(fps);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        });
        updateThread.start();
    }


    private void Initialize() {
        onAction.onInit();
    }

    private synchronized void PhysicsCalculation() {
        physicsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!physicsThread.isInterrupted()) {
                    try {
                        // Perform your physics calculations in the background thread

                        // Update the UI components (JavaFX nodes) from the JavaFX application thread
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                onAction.onPhysicsUpdate();
                                // You can also update other UI components here
                            }
                        });

                        Thread.sleep(fps);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        });

        physicsThread.start();
    }


    public void start() {
        time = 0;
        Initialize();
        Update();
        PhysicsCalculation();
        TimeStart();
        isStopped = false;
    }

//change from .stop to .interrupt
    public void stop() {
        if (!isStopped) {
            isStopped = true;
            updateThread.interrupt();
            physicsThread.interrupt();
            timeThread.interrupt();
        }
    }

    private long time = 0;

    private Thread timeThread;

    private void TimeStart() {
        timeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!timeThread.isInterrupted()) {
                        time++;
                        onAction.onTime(time);
                        Thread.sleep(1);
                      //  break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        timeThread.start();
    }




}

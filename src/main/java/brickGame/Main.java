package brickGame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

import java.util.Random;

import static brickGame.BallControl.*;
import static brickGame.TutorialController.viewTutorial;
import static brickGame.WinController.showGameWinningScreen;

public class Main extends Application implements EventHandler<KeyEvent>, OnAction {

    private final int endLevel = 18;
    public static int level = 1 ;
    public static double xBreak = 0.0f;
    public static double centerBreakX;
    public static double yBreak = 640.0f;
    public static final int breakWidth     = 130;
    public static final int breakHeight    = 30;
    public static final int halfBreakWidth = breakWidth / 2;
    public static final int sceneWidth = 500;
    public static final int sceneHeight = 700;
    public static final  int LEFT  = 1;
    public static final  int RIGHT = 2;
    public static Circle ball;
    public static double xBall;
    public static double yBall;

    public static boolean isGoldStatus      = false;
    public static boolean isExistHeartBlock = false;

    public static Rectangle rect;
    public static final int ballRadius = 10;

    //public static int destroyedBlockCount = 0;

    public static int remainingBlockCount = 0;

    public static double v = 1.000;

    public static int  heart    = 1000;
    public static int  score    = 0;
    public static long time     = 0;
    public static long hitTime  = 0;
    public static long goldTime = 0;

    public static GameEngine engine;
    public static final String savePathDir = "save"; // Relative to the project directory

    // Construct the complete path using the directory and filename
    public static String savePath = savePathDir + "/save.mdds";

    public static ArrayList<Block> blocks = new ArrayList<>();
    private ArrayList<Bonus> chocos = new ArrayList<>();
    protected static final Color[] colors = new Color[]{
            Color.MAGENTA,
            Color.RED,
            Color.GOLD,
            Color.CORAL,
            Color.AQUA,
            Color.VIOLET,
            Color.GREENYELLOW,
            Color.ORANGE,
            Color.PINK,
            Color.SLATEGREY,
            Color.YELLOW,
            Color.TOMATO,
            Color.TAN,
    };
    public  Pane             root;

    public HBox lefthbox;
    public HBox righthbox;
    public HBox parentHbox;
    private Label            scoreLabel;
    private Label            heartLabel;
    private Label            levelLabel;

    private boolean loadFromSave = false;

    public static Stage  primaryStage;
    Button pauseButton = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        initializeMenuScene();
    }


    private void initializeMenuScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Menu.fxml"));
        Scene menuScene = null;
        try {
            menuScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        primaryStage.setTitle("Brick Breaker Game");
        primaryStage.setScene(menuScene);
        primaryStage.show();

        Button btn = (Button) menuScene.lookup("#startButton");
        btn.setOnAction(actionEvent -> {
            startGame(primaryStage);
        });

        Button tutorialBtn = (Button) menuScene.lookup("#tutorialButton");
        tutorialBtn.setOnAction(actionEvent -> {
            viewTutorial(primaryStage);
        });
    }

    private void startGame(Stage primaryStage){
        this.primaryStage = primaryStage;

        root = new Pane();
        lefthbox = new HBox();
        righthbox = new HBox();
        parentHbox = new HBox();
        pauseButton = new Button("Pause");

        scoreLabel = new Label("Score: " + score);
        levelLabel = new Label("Level: " + level);
        heartLabel = new Label("Heart : " + heart);
        root.requestFocus();

        // Disable focus traversal for the button
        pauseButton.setFocusTraversable(false);


        lefthbox.getChildren().addAll(scoreLabel,levelLabel,heartLabel);
        HBox.setHgrow(pauseButton, Priority.ALWAYS);

        righthbox.getChildren().add(pauseButton);
        parentHbox.getChildren().addAll(lefthbox,righthbox);
        System.out.println(level);
        if (!loadFromSave) {
            System.out.println("test: " + level);
//            level++;
            if (level >1){
                new Score(this).showMessage("Level Up :)",300,300);

            }
            if (level == endLevel) {
                showGameWinningScreen(primaryStage);
                return;
            }

            Init init = new Init();

            init.initBall();
            init.initBreak();
            init.initBoard();
            root.getChildren().addAll(rect, ball,parentHbox);
        } else {
            root.getChildren().addAll(rect, ball, parentHbox);
        }

        // add block to the screen
        for (Block block : blocks) {
            root.getChildren().add(block.rect);
        }



        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        scene.getStylesheets().add("style.css");
        scene.setOnKeyPressed(this);

        primaryStage.setTitle("Brick Breaker Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        pauseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                saveGame();
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Menu.fxml"));
                Scene menuScene = null;
                try {
                    menuScene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                menuScene.getStylesheets().add("style.css");
                Button startbtn = (Button) menuScene.lookup("#startButton");
                startbtn.setText("Resume");

                primaryStage.setTitle("Brick Breaker Game");
                primaryStage.setScene(menuScene);
                primaryStage.show();

                startbtn.setOnAction(actionEvent -> {
                    loadGame();
                });
            }
        });


        if(level != endLevel){
            if (!loadFromSave) {
                remainingBlockCount = blocks.size();
                if (level >= 1 && level < endLevel) {
                    engine = new GameEngine();
                    engine.setOnAction(this);
                    engine.setFps(120);
                    engine.start();
                }
            } else {
                engine = new GameEngine();
                engine.setOnAction(this);
                engine.setFps(120);
                engine.start();
                loadFromSave = false;
            }
        }


    }



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void handle(KeyEvent event) {
        //System.out.println("Key pressed: " + event.getCode()); // Add this line
        switch (event.getCode()) {
            case LEFT:
                move(LEFT);
                break;
            case RIGHT:
                move(RIGHT);
                break;
            case DOWN:
                // Uncomment if needed: setPhysicsToBall();
                break;
            case S:
                // Uncomment if needed: saveGame();
                break;
        }
    }



    private void move(final int direction) {
        //Move the platform
        new Thread(new Runnable() {
            @Override
            public void run() {
                int sleepTime = 4;
                for (int i = 0; i < 30; i++) {
                    if (xBreak == (sceneWidth - breakWidth) && direction == RIGHT) {
                        return;
                    }
                    if (xBreak == 0 && direction == LEFT) {
                        return;
                    }
                    if (direction == RIGHT) {
                        xBreak++;
                    } else {
                        xBreak--;
                    }
                    centerBreakX = xBreak + halfBreakWidth;
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.printf("%s","move function in Main.java:");
                    }
                    if (i >= 20) {
                        sleepTime = i;
                    }
                }
            }
        }).start();
    }

    private void checkDestroyedCount() {
        //System.out.println(remainingBlockCount);
      if (0 == remainingBlockCount && level != endLevel) {
          //TODO win level todo...
           //System.out.println("You Win");
           nextLevel();
      }


    }

    private void saveGame() {
        new Thread(()->{
               new File(savePathDir).mkdirs();
               File file = new File(savePath);
               ObjectOutputStream outputStream = null;

            engine.stop();
            try {
                outputStream = new ObjectOutputStream(new FileOutputStream(file));

                outputStream.writeInt(level);
                outputStream.writeInt(score);
                outputStream.writeInt(heart);
                outputStream.writeInt(remainingBlockCount);


                outputStream.writeDouble(xBall);
                outputStream.writeDouble(yBall);
                outputStream.writeDouble(xBreak);
                outputStream.writeDouble(yBreak);
                outputStream.writeDouble(centerBreakX);
                outputStream.writeLong(time);
                outputStream.writeLong(goldTime);
                outputStream.writeDouble(vX);


                outputStream.writeBoolean(isExistHeartBlock);
                outputStream.writeBoolean(isGoldStatus);
                outputStream.writeBoolean(goDownBall);
                outputStream.writeBoolean(goRightBall);
                outputStream.writeBoolean(collideToBreak);
                outputStream.writeBoolean(collideToBreakAndMoveToRight);
                outputStream.writeBoolean(collideToRightWall);
                outputStream.writeBoolean(collideToLeftWall);
                outputStream.writeBoolean(collideToRightBlock);
                outputStream.writeBoolean(collideToBottomBlock);
                outputStream.writeBoolean(collideToLeftBlock);
                outputStream.writeBoolean(collideToTopBlock);

                ArrayList<BlockSerializable> blockSerializables = new ArrayList<>();
                for (Block block : blocks) {
                    if (block.isDestroyed) {
                        continue;
                    }
                    blockSerializables.add(new BlockSerializable(block.row, block.column, block.type));
                }

                System.out.println(blockSerializables.size());

                outputStream.writeObject(blockSerializables);

                new Score(this).showMessage("Game Saved", 300,300);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.printf("%s","savegame (FNTE) function in Main.java:");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.printf("%s","savegame (IOE) function in Main.java:");
            } finally {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.printf("%s", "savegame (IOE2) function in Main.java:");
                }
            }
        }).start();

    }

    private void loadGame() {

        LoadSave loadSave = new LoadSave();
        loadSave.read();

        isExistHeartBlock = loadSave.isExistHeartBlock;
        isGoldStatus = loadSave.isGoldStatus;
        goDownBall = loadSave.goDownBall;
        goRightBall = loadSave.goRightBall;
        collideToBreak = loadSave.collideToBreak;
        collideToBreakAndMoveToRight = loadSave.collideToBreakAndMoveToRight;
        collideToRightWall = loadSave.collideToRightWall;
        collideToLeftWall = loadSave.collideToLeftWall;
        collideToRightBlock = loadSave.collideToRightBlock;
        collideToBottomBlock = loadSave.collideToBottomBlock;
        collideToLeftBlock = loadSave.collideToLeftBlock;
        collideToTopBlock = loadSave.collideToTopBlock;
        level = loadSave.level;
        score = loadSave.score;
        heart = loadSave.heart;
        remainingBlockCount = loadSave.remainingBlockCount;
        System.out.printf("remainingBlockCount : %d",remainingBlockCount);
        xBall = loadSave.xBall;
        yBall = loadSave.yBall;
        xBreak = loadSave.xBreak;
        yBreak = loadSave.yBreak;
        centerBreakX = loadSave.centerBreakX;
        time = loadSave.time;
        goldTime = loadSave.goldTime;
        vX = loadSave.vX;

        blocks.clear();
        chocos.clear();

        for (BlockSerializable ser : loadSave.blocks) {
            int r = new Random().nextInt(200);
            blocks.add(new Block(ser.row, ser.j, colors[r % colors.length], ser.type));
        }


        try {
            loadFromSave = true;
            if (loadFromSave) {
                // Delete the saved game file
                File saveFile = new File(Main.savePath);
                if (saveFile.exists()) {
                    if (saveFile.delete()) {
                        System.out.println("Deleted the saved game file.");
                    } else {
                        System.err.println("Failed to delete the saved game file.");
                    }
                }
            }
            startGame(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("%s","loadGame function in Main.java:");
        }


    }

    private void nextLevel() {
        System.out.println("nextlevel run");
        Platform.runLater(()->{
            try {
                vX = 1.000;
                //to slow down the ball to prevent heart deducting very fast
                vY = 1.000;

                engine.stop();
                resetcollideFlags();
                goDownBall = true;

                isGoldStatus = false;
                isExistHeartBlock = false;

                hitTime = 0;
                time = 0;
                goldTime = 0;

                engine.stop();
                blocks.clear();
                chocos.clear();
                remainingBlockCount = 0;
                if(level < endLevel) {
                    level++;
                }
                startGame(primaryStage);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.printf("%s","nextLevel function in Main.java:");
            }

        });
    }

    public void restartGame() {

        try {
            level = 0;
            heart = 3;
            score = 0;
            vX = 1.000;
            remainingBlockCount = 0;
            resetcollideFlags();
            goDownBall = true;

            isGoldStatus = false;
            isExistHeartBlock = false;
            hitTime = 0;
            time = 0;
            goldTime = 0;

            blocks.clear();
            chocos.clear();

            startGame(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("%s","restart  function in Main.java:");
        }
    }


    @Override
    public void onUpdate() {
        Platform.runLater(() ->{

                    scoreLabel.setText("Score: " + score);
                    heartLabel.setText("Heart : " + heart);

                    rect.setX(xBreak);
                    rect.setY(yBreak);
                    ball.setCenterX(xBall);
                    ball.setCenterY(yBall);
                }
        );


        if (yBall >= Block.getPaddingTop() && yBall <= (Block.getHeight() * (level + 1)) + Block.getPaddingTop()) {
            for (final Block block : blocks) {
                try {
                    int hitCode = block.checkHitToBlock(xBall, yBall);
                    if (hitCode != Block.NO_HIT) {
                        score += 1;

                        new Score(this).show(block.x, block.y, 1);

                        block.rect.setVisible(false);
                        block.isDestroyed = true;
                        remainingBlockCount--;
                        //System.out.println("size is " + blocks.size());
                        resetcollideFlags();

                        if (block.type == Block.BLOCK_CHOCO) {
                            final Bonus choco = new Bonus(block.row, block.column);
                            choco.setTimeCreated(time);
                            Platform.runLater(() -> root.getChildren().add(choco.choco));

                            chocos.add(choco);
                        }

                        if (block.type == Block.BLOCK_STAR) {
                            goldTime = time;
                            ball.setFill(new ImagePattern(new Image("goldball.png")));
                            System.out.println("gold ball");
                            root.getStyleClass().add("goldRoot");
                            isGoldStatus = true;
                        }

                        if (block.type == Block.BLOCK_HEART) {
                            // if(heart != 3){
                            heart++;
                            System.out.println("heart hitted");
                            //   }
                        }

                        if (hitCode == Block.HIT_RIGHT) {
                            collideToRightBlock = true;
                        } else if (hitCode == Block.HIT_BOTTOM) {
                            collideToBottomBlock = true;
                        } else if (hitCode == Block.HIT_LEFT) {
                            collideToLeftBlock = true;
                        } else if (hitCode == Block.HIT_TOP) {
                            collideToTopBlock = true;
                        }
                    }

                    //TODO hit to break and some work here....
                    //System.out.println("Break in row:" + block.row + " and column:" + block.column + " hit");

                } catch (Exception e) {
                    e.printStackTrace();

                    System.out.println("Onupdate function");
                    break;
                }
            }

        }
    }


    @Override
    public void onInit() {


    }

    @Override
    public void onPhysicsUpdate() {
        if(level != endLevel){
            checkDestroyedCount();
        }
        setPhysicsToBall(this);

        if (time - goldTime > 5000) {
            ball.setFill(new ImagePattern(new Image("ball.png")));
            root.getStyleClass().remove("goldRoot");
            isGoldStatus = false;
        }
        //this part is about the Bonus object
        for (Bonus choco : chocos) {
            if (choco.getY() > sceneHeight || choco.isTaken()) {
                continue;
            }
            if (choco.getY() >= yBreak && choco.getY() <= yBreak + breakHeight && choco.getX() >= xBreak && choco.getX() <= xBreak + breakWidth) {
                System.out.println("You Got it and +3 score for you");
                choco.setTaken(true);
                choco.choco.setVisible(false);
                System.out.println("choco hited");
                score += 3;
                new Score(this).show(choco.getX(), choco.getY(), 3);
            }else {
                // Update choco's position to make it drop
                double timeElapsed = (time - choco.getTimeCreated()) / 1000.0; // Time in seconds
                double gravity = 9.8; // Adjust the gravity as needed
                double deltaY = 0.5 * gravity * timeElapsed * timeElapsed; // Displacement formula
                choco.setY(deltaY + choco.getY());
                Platform.runLater(() -> {
                    // Update UI to reflect the new position
                    choco.choco.setLayoutY(choco.getY());
                });

            }
        }
    }


    @Override
    public void onTime(long time) {
        this.time = time;
    }
}

package brickGame;

import brickGame.DroppableItem.Bonus;
import brickGame.DroppableItem.Penalty;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class InitGameComponent {

    private  static  InitGameComponent instance;
    private InitGameComponent() {

    }
    public static InitGameComponent getInstance() {
        if (instance == null) {
            instance = new InitGameComponent();
        }
        return instance;
    }

    private  BallControl ballControl;
    private  GameLogicHandler gameLogicHandler;




    private Rectangle paddle;
    private double xPaddle = 0.0f;
    private double centerPaddleX;
    private double yPaddle = 690.0f;
    private final int PADDLE_WIDTH = 130;
    private final int PADDLE_HEIGHT = 10;
    private final int HALF_PADDLE_WIDTH = getPADDLE_WIDTH() / 2;
    private final int SCENE_WIDTH = 500;
    private final int SCENE_HEIGHT = 750;
    private final int BALL_RADIUS = 10;
    private boolean isExistHeartBlock = false;

    private boolean isExistBombBlock = false;

    private ArrayList<Block> blocks = new ArrayList<>();
    private ArrayList<Bonus> chocos = new ArrayList<>();

    private ArrayList<Penalty> bombs = new ArrayList<>();

    //set up the brick in the game according to the current level
    public void initBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < gameLogicHandler.getLevel() + 1; j++) {
                int r = new Random().nextInt(500);
                if (r % 5 == 0) {
                    continue;
                }
                Block.BLOCK_TYPE type;
                if (r % 10 == 1) {
                    type = Block.BLOCK_TYPE.BLOCK_CHOCO;
                } else if (r % 10 == 2) {
                    if (!isExistHeartBlock()) {
                        type = Block.BLOCK_TYPE.BLOCK_HEART;
                        setExistHeartBlock(true);
                    } else {
                        type =Block.BLOCK_TYPE.BLOCK_NORMAL;
                    }
                } else if (r % 10 == 3) {
                    type = Block.BLOCK_TYPE.BLOCK_STAR;
                }
                else if (r % 10 == 4) {
                    if (!isExistBombBlock) {
                        type = Block.BLOCK_TYPE.BLOCK_BOMB;
                        setExistBombBlock(true);
                    } else {
                        type =Block.BLOCK_TYPE.BLOCK_NORMAL;
                    }

                }
                // TODO: add new block which will drop a item, need to catch it else will minus point
                else {
                    type = Block.BLOCK_TYPE.BLOCK_NORMAL;
                }
                getBlocks().add(new Block(j, i, type));
                //System.out.println("colors " + r % (colors.length));
            }
        }
        System.out.println("init board completed");
    }

    public void initBall() {
        Random random = new Random();

        // Randomly set the x-coordinate within the width of the game scene
        double xBall = Math.max(getBALL_RADIUS(), Math.min(random.nextInt(getSCENE_WIDTH() - getBALL_RADIUS()) + 1, getSCENE_WIDTH() - getBALL_RADIUS()));

        // Ensure that the ball starts above the screen's bottom edge
        int minYBall = ((gameLogicHandler.getLevel() + 1) * Block.getHeight()) + 2 * getBALL_RADIUS();

        // Randomly set the y-coordinate within the valid range
        double yBall = Math.max(minYBall, Math.min(random.nextInt(getSCENE_HEIGHT() - minYBall) + minYBall, getSCENE_HEIGHT() - minYBall));

        // Create the ball object with the specified radius and image pattern
        Circle ball = new Circle();
        ball.setRadius(getBALL_RADIUS());
       if(gameLogicHandler.isGoldStatus()){
           ball.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/images/goldball.png"))));
        }else{
           ball.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/images/ball.png"))));
       }


        // Set the ball's properties in the BallControl instance
        ballControl.setxBall(xBall);
        ballControl.setyBall(yBall);
        ballControl.setBall(ball);
        System.out.println("init ball completed");
    }

    // the paddle
    public void initPaddle() {
        setPaddle(new Rectangle());
        getPaddle().setWidth(getPADDLE_WIDTH());
        getPaddle().setHeight(getPADDLE_HEIGHT());
        getPaddle().setX(getxPaddle());
        getPaddle().setY(getyPaddle());

        ImagePattern pattern = new ImagePattern(new Image(getClass().getResourceAsStream("/images/paddle.jpg")));

        getPaddle().setFill(pattern);
        System.out.println("init paddle  completed");
    }

    public void setBallControl(BallControl ballControl) {
        this.ballControl = ballControl;
    }

    public void setGameLogicHandler(GameLogicHandler gameLogicHandler) {
        this.gameLogicHandler = gameLogicHandler;
    }

    public Rectangle getPaddle() {
        return paddle;
    }

    public void setPaddle(Rectangle paddle) {
        this.paddle = paddle;
    }

    public double getxPaddle() {
        return xPaddle;
    }

    public void setxPaddle(double xPaddle) {
        this.xPaddle = xPaddle;
    }

    public double getCenterPaddleX() {
        return centerPaddleX;
    }

    public void setCenterPaddleX(double centerPaddleX) {
        this.centerPaddleX = centerPaddleX;
    }

    public double getyPaddle() {
        return yPaddle;
    }

    public void setyPaddle(double yPaddle) {
        this.yPaddle = yPaddle;
    }

    public int getPADDLE_WIDTH() {
        return PADDLE_WIDTH;
    }

    public int getPADDLE_HEIGHT() {
        return PADDLE_HEIGHT;
    }

    public int getHALF_PADDLE_WIDTH() {
        return HALF_PADDLE_WIDTH;
    }

    public int getSCENE_WIDTH() {
        return SCENE_WIDTH;
    }

    public int getSCENE_HEIGHT() {
        return SCENE_HEIGHT;
    }

    public int getBALL_RADIUS() {
        return BALL_RADIUS;
    }

    public boolean isExistHeartBlock() {
        return isExistHeartBlock;
    }

    public void setExistHeartBlock(boolean existHeartBlock) {
        isExistHeartBlock = existHeartBlock;
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(ArrayList<Block> blocks) {
        this.blocks = blocks;
    }

    public ArrayList<Bonus> getChocos() {
        return chocos;
    }

    public void setChocos(ArrayList<Bonus> chocos) {
        this.chocos = chocos;
    }

    public ArrayList<Penalty> getBombs() {
        return bombs;
    }

    public void setExistBombBlock(boolean existBombBlock) {
        isExistBombBlock = existBombBlock;
    }
}
package brickGame;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import static brickGame.Main.isGameRun;
import static brickGame.Main.primaryStage;

public class TutorialController {

    private Main main;
    private Stage primaryStage;

    public TutorialController() {

    }

    public TutorialController(Main main, Stage primaryStage) {
        this.main = main;
        this.primaryStage = primaryStage;
    }

    @FXML
    public void back() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(TutorialController.class.getResource("Menu.fxml"));

            fxmlLoader.setControllerFactory(c -> {
                return new MenuController(this.main, primaryStage);
            });
            Scene menuScene = new Scene(fxmlLoader.load());

            Button startButton = (Button) menuScene.lookup("#startButton");

            if(isGameRun){
                startButton.setText("Resume");
            }else{
                startButton.setText("Start Game");
            }

            primaryStage.setTitle("Brick Breaker Game");
            primaryStage.setScene(menuScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    @FXML
//    public void viewTutorial(Stage primaryStage){
//        try{
//            FXMLLoader fxmlLoader1 = new FXMLLoader(Main.class.getResource("Tutorial.fxml"));
//            Scene tutorialScene= new Scene(fxmlLoader1.load());
//
//            primaryStage.setTitle("Brick Breaker Game");
//            primaryStage.setScene(tutorialScene);
//            primaryStage.show();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}

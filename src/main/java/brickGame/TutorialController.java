package brickGame;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static brickGame.Main.primaryStage;

public class TutorialController {

    @FXML
    public void back(){
        try{
            FXMLLoader fxmlLoader1 = new FXMLLoader(TutorialController.class.getResource("Menu.fxml"));
            Scene menuScene= new Scene(fxmlLoader1.load());

            primaryStage.setTitle("Brick Breaker Game");
            primaryStage.setScene(menuScene);
            primaryStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void viewTutorial(Stage primaryStage){
        try{
            FXMLLoader fxmlLoader1 = new FXMLLoader(Main.class.getResource("Tutorial.fxml"));
            Scene tutorialScene= new Scene(fxmlLoader1.load());

            primaryStage.setTitle("Brick Breaker Game");
            primaryStage.setScene(tutorialScene);
            primaryStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

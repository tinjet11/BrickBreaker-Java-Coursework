package brickGame;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import static brickGame.Main.score;

public class WinController {

    public static void showGameWinningScreen(Stage primaryStage){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(WinController.class.getResource("Win.fxml"));
            Scene winScene= new Scene(fxmlLoader.load());

            Label scorePlaceholder = (Label) winScene.lookup("#scorePlaceholder");
            scorePlaceholder.setText(String.valueOf(score));

            primaryStage.setTitle("Brick Breaker Game");
            primaryStage.setScene(winScene);
            primaryStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

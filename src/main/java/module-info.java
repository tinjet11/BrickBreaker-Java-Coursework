module brickGame {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.media;

    opens brickGame to javafx.fxml;
    exports brickGame;
    exports brickGame.controller;
    opens brickGame.controller to javafx.fxml;
    exports brickGame.model.dropitem;
    opens brickGame.model.dropitem to javafx.fxml;
    exports brickGame.handler.blockhit;
    opens brickGame.handler.blockhit to javafx.fxml;
    exports brickGame.factory.blockhithandler;
    opens brickGame.factory.blockhithandler to javafx.fxml;
    exports brickGame.factory;
    opens brickGame.factory to javafx.fxml;
    exports brickGame.model;
    opens brickGame.model to javafx.fxml;
    exports brickGame.handler;
    opens brickGame.handler to javafx.fxml;
    exports brickGame.serialization;
    opens brickGame.serialization to javafx.fxml;
}
package com.main;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

import java.io.IOException;
import javafx.util.Duration;

public class App extends Application {

    private static Scene scene;
    private Pane root;
    private Ghost ghost;
    private EventHandler eventHandler;

    @Override
    public void start(Stage stage) throws IOException {
        root = new Pane();
        scene = new Scene(root, 640, 480);
        eventHandler = new EventHandler(scene);
        this.ghost = new Ghost(0, 0);

        root.getChildren().add(ghost.sprite);
        stage.setScene(scene);
        stage.show();

        Duration frameDuration = Duration.millis(16);
        KeyFrame keyFrame = new KeyFrame(frameDuration, event -> update());
        Timeline gameLoop = new Timeline(keyFrame);
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    public static void main(String[] args) {
        launch();
    }

    public void update(){
        ghost.update(eventHandler);
    }

    public static double getWidth(){
        return scene.getWindow().getWidth();
    }

    public static double getHeight(){
        return scene.getWindow().getHeight();
    }
}
package com.main;

import com.main.entities.player.Ghost;
import com.main.entities.units.Soldier;
import com.main.entities.enemies.Zombie;
import com.main.map.Base;
import com.main.map.WarMap;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.util.Duration;

public class App extends Application {

    private static Scene scene;
    private Pane root;
    private EventHandler eventHandler;

    private Base playerBase;
    private Base enemyBase;
    private List<Soldier> units;
    private List<Zombie> zombies;
    private WarMap map;

    @Override
    public void start(Stage stage) throws IOException {
        root = new Pane();
        scene = new Scene(root, 1920, 1080);
        eventHandler = new EventHandler(scene);
        playerBase = new Base(0, (int)scene.getHeight());
        enemyBase = new Base(0, 0);
        this.units = new ArrayList<>();
        this.zombies = new ArrayList<>();
        this.map = new WarMap(scene);

        // root.getChildren().add(ghost.getSprite());
        root.getChildren().add(this.map.getSprite());
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
        // ghost.update(eventHandler);
    }

    public static double getWidth(){
        return scene.getWindow().getWidth();
    }

    public static double getHeight(){
        return scene.getWindow().getHeight();
    }
}
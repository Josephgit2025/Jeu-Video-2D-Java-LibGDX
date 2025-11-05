package com.main.map;

import java.util.List;
import java.util.ArrayList;
import javafx.scene.Scene;

public class WarMap {
    private int height;
    private int width;
    private List<Obstacle> obstacles;

    public WarMap(Scene scene){
        this.height = (int)scene.getHeight();
        this.width = (int)scene.getWidth();
        this.obstacles = new ArrayList<>();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }
}

package com.main.map;

import com.main.utils.Position;


public class Obstacle {
    private int height;
    private int width;
    private Position position;


    public Obstacle(String filePath, int height, int width, Position position){
        this.height = height;
        this.width = width;
        this.position = position;
    }
}
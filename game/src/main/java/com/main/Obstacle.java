package com.main;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Obstacle {
    private int height;
    private int width;
    private Position position;
    private Image spriteSheet;
    private ImageView sprite;

    public Obstacle(String filePath, int height, int width, Position position){
        this.height = height;
        this.width = width;
        this.position = position;
        this.spriteSheet = new Image(getClass().getResourceAsStream(filePath));
        this.sprite = new ImageView(spriteSheet);
    }
}

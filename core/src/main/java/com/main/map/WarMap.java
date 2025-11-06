package com.main.map;

import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class WarMap {
    private double height;
    private double width;
    private List<Obstacle> obstacles;
    private SpriteBatch batch;
    private Texture sprite;

    public WarMap(Scene scene){
        this.height = scene.getHeight();
        this.width = scene.getWidth();
        this.obstacles = new ArrayList<>();
        this.spriteSheet = new Image(getClass().getResourceAsStream("/com/main/map/map.png"));
        this.sprite = new ImageView(this.spriteSheet);
        this.sprite.setFitHeight(scene.getHeight());
        this.sprite.setFitWidth(scene.getWidth());
        this.sprite.setTranslateX(0);
        this.sprite.setTranslateY(0);
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public Image getSpriteSheet() {
        return spriteSheet;
    }

    public ImageView getSprite() {
        return sprite;
    }
}
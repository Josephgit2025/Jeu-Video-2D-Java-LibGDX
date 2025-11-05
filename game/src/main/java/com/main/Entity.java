package com.main;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Entity {
    protected int posX;
    protected int posY;
    protected Image spriteSheet;
    protected ImageView sprite;

    public Entity(String filePath, int posX, int posY){
        this.posX = posX;
        this.posY = posY;
        this.spriteSheet = new Image(getClass().getResourceAsStream(filePath));
        this.sprite = new ImageView(spriteSheet);
        this.sprite.setFitHeight(120);
        this.sprite.setFitWidth(160);
        this.sprite.setTranslateX(posX);
        this.sprite.setTranslateY(posY);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    abstract public void update(EventHandler event);

    public void setSpritePosX(int posX){
        this.sprite.setTranslateX(posX);
        this.posX = posX;
    }

    public void setSpritePosY(int posY){
        this.sprite.setTranslateY(posY);
        this.posY = posY;
    }
}

package com.main.entities;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.ImageView;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

public abstract class Unit {
    protected int posX;
    protected int posY;
    protected Image spriteSheet;
    protected ImageView sprite;
    protected int health;
    protected int attackDamage;
    protected int attackSpeed;
    protected int speed;
    protected Unit target;
    protected List<Effect> modifiers = new ArrayList<>();
    protected int range;

    public Unit(String filePath, int posX, int posY){
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

    public ImageView getSprite() {
        return sprite;
    }

    public void setSpritePosX(int posX){
        this.sprite.setTranslateX(posX);
        this.posX = posX;
    }

    public void setSpritePosY(int posY){
        this.sprite.setTranslateY(posY);
        this.posY = posY;
    }

    public void attack() {
        // Implement attack logic here 
    }

    public void specialAbility() {
        // Implement special ability logic here
    }
}
package com.main.entities;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.ImageView;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public abstract class Unit {
    protected int posX;
    protected int posY;
    protected int health;
    protected int attackDamage;
    protected int attackSpeed;
    protected int speed;
    protected Unit target;
    // protected List<Effect> modifiers = new ArrayList<>();
    protected int range;
    protected Texture texture;
    protected float width, height;

    public Unit(String filePath, int posX, int posY){
        this.posX = posX;
        this.posY = posY;
        this.texture = new Texture(filePath);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void attack() {
        // Implement attack logic here 
    }

    public void specialAbility() {
        // Implement special ability logic here
    }
}
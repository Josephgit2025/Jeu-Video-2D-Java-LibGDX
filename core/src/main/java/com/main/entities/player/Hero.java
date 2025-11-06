package com.main.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.main.entities.Unit;
import java.util.List;
import java.util.ArrayList;
import com.main.weapons.*;


public class Hero extends Unit {

    protected int xp;
    protected int level;
    protected List<Ability> abilities = new ArrayList<>();
    protected int strength;
    protected int dexterity;
    protected int agility; 
    protected Weapon weapon;
   

    public Hero(float posX, float posY) {
        super("hero/left1.png", posX, posY);
        this.health = 500;
        this.weapon = new Machette();
        this.speed = 8;
        this.attackSpeed = 1;
    }

    public float getSpeed() {
        return speed;
    }

    /**
     * Met à jour le Hero (déplacements avec LibGDX)
     * @param delta Le temps écoulé depuis la dernière frame
     * @param mapWidth Largeur de la map en pixels
     * @param mapHeight Hauteur de la map en pixels
     */
    public void update(float delta, float mapWidth, float mapHeight) {
        // Déplacement avec les touches WASD ou flèches
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            moveUp(delta, mapHeight);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            moveDown(delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            moveRight(delta, mapWidth);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            moveLeft(delta);
        }
    }

    private void moveUp(float delta, float mapHeight) {
        float newY = this.getPosY() + speed * delta * 60; // delta pour smooth movement
        if (newY + 120 > mapHeight) {
            this.setSpritePosY(mapHeight - 120);
        } else {
            this.setSpritePosY(newY);
        }
    }

    private void moveDown(float delta) {
        float newY = this.getPosY() - speed * delta * 60;
        if (newY < 0) {
            this.setSpritePosY(0);
        } else {
            this.setSpritePosY(newY);
        }
    }

    private void moveLeft(float delta) {
        float newX = this.getPosX() - speed * delta * 60;
        if (newX < 0) {
            this.setSpritePosX(0);
        } else {
            this.setSpritePosX(newX);
        }
    }

    private void moveRight(float delta, float mapWidth) {
        float newX = this.getPosX() + speed * delta * 60;
        if (newX + 160 > mapWidth) {
            this.setSpritePosX(mapWidth - 160);
        } else {
            this.setSpritePosX(newX);
        }
    }

    @Override
    public void attack() {
        if (target == null || target.isDead()) {
            return;
        }
        if (attackCooldown <= 0 && weapon != null) {
            if (weapon.getMunitions() > 0) {
                weapon.attack(); // Décrémente les munitions
                int totalDamage = weapon.getDamage(); // Seulement l'arme
                target.takeDamage(totalDamage);
                attackCooldown = weapon.getAttackSpeed();
            } else {
                weapon.reload();
            }
        }
    }
    public void render(SpriteBatch batch){
        batch.draw(this.texture, this.posX, this.posY);
    }

    public void move(){}
}
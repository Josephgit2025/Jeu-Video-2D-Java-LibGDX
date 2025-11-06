package com.main.entities.player;

import com.main.EventHandler;
import com.main.entities.Unit;
import com.main.entities.player.*;
import java.util.List;
import java.util.ArrayList;
import com.main.weapons.*;
import com.main.EventHandler;
import com.main.App;
import com.main.utils.Position;

import javafx.scene.input.KeyCode;

public class Hero extends Unit {

    protected int xp;
    protected int level;
    protected List<Ability> abilities = new ArrayList<>();
    protected int strength;
    protected int dexterity;
    protected int agility;
    protected Weapon weapon;

    public Hero(int posX, int posY) {
        super("/com/main/assets/hero/down1.png", posX, posY);
        this.health = 500;
        this.weapon = new Machette();
        this.speed = 3;
        this.attackSpeed = 1;
        this.attackDamage = 0;
    }

    public int getSpeed() {
        return speed;
    }

    public void update(EventHandler event) {
        if (event.isPressed(KeyCode.W) || event.isPressed(KeyCode.UP)) {
            moveUp();
        }
        if (event.isPressed(KeyCode.S) || event.isPressed(KeyCode.DOWN)) {
            moveDown();
        }
        if (event.isPressed(KeyCode.D) || event.isPressed(KeyCode.RIGHT)) {
            moveRight();
        }
        if (event.isPressed(KeyCode.A) || event.isPressed(KeyCode.LEFT)) {
            moveLeft();
        }
    }

    private void moveUp() {
        if (this.getPosY() - speed < 0) {
            this.setSpritePosY(0);
        } else {
            this.setSpritePosY(this.getPosY() - speed);
        }

    }

    private void moveDown() {
        if (this.getPosY() + speed + 120 > App.getHeight()) {
            this.setSpritePosY((int) App.getHeight() - 120);
        } else {
            this.setSpritePosY(this.getPosY() + speed);
        }
    }

    private void moveLeft() {
        if (this.getPosX() - speed < 0) {
            this.setSpritePosX(0);
        } else {
            this.setSpritePosX(this.getPosX() - speed);
        }

    }

    private void moveRight() {
        if (this.getPosX() + speed + 160 > App.getWidth()) {
            this.setSpritePosX((int) App.getWidth() - 160);
        } else {
            this.setSpritePosX(this.getPosX() + speed);
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

}

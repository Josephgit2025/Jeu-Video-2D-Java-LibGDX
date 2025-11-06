package com.main.entities.enemies;

import com.main.entities.Unit;

public class Zombie extends Unit {

    public Zombie (String filePath, float posX, float posY) {
        super(filePath, posX, posY);
        this.health = 100;
        this.attackDamage = 15;
        this.speed = 2;
        this.attackSpeed = 1;
        this.range = 100;
    }

    public void move(){
        this.setSpritePosX(this.posX - this.speed);
    }
}

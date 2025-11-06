package com.main.entities.units;

public class Tank extends Soldier {
    
    public Tank (float posX, float posY) {
        super("units/Tank.png", posX, posY);
        this.health = 500;
        this.attackDamage = 30;
        this.speed = 3;
        this.attackSpeed = 3;
        this.range = 100;
    }

}
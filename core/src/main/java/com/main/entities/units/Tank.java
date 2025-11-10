package com.main.entities.units;

public class Tank extends Soldier {
    
    public Tank (String filePath, float posX, float posY) {
        super(filePath, posX, posY);
        this.health = 500;
        this.attackDamage = 30;
        this.speed = 15;
        this.attackSpeed = 3;
        this.range = 100;
    }

}